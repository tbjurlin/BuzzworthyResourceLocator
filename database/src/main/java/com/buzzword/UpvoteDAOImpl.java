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
     */
    public UpvoteDAOImpl(MongoDatabase db) {
        this.upvotes = db.getCollection("upvotes");
        this.counterDAO = new CounterDAOImpl(db);
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
    public void addUpvote(Credentials user, Upvote upvote, int resourceId) {
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with missing or invalid system role %s attempted to add an upvote.", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("System role of user is missing or invalid.");
        }

        Document existingDoc = upvotes.find(
            Filters.and(
                Filters.eq("resourceId", resourceId),
                Filters.eq("creatorId", user.getId()))).first();

        if (existingDoc != null) {
            logger.error(String.format("User %d has attempted to insert an upvote when one already exists.", user.getId()));
            throw new RecordAlreadyExistsException("Upvote already exists.");
        }


        Document upvoteDoc = new Document()
            .append("creatorId", user.getId())
            .append("upvoteId", counterDAO.getNextUpvoteId(resourceId))
            .append("resourceId", resourceId)
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("dateCreated", upvote.getCreationDate());

        upvotes.insertOne(
            upvoteDoc
        );

        logger.info(String.format("User %d added upvote to resource %d", user.getId(), resourceId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpvote(Credentials user,  int upvoteId, int resourceId) {
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with missing or invalid system role %s prevented from deleting upvote", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User attempted to delete upvote with invalid or missing system role.");
        }
        Document targetUpvote = upvotes.find(Filters.and(Filters.eq("upvoteId", upvoteId), Filters.eq("resourceId", resourceId))).first();

        if (targetUpvote == null) {
            logger.warn(String.format("Failed to remove upvote from user %d for resource %d - resource or upvote not found", user.getId(), resourceId));
            throw new RecordDoesNotExistException(String.format("Failed to remove upvote from resource %d.", resourceId));
        }
        if (targetUpvote.getInteger("creatorId") != user.getId()) {
            logger.error(String.format("User %d attempted to delete an upvote they did not create.", user.getId()));
            throw new AuthorizationException("User attempted to delete an upvote they did not create");
        }

        // Delete the upvote document and decrement the counter atomically
        com.mongodb.client.result.DeleteResult result = upvotes.deleteOne(
            Filters.and(
                Filters.eq("upvoteId", upvoteId),
                Filters.eq("resourceId", resourceId)
            )
        );

        if (result.getDeletedCount() == 0) {
            logger.warn(String.format("Failed to remove upvote from user %d for resource %d - resource or upvote not found", user.getId(), resourceId));
            throw new RecordDoesNotExistException(String.format("Failed to remove upvote from resource %d.", resourceId));
        }

        logger.info(String.format("User %d removed upvote from resource %d", user.getId(), resourceId));
    }
}