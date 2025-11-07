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
 * Interface for accessing logging.
 * <p>
 * Provides methods to produce logs with the following levels:
 * <ul>
 * <li>Trace - Highly granular, only for debugging.</li>
 * <li>Debug - High level debugging messages.</li>
 * <li>Info - Level for standard events.</li>
 * <li>Warn - Level for potential issues.</li>
 * <li>Error - Level for actual issues.</li>
 * </ul>
 * <p>
 * Messages must be Strings.
 * @author Ted Bjurlin
 */
public interface Logger {

    /**
     * Logs a message at the trace level.
     * <p>
     * Used for highly granular logging during debugging. Disable in production.
     * @param message message to send to the log.
     */
    public abstract void trace(String message);

    /**
     * Logs a message at the debug level.
     * <p>
     * Used for less detailed logging in debugging. Ex. Identifying which functions
     * are called, which if/else branches are followed. Disable in production.
     * @param message message to send to the log.
     */
    public abstract void debug(String message);
    
    /**
     * Logs a message at the info level.
     * <p>
     * Used for the logging of events that occur in the normal execution of the program.
     * @param message message to send to the log.
     */
    public abstract void info(String message);

    /**
     * Logs a message at the warn level.
     * <p>
     * Used for the logging of potential problems or risks that do not impact the immediate
     * ability of the program to function.
     * @param message message to send to the log.
     */
    public abstract void warn(String message);

    /**
     * Logs a message at the error level.
     * <p>
     * Used for the logging of errors that arise during the execution of the program.
     * @param message message to send to the log.
     */
    public abstract void error(String message);
}
