package net.thumbtack.onlineshop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class OnlineShopExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<OnlineShopException> thisLoginIsExsist(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(new OnlineShopException("login", "Такой логин уже занят"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<OnlineShopException> validatore(ValidationException ex, WebRequest request) {
        return new ResponseEntity<>(new OnlineShopException(request.getContextPath(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(OnlineShopExceptionOld.class)
    public ResponseEntity<OnlineShopException> costumException(OnlineShopExceptionOld ex, WebRequest request) {
        return new ResponseEntity<>(new OnlineShopException(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    private static class OnlineShopException {
        private final String ErrorCode = "400";

        private String field;

        private String message;

        public OnlineShopException(String message) {
            this.message = message;
        }
    }
}
