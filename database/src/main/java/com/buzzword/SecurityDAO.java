package com.buzzword;

public interface SecurityDAO {

    /**
     * @param role role of the user
     * @return true if the user can insert a resource, false otherwise
    */
    boolean canInsertResource(String role);

    /**
     * @param role role of the user
     * @param resourceCreatorId ID of the user who created the resource
     * @param userId ID of the user attempting to delete the resource
     * @return true if the user can delete the resource, false otherwise
    */
    boolean canDeleteResource(String role, int resourceCreatorId, int userId);

    /**
     * @param role role of the user
     * @param commentCreatorId ID of the user who created the comment
     * @param userId ID of the user attempting to delete the comment
     * @return true if the user can delete the comment, false otherwise
     */
    boolean canDeleteComment(String role, int commentCreatorId, int userId);
}