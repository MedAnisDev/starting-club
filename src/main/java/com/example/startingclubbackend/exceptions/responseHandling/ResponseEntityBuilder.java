package com.example.startingclubbackend.exceptions.responseHandling;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    public static ResponseEntity<Object> buildResponse(ApiError apiError){

        HttpStatusCode status = HttpStatusCode.valueOf(apiError.getStatusCode()) ;
        return new ResponseEntity<>(apiError , status) ;
    }
}
