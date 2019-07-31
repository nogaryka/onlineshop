package net.thumbtack.onlineshop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnlineShopExceptionOld extends RuntimeException {
    private String message;
}
