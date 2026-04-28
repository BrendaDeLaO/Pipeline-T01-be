package pe.edu.vallegrande.wines.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import pe.edu.vallegrande.wines.model.Product;
import pe.edu.vallegrande.wines.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Product management")
public class ProductRest {

    private final ProductService service;

    public ProductRest(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "List all products")
    public Flux<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Find product by ID")
    public Mono<ResponseEntity<Product>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Get products by state", description = "Filter by active/inactive")
    public Flux<Product> getByState(@PathVariable Boolean state) {
        return service.getByState(state);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get product by code", description = "Find product by code")
    public Mono<ResponseEntity<Product>> getByCode(@PathVariable String code) {
        return service.getByCode(code)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Add a new product")
    public Mono<Product> create(@RequestBody Product product) {
        return service.save(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Modify product data")
    public Mono<ResponseEntity<Product>> update(@PathVariable String id, @RequestBody Product product) {
        return service.getById(id)
                .flatMap(existing -> {
                    existing.setProductName(product.getProductName());
                    existing.setProductCategory(product.getProductCategory());
                    existing.setProductDescription(product.getProductDescription());
                    existing.setPrice(product.getPrice());
                    existing.setProductSize(product.getProductSize());
                    existing.setAwards(product.getAwards());
                    existing.setVintageYear(product.getVintageYear());
                    existing.setStock(product.getStock());
                    return service.save(existing);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/delete/{id}")
    @Operation(summary = "Delete product", description = "Logical delete")
    public Mono<Product> deleteLogical(@PathVariable String id) {
        return service.deleteLogical(id);
    }

    @PatchMapping("/restore/{id}")
    @Operation(summary = "Restore product", description = "Reactivate product")
    public Mono<Product> restoreLogical(@PathVariable String id) {
        return service.restoreLogical(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search by name or category")
    public Flux<Product> search(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String category) {

        if (name != null && category != null) {
            return service.searchByNameOrCategory(name, category);
        } else if (name != null) {
            return service.searchByName(name);
        } else if (category != null) {
            return service.searchByCategory(category);
        }
        return service.getAll();
    }
}