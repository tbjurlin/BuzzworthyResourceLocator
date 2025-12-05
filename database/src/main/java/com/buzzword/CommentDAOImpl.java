package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CommentDAOImpl implements CommentDAO {
    private final MongoCollection<Document> comments;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    /**
     * Constructs a CommentDAOImpl with the specified MongoDB database.
     * <p>
     * Initializes the comments collection and creates a counter DAO for managing comment IDs.
     * 
     * @param db the MongoDB database to use for data access
     * @throws IllegalArgumentException if db is null
     */
    public CommentDAOImpl(MongoDatabase db) {
        // Check for null database
        if (db == null) {
            logger.error("Attempted to construct CommentDAOImpl with null database.");
            throw new IllegalArgumentException("Database cannot be null.");
        }
        this.comments = db.getCollection("comments");
        counterDAO = new CounterDAOImpl(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCounterDAO(CounterDAO counterDAO) {
        // Check for null CounterDAO
        if (counterDAO == null) {
            logger.error("Attempted to set null CounterDAO.");
            throw new IllegalArgumentException("CounterDAO cannot be null.");
        }
        this.counterDAO = counterDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(Credentials user, Comment comment, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to add comment with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to add comments.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to add comments.");
        }
        // Check for null comment
        if (comment == null) {
            logger.error("Attempted to add null comment.");
            throw new IllegalArgumentException("Comment cannot be null.");
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
        comments.insertOne(commentDoc);

        logger.info(String.format("User %d added comment %d to resource %d.", user.getId(), comment.getId(), resourceId));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void editComment(Credentials user, int commentId, Comment comment, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to edit comment with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to edit comments.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to edit comments.");
        }
        // Check for null comment
        if (comment == null) {
            logger.error("Attempted to edit with null comment.");
            throw new IllegalArgumentException("Comment cannot be null.");
        }

        // Only the creator of a comment can edit it
        Document doc = comments.find(
            Filters.and(Filters.eq("commentId", commentId), Filters.eq("resourceId", resourceId))).first();
        if (doc == null) {
            logger.error(String.format("Failed to find comment %d for update by user %d.", commentId, user.getId()));
            throw new RecordDoesNotExistException("Failed to find comment to update.");
        }

        // Check if user has permission to edit this comment
        // Allow editing only if the user is the original creator of the comment
        int commentCreatorId = doc.getInteger("creatorId");

        if (commentCreatorId != user.getId()) {
            logger.error(String.format("User %d denied permission to edit comment %d because they are not the creator.", user.getId(), commentId));
            throw new AuthorizationException("User does not have permission to edit this comment because they are not the creator.");
        }

        Bson filter = Filters.and(
                        Filters.eq("commentId", commentId), 
                        Filters.eq("resourceId", resourceId));
        Bson updateComment = Updates.combine(
                                Updates.set("contents", comment.getContents()),
                                Updates.set("isEdited", true));

        UpdateResult result = comments.updateOne(filter, updateComment);
        if(result.getMatchedCount() == 0) {
            logger.error(String.format("Comment %d not found for editing by user %d.", commentId, user.getId()));
            throw new RecordDoesNotExistException("Comment not found for editing.");
        } else {
            logger.info(String.format("User %d edited comment %d on resource %d.", user.getId(), commentId, resourceId));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComment(Credentials user, int commentId, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to delete comment with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to delete comments.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to delete comments.");
        }

        Document doc = comments.find(Filters.and(
                                        Filters.eq("commentId", commentId), 
                                        Filters.eq("resourceId", resourceId)))
                                .first();
        if (doc == null) {
            logger.error(String.format("Failed to find comment %d for removal by user %d.", commentId, user.getId()));
            throw new RecordDoesNotExistException("Failed to find comment for removal.");
        }

        // An admin may remove any comment; only do ownership check if they're a commenter or contributor
        if (!user.getSystemRole().equals("Admin")) {
            

            // Check if user has permission to delete this comment
            // Allow deletion only if the user is the original creator of the comment
            int commentCreatorId = doc.getInteger("creatorId");

            if (commentCreatorId != user.getId()) {
                logger.error(String.format("User %d denied permission to delete comment %d because they are not the creator.", user.getId(), commentId));
                throw new AuthorizationException("User does not have permission to delete this comment because they are not the creator.");
            }
        }

        // Delete the comment document
        DeleteResult result = comments.deleteOne(Filters.and(
                                                    Filters.eq("resourceId", resourceId),
                                                    Filters.eq("commentId", commentId)));

        // Check if deletion was successful
        if (result.getDeletedCount() > 0) {
            logger.info(String.format("User %d removed comment %d from resource %d.", user.getId(), commentId, resourceId));
        } else {
            logger.error(String.format("User %d failed to remove comment %d from resource %d.", user.getId(), commentId, resourceId));
            throw new RecordDoesNotExistException("Failed to remove comment.");
        }
    }
}