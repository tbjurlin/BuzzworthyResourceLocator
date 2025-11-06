package com.buzzword;

public interface CommentDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);

    /**
     * <p>
     * Allows a user to add a comment to a specific resource.
     * 
     * @param user credentials of the user
     * @param comment comment to be added
    */
    void addComment(Credentials user, Comment comment, int resourceId);
    
    /**
     * <p>
     * A user may delete their own comment or an admin may remove any comments.
     * 
     * @param user credentials of the user
     * @param comment comment to be removed
    */
    void removeComment(Credentials user, int commentId, int resourceId);
}