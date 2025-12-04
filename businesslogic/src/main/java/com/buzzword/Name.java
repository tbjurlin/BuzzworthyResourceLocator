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
 * This Name class defines a generic name object that 
 * can be used to create a first or last name.
 * <p>
 * @author Dennis Shelby
 * @version 1.0
 */
public class Name {
    private String nameString = "Name";

    private XssSanitizer mySanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();

    /**
     * Constructs a new Name object with default values.
     */
    public Name() {
        mySanitizer = new XssSanitizerImpl();
    }

    /**
     * Constructs a new Name object with the specified name string.
     * 
     * @param nameString the initial name value
     */
    public Name(String nameString) {
        mySanitizer = new XssSanitizerImpl();
        this.nameString = nameString;
    }

     /**
     * Validates a generic name string.
     * <br>
     * <br>
     * The business rules are:
     * <ul>
     *   <li>the name must <strong>not</strong> be null</li>
     *   <li>the name must <strong>not</strong> be empty</li>
     *   <li>the name must have a max length of 64 chars</li>
     *   <li>XSS strings within the first name will be removed</li>
     * </ul>
     *
     * @param name a generic name
     * @throws IllegalArgumentException if the name is invalid
     */
    public void setName(String name) {
        logger.debug("setting the name");
        final int maxLength = 64;

        if (name == null) {
            logger.error("name must not be null.");
            throw new IllegalArgumentException("name must not be null.");
        }

        String sanitizedName = mySanitizer.sanitizeInput(name);

        if (sanitizedName.isEmpty()) {
            logger.error("name must not be empty.");
            throw new IllegalArgumentException("name must not be empty.");
        }
        if (sanitizedName.length() > maxLength) {
            logger.error("name must not exceed 64 characters");
            throw new IllegalArgumentException("name must not exceed 64 characters");
        }
        
        this.nameString = sanitizedName;
    }

    /**
     * Returns the name.
     * @return the name string
     */
    public String getName() {
        logger.debug("returning the name: " + nameString);
        return nameString;
    }

}
