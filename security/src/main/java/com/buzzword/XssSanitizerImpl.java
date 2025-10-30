/**
 *This is the utility class to sanitize String against XSS attacks.
 * 
 * @author Janniebeth Melendez
 * @version 1.0
 */
package com.buzzword;

import java.io.IOExeption;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*Temp Notes (remove Later) 
 * 
 * Min size, max size, a string. Check that it's these things. If it is, it's sanitized.
 * The URL. It has to have the right parts! Https://, .org, .com, etc.
 * Take data from authentication methods and call setters to make the actual changes.
 * User credentials has a bunch of fields with set methods that you call with the data from the validation center
*/
public class XssSanitizerImpl
    implements XssSanitizer {
    
}
