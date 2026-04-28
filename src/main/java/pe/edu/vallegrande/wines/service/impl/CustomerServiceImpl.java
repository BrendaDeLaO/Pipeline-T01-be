package pe.edu.vallegrande.wines.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.wines.model.Customer;
import pe.edu.vallegrande.wines.repository.CustomerRepository;
import pe.edu.vallegrande.wines.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Customer> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado con ID: " + id)));
    }

    @Override
    public Flux<Customer> getByState(String state) {
        return repository.findByState(state);
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        customer.setState("Activo");
        customer.setCreatedAt(LocalDateTime.now());
        return repository.save(customer);
    }

    @Override
    public Mono<Customer> update(String id, Customer customerDetails) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    existing.setName(customerDetails.getName());
                    existing.setLastName(customerDetails.getLastName());
                    existing.setEmail(customerDetails.getEmail());
                    existing.setPhone(customerDetails.getPhone());
                    existing.setAddress(customerDetails.getAddress());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Customer> deleteLogical(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    existing.setState("Inactivo");
                    existing.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Customer> restoreLogical(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    existing.setState("Activo");
                    existing.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existing);
                });
    }
}
