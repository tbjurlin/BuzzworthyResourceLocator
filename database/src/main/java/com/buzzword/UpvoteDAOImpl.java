/**
 * This is the UserDAO Implementation file, which handles operations related to users.
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */
package com.buzzword;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class UpvoteDAOImpl 
    implements UpvoteDAO {

    private final MongoCollection<Document> upvotes;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

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
    public void addUpvote(Credentials user, UpVote upvote, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
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
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
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