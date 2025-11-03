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
    Boolean removeResource(Credentials user, Resource resource);

    /**
     * @param query search query
     * @return list of resources that match the query
    */
    List<Resource> searchResources(String query);
}