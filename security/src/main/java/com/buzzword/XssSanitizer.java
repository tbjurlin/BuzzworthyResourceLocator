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

import org.jsoup.safety.Safelist;

/**
 * This is the utility interface to sanitize String against xss attacks.
 * 
 * @author Janniebeth Melendez
 * @version 1.0
 */
public interface XssSanitizer {

    /**
	 * Returns the current rules.
	 * <p>
	 * @return the current rules
	 */
	Safelist getRules();

    /**
	 * Loads a new rules.
	 * <p>
	 * @param rules the rules definition to load
	 */
	void setRules(Safelist rules);

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
