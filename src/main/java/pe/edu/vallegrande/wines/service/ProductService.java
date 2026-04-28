package pe.edu.vallegrande.wines.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import pe.edu.vallegrande.wines.model.Product;

public interface ProductService {

    Flux<Product> getAll();

    Mono<Product> getById(String id);

    Flux<Product> getByState(Boolean state);
    
    Mono<Product> getByCode(String code);

    Mono<Product> save(Product product);

    Mono<Product> deleteLogical(String id);

    Mono<Product> restoreLogical(String id);

    Flux<Product> searchByName(String name);

    Flux<Product> searchByCategory(String category);

    Flux<Product> searchByNameOrCategory(String name, String category);
}