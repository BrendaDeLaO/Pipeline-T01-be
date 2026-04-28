package pe.edu.vallegrande.wines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pe.edu.vallegrande.wines.model.TastingEvent;
import pe.edu.vallegrande.wines.model.TastingEventDetails;

import java.util.List;

@Data
@AllArgsConstructor
public class CompleteTastingEventResponse {

    private TastingEvent event;
    private List<TastingEventDetails> details;
}
