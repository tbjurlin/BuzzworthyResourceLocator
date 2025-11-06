package com.buzzword;

/**
 * A database access object that manages IDs for records.
 * 
 * @author Ted Bjurlin
 * @version 1.0
 */
public interface CounterDAO {
    
    /**
     * Removes the id tracking document for the target resource
     * @param resourceId
     */
    public void removeResourceCounters(int resourceId);

    /**
     * Gets the next available resource ID.
     * @return an available resource ID
     */
    public int getNextResourceId();

    /**
     * Gets the next available comment ID for the given resource.
     * @param resourceId
     * @return an available comment ID
     */
    public int getNextCommentId(int resourceId);

    /**
     * Gets the next available flag ID for the given resource.
     * @param resourceId
     * @return an available flag ID
     */
    public int getNextReviewFlagId(int resourceId);

    /**
     * Gets the next available upvote ID for the given resource.
     * @param resourceId
     * @return an available upvote ID
     */
    public int getNextUpvoteId(int resourceId);
}
