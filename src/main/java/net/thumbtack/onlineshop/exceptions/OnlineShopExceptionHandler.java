package net.thumbtack.onlineshop.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class OnlineShopExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<OnlineShopExceptionOld> thisLoginIsExsist(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(new OnlineShopExceptionOld("login", "Такой логин уже занят"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<OnlineShopExceptionOld> validatore(ValidationException ex, WebRequest request) {
        return new ResponseEntity<>(new OnlineShopExceptionOld(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlineShopExceptionOld.class)
    public OnlineShopExceptionOld costumException(OnlineShopExceptionOld ex) {
        return new OnlineShopExceptionOld(ex.getField(), ex.getMessage());
    }

}
