package com.buzzword;

public class SecurityDAOImpl implements SecurityDAO {
    private static final String ROLE_MANAGER = "Manager";
    private static final String ROLE_DEVELOPER = "Developer";
    private static final String ROLE_GENERAL_USER = "General User";

    @Override
    public boolean canInsertResource(String role) {
        // All users can insert resources
        return true;
    }

    @Override
    public boolean canDeleteResource(String role, int resourceCreatorId, int userId) {
        // Managers can delete any resource
        // Developers and General Users can only delete their own resources
        return ROLE_MANAGER.equals(role) || resourceCreatorId == userId;
    }

    @Override
    public boolean canDeleteComment(String role, int commentCreatorId, int userId) {
        // Managers can delete any comment
        // Developers and General Users can only delete their own comments
        return ROLE_MANAGER.equals(role) || commentCreatorId == userId;
    }
}