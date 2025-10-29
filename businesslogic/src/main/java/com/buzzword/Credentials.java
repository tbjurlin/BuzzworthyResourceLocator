package com.buzzword;

public class Credentials {
    private Integer id;
    private String firstName;
    private String lastName;
    private String title;
    private String department;
    private String location;
    private String systemRole;

    public Credentials(Integer id, String firstName, String lastName, String title, String department, String location) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setTitle(title);
        setDepartment(department);
        setLocation(location);
        setSystemRole();
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
