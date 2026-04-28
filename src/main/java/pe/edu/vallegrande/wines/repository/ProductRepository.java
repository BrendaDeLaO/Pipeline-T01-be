package pe.edu.vallegrande.wines.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import pe.edu.vallegrande.wines.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Flux<Product> findByState(Boolean state);

    Mono<Product> findByIdAndState(String id, Boolean state);

    Mono<Product> findByCode(String code);

    Flux<Product> findByProductNameContainingIgnoreCase(String name);

    Flux<Product> findByProductCategoryContainingIgnoreCase(String category);

    Flux<Product> findByProductNameContainingIgnoreCaseOrProductCategoryContainingIgnoreCase(String name, String category);

    Mono<Product> findTopByCodeStartingWithOrderByCodeDesc(String prefix);
}