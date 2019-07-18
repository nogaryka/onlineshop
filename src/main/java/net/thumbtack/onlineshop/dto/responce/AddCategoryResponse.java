package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryResponse {
    private Integer id;

    private  String name;

    private Integer idParentCategory;

    private String nameParent;

    public AddCategoryResponse(Integer id, String name, Integer idParentCategory) {
        this.id = id;
        this.name = name;
        this.idParentCategory = idParentCategory;
    }
}
