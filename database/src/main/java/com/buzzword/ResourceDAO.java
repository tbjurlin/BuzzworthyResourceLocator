package com.buzzword;

import java.util.List;

public interface ResourceDAO {

    /**
     * @param user credentials of the user
     * @param resource resource to be inserted
    */
    Void insertResource(Credentials user, Resource resource);

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