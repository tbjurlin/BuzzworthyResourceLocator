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
abstract public class Name {
    @JsonIgnore
    @JsonProperty("fName")
    @JsonAlias({"fName"})
    private String firstName;
    @JsonIgnore
    @JsonProperty("lName")
    @JsonAlias({"lName"})
    private String lastName;

    private XssSanitizer mySanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();

    /* Constructor */
    public Name() {
        mySanitizer = new XssSanitizerImpl();
        logger.debug("finishing default constructor");
    }

    /**
     * Validates a first or last name string.
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
     * @param name is a first or last name
     * @throws IllegalArgumentException if the name is invalid
     */
    private String validateName(final String name) {
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
        
        return santizedName;
    } 

    /**
     * Returns the firstName value for the Record
     * @return firstName
     */
    public String getFirstName() {
        logger.debug("returning the first name: " + firstName);
        return firstName;
    }

    /**
     * Sets the first name value for the creator.
     * <br>
     * <br>
     * The business rules are:
     * <ul>
     *   <li>the first name must <strong>not</strong> be null</li>
     *   <li>the first name must <strong>not</strong> be empty</li>
     *   <li>the first name must max length of 40 chars</li>
     *   <li>XSS strings within the first name will be removed</li>
     * </ul>
     *
     * @param firstName the value to set into the firstName field
     * @throws IllegalArgumentException if the first name is invalid
     */
    public void setFirstName(final String firstName) {
        logger.debug("setting the first name");
        this.firstName = validateName(firstName);
    }

    /**
     * Returns the lastName value for the Record
     * @return lastName
     */
    public String getLastName() {
        logger.debug("returning the last name: " + lastName);
        return lastName;
    }

    /**
     * Sets the last name value for the creator.
     * <br>
     * <br>
     * The business rules are:
     * <ul>
     *   <li>the last name must <strong>not</strong> be null</li>
     *   <li>the last name must <strong>not</strong> be empty</li>
     *   <li>the last name must max length of 40 chars</li>
     *   <li>XSS strings within the last name will be removed</li>
     * </ul>
     *
     * @param lastName the value to set into the lastName field
     * @throws IllegalArgumentException if the last name is invalid
     */
    public void setLastName(final String lastName) {
        logger.debug("setting the last name");
        this.lastName = validateName(lastName);
    }

}
