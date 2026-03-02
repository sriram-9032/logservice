package com.dtt.logs.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleEverything(Exception ex) {
        // Log the exception name to your console so you can see what it is!
        System.out.println("RESTler triggered: " + ex.getClass().getName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Input error: Please send Request Body Correctly");
    }


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateError(DateTimeParseException ex) {
        return ResponseEntity.badRequest().body("Invalid date format provided.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Parameter '" + ex.getName() + "' has invalid value.");
    }

    // Catches errors during JSON deserialization (Jackson errors)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Malformed JSON request or invalid data types.");
    }
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<Object> handleIndexOutOfBounds(IndexOutOfBoundsException ex) {
        return ResponseEntity.badRequest().body("Invalid data format: String length issue.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        // This catches "Upsert failed" and other unexpected logic crashes
        return ResponseEntity.badRequest().body("Processing error: " + ex.getMessage());
    }

}
