package com.buzzword;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

abstract class Record extends Name{
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int creatorId;
    @JsonIgnore
    private String creationDate;
    @JsonIgnore
    private Boolean isEdited;

    private XssSanitizer mySanitizer;

    /* Constructor */
    public Record() {
        mySanitizer = new XssSanitizerImpl();
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
