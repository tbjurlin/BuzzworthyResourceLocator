package com.buzzword;

import java.util.List;

public interface ResourceDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);

    /**
     * A contributor or admin may insert a resource into the database.
     * <p>
     * This resource contains information such as a title, the description, and Url link.
     * It will also contain the id of the user who uploaded the resource as well as it's date of creation.
     * 
     * @param user credentials of the user
     * @param resource resource to be inserted
    */
    void insertResource(Credentials user, Resource resource);

    /**
     * A contributor may delete their own resource or an admin may remove any resource.
     * 
     * @param user credentials of the user
     * @param resource resource to be removed
    */
    void removeResource(Credentials user, int id);

    /**
     * List all resources available in the system.
     * 
     * @param user credentials of the user
     * @return list of all resources
    */
    List<Resource> listAllResources(Credentials user);
}