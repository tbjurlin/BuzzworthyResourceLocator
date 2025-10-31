/**
 * This is the UserDAO Interface for the 
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */

package com.buzzword;

import java.util.List;

/*All the use cases for user operations
 * add/remove a Resource
 * add/remove a Comment
 * add/remove an upVote
 * add/remove a reviewFlag
 * search by all, user, tags, phrase, date (get)
 * 
 */

public interface UserDAO {
    
    /**
     * @param user credentials of the user
     * @param resource resource to be inserted
     */
    Void insertResource(Credentials user, Resource resource);

    /**
     * @param user credentials of the user
     * @param resource resource to be removed 
     * @return true if resource was removed, false otherwise
    */
    Boolean removeResource(Credentials user, Resource resource);

    /**
     * @param user credentials of the user
     * @param comment comment to be inserted
    */
    Void addComment(Credentials user, Comment comment);

    /**
     * @param user credentials of the user
     * @param comment comment to be removed
     * @return true if comment was removed, false otherwise
    */
    Boolean removeComment(Credentials user, Comment comment);

    /**
     * @param user credentials of the user
     * @param resource resource to be upvoted
    */
    Void addUpVote(Credentials user, Resource resource);
    
    /**
     * @param user credentials of the user
     * @param resource resource to have upvote removed
    */
    Void removeUpVote(Credentials user, Resource resource);

    /**
     * @param user credentials of the user
     * @param resource resource to be flagged for review
    */
    Void addReviewFlag(Credentials user, Resource resource);

    /**
     * @param user credentials of the user
     * @param resource resource to have review flag removed
    */
    Void removeReviewFlag(Credentials user, Resource resource);

    /**
     * @param query search query
     * @return list of resources that match the query
    */
    List<Resource> searchResources(String query);

}