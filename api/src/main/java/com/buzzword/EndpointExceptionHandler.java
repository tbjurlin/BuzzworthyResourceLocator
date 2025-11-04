package com.buzzword;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.beans.TypeMismatchException;

/**
 * This class catches and handles exceptions in order to provide
 * the appropriate HTTP error codes with helpful messages to the
 * end-user accessing the API.
 * 
 * @author Ben Edens
 * @version 1.0
 */
@ControllerAdvice
public class EndpointExceptionHandler {
    /**
     * Exception handler for the exception thrown when the expected requested resource cannot be found.
     * 
     * @param e A NoResourceFoundException.
     * @return  A JSON-formatted HTTP response with a 404 error code and message.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("Resource not found. Check that the request matches one of the REST operations outlined in the user documentation.");
    }

    /**
     * Exception handler for when required parameters are missing from HTTP request.
     * 
     * @param e A MissingServletRequestParameterException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("Missing required parameter for the requested operation.");
    }

    /**
     * Exception handler for when invalid parameters are supplied in HTTP request.
     * 
     * @param e A MethodArgumentNotValidException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("Invalid parameter provided for the requested operation.");
    }

    /**
     * Exception handler for when HTTP request body cannot be read.
     * 
     * @param e An HttpMessageNotReadableException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("Unable to read request body. Check that the request body matches the formatting outlined in the user documentation.");
    }

    /**
     * Exception handler for when user cannot be authenticated or lacks proper authorization.
     * 
     * @param e An AuthenticationException.
     * @return  A JSON-formatted HTTP response with a 401 error code and message.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("User not authorized to perform this request. Potential causes include invalid web token, inadequate user permissions, or inability to contact authentication services.");
    }

    /**
     * Exception handler for when the HTTP request format is recognized but uses an incorrect CRUD operation.
     * 
     * @param e An HttpRequestMethodNotSupportedException.
     * @return  A JSON-formatted HTTP response with a 405 error code and message.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("Request method not supported. Check that the request includes the appropriate CRUD operation and matches one of the REST operations outlined in the user documentation.");
    }

    /**
     * Exception handler for when an HTTP request parameter cannot be properly type-converted. 
     * 
     * @param e A TypeMismatchException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(TypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("Invalid parameter provided for the requested operation.");
    }

    /* 
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(e.toString());
    }*/

/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(e.toString());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(e.toString());
    } */
}
