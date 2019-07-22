package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyProductResponse {
    private Integer id;

    private String name;

    private Integer price;

    private Integer count;
}

