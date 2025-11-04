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
     * <p>
     * Adds an upvote to the database for the specific resource by the user.
     * 
     * @param user credentials of the user
     * @param resource resource to be upvoted
    */
    void addUpVote(Credentials user, Resource resource);
    
    /**
     * <p>
     * instead of downvoting, a user can remove their upvote from a previously upvoted resource within the database.
     * 
     * @param user credentials of the user
     * @param resource resource to have upvote removed
    */
    void removeUpVote(Credentials user, Resource resource);

    /**
     * <p>
     * Adds a review flag to the databse for the specific resource for managerial review.
     * 
     * @param user credentials of the user
     * @param resource resource to be flagged for review
    */
    void addReviewFlag(Credentials user, Resource resource);

    /**
     * <p>
     * After a manager has reviewed the resouce, they can remove the review flag from the resource.
     * 
     * @param user credentials of the user
     * @param resource resource to have review flag removed
    */
    void removeReviewFlag(Credentials user, Resource resource);

}