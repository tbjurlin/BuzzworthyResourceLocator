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
import com.mongodb.client.result.UpdateResult;

public class CommentDAOImpl implements CommentDAO {
    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    /**
     * Constructs a CommentDAOImpl with the specified MongoDB database.
     * <p>
     * Initializes the comments collection and creates a counter DAO for managing comment IDs.
     * 
     * @param db the MongoDB database to use for data access
     */
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
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
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
    public void editComment(Credentials user, int commentId, Comment comment, int resourceId) {
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with invalid system role %s prevented from editing a comment", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to edit a comment.");
        }
        // Only the creator of a comment can edit it.
        Document doc = resources.find(
            Filters.and(Filters.eq("commentId", commentId), Filters.eq("resourceId", resourceId))).first();
        if (doc == null) {
            throw new RecordDoesNotExistException("Failed to find comment to update");
        }

        // Check if user has permission to delete this comment.
        // Allow deletion only if the user is an Admin or the original creator of the comment.
        int commentCreatorId = doc.getInteger("creatorId");

        if (commentCreatorId != user.getId()) {
            logger.error(String.format("User %d has attempted to edit a comment that they did not create.", user.getId()));
            throw new AuthorizationException("User does not have permission to edit this comment");
        }

        Bson filter = Filters.and(Filters.eq("commentId", commentId), Filters.eq("resourceId", resourceId));
        Bson updateComment = Updates.set("contents", comment.getContents());

        UpdateResult result = resources.updateOne(filter, updateComment);
        if(result.getMatchedCount() == 0) {
            logger.warn(String.format("User %d failed to edit comment %d on resource %d", user.getId(), commentId, resourceId));
            throw new RecordDoesNotExistException("Failed to find comment for editing");
        } else {
            logger.info(String.format("User %d edited comment %d on resource %d", user.getId(), commentId, resourceId));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComment(Credentials user, int commentId, int resourceId) {
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with invalid system role %s prevented from deleting a comment", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to delete a comment.");
        }
        if (!user.getSystemRole().equals("Admin")) {        
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