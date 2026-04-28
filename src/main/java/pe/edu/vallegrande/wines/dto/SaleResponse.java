package pe.edu.vallegrande.wines.dto;

import lombok.Data;
import pe.edu.vallegrande.wines.model.Sale;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleResponse {

    private Sale sale;
    private List<SaleDetailResponse> details;
    private BigDecimal totalCalculatedSale;
}
