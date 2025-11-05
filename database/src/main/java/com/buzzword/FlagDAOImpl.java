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

public class FlagDAOImpl 
    implements FlagDAO {

    private final MongoCollection<Document> flags;
    private final Logger logger = LoggerFactory.getEventLogger();

    public FlagDAOImpl(MongoDatabase db) {
        this.flags = db.getCollection("flags");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addReviewFlag(Credentials user, ReviewFlag flag, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with missing or invalid system role %s attempted to add an flag.", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("System role of user is missing or invalid.");
        }

        Document existingDoc = flags.find(
            Filters.and(
                Filters.eq("resourceId", resourceId),
                Filters.eq("creatorId", user.getId()))).first();

        if (existingDoc != null) {
            logger.error(String.format("User %d has attempted to insert an flag when one already exists.", user.getId()));
            throw new RecordAlreadyExistsException("Flag already exists.");
        }


        Document flagDoc = new Document()
            .append("creatorId", user.getId())
            .append("flagId", flag.getId())
            .append("resourceId", resourceId)
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("dateCreated", flag.getCreationDate());

        flags.insertOne(
            flagDoc
        );

        logger.info(String.format("User %d added flag to resource %d", user.getId(), resourceId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeReviewFlag(Credentials user,  int flagId, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with missing or invalid system role %s prevented from deleting flag", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User attempted to delete flag with invalid or missing system role.");
        }
        if (user.getSystemRole() != "Admin") {
            Document targetUpvote = flags.find(Filters.and(Filters.eq("upvoteId", flagId), Filters.eq("resourceId", resourceId))).first();
            if (targetUpvote != null && targetUpvote.getInteger("creatorId") != user.getId()) {
                logger.error(String.format("User %d attempted to delete an flag they did not create.", user.getId()));
                throw new AuthorizationException("User attempted to delete an flag they did not create");
            }
        }

        // Delete the upvote document and decrement the counter atomically
        com.mongodb.client.result.DeleteResult result = flags.deleteOne(
            Filters.and(
                Filters.eq("flagId", user.getId()),
                Filters.eq("resourceId", resourceId)
            )
        );

        if (result.getDeletedCount() == 0) {
            logger.warn(String.format("Failed to remove flag from user %d for resource %d - resource or upvote not found", user.getId(), resourceId));
            return;
        }

        logger.info(String.format("User %d removed flag from resource %d", user.getId(), resourceId));
    }
}