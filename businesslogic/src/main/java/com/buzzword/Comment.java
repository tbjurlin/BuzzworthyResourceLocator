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
 * The Comment class creates a comment object that holds the content of a 
 * comment as well as all fields from the Record class.
 * 
 * @author Dennis Shelby
 * @version 1.0
 */
public class Comment extends Record {
    private String contents;

    private final XssSanitizer commentSanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();

    /**
     * Constructs a new Comment object with default values.
     */
    public Comment() {
        super();
        commentSanitizer = new XssSanitizerImpl();
    }

    /**
     * Validates the contents of the comment.
     * The contents must not be null, must have a minimum length of 1 character,
     * and must not exceed 200 characters.
     * 
     * @param contents the contents to validate
     * @return the sanitized and validated contents
     */
    private String validateContents(final String contents) {
        final int minLength = 1;
        final int maxLength = 200;

        if (contents == null) {
            logger.error("Contents must not be null");
            throw new IllegalArgumentException("Contents must not be null.");
        }

        String sanitizedContents = commentSanitizer.sanitizeInput(contents);

        if (sanitizedContents.isEmpty()) {
            logger.error("Contents must not be empty");
            throw new IllegalArgumentException("Contents must not be empty.");
        }
        if(sanitizedContents.length() < minLength ) {
            logger.error("Contents must be at least 1 character long");
            throw new IllegalArgumentException("Contents must be at least 1 character long");
        }
        if (sanitizedContents.length() > maxLength ) {
            logger.error("Contents must not exceed 200 characters");
            throw new IllegalArgumentException("Contents must not exceed 200 characters");
        }

        return sanitizedContents;
    }
    
    /**
     * Gets the contents of the comment.
     * 
     * @return the comment contents
     */
    public String getContents() {
        logger.debug("returning the contents");
        return contents;
    }
    
    /**
     * Sets the contents of the comment.
     * <p>
     * The contents will be validated and sanitized before being set.
     * 
     * @param contents the comment contents to set
     */
    public void setContents(String contents) {
        logger.debug("setting contents");
        this.contents = validateContents(contents);
    }
}
