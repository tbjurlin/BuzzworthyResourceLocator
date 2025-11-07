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
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.util.Map;

/**
 * The Credentials class creates an object that contains
 * credentials that can be used to determine a user's
 * authorization.
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class Credentials {
    
    @JsonProperty("id")
    @JsonAlias({"id"})
    private int id;
    @JsonProperty("fName")
    @JsonAlias({"fName"})
    private Name firstName;
    @JsonProperty("lName")
    @JsonAlias({"lName"})
    private Name lastName;
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

    private final Logger logger = LoggerFactory.getSecurityLogger();

    /* Constructor */
    public Credentials() {
        mySanitizer = new XssSanitizerImpl();
        logger.debug("finishing the default constructor");
        firstName = new Name();
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
     * Returns the id for the Credentials
     * <p>
     * @return id
     */
    public int getId() {
        logger.debug("returning the id: " + id);
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
    public void setId(int id) {
        logger.debug("setting the id");
        if (id < 0) {
            logger.error("id must be non-negative");
            throw new IllegalArgumentException("id must be non-negative");
        }
        this.id = id;
    }

    /**
     * Returns the title of the Credentials
     * <p>
     * 
     * @return title
     */
    public String getTitle() {
        logger.debug("returning the title: " + title);
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
        logger.debug("setting the title");
        final int maxLenth = 64;

        String sanitizedTitle = mySanitizer.sanitizeInput(title);

        if (sanitizedTitle.length() > maxLenth ) {
            logger.error("name must not exceed 64 characters");
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
        logger.debug("returning the department: " + department);
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
        logger.debug("setting the department");
        final int maxLenth = 64;

        String sanitizedDepartment = mySanitizer.sanitizeInput(department);

        if (sanitizedDepartment.length() > maxLenth ) {
            logger.error("name must not exceed 64 characters");
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
        logger.debug("returning the location: " + location);
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
        logger.debug("setting the location");
        final int maxLenth = 64;

        String sanitizedLocation = mySanitizer.sanitizeInput(location);

        if (sanitizedLocation.length() > maxLenth ) {
            logger.error("name must not exceed 64 characters");
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
        RoleConfiguration config = new RoleConfigurationImpl(ConfigurationManagerImpl.getInstance());
        Map<String, String> roleMap = config.getRoleMap();

        String systemRole = roleMap.get(title);

        if (systemRole != "Admin" && systemRole != "Contributor" && systemRole != "Commenter") {
            logger.error(String.format("User possesses invalid system role %s", systemRole));
            throw new IllegalArgumentException("Invalid system role");
        }
        this.systemRole = systemRole;
        logger.debug(String.format("setting the system role: %s", systemRole));

        logger.debug("returning the system role: " + systemRole);
        return systemRole;
    }
}
