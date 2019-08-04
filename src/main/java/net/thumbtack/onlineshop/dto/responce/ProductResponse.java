package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.entity.Category;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;

    private String name;

    private Integer price;

    private Integer count;

    private List<Category> categories;

    private Integer  totalPricePerItem;

    public ProductResponse(Integer id, String name, Integer price, Integer count, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
        this.totalPricePerItem = price * count;
    }
}
