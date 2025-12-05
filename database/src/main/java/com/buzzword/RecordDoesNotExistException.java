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

/**
 * Exception thrown when attempting to access or modify a record that does not exist.
 * <p>
 * This runtime exception is thrown when database operations fail to find the requested
 * record, such as when trying to update, delete, or retrieve a non-existent resource,
 * comment, or other database entity.
 */
public class RecordDoesNotExistException extends RuntimeException {
    /**
     * Constructs a new RecordDoesNotExistException with no detail message.
     */
    public RecordDoesNotExistException() {
        super();
    }

    /**
     * Constructs a new RecordDoesNotExistException with the specified detail message.
     * @param message the detail message
     */
    public RecordDoesNotExistException(String message) {
        super(message);
    }

    /**
     * Constructs a new RecordDoesNotExistException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public RecordDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}