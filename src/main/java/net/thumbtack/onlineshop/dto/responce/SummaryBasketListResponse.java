package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryBasketListResponse {
    List<BasketOrPurchaseResponse> baskets;

    Integer totalAmountOfAllBaskets;

    List<ProductResponse> notSufficeProduct;

    Integer totalAmountOfAllBasketsNotSuffice;
}
