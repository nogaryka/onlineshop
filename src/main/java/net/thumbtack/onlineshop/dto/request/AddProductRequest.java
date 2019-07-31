package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer price;

    @Min(0)
    private Integer count;

    private List<Integer> idCategories;

    public AddProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
        count = 1;
    }
}

