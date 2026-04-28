package pe.edu.vallegrande.wines.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tasting_event_detail")
public class TastingEventDetails {

    @Id
    private String id;

    @Field("tasting_event_id")
    private String tastingEventId;

    @Field("product_id")
    private String productId; 

    @Field("quantity_offered")
    private Double quantityOffered;

    @Field("order_in_event")
    private Integer orderInEvent;

    @Field("notes")
    private String notes;

    @Field("price_override")
    private Double priceOverride; 

    @Field("status")
    private Boolean status = true;
}
