package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.validator.annotation.GreatZero;

import javax.validation.ValidationException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryRequest {

    private String name ;

    @GreatZero
    private Integer idParent;

}
