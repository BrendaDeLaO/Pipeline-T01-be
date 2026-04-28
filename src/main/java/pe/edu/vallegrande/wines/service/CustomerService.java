package pe.edu.vallegrande.wines.service;

import pe.edu.vallegrande.wines.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<Customer> getAll();

    Mono<Customer> getById(String id);

    Flux<Customer> getByState(String state);

    Mono<Customer> save(Customer customer);

    Mono<Customer> update(String id, Customer customerDetails);

    Mono<Customer> deleteLogical(String id);

    Mono<Customer> restoreLogical(String id);
}
