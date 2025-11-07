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

import org.apache.logging.log4j.LogManager;

/**
 * Implementation of {@link Logger} using Log4J.
 * <p>
 * Logs messages to the Log4J logger using the same log
 * levels as defined in {@link Logger}.
 * 
 * @author Ted Bjurlin
 */
public class LoggerImpl implements Logger {

    /**
     * The Log4J logger instance to log messages to.
     */
    private final org.apache.logging.log4j.Logger logger;

    /**
     * Creates a LoggerImpl that logs messages to the named Log4J logger.
     * @param loggerName the name of the logger to send log messages to.
     */
    public LoggerImpl(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String message) {
        logger.trace("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String message) {
        logger.debug("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message) {
        logger.info("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message) {
        logger.warn("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message) {
        logger.error("{}", message);
    }
    
}
