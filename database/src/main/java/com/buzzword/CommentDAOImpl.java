package com.buzzword;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

public class CommentDAOImpl implements CommentDAO {
    private final MongoCollection<Document> usersCollection;
    private final SecurityDAO securityDAO;
    private final Logger logger = LoggerFactory.getEventLogger();

    public CommentDAOImpl(MongoCollection<Document> usersCollection, SecurityDAO securityDAO) {
        this.usersCollection = usersCollection;
        this.securityDAO = securityDAO;
    }

    @Override
    public Void addComment(Credentials user, Comment comment) {
        // All users can add comments (General User, Developer, Manager)
        Document commentDoc = new Document()
            .append("id", comment.getId())
            .append("creatorId", user.getId())  // Set comment creator to current user
            .append("contents", comment.getContents())
            .append("creationDate", new java.util.Date());  // Set current timestamp

        // Push the comment into the resource's comments array
        usersCollection.updateOne(
            Filters.elemMatch("resources", new Document("id", comment.getCreatorId())), // creatorId holds resourceId
            new Document("$push", new Document("resources.$.comments", commentDoc))
        );

        logger.info(String.format("User %d added comment %d to resource %d", user.getId(), comment.getId(), comment.getCreatorId()));
        return null;
    }
    
    @Override
    public Boolean removeComment(Credentials user, Comment comment) {
        // First, find the comment to check ownership
        Document doc = usersCollection.find(
            Filters.elemMatch("resources", 
                new Document("comments.id", comment.getId()))
        ).first();

        if (doc == null) {
            return false;  // Comment not found
        }

        // Get the comment details to check permissions
        Document commentDoc = null;
        outer: for (Document resource : doc.getList("resources", Document.class)) {
            List<Document> comments = resource.getList("comments", Document.class);
            if (comments != null) {
                for (Document c : comments) {
                    if (c.getInteger("id") == comment.getId()) {
                        commentDoc = c;
                        break outer;
                    }
                }
            }
        }

        if (commentDoc == null) {
            return false;  // Comment not found
        }

        // Check if user has permission to delete this comment
        if (!securityDAO.canDeleteComment(user.getSystemRole(), commentDoc.getInteger("creatorId"), user.getId())) {
            throw new SecurityException("User does not have permission to delete this comment");
        }

        // Remove the comment from the resource's comments array
        com.mongodb.client.result.UpdateResult result = usersCollection.updateOne(
            Filters.elemMatch("resources", new Document("id", comment.getCreatorId())), // creatorId holds resourceId
            new Document("$pull", 
                new Document("resources.$.comments", 
                    new Document("id", comment.getId())
                )
            )
        );

        boolean removed = result.getModifiedCount() > 0;
        if (removed) {
            logger.info(String.format("User %d removed comment %d from resource %d", user.getId(), comment.getId(), comment.getCreatorId()));
        } else {
            logger.warn(String.format("User %d failed to remove comment %d from resource %d", user.getId(), comment.getId(), comment.getCreatorId()));
        }
        return removed;
    }
}