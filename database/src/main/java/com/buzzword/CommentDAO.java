package com.buzzword;

public interface CommentDAO {

    /**
     * @param user credentials of the user
     * @param comment comment to be added
    */
    Void addComment(Credentials user, Comment comment);
    
    /**
     * @param user credentials of the user
     * @param comment comment to be removed
    */
    boolean removeComment(Credentials user, long id);
}