package pe.edu.vallegrande.wines.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.wines.model.Customer;
import pe.edu.vallegrande.wines.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Customer management")
public class CustomerRest {

    private final CustomerService service;

    public CustomerRest(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "List all customers")
    public Flux<Customer> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Find customer by ID")
    public Mono<ResponseEntity<Customer>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Get customers by state", description = "Filter customers by Activo / Inactivo")
    public Flux<Customer> getByState(@PathVariable String state) {
        return service.getByState(state);
    }

   
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create customer", description = "Add a new customer (state defaults to Activo)")
    public Mono<Customer> create(@RequestBody Customer customer) {
        return service.save(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Modify customer data (state is not changed)")
    public Mono<ResponseEntity<Customer>> update(@PathVariable String id,
                                                  @RequestBody Customer customerDetails) {
        return service.update(id, customerDetails)
                .map(ResponseEntity::ok)
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/delete/{id}")
    @Operation(summary = "Delete customer", description = "Logical delete — sets state to Inactivo")
    public Mono<ResponseEntity<Customer>> deleteLogical(@PathVariable String id) {
        return service.deleteLogical(id)
                .map(ResponseEntity::ok)
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/restore/{id}")
    @Operation(summary = "Restore customer", description = "Logical restore — sets state to Activo")
    public Mono<ResponseEntity<Customer>> restoreLogical(@PathVariable String id) {
        return service.restoreLogical(id)
                .map(ResponseEntity::ok)
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }
}
