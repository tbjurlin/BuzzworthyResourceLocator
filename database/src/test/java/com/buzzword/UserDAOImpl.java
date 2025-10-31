/**
 * This is the UserDAO Implementation for the 
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */
package com.buzzword;

import java.util.List;

//will get passed a mongo database object

public abstract class UserDAOImpl 
    implements UserDAO {

    
    public Void insertResource(Credentials user, Resource resource) {
        // TODO
        return null;
    }
    public Boolean removeResource(Credentials user, Resource resource) {
        // TODO
        return null;
    }
    public Void addComment(Credentials user, Comment comment) {
        // TODO
        return null;
    }
    public Boolean removeComment(Credentials user, Comment comment) {
        // TODO
        return null;
    }
    public Void addUpVote(Credentials user, Resource resource) {
        // TODO
        return null;
    }
    public Void removeUpVote(Credentials user, Resource resource) {
        // TODO
        return null;
    }
    public Void addReviewFlag(Credentials user, Resource resource) {
        // TODO
        return null;
    }
    public Void removeReviewFlag(Credentials user, Resource resource) {
        // TODO
        return null;
    }

    public List<Resource> searchByAll(String query) {
        // TODO
        return null;
    }

}
