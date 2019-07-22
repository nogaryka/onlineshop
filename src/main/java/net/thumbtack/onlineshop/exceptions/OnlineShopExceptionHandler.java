package net.thumbtack.onlineshop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class OnlineShopExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AwesomeException> thisLoginIsExsist(Exception ex) {
        return new ResponseEntity<AwesomeException>(new AwesomeException("login", "Такой логин уже занят"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AwesomeException> validatora(Exception ex, WebRequest request) {
        return new ResponseEntity<AwesomeException>(new AwesomeException(request.getContextPath(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    private static class AwesomeException {
        private final String ErrorCode = "400";

        private String field;

        private String message;
    }
}
