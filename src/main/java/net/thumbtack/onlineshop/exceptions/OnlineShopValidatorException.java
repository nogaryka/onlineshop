package net.thumbtack.onlineshop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineShopValidatorException extends RuntimeException {
    private String message;
}
