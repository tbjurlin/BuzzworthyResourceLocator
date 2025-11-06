package com.buzzword;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class CommentDAOImpl implements CommentDAO {
    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    public CommentDAOImpl(MongoDatabase db) {
        this.resources = db.getCollection("comments");
        counterDAO = new CounterDAOImpl(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCounterDAO(CounterDAO counterDAO) {
        this.counterDAO = counterDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(Credentials user, Comment comment, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with invalid system role %s prevented from adding a comment", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to add a comment.");
        }

        // All users can add comments (General User, Developer, Manager)
        Document commentDoc = new Document()
            .append("commentId", counterDAO.getNextCommentId(resourceId))
            .append("resourceId", resourceId)
            .append("creatorId", user.getId())
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("contents", comment.getContents())
            .append("dateCreated", comment.getCreationDate());  // Set current timestamp

        // Push the comment into the resource's comments array
        resources.insertOne(commentDoc);

        logger.info(String.format("User %d added comment %d to resource %d", user.getId(), comment.getId(), comment.getCreatorId()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComment(Credentials user, int commentId, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with invalid system role %s prevented from deleting a comment", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to delete a comment.");
        }
        if (user.getSystemRole() != "Admin") {        
            //An admin may remove the comment, only do find if they're a commentor or contributor.
            Document doc = resources.find(
                Filters.and(Filters.eq("commentId", commentId), Filters.eq("resourceId", resourceId))).first();
            if (doc == null) {
                throw new RecordDoesNotExistException("Failed to find comment for removal");
            }

            // Check if user has permission to delete this comment.
            // Allow deletion only if the user is an Admin or the original creator of the comment.
            int commentCreatorId = doc.getInteger("creatorId");

            if (commentCreatorId != user.getId()) {
                logger.error(String.format("User %d has attempted to delete a comment that they did not create.", user.getId()));
                throw new AuthorizationException("User does not have permission to delete this comment");
            }
        }
        // Delete the comment document
        com.mongodb.client.result.DeleteResult result = resources.deleteOne(
            Filters.and(
                Filters.eq("resourceId", resourceId),
                Filters.eq("commentId", commentId)
            )
        );

        boolean removed = result.getDeletedCount() > 0;
        if (removed) {
            logger.info(String.format("User %d removed comment %d from resource %d", user.getId(), commentId, resourceId));
        } else {
            logger.warn(String.format("User %d failed to remove comment %d from resource %d", user.getId(), commentId, resourceId));
            throw new RecordDoesNotExistException("Failed to find comment for removal");
        }
    }
}