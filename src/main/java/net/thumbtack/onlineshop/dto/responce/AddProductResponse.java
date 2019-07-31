package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public AddProductResponse(Integer id, String name, Integer price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.idCategories = new ArrayList<>();
    }
}

