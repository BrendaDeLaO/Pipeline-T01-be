package pe.edu.vallegrande.wines.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.wines.model.TastingEventDetails;
import reactor.core.publisher.Flux;

public interface TastingEventDetailsRepository extends ReactiveMongoRepository<TastingEventDetails, String> {
    Flux<TastingEventDetails> findByTastingEventId(String tastingEventId);
}
