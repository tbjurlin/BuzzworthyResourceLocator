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

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private boolean isEdited;

    private final Logger logger = LoggerFactory.getEventLogger();

    /* Constructor */
    public Record() {
        this.creationDate = new Date();
        this.isEdited = false;
        logger.debug("finishing default constructor");
        firstName = new Name();
        lastName = new Name();
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
     * <p>
     * @return
     */
    public String getFirstName() {
        return firstName.getName();
    }

    /**
     * Sets the first name
     * <p>
     * @param name
     */
    public void setFirstName(String name) {
        firstName.setName(name);
    }

    /**
     * Returns the last name
     * <p>
     * @return lastName
     */
    public String getLastName() {
        return lastName.getName();
    }

    /**
     * Sets the last name
     * <p>
     * @param name
     */
    public void setLastName(String name) {
        lastName.setName(name);
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
