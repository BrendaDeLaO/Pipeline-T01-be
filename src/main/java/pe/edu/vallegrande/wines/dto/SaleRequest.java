package pe.edu.vallegrande.wines.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {

    private String customerId;
    private String deliveryType;
    private String paymentMethod;
    private List<SaleDetailRequest> details;

    @Data
    public static class SaleDetailRequest {
        private String productId;
        private Integer quantity;
        private BigDecimal priceSale;
    }
}
