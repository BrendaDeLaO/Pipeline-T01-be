package pe.edu.vallegrande.wines.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import pe.edu.vallegrande.wines.model.Product;
import pe.edu.vallegrande.wines.repository.ProductRepository;
import pe.edu.vallegrande.wines.service.ProductService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Product> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Product> getByState(Boolean state) {
        return repository.findByState(state);
    }

    @Override
    public Mono<Product> getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Mono<Product> save(Product product) {

        if (product.getId() == null) {
            product.setCreatedAt(LocalDateTime.now());
            product.setRegisterDate(LocalDate.now());
            product.setState(true);

            return repository.findTopByCodeStartingWithOrderByCodeDesc("P")
                    .defaultIfEmpty(new Product())
                    .flatMap(last -> {
                        int next = 1;
                        if (last.getCode() != null) {
                            try {
                                next = Integer.parseInt(last.getCode().substring(1)) + 1;
                            } catch (Exception ignored) {}
                        }
                        product.setCode(String.format("P%03d", next));
                        return repository.save(product);
                    });

        } else {
            product.setUpdatedAt(LocalDateTime.now());
            return repository.save(product);
        }
    }

    @Override
    public Mono<Product> deleteLogical(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(p -> {
                    p.setState(false);
                    p.setDeletedAt(LocalDateTime.now());
                    return repository.save(p);
                });
    }

    @Override
    public Mono<Product> restoreLogical(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(p -> {
                    p.setState(true);
                    p.setRestoredAt(LocalDateTime.now());
                    return repository.save(p);
                });
    }

    @Override
    public Flux<Product> searchByName(String name) {
        return repository.findByProductNameContainingIgnoreCase(name);
    }

    @Override
    public Flux<Product> searchByCategory(String category) {
        return repository.findByProductCategoryContainingIgnoreCase(category);
    }

    @Override
    public Flux<Product> searchByNameOrCategory(String name, String category) {
        return repository.findByProductNameContainingIgnoreCaseOrProductCategoryContainingIgnoreCase(name, category);
    }
}