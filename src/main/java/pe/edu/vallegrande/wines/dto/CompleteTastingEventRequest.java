package pe.edu.vallegrande.wines.dto;

import lombok.Data;
import pe.edu.vallegrande.wines.model.TastingEvent;

import java.util.List;

@Data
public class CompleteTastingEventRequest {

    private TastingEvent event;
    private List<TastingEventDetailRequest> details;

    @Data
    public static class TastingEventDetailRequest {
        private String productId;
        private Double quantityOffered;
        private Integer orderInEvent;
        private String notes;
        private Double priceOverride;
    }
}
