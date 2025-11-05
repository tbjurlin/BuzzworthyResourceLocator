package com.buzzword;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * This class catches and handles exceptions in order to provide
 * the appropriate HTTP responses with error codes and helpful 
 * messages to the end-user accessing the API.
 * 
 * @author Ben Edens
 * @version 1.0
 */
@ControllerAdvice
public class EndpointExceptionHandler {

    /*
     * =======================================================================================
     *      400 Errors (BAD REQUEST)
     * =======================================================================================
     */

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
                             .body("{\"errorMsg\": \"Missing required parameter for the requested operation.\"}");
    }

    /**
     * Exception handler for when required headers are missing from HTTP request.
     * 
     * @param e A MissingRequestHeaderException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Missing required header for the requested operation.\"}");
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
                             .body("{\"errorMsg\": \"Invalid parameter provided for the requested operation.\"}");
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
                             .body("{\"errorMsg\": \"Unable to read request body. Check that the request body matches the formatting outlined in the user documentation.\"}");
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
                             .body("{\"errorMsg\": \"Invalid parameter provided for the requested operation.\"}");
    }


    
    /*
     * =======================================================================================
     *      401 Errors (UNAUTHORIZED)
     * =======================================================================================
     */

    /**
     * Exception handler for when user cannot be authenticated.
     * 
     * @param e An AuthenticationException.
     * @return  A JSON-formatted HTTP response with a 401 error code and message.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Unable to authenticate user. Potential causes include invalid web token or inability to contact authentication services.\"}");
    }



    /*
     * =======================================================================================
     *      403 Errors (FORBIDDEN)
     * =======================================================================================
     */

    /**
     * Exception handler for when user lacks proper authorization.
     * 
     * @param e An AuthorizationException.
     * @return  A JSON-formatted HTTP response with a 403 error code and message.
     */
    /*@ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"User lacks necessary permissions to perform request.\"}");
    }*/

    /*
     * =======================================================================================
     *      404 Errors (NOT FOUND)
     * =======================================================================================
     */

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
                             .body("{\"errorMsg\": \"Resource not found. Check that the request matches one of the REST operations outlined in the user documentation.\"}");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handlesNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Resource not found. Check that the request matches one of the REST operations outlined in the user documentation.\"}");
    }

    /*
     * =======================================================================================
     *      405 Errors (METHOD NOT ALLOWED)
     * =======================================================================================
     */

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
                             .body("{\"errorMsg\": \"Request method not supported. Check that the request includes the appropriate CRUD operation and matches one of the REST operations outlined in the user documentation.\"}");
    }



    /*
     * =======================================================================================
     *      415 Errors (UNSUPPORTED MEDIA TYPE)
     * =======================================================================================
     */

    /**
     * Exception handler for when the HTTP request uses an unsupported media type.
     * 
     * @param e An HttpMediaTypeNotSupportedException.
     * @return  A JSON-formatted HTTP response with a 415 error code and message.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Request media type not supported. Please use application/json media type.\"}");
    }


    
    /*
     * =======================================================================================
     *      500 Errors (INTERNAL SERVER ERROR)
     * =======================================================================================
     */






/*  Exception Handler Template

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(e.toString());
    } */
}
