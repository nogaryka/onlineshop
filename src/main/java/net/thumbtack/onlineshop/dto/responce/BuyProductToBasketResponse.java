package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyProductToBasketResponse {
    List<BuyProductResponse> bought = new ArrayList<>();

    List<BuyProductResponse> remaining = new ArrayList<>();
}
