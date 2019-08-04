package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryPurchaseListResponse {
    List<BasketOrPurchaseResponse> purchases;

    Integer totalAmountOfAllPurchases;
}
