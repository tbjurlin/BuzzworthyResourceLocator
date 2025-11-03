package com.buzzword;

import java.util.Date;

abstract class Record {
    private int id;
    private int creatorId;
    private String creatorFirstName;
    private String creatorLastName;
    private String creationDate;
    private Boolean isEdited;

    private XssSanitizer mySanitizer;

    /* Constructor */
    public Record() {
        this(new XssSanitizerImpl());
    }

    public Record(final XssSanitizer sanitizer) {
        mySanitizer = sanitizer;
    }

    /**
     * Returns id value for the Record
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for the Record.
     * <br>
     * <br>
     * The business rules are:
     * <ul>
     *   <li>the id must be non-negative</li>
     * </ul>
     *
     * @param id the value to set into the recordId field
     * @throws IllegalArgumentException if the record id is invalid
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be non-negative");
        }
        this.id = id;
    }

    /**
     * Returns the creatorId for the Record
     * @return creatorId
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Sets the creatorId value for the creator.
     * <br>
     * <br>
     * The business rules are:
     * <ul>
     *   <li>the creatorId must be non-negative</li>
     * </ul>
     *
     * @param creatorId the value to set into the creatorId field
     * @throws IllegalArgumentException if the id is invalid
     */
    public void setCreatorId(int creatorId) {
        if (creatorId < 0) {
            throw new IllegalArgumentException("creatorId must be non-negative");
        }
        this.creatorId = creatorId;
    }

    /**
     * Returns the creatorFirstName value for the Record
     * @return creatorFirstName
     */
    public String getCreatorFirstName() {
        return creatorFirstName;
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
     * @param creatorFirstName the value to set into the creatorFirstName field
     * @throws IllegalArgumentException if the first name is invalid
     */
    public void setCreatorFirstName(final String creatorFirstName) {
        final int maxLenth = 40;

        if (creatorFirstName == null) {
            throw new IllegalArgumentException("creatorFirstName must not be null.");
        }

        String sanitizedFirstName = mySanitizer.sanitizeInput(creatorFirstName);

        if (sanitizedFirstName.isEmpty()) {
            throw new IllegalArgumentException("creatorFirstName must not be empty.");
        }
        if (sanitizedFirstName.length() > maxLenth ) {
            throw new IllegalArgumentException("creatorFirstName must not exceed 40 characters");
        }
        
        this.creatorFirstName = sanitizedFirstName;
    }

    /**
     * Returns the creatorLastName value for the Record
     * @return creatorLastName
     */
    public String getCreatorLastName() {
        return creatorLastName;
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
     * @param creatorLastName the value to set into the creatorLastName field
     * @throws IllegalArgumentException if the last name is invalid
     */
    public void setCreatorLastName(String creatorLastName) {
        final int maxLenth = 40;
        final int minLenth = 2;

        if (creatorLastName == null) {
            throw new IllegalArgumentException("creatorLastName must not be null.");
        }

        String sanitizedLastName = mySanitizer.sanitizeInput(creatorLastName);

        if (sanitizedLastName.isEmpty()) {
            throw new IllegalArgumentException("creatorLastName must not be empty.");
        }
        if (sanitizedLastName.length() < minLenth ) {
            throw new IllegalArgumentException("creatorLastName must not conatin fewer than 2 characters");
        }
        if (sanitizedLastName.length() > maxLenth ) {
            throw new IllegalArgumentException("creatorLastName must not exceed 40 characters");
        }
        
        this.creatorLastName = sanitizedLastName;

    }

    /**
     * Returns the creationDate value for the Record
     * @return creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date value for the Record.
     * <p>
     * The business rules are:
     * <ul>
     *   <li>if the creation date is not provided, the current date will be used.</li>
     *   <li>the creation date must <strong>today or earlier</strong></li>
     * </ul>
     * 
     * @param creationDate the value to set into the creationDate field
     * @throws IllegalArgumentException if the creation date is invalid
     */
    public void setCreationDate() {
        // TODO: Set creationDate to the current date
    }

    public void setCreationDate(String creationDate) {
        // TODO: Add validation for creationDate
        this.creationDate = creationDate;
    }

    /**
     * Returns the isEdited value for the Record
     * @return isEdited
     */
    public Boolean getIsEdited() {
        return isEdited;
    }

    /**
     * Sets the is edited value for the record.
     * <p>
     * The business rules are:
     * <ul>
     *   <li>the is edited value must...</li>
     * </ul>
     * 
     * @param isEdited the value to set into the isEdited field
     * @throws IllegalArgumentException if the is edited value is invalid
     */
    public void setIsEdited(Boolean isEdited) {
        // TODO: Add validation for isEdited
        this.isEdited = isEdited;
    }
}
