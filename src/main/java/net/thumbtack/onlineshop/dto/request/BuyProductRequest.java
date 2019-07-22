package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyProductRequest {
    @Min(1)
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @Min(1)
    @NotNull
    private Integer price;

    @Min(1)
    private Integer count = 1;
}
