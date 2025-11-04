package com.buzzword;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class Credentials extends Name{
    
    @JsonProperty("id")
    @JsonAlias({"id"})
    private Integer id;
    // @JsonProperty("fName")
    // @JsonAlias({"fName"})
    // private String firstName;
    // @JsonProperty("lName")
    // @JsonAlias({"lName"})
    // private String lastName;
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

    private XssSanitizer mySanitizer;

    /* Constructor */
    public Credentials() {
        mySanitizer = new XssSanitizerImpl();
    }

    /**
     * Returns the id for the Credentials
     * <p>
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id for the Credentials
     * <p>
     * The business rules are:
     * <ul>
     *   <li>the id must be non-negative</li>
     * </ul>
     *
     * @param id the value to set into the id field
     * @throws IllegalArgumentException if the id is invalid
     */
    public void setId(Integer id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be non-negative");
        }
        this.id = id;
    }

    // public String getFirstName() {
    //     return firstName;
    // }

    // public void setFirstName(String firstName) {
    //     // TODO: Add validation logic for firstName
    //     this.firstName = firstName;
    // }

    // public String getLastName() {
    //     return lastName;
    // }

    // public void setLastName(String lastName) {
    //     // TODO: Add validation logic for lastName
    //     this.lastName = lastName;
    // }

    /**
     * Returns the title of the Credentials
     * <p>
     * 
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the Credentials
     * <p>
     * The business rules are:
     * <ul>
     *   <li>the name must have a max length of 64 chars</li>
     *   <li>XSS strings within the title will be removed</li>
     * </ul>
     * @param title
     * @throws IllegalArgumentException if the title is invalid
     */
    public void setTitle(String title) {
        final int maxLenth = 64;

        String sanitizedTitle = mySanitizer.sanitizeInput(title);

        if (sanitizedTitle.length() > maxLenth ) {
            throw new IllegalArgumentException("name must not exceed 64 characters");
        }

        this.title = sanitizedTitle;
    }

    /**
     * Returns the department of the Credentials
     * <p>
     * @return department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the Credentials
     * <p>
     * The business rules are:
     * <ul>
     *   <li>the department must have a max length of 64 chars</li>
     *   <li>XSS strings within the department will be removed</li>
     * </ul>
     * @param department
     * @throws IllegalArgumentException if the department is invalid
     */
    public void setDepartment(String department) {
        final int maxLenth = 64;

        String sanitizedDepartment = mySanitizer.sanitizeInput(department);

        if (sanitizedDepartment.length() > maxLenth ) {
            throw new IllegalArgumentException("name must not exceed 64 characters");
        }

        this.department = sanitizedDepartment;
    }

    /**
     * Returns the location of the Credentials
     * <p>
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the Credentials
     * <p>
     * The business rules are:
     * <ul>
     *   <li>the location must max length of 64 chars</li>
     *   <li>XSS strings within the location will be removed</li>
     * </ul>
     * @param location
     * @throws IllegalArgumentException if the location is invalid
     */
    public void setLocation(String location) {
        final int maxLenth = 64;

        String sanitizedLocation = mySanitizer.sanitizeInput(location);

        if (sanitizedLocation.length() > maxLenth ) {
            throw new IllegalArgumentException("name must not exceed 64 characters");
        }
        this.location = sanitizedLocation;
    }
    
    /** 
     * Returns the system role of the user
     * <p>
     * @return systemRole
     */
    public String getSystemRole() {
        return systemRole;
    }

    /**
     * Sets the user's system role
     * <p>
     * The business rules are:
     * <ul>
     *   <li></li>
     * </ul>
     */
    private void setSystemRole() {
        // TODO: Add logic to compute system role
        // this.systemRole = Map.get(title);
    }
}
