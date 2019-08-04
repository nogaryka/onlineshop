package net.thumbtack.onlineshop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnlineShopExceptionOld extends RuntimeException {
    private final String errorCode = "404";

    private String field;

    private String message;

    public OnlineShopExceptionOld(String message) {
        this.message = message;
    }
}
