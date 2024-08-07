package com.example.startingclubbackend.exceptions;

import com.example.startingclubbackend.exceptions.custom.*;
import com.example.startingclubbackend.exceptions.responseHandling.ApiError;
import com.example.startingclubbackend.exceptions.responseHandling.ResponseEntityBuilder;
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
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<Object> handleEmailSendingException(EmailSendingException e){
        return e.handleResponse(e.getMessage(),"failed to send email",HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler(EventAlreadyOccurredException.class)
    public ResponseEntity<Object> handleEventAlreadyOccurredException(EventAlreadyOccurredException e){
        return e.handleResponse(e.getMessage(),"Event Update Error",HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<Object> handleFileValidationException(FileValidationException e){
        return e.handleResponse(e.getMessage() , "File Validation Failed" , HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException e){
        return handleResponseException(e.getMessage() ,"illegal or inappropriate argument.: " ,HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e){
        return handleResponseException(e.getMessage() ,"File operation error: " ,HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<Object> handleDatabaseException(@NotNull DatabaseException e) {
        return e.handleResponse(e.getMessage() ,"Internal Server Error" , HttpStatus.INTERNAL_SERVER_ERROR);
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
