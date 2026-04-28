package pe.edu.vallegrande.wines.service;

import pe.edu.vallegrande.wines.dto.SaleRequest;
import pe.edu.vallegrande.wines.dto.SaleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SaleService {

    Flux<SaleResponse> getAll();

    Mono<SaleResponse> getById(String id);

    Mono<SaleResponse> create(SaleRequest request);
}
