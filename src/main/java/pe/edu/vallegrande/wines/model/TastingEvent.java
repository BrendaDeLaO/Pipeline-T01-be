package pe.edu.vallegrande.wines.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "tasting_event")
public class TastingEvent {

    @Id
    private String id;

    @Field("event_name")
    private String eventName;

    @Field("event_description")
    private String eventDescription;

    @Field("event_date")
    private LocalDate eventDate;

    @Field("max_capacity")
    private Integer maxCapacity;

    @Field("price_per_person")
    private Double pricePerPerson;

    @Field("status")
    private Boolean status = true;

    // Auditoría
    @Field("created_at")
    private LocalDateTime createdAt; 

    @Field("updated_at")
    private LocalDateTime updatedAt; 

    @Field("deleted_at")
    private LocalDateTime deletedAt;
    
    @Field("restored_at")
    private LocalDateTime restoredAt;
    
}
