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
 * Factory class to provide access to Loggers.
 * <p>
 * Provides access to the implementations of the security and event loggers.
 * 
 * @author Ted Bjurlin
 */
public class LoggerFactory {

    /**
     * The logger instance that logs security events.
     */
    private static Logger securityLogger;

    /**
     * The logger instance that logs application events.
     */
    private static Logger eventLogger;    

    /**
     * Provides singleton access to the security logger.
     * @return the logger instance for security
     */
    public static Logger getSecurityLogger() {
        if (securityLogger == null) {
            securityLogger = new LoggerImpl("com.buzzword.SecurityLogger");
        }
        return securityLogger;
    }

    /**
     * Provides singleton access to the event logger.
     * @return the logger instance for application events
     */
    public static Logger getEventLogger() {
        if (eventLogger == null) {
            eventLogger = new LoggerImpl("com.buzzword.EventLogger");
        }
        return eventLogger;
    }
}
