package net.thumbtack.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OnlineShopExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlineShopExceptionOld.class)
    public  ResponseEntity<OnlineShopExceptionOld> costumException(OnlineShopExceptionOld ex) {
        return new ResponseEntity<>(new OnlineShopExceptionOld(ex.getField(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
