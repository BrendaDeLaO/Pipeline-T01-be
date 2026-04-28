package pe.edu.vallegrande.wines.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import pe.edu.vallegrande.wines.dto.CompleteTastingEventRequest;
import pe.edu.vallegrande.wines.dto.CompleteTastingEventResponse;
import pe.edu.vallegrande.wines.dto.TastingEventWithDetailsResponse;
import pe.edu.vallegrande.wines.model.TastingEvent;
import pe.edu.vallegrande.wines.model.TastingEventDetails;
import pe.edu.vallegrande.wines.repository.TastingEventDetailsRepository;
import pe.edu.vallegrande.wines.repository.TastingEventRepository;
import pe.edu.vallegrande.wines.service.TastingEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TastingEventServiceImpl implements TastingEventService {

    private final TastingEventRepository tastingEventRepository;
    private final TastingEventDetailsRepository tastingEventDetailsRepository;

    public TastingEventServiceImpl(TastingEventRepository tastingEventRepository,
                                   TastingEventDetailsRepository tastingEventDetailsRepository) {
        this.tastingEventRepository = tastingEventRepository;
        this.tastingEventDetailsRepository = tastingEventDetailsRepository;
    }

    @Override
    public Flux<TastingEvent> getAll() {
        return tastingEventRepository.findAll();
    }

    @Override
    public Mono<TastingEvent> getById(String id) {
        return tastingEventRepository.findById(id);
    }

    @Override
    public Flux<TastingEvent> getByStatus(Boolean status) {
        return tastingEventRepository.getByStatus(status);
    }

    @Override
    public Mono<TastingEvent> save(TastingEvent tastingEvent) {
        if (tastingEvent.getId() == null) {
            tastingEvent.setCreatedAt(LocalDateTime.now());
        }
        tastingEvent.setStatus(true);
        return tastingEventRepository.save(tastingEvent);
    }

    @Override
    public Mono<TastingEvent> update(TastingEvent tastingEvent) {
        tastingEvent.setUpdatedAt(LocalDateTime.now());
        return tastingEventRepository.save(tastingEvent);
    }

    @Override
    public Mono<TastingEvent> delete(String id) {
        return tastingEventRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Evento no encontrado")))
                .flatMap(event -> {
                    event.setStatus(false);
                    event.setDeletedAt(LocalDateTime.now());
                    return tastingEventRepository.save(event);
                });
    }

    @Override
    public Mono<TastingEvent> restore(String id) {
        return tastingEventRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Evento no encontrado")))
                .flatMap(event -> {
                    event.setStatus(true);
                    event.setRestoredAt(LocalDateTime.now());
                    return tastingEventRepository.save(event);
                });
    }

    // TRANSACCIONAL: Guardar TastingEvent + sus TastingEventDetails
    @Transactional
    @Override
    public Mono<CompleteTastingEventResponse> saveComplete(CompleteTastingEventRequest request) {

            // 1. Preparar y guardar el TastingEvent
            TastingEvent event = request.getEvent();
            event.setStatus(true);
            event.setCreatedAt(LocalDateTime.now());

            return tastingEventRepository.save(event)
                    // 2. Con el ID del evento guardado, construir los detalles
                    .flatMap(savedEvent -> {

                        List<TastingEventDetails> details = request.getDetails().stream()
                                .map(detailReq -> {
                                    TastingEventDetails detail = new TastingEventDetails();
                                    detail.setTastingEventId(savedEvent.getId());
                                    detail.setProductId(detailReq.getProductId());
                                    detail.setQuantityOffered(detailReq.getQuantityOffered());
                                    detail.setOrderInEvent(detailReq.getOrderInEvent());
                                    detail.setNotes(detailReq.getNotes());
                                    detail.setPriceOverride(detailReq.getPriceOverride());
                                    detail.setStatus(true);
                                    return detail;
                                })
                                .collect(Collectors.toList());

                        // 3. Guardar todos los detalles y retornar la respuesta completa
                        return tastingEventDetailsRepository.saveAll(details)
                                .collectList()
                                .map(savedDetails -> new CompleteTastingEventResponse(savedEvent, savedDetails));
                    });
    }

    @Override
    public Mono<TastingEventWithDetailsResponse> getByIdWithDetails(String id) {
        return tastingEventRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Evento no encontrado")))
                .flatMap(event -> tastingEventDetailsRepository.findByTastingEventId(event.getId())
                        .collectList()
                        .map(details -> new TastingEventWithDetailsResponse(event, details)));
    }
}
