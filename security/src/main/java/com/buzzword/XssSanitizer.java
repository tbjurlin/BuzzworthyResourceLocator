/**
 * This is the utility interface to sanitize String against xss attacks.
 * 
 * @author Janniebeth Melendez
 * @version 1.0
 */
package com.buzzword;


public interface XssSanitizer {

    /**
     * Scan Routine
     * 
     * @param input string that we are scanning
     * @return the clean/sanitized string
     */
    String sanitizeInput(String input);


    /**
     * Encode Routine
     * <p>
     * Sanitizes the ouput as a way to avoid unsafe HTML, as well as remove
     * whitespace to ensure safe and clean content is displayed.
     * 
     * @param input
     * @return
     */
    String sanitizeOutput(String input);

}
