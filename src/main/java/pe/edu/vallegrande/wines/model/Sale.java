package pe.edu.vallegrande.wines.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "sale")
public class Sale {

    @Id
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("delivery_type")
    private String deliveryType;

    @Field("payment_method")
    private String paymentMethod;

    @Field("sale_date")
    private LocalDate saleDate;

    @Field("total")
    private BigDecimal total;

    @Field("state")
    private Boolean state;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
