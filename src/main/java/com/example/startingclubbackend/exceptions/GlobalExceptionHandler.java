package com.example.startingclubbackend.exceptions;

import com.example.startingclubbackend.exceptions.custom.*;
import com.example.startingclubbackend.exceptions.responseHandling.ApiError;
import com.example.startingclubbackend.exceptions.responseHandling.ResponseEntityBuilder;
import io.jsonwebtoken.JwtException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredCustomException.class)
    public ResponseEntity<Object> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredCustomException e){
        return e.handleResponse(e.getMessage() , "that email is already registered" , HttpStatus.CONFLICT) ;
    }


    @ExceptionHandler(PhoneAlreadyRegisteredCustomException.class)
    public ResponseEntity<Object> handlePhoneAlreadyRegisteredException(PhoneAlreadyRegisteredCustomException e){
        return e.handleResponse(e.getMessage(),"phone number is already registered",HttpStatus.CONFLICT) ;
    }
    @ExceptionHandler(ResourceNotFoundCustomException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundCustomException e){
        return e.handleResponse(e.getMessage(),"Resource not found",HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(InvalidTokenCustomException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenCustomException e){
        return e.handleResponse(e.getMessage(),"token is not valid",HttpStatus.UNAUTHORIZED) ;
    }

    @ExceptionHandler(ExpiredTokenCustomException.class)
    public ResponseEntity<Object> handleExpiredTokenExceptionHandler(ExpiredTokenCustomException e){
        return e.handleResponse(e.getMessage(),"token is expired",HttpStatus.FORBIDDEN) ;
    }

    @ExceptionHandler(RevokedTokenCustomException.class)
    public ResponseEntity<Object> handleRevokedTokenException(RevokedTokenCustomException e){
        return e.handleResponse(e.getMessage(),"token is revoked",HttpStatus.FORBIDDEN) ;
    }
    @ExceptionHandler(EmailSendingCustomException.class)
    public ResponseEntity<Object> handleEmailSendingException(EmailSendingCustomException e){
        return e.handleResponse(e.getMessage(),"failed to send email",HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler(EventAlreadyOccurredCustomException.class)
    public ResponseEntity<Object> handleEventAlreadyOccurredException(EventAlreadyOccurredCustomException e){
        return e.handleResponse(e.getMessage(),"Event Update Error",HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(FileValidationCustomException.class)
    public ResponseEntity<Object> handleFileValidationException(FileValidationCustomException e){
        return e.handleResponse(e.getMessage() , "File Validation Failed" , HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(CustomDataIntegrityViolationCustomException.class)
    protected ResponseEntity<Object> handleCustomDataIntegrityViolationException(@NotNull CustomDataIntegrityViolationCustomException e) {
        return e.handleResponse(e.getMessage() ,"Data integrity violation" , HttpStatus.BAD_REQUEST);
    }

    //General Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e){
        return handleResponseException(e.getMessage() ,"An unexpected error occurred.: " ,HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException e){
        return handleResponseException(e.getMessage() ,"illegal or inappropriate argument.: " ,HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e){
        return handleResponseException(e.getMessage() ,"File operation error: " ,HttpStatus.BAD_REQUEST) ;
    }


    public ResponseEntity<Object> handleResponseException(String errors , String message ,@NotNull HttpStatus status){
        List<String> details = new ArrayList<>();
        details.add(errors);

        ApiError apiError = ApiError.builder()
                .errorMessage(message)
                .errors(details)
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .build();
        return ResponseEntityBuilder.buildResponse(apiError) ;
    }

}
