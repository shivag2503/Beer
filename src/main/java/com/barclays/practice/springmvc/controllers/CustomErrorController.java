package com.barclays.practice.springmvc.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity handleJPAErrors(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();
        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ce = (ConstraintViolationException) exception.getCause().getCause();

            List errors = ce.getConstraintViolations().stream()
                    .map(constrains -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(constrains.getPropertyPath().toString(), constrains.getMessage());
                        return errorMap;
                    }).collect(Collectors.toList());
            return bodyBuilder.body(errors);
        }
        return bodyBuilder.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception) {
        List errorList = exception.getFieldErrors().stream()
                .map(fieldErrors -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldErrors.getField(), fieldErrors.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }
}
