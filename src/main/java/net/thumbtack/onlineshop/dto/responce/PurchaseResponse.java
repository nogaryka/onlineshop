package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private InformarionAdoutClientsForAdminResponse client;

    private List<ProductResponse> products;

    private Integer allCost;

    public PurchaseResponse(InformarionAdoutClientsForAdminResponse client) {
        this.client = client;
        this.products = new ArrayList<>();
        this.allCost = 0;
    }
}
