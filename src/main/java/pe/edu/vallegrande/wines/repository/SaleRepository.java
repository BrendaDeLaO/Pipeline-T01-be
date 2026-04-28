package pe.edu.vallegrande.wines.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.wines.model.Sale;
import reactor.core.publisher.Flux;

public interface SaleRepository extends ReactiveMongoRepository<Sale, String> {

    Flux<Sale> findByState(Boolean state);
}
