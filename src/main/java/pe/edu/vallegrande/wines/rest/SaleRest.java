package pe.edu.vallegrande.wines.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.wines.dto.SaleRequest;
import pe.edu.vallegrande.wines.dto.SaleResponse;
import pe.edu.vallegrande.wines.service.SaleService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sale")
@Tag(name = "Sale API", description = "Sale transactional management (header + detail)")
public class SaleRest {

    private final SaleService service;

    public SaleRest(SaleService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all sales", description = "List all sales with their details and computed total")
    public Flux<SaleResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID", description = "Find a sale with its details and computed total by ID")
    public Mono<ResponseEntity<SaleResponse>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create sale", description = "Create a new sale with header and detail records")
    public Mono<ResponseEntity<SaleResponse>> create(@RequestBody SaleRequest request) {
        return service.create(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
