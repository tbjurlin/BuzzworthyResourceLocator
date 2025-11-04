package com.buzzword;

import java.util.List;

public interface ResourceDAO {

    /**
     * <p>
     * A contributor or admin may insert a resource into the database.
     * This resource contains information such as a title, the description, and Url link.
     * It will also contain the id of the user who uploaded the resource as well as it's date of creation.
     * 
     * @param user credentials of the user
     * @param resource resource to be inserted
    */
    void insertResource(Credentials user, Resource resource);

    /**
     * @param user credentials of the user
     * @param resource resource to be removed
    */
    boolean removeResource(Credentials user, long id);

    /**
     * List all resources available in the system.
     * @return list of all resources
    */
    List<Resource> listAllResources(Credentials user);
}