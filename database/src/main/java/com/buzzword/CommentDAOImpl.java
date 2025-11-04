package com.buzzword;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class CommentDAOImpl implements CommentDAO {
    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();

    public CommentDAOImpl(MongoDatabase db) {
        this.resources = db.getCollection("resources");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(Credentials user, Comment comment) {
        // All users can add comments (General User, Developer, Manager)
        Document commentDoc = new Document()
            .append("id", comment.getId())
            .append("creatorId", user.getId())  // Set comment creator to current user
            .append("contents", comment.getContents())
            .append("creationDate", new java.util.Date());  // Set current timestamp

        // Push the comment into the resource's comments array
        resources.updateOne(
            Filters.elemMatch("resources", new Document("id", comment.getCreatorId())), // creatorId holds resourceId
            new Document("$push", new Document("resources.$.comments", commentDoc))
        );

        logger.info(String.format("User %d added comment %d to resource %d", user.getId(), comment.getId(), comment.getCreatorId()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeComment(Credentials user, long id) {
        //An admin may remove the comment, only do find if they're a commentor or contributor.
        Document doc = resources.find(
            Filters.eq("commentId", id)).first();

        if (doc == null) {
            return false;  // Comment not found
        }

        // Check if user has permission to delete this comment.
        // Allow deletion only if the user is an Admin or the original creator of the comment.
        boolean isAdmin = "Admin".equals(user.getSystemRole());
        Integer commentCreatorId = doc.getInteger("creatorId");
        boolean isCreator = commentCreatorId != null && commentCreatorId.equals(user.getId());

        if (!isAdmin && !isCreator) {
            throw new AuthorizationException("User does not have permission to delete this comment");
        }

        // Delete the comment document
        com.mongodb.client.result.DeleteResult result = resources.deleteOne(
            Filters.and(
                Filters.eq("resourceId", id), // creatorId holds resourceId
                Filters.eq("commentId", id)
            )
        );

        boolean removed = result.getDeletedCount() > 0;
        if (removed) {
            logger.info(String.format("User %d removed comment %d from resource %d", user.getId(), id, doc.getLong("resourceId")));
        } else {
            logger.warn(String.format("User %d failed to remove comment %d from resource %d", user.getId(), id, doc.getLong("resourceId")));
        }
        return removed;
    }
}