package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.entity.Category;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductResponse {
    private Integer id;

    private String name;

    private Integer price;

    private Integer count;

    private List<Integer> idCategories;
}

