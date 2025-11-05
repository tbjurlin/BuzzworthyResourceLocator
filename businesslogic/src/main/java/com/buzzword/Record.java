package com.buzzword;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

/**
 * This abstract Record class is the parent class for the Resource,
 * Comment, ReviewFlag, and Upvote classes. It contains the 
 * business logic for common fields associated with a Record.
 * <p>
 * @author Dennis Shelby
 * @version 1.0
 */
abstract class Record extends Name{
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int creatorId;
    @JsonIgnore
    private Date creationDate;
    @JsonIgnore
    private boolean isEdited;

    private final Logger logger = LoggerFactory.getEventLogger();

    /* Constructor */
    public Record() {
        this.creationDate = new Date();
        this.isEdited = false;
        logger.debug("finishing default constructor");
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
     * @param id the value to set into the recordId field
     * @throws IllegalArgumentException if the record id is invalid
     */
    public void setId(int id) {
        logger.debug("setting the id");

        if (id < 0) {
            logger.error("id must be non-negative");
            throw new IllegalArgumentException("id must be non-negative");
        }
        this.id = id;
    }

    /**
     * Returns the creatorId for the Record
     * @return creatorId
     */
    public int getCreatorId() {
        logger.debug("returning the id: " + id);
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
        logger.debug(("setting the creatorId: " + creatorId));
        if (creatorId < 0) {
            logger.error("creatorId must be non-negative");
            throw new IllegalArgumentException("creatorId must be non-negative");
        }
        this.creatorId = creatorId;
    }

    /**
     * Returns the creationDate value for the Record
     * @return creationDate
     */
    public Date getCreationDate() {
        logger.debug("returns the creationDate: " + creationDate);
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
    public void setCreationDate(Date creationDate) {
        logger.debug("setting the creationDate: " + creationDate);
        if (creationDate == null) {
            logger.error("creationDate must not be null");
            throw new IllegalArgumentException("creationDate must not be null.");
        }
        Date nowDate = new Date();
        if (creationDate.after(nowDate)) {
            logger.error("creationDate must not be in the future");
            throw new IllegalArgumentException("creationDate must not be in the future.");
        }
        this.creationDate = creationDate;
    }

    /**
     * Returns the isEdited value for the Record
     * @return isEdited
     */
    public Boolean getIsEdited() {
        logger.debug("returning isEdited: " + isEdited);
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
     */
    public void setIsEdited(boolean isEdited) {
        logger.debug("setting isEdited");
        this.isEdited = isEdited;
    }
}
