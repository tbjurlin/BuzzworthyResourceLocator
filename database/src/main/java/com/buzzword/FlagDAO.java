/**
 * This is the interface for the flag data access object.
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */

package com.buzzword;

public interface FlagDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);

    /**
     * Adds a review flag to the databse for the specific resource for managerial review.
     * 
     * @param user credentials of the user
     * @param resource resource to be flagged for review
    */
    void addReviewFlag(Credentials user, ReviewFlag flag, int resourceId);

    /**
     * After a manager has reviewed the resouce, they can remove the review flag from the resource.
     * 
     * @param user credentials of the user
     * @param resource resource to have review flag removed
    */
    void removeReviewFlag(Credentials user, int flagId, int resourceId);

}