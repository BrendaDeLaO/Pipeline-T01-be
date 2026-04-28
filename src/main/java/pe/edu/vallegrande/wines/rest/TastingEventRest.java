package pe.edu.vallegrande.wines.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.wines.dto.CompleteTastingEventRequest;
import pe.edu.vallegrande.wines.dto.CompleteTastingEventResponse;
import pe.edu.vallegrande.wines.dto.TastingEventWithDetailsResponse;
import pe.edu.vallegrande.wines.model.TastingEvent;
import pe.edu.vallegrande.wines.service.TastingEventService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tasting Events", description = "Operations related to tasting events")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/apitastingevent")
public class TastingEventRest {

    private final TastingEventService tastingEventService;

    @Autowired
    public TastingEventRest(TastingEventService tastingEventService) {
        this.tastingEventService = tastingEventService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los eventos")
    public Flux<TastingEvent> getAll() {
        return tastingEventService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID")
    public Mono<ResponseEntity<TastingEvent>> getById(@PathVariable String id) {
        return tastingEventService.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Eventos por estado (true=activo, false=inactivo)")
    public Flux<TastingEvent> getByStatus(@PathVariable Boolean status) {
        return tastingEventService.getByStatus(status);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo evento")
    public Mono<TastingEvent> create(@RequestBody TastingEvent tastingEvent) {
        return tastingEventService.save(tastingEvent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento existente")
    public Mono<ResponseEntity<TastingEvent>> update(@PathVariable String id, @RequestBody TastingEvent tastingEvent) {
        return tastingEventService.getById(id)
                .flatMap(existing -> {
                    existing.setEventName(tastingEvent.getEventName());
                    existing.setEventDescription(tastingEvent.getEventDescription());
                    existing.setEventDate(tastingEvent.getEventDate());
                    existing.setMaxCapacity(tastingEvent.getMaxCapacity());
                    existing.setPricePerPerson(tastingEvent.getPricePerPerson());
                    return tastingEventService.update(existing);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/delete/{id}")
    @Operation(summary = "Baja lógica del evento")
    public Mono<TastingEvent> delete(@PathVariable String id) {
        return tastingEventService.delete(id);
    }

    @PatchMapping("/restore/{id}")
    @Operation(summary = "Restaurar evento")
    public Mono<TastingEvent> restore(@PathVariable String id) {
        return tastingEventService.restore(id);
    }

    @PostMapping("/complete")
    @Operation(summary = "Crear evento con sus detalles de productos (transaccional)")
    public Mono<ResponseEntity<CompleteTastingEventResponse>> createComplete(
            @RequestBody CompleteTastingEventRequest request) {
        return tastingEventService.saveComplete(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(ex -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .<CompleteTastingEventResponse>build()));
    }

    @GetMapping("/complete/{id}")
    @Operation(summary = "Obtener un evento con sus detalles por ID")
    public Mono<ResponseEntity<TastingEventWithDetailsResponse>> getByIdWithDetails(@PathVariable String id) {
        return tastingEventService.getByIdWithDetails(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
