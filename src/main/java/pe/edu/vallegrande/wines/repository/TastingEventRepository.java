package pe.edu.vallegrande.wines.repository;

import reactor.core.publisher.Flux;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.wines.model.TastingEvent;

public interface TastingEventRepository extends ReactiveMongoRepository<TastingEvent, String> {
    Flux<TastingEvent> getByStatus(Boolean status);
}
