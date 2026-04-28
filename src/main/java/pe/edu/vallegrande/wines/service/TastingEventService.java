package pe.edu.vallegrande.wines.service;

import pe.edu.vallegrande.wines.dto.CompleteTastingEventRequest;
import pe.edu.vallegrande.wines.dto.CompleteTastingEventResponse;
import pe.edu.vallegrande.wines.dto.TastingEventWithDetailsResponse;
import pe.edu.vallegrande.wines.model.TastingEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TastingEventService {

    Flux<TastingEvent> getAll();
    Mono<TastingEvent> getById(String id);
    Flux<TastingEvent> getByStatus(Boolean status);
    Mono<TastingEvent> save(TastingEvent tastingEvent);
    Mono<TastingEvent> update(TastingEvent tastingEvent);
    Mono<TastingEvent> delete(String id);
    Mono<TastingEvent> restore(String id);
    Mono<CompleteTastingEventResponse> saveComplete(CompleteTastingEventRequest request);
    Mono<TastingEventWithDetailsResponse> getByIdWithDetails(String id);
}
