package com.buzzword;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class Credentials {
    
    @JsonProperty("id")
    @JsonAlias({"id"})
    private Integer id;
    @JsonProperty("fName")
    @JsonAlias({"fName"})
    private String firstName;
    @JsonProperty("lName")
    @JsonAlias({"lName"})
    private String lastName;
    @JsonProperty("title")
    @JsonAlias({"title"})
    private String title;
    @JsonProperty("dept")
    @JsonAlias({"dept"})
    private String department;
    @JsonProperty("loc")
    @JsonAlias({"loc"})
    private String location;
    @JsonIgnore
    private String systemRole;

    public Credentials() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        // TODO: Add validation logic for id
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        // TODO: Add validation logic for firstName
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        // TODO: Add validation logic for lastName
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // TODO: Add validation logic for title
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        // TODO: Add validation for department
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        // TODO: Add validation for location
        this.location = location;
    }

    private void setSystemRole() {
        // TODO: Add logic to compute system role
        this.systemRole = "General User";
    }

    public String getSystemRole() {
        return systemRole;
    }
}
