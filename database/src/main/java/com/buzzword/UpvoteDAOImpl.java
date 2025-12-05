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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

/**
 * This is the UserDAO Implementation file, which handles operations related to users.
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */
public class UpvoteDAOImpl 
    implements UpvoteDAO {

    private final MongoCollection<Document> upvotes;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    /**
     * Constructs an UpvoteDAOImpl with the specified MongoDB database.
     * <p>
     * Initializes the upvotes collection and creates a counter DAO for managing upvote IDs.
     * 
     * @param db the MongoDB database to use for data access
     * @throws IllegalArgumentException if db is null
     */
    public UpvoteDAOImpl(MongoDatabase db) {
        // Check for null database
        if (db == null) {
            logger.error("Attempted to construct UpvoteDAOImpl with null database.");
            throw new IllegalArgumentException("Database cannot be null.");
        }
        this.upvotes = db.getCollection("upvotes");
        this.counterDAO = new CounterDAOImpl(db);
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
    public void addUpvote(Credentials user, Upvote upvote, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to add upvote with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to add upvotes.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to add upvotes.");
        }
        // Check for null upvote
        if (upvote == null) {
            logger.error("Attempted to add null upvote.");
            throw new IllegalArgumentException("Upvote cannot be null.");
        }

        // Check if upvote already exists
        Document existingDoc = upvotes.find(Filters.and(
                                                Filters.eq("resourceId", resourceId),
                                                Filters.eq("creatorId", user.getId())))
                                      .first();
        if (existingDoc != null) {
            logger.error(String.format("User %d attempted to add duplicate upvote to resource %d.", user.getId(), resourceId));
            throw new RecordAlreadyExistsException("Upvote already exists.");
        }

        // Create the upvote document
        Document upvoteDoc = new Document()
            .append("creatorId", user.getId())
            .append("upvoteId", counterDAO.getNextUpvoteId(resourceId))
            .append("resourceId", resourceId)
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("dateCreated", upvote.getCreationDate());

        // Insert the upvote document into the collection
        upvotes.insertOne(upvoteDoc);

        logger.info(String.format("User %d added upvote to resource %d.", user.getId(), resourceId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpvote(Credentials user,  int upvoteId, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to delete upvote with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to delete upvotes.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to delete upvotes.");
        }

        // Find the upvote to verify ownership
        Document targetUpvote = upvotes.find(Filters.and(Filters.eq("upvoteId", upvoteId), Filters.eq("resourceId", resourceId))).first();

        if (targetUpvote == null) {
            logger.warn(String.format("Failed to find upvote %d for removal by user %d.", upvoteId, user.getId()));
            throw new RecordDoesNotExistException("Failed to find upvote for removal.");
        }

        // Check if user has permission to delete this upvote
        // Allow deletion only if the user is the original creator of the upvote
        if (targetUpvote.getInteger("creatorId") != user.getId()) {
            logger.error(String.format("User %d denied permission to delete upvote %d because they are not the creator.", user.getId(), upvoteId));
            throw new AuthorizationException("User does not have permission to delete this upvote because they are not the creator.");
        }

        // Delete the upvote document
        DeleteResult result = upvotes.deleteOne(Filters.and(
                                                    Filters.eq("upvoteId", upvoteId),
                                                    Filters.eq("resourceId", resourceId)));

        // Check if deletion was successful
        if (result.getDeletedCount() == 0) {
            logger.warn(String.format("User %d failed to remove upvote %d from resource %d.", user.getId(), upvoteId, resourceId));
            throw new RecordDoesNotExistException("Failed to find upvote for removal.");
        }

        logger.info(String.format("User %d removed upvote from resource %d.", user.getId(), resourceId));
    }
}