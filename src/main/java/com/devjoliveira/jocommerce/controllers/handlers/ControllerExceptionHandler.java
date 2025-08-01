package com.devjoliveira.jocommerce.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devjoliveira.jocommerce.dto.CustomError;
import com.devjoliveira.jocommerce.dto.ValidationError;
import com.devjoliveira.jocommerce.services.Exceptions.DatabaseException;
import com.devjoliveira.jocommerce.services.Exceptions.ForbiddenException;
import com.devjoliveira.jocommerce.services.Exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    CustomError err = new CustomError(Instant.now(), status.value(),
        e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<CustomError> database(DatabaseException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    CustomError err = new CustomError(Instant.now(), status.value(),
        e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e,
      HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

    // Java Spring Professional - Customazin validation errors
    // ****************************************************************************************
    ValidationError err = new ValidationError(Instant.now(), status.value(),
        "Invalid data", request.getRequestURI());

    for (FieldError f : e.getBindingResult().getFieldErrors()) {
      err.addError(f.getField(), f.getDefaultMessage());
    }

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.FORBIDDEN;
    CustomError err = new CustomError(Instant.now(), status.value(),
        e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

}
