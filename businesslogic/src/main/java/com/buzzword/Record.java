package com.buzzword;

import java.util.Date;

abstract class Record {
    private int id;
    private int creatorId;
    private String creatorFirstName;
    private String creatorLastName;
    private Date creationDate;
    private Boolean isEdited;

    /* Constructor */
    public void Record() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        // TODO: Add validation for id
        self.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        // TODO: Add validation for creatorId
        self.creatorId = creatorId;
    }

    public String getCreatorFirstName() {
        return creatorFirstName;
    }

    public void setCreatorFirstName(String creatorFirstName) {
        // TODO: Add validation for creatorFirstName
        self.creatorFirstName = creatorFirstName;
    }

    public String getCreatorLastName() {
        return creatorLastName;
    }

    public void setCreatorLastName(String creatorLastName) {
        // TODO: Add validation for creatorLastName
        self.creatorLastName = creatorLastName;

    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        // TODO: Add validation for creationDate
        self.creationDate = creationDate;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(Boolean isEdited) {
        // TODO: Add validation for isEdited
        self.isEdited = isEdited;
    }
}
