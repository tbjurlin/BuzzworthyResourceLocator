/**
 * This is the interface for the upvote data access object.
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */

package com.buzzword;

public interface UpvoteDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);
 
    /**
     * Adds an upvote to the database for the specific resource by the user.
     * 
     * @param user credentials of the user
     * @param resource resource to be upvoted
    */
    void addUpvote(Credentials user, UpVote upvote, int resourceId);
    
    /**
     * instead of downvoting, a user can remove their upvote from a previously upvoted resource within the database.
     * 
     * @param user credentials of the user
     * @param resource resource to have upvote removed
    */
    void removeUpvote(Credentials user, int commentId, int resourceId);
}