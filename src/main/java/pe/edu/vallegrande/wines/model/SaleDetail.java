package pe.edu.vallegrande.wines.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Document(collection = "sale_detail")
public class SaleDetail {

    @Id
    private String id;

    @Field("sale_id")
    private String saleId;

    @Field("product_id")
    private String productId;

    @Field("product_code")
    private String productCode;

    @Field("quantity")
    private Integer quantity;

    @Field("price_sale")
    private BigDecimal priceSale;
}
