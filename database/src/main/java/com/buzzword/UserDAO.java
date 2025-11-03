/**
 * This is the UserDAO Interface for the 
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */

package com.buzzword;

public interface UserDAO {
 
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

}