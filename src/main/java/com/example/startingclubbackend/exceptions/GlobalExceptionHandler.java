package com.example.startingclubbackend.exceptions;

import com.example.startingclubbackend.exceptions.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException e){
        return e.handleResponse(e.getMessage() , "that email is already registered" , HttpStatus.CONFLICT) ;
    }


    @ExceptionHandler(PhoneAlreadyRegisteredException.class)
    public ResponseEntity<Object> handlePhoneAlreadyRegisteredException(PhoneAlreadyRegisteredException e){
        return e.handleResponse(e.getMessage(),"phone number is already registered",HttpStatus.CONFLICT) ;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e){
        return e.handleResponse(e.getMessage(),"Resource not found",HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException e){
        return e.handleResponse(e.getMessage(),"token is not valid",HttpStatus.UNAUTHORIZED) ;
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<Object> handleExpiredTokenExceptionHandler(ExpiredTokenException e){
        return e.handleResponse(e.getMessage(),"token is expired",HttpStatus.FORBIDDEN) ;
    }

    @ExceptionHandler(RevokedTokenException.class)
    public ResponseEntity<Object> handleRevokedTokenException(RevokedTokenException e){
        return e.handleResponse(e.getMessage(),"token is revoked",HttpStatus.FORBIDDEN) ;
    }


}
