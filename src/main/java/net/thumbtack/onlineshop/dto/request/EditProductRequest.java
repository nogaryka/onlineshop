package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditProductRequest {
    private String name;

    @Min(value = 1)
    private Integer price;

    @Min(value = 0)
    private Integer count;

    private List<Integer> idCategory;
}
