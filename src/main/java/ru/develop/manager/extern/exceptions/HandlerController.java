package ru.develop.manager.extern.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class HandlerController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentialsException(BadCredentialsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<AppError> handleWrongDataException(WrongDataException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EnumConstantNotPresentException.class)
    public ResponseEntity<AppError> handleEnumConstantNotPresentException(EnumConstantNotPresentException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PermissionDeniedDataAccessException.class)
    public ResponseEntity<AppError> handlePermissionDeniedDataAccessException(PermissionDeniedDataAccessException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> methodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errorMap = new HashMap<>();

        log.error(e.getMessage());

        e.getBindingResult().getAllErrors().forEach(violation -> {
            String fieldName = ((FieldError) violation).getField();
            String errorMessage = violation.getDefaultMessage();
            errorMap.put(fieldName, errorMessage);
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();

        log.error(e.getMessage());

        e.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errorMap.put(fieldName, errorMessage);
        });
        return errorMap;
    }


}
