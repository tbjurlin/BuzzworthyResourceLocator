package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

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

    private final Logger logger = LoggerFactory.getEventLogger();
    private final Logger securityLogger = LoggerFactory.getSecurityLogger();

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
        logger.error("Returning HTTP response code 400: Missing required parameter for the requested operation.");
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
        logger.error("Returning HTTP response code 400: Missing required header for the requested operation.");
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
        logger.error("Returning HTTP response code 400: Invalid parameter provided for the requested operation.");
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
        logger.error("Returning HTTP response code 400: Unable to read request body. Check that the request body matches the formatting outlined in the user documentation.");
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
        logger.error("Returning HTTP response code 400: Invalid parameter provided for the requested operation.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Invalid parameter provided for the requested operation.\"}");
    }

    /**
     * Exception handler for when a record being added already exists. 
     * 
     * @param e A RecordAlreadyExistsException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<String> handleRecordAlreadyExistsException(RecordAlreadyExistsException e) {
        logger.error("Returning HTTP response code 400: The record to be added already exists.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"The record to be added already exists.\"}");
    }

    /**
     * Exception handler for when a requested record does not exist. 
     * 
     * @param e A RecordDoesNotExistException.
     * @return  A JSON-formatted HTTP response with a 400 error code and message.
     */
    @ExceptionHandler(RecordDoesNotExistException.class)
    public ResponseEntity<String> handleRecordDoesNotExistException(RecordDoesNotExistException e) {
        logger.error("Returning HTTP response code 400: The requested record does not exist.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"The requested record does not exist.\"}");
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
        securityLogger.error("Returning HTTP response code 401: Unable to authenticate user. Potential causes include invalid web token or inability to contact authentication services.");
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
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException e) {
        securityLogger.error("Returning HTTP response code 403: User lacks necessary permissions to perform request.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"User lacks necessary permissions to perform request.\"}");
    }

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
        logger.error("Returning HTTP response code 404: Resource not found. Check that the request matches one of the REST operations outlined in the user documentation.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Resource not found. Check that the request matches one of the REST operations outlined in the user documentation.\"}");
    }

    /**
     * Exception handler for when no handler is found for the request.
     * 
     * @param e A NoHandlerFoundException.
     * @return  A JSON-formatted HTTP response with a 404 error code and message.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handlesNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error("Returning HTTP response code 404: Resource not found. Check that the request matches one of the REST operations outlined in the user documentation.");
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
        logger.error("Returning HTTP response code 405: Request method not supported. Check that the request includes the appropriate CRUD operation and matches one of the REST operations outlined in the user documentation.");
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
        logger.error("Returning HTTP response code 415: Request media type not supported. Please use application/json media type.");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Request media type not supported. Please use application/json media type.\"}");
    }


    
    /*
     * =======================================================================================
     *      500 Errors (INTERNAL SERVER ERROR)
     * =======================================================================================
     */

    /**
     * Exception handler for when an internal error occurs due to an illegal argument.
     * 
     * @param e An IllegalArgumentException.
     * @return  A JSON-formatted HTTP response with a 500 error code and message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Returning HTTP response code 500: Fatal internal error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Fatal internal error occurred.\"}");
    }

    /**
     * Exception handler for when an internal error occurs due to a null pointer.
     * 
     * @param e A NullPointerException.
     * @return  A JSON-formatted HTTP response with a 500 error code and message.
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        logger.error("Returning HTTP response code 500: Fatal internal error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"errorMsg\": \"Fatal internal error occurred.\"}");
    }

}
