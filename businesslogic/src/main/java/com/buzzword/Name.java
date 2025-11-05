package com.buzzword;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This abstract Name class is the parent class for the Record
 * and Credentials classes. It contains the business logic for 
 * handling first and last names.
 * <p>
 * @author Dennis Shelby
 * @version 1.0
 */
public class Name {
    private String nameString;

    private XssSanitizer mySanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();


    public Name() {
        mySanitizer = new XssSanitizerImpl();
    }

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
     *   <li>the name must max length of 64 chars</li>
     *   <li>XSS strings within the first name will be removed</li>
     * </ul>
     *
     * @param name is a generic name
     * @throws IllegalArgumentException if the name is invalid
     */
    public void setName(String name) {
        logger.debug("setting the name");
        final int maxLenth = 64;

        if (name == null) {
            logger.error("name must not be null.");
            throw new IllegalArgumentException("name must not be null.");
        }

        String santizedName = mySanitizer.sanitizeInput(name);

        if (santizedName.isEmpty()) {
            logger.error("name must not be empty.");
            throw new IllegalArgumentException("name must not be empty.");
        }
        if (santizedName.length() > maxLenth ) {
            logger.error("name must not exceed 64 characters");
            throw new IllegalArgumentException("name must not exceed 64 characters");
        }
        
        // return santizedName;
        this.nameString = santizedName;
    }

    public String getName() {
        logger.debug("returning the name: " + nameString);
        return nameString;
    }

}
