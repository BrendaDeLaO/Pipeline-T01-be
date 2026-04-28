package pe.edu.vallegrande.wines.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "product")
public class Product {

    @Id
    private String id;

    @Field("code")
    private String code;

    @Field("product_name")
    private String productName;

    @Field("product_size")
    private Integer productSize;

    @Field("price")
    private Double price;

    @Field("product_category")
    private String productCategory;

    @Field("product_description")
    private String productDescription;

    @Field("awards")
    private String awards;

    @Field("vintage_year")
    private Integer vintageYear;

    @Field("register_date")
    private LocalDate registerDate;

    @Field("series")
    private String series;

    @Field("stock")
    private Integer stock;

    @Field("state")
    private Boolean state;

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