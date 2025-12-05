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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * This abstract Record class is the parent class for the Resource,
 * Comment, ReviewFlag, and Upvote classes and contains the 
 * business logic for the common fields associated with a Record.
 * <p>
 * @author Dennis Shelby
 * @version 1.0
 */
abstract class Record{
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Name firstName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Name lastName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int creatorId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date creationDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean currentUserCanDelete;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean currentUserCanEdit;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isEdited;

    private final Logger logger = LoggerFactory.getEventLogger();

    /**
     * Constructs a new Record object with default values.
     * <p>
     * Initializes the creation date to the current date, sets isEdited to false,
     * and initializes the first and last name objects.
     */
    public Record() {
        this.creationDate = new Date();
        this.isEdited = false;
        logger.debug("finishing default constructor");
        firstName = new Name();
        lastName = new Name();
        currentUserCanDelete = false;
        isEdited = false;
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
     * Returns the first name.
     * @return the first name
     */
    public String getFirstName() {
        return firstName.getName();
    }

    /**
     * Sets the first name.
     * @param name the first name to set
     */
    public void setFirstName(String name) {
        firstName.setName(name);
    }

    /**
     * Returns the last name.
     * @return the last name
     */
    public String getLastName() {
        return lastName.getName();
    }

    /**
     * Sets the last name.
     * @param name the last name to set
     */
    public void setLastName(String name) {
        lastName.setName(name);
    }

    /**
     * Returns the creatorId for the Record.
     * @return the creator ID
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
     * Returns the creationDate value for the Record.
     * @return the creation date
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
     * Returns the isEdited value for the Record.
     * @return true if the record has been edited, false otherwise
     */
    public Boolean getIsEdited() {
        logger.debug("returning isEdited: " + isEdited);
        return isEdited;
    }

    /**
     * Sets the is edited value for the record.
     * @param isEdited the value to set into the isEdited field
     */
    public void setIsEdited(boolean isEdited) {
        logger.debug("setting isEdited");
        this.isEdited = isEdited;
    }

    /**
     * Returns the currentUserCanDelete value for the Record.
     * @return true if the current user can delete this record, false otherwise
     */
    public boolean getCurrentUserCanDelete() {
        logger.debug("returning currentUserCanDelete: " + currentUserCanDelete);
        return currentUserCanDelete;
    }

    /**
     * Sets the currentUserCanDelete value for the Record.
     * @param currentUserCanDelete the value to set into the currentUserCanDelete field
     */
    public void setCurrentUserCanDelete(boolean currentUserCanDelete) {
        logger.debug("setting currentUserCanDelete");
        this.currentUserCanDelete = currentUserCanDelete;
    }

    /**
     * Returns the currentUserCanEdit value for the Record.
     * @return true if the current user can edit this record, false otherwise
     */
    public boolean getCurrentUserCanEdit() {
        logger.debug("returning currentUserCanEdit: " + currentUserCanEdit);
        return currentUserCanEdit;
    }

    /**
     * Sets the currentUserCanEdit value for the Record.
     * @param currentUserCanEdit the value to set into the currentUserCanEdit field
     */
    public void setCurrentUserCanEdit(boolean currentUserCanEdit) {
        logger.debug("setting currentUserCanEdit");
        this.currentUserCanEdit = currentUserCanEdit;
    }
}
