package net.thumbtack.onlineshop.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.validator.annotation.GreatZero;

import javax.validation.ValidationException;

@Data
@NoArgsConstructor
public class EditCategoryRequest {

    private String name;

    @GreatZero
    private Integer idParent;

    public EditCategoryRequest(String name, Integer idParent) {
        if(name.isEmpty() && idParent == null) {
            throw new ValidationException();
        } else {
            this.name = name;
            this.idParent = idParent;
        }
    }
}
