package com.buzzword;

abstract public class Name {
    String firstName;
    String lastName;

    private XssSanitizer mySanitizer;

    /* Constructor */
    public Name() {
        mySanitizer = new XssSanitizerImpl();
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
            throw new IllegalArgumentException("name must not be null.");
        }

        String santizedName = mySanitizer.sanitizeInput(name);

        if (santizedName.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty.");
        }
        if (santizedName.length() > maxLenth ) {
            throw new IllegalArgumentException("name must not exceed 40 characters");
        }
        
        return santizedName;
    } 

    /**
     * Returns the firstName value for the Record
     * @return firstName
     */
    public String getFirstName() {
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
        
        this.firstName = validateName(firstName);
    }

    /**
     * Returns the lastName value for the Record
     * @return lastName
     */
    public String getLastName() {
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
        
        this.lastName = validateName(lastName);
    }

}
