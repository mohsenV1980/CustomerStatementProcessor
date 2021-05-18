package com.mohsen.customer.statementprocessor.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mohsen.customer.statementprocessor.entity.StatementResponse;



@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
								        HttpMessageNotReadableException ex, HttpHeaders headers,
								        HttpStatus status, WebRequest request) {
    	
    	StatementResponse response=new StatementResponse();
        response.setResult("BAD_REQUEST");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    	
    	
    }
}