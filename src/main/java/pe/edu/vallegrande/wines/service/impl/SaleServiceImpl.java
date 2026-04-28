package pe.edu.vallegrande.wines.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.wines.dto.SaleDetailResponse;
import pe.edu.vallegrande.wines.dto.SaleRequest;
import pe.edu.vallegrande.wines.dto.SaleResponse;
import pe.edu.vallegrande.wines.model.Sale;
import pe.edu.vallegrande.wines.model.SaleDetail;
import pe.edu.vallegrande.wines.repository.ProductRepository;
import pe.edu.vallegrande.wines.repository.SaleDetailRepository;
import pe.edu.vallegrande.wines.repository.SaleRepository;
import pe.edu.vallegrande.wines.service.SaleService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;

    public SaleServiceImpl(SaleRepository saleRepository,
                           SaleDetailRepository saleDetailRepository,
                           ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Flux<SaleResponse> getAll() {
        return saleRepository.findAll()
                .flatMap(this::buildSaleResponse);
    }

    @Override
    public Mono<SaleResponse> getById(String id) {
        return saleRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Venta no encontrada con ID: " + id)))
                .flatMap(this::buildSaleResponse);
    }

    @Override
    public Mono<SaleResponse> create(SaleRequest request) {
        // Construir la cabecera Sale
        Sale sale = new Sale();
        sale.setCustomerId(request.getCustomerId());
        sale.setDeliveryType(request.getDeliveryType());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setSaleDate(LocalDate.now());
        sale.setState(true);
        sale.setCreatedAt(LocalDateTime.now());

        // Calcular total desde el request
        BigDecimal total = request.getDetails().stream()
                .map(d -> d.getPriceSale().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.setTotal(total);

        //  Guardar cabecera → luego guardar detalles → devolver SaleResponse
        return saleRepository.save(sale)
                .flatMap(savedSale -> {
                    // Construir lista de SaleDetail con el saleId generado
                    List<SaleDetail> details = request.getDetails().stream()
                            .map(d -> {
                                SaleDetail detail = new SaleDetail();
                                detail.setSaleId(savedSale.getId());
                                detail.setProductId(d.getProductId());
                                detail.setQuantity(d.getQuantity());
                                detail.setPriceSale(d.getPriceSale());
                                return detail;
                            })
                            .toList();

                    // Guardar todos los detalles reactivamente y enriquecer con código de producto
                    return Flux.fromIterable(details)
                            .flatMap(detail ->
                                    productRepository.findById(detail.getProductId())
                                            .doOnNext(product -> detail.setProductCode(product.getCode()))
                                            .thenReturn(detail)
                            )
                            .collectList()
                            .flatMap(enrichedDetails ->
                                    saleDetailRepository.saveAll(enrichedDetails).collectList()
                            )
                            .map(savedDetails -> {
                                List<SaleDetailResponse> detailResponses = savedDetails.stream()
                                        .map(this::toDetailResponse)
                                        .toList();

                                SaleResponse response = new SaleResponse();
                                response.setSale(savedSale);
                                response.setDetails(detailResponses);
                                response.setTotalCalculatedSale(savedSale.getTotal());
                                return response;
                            });
                });
    }

    /**
     * Construimos un SaleResponse a partir de una Sale ya persistida,
     * buscando sus detalles en la colección sale_detail.
     */
    private Mono<SaleResponse> buildSaleResponse(Sale sale) {
        return saleDetailRepository.findBySaleId(sale.getId())
                .map(this::toDetailResponse)
                .collectList()
                .map(detailResponses -> {
                    BigDecimal calculatedTotal = detailResponses.stream()
                            .map(d -> d.getPriceSale().multiply(BigDecimal.valueOf(d.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    SaleResponse response = new SaleResponse();
                    response.setSale(sale);
                    response.setDetails(detailResponses);
                    response.setTotalCalculatedSale(calculatedTotal);
                    return response;
                });
    }

    /**
     * Convertimos un SaleDetail (modelo) en SaleDetailResponse (DTO)
     */
    private SaleDetailResponse toDetailResponse(SaleDetail detail) {
        SaleDetailResponse dto = new SaleDetailResponse();
        dto.setId(detail.getId());
        dto.setSaleId(detail.getSaleId());
        dto.setProductId(detail.getProductId());
        dto.setProductCode(detail.getProductCode());
        dto.setQuantity(detail.getQuantity());
        dto.setPriceSale(detail.getPriceSale());
        return dto;
    }
}
