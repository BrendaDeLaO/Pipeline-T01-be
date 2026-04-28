package pe.edu.vallegrande.wines.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleDetailResponse {

    private String id;
    private String saleId;
    private String productId;
    private String productCode;
    private Integer quantity;
    private BigDecimal priceSale;
}
