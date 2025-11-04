/**
 * This is the UserDAO Implementation file, which handles operations related to users.
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */
package com.buzzword;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class UserDAOImpl 
    implements UserDAO {

    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();

    public UserDAOImpl(MongoDatabase db) throws IOException {
        this.resources = db.getCollection("users");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addUpVote(Credentials user, Resource resource) {
        // Add an upvote sub-document under the matched resource. Use $addToSet so the same
        // user cannot upvote the same resource more than once (we key by creatorId).
        Document upvoteDoc = new Document()
            .append("creatorId", user.getId())
            .append("creationDate", new java.util.Date());

        com.mongodb.client.result.UpdateResult result = resources.updateOne(
            Filters.and(Filters.eq("id", user.getId()), Filters.eq("resources.id", resource.getId())),
            new Document("$addToSet", new Document("resources.$.upVotes", upvoteDoc))
        );

        if (result.getModifiedCount() == 0) {
            logger.warn(String.format("Failed to add upvote from user %d to resource %d - resource not found", user.getId(), resource.getId()));
        }

        // Also increment the upvoteCount for quick reads. Use the same filter.
        result = resources.updateOne(
            Filters.and(Filters.eq("id", user.getId()), Filters.eq("resources.id", resource.getId())),
            new Document("$inc", new Document("resources.$.upvoteCount", 1))
        );

        logger.info(String.format("User %d added upvote to resource %d", user.getId(), resource.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpVote(Credentials user, Resource resource) {
        // Delete the upvote document and decrement the counter atomically
        com.mongodb.client.result.DeleteResult result = resources.deleteOne(
            Filters.and(
                Filters.eq("id", user.getId()),
                Filters.eq("resources.id", resource.getId()),
                Filters.eq("resources.upVotes.creatorId", user.getId())
            )
        );

        if (result.getDeletedCount() == 0) {
            logger.warn(String.format("Failed to remove upvote from user %d for resource %d - resource or upvote not found", user.getId(), resource.getId()));
            return;
        }

        // Still need to decrement the counter since it's a separate field
        resources.updateOne(
            Filters.and(Filters.eq("id", user.getId()), Filters.eq("resources.id", resource.getId())),
            new Document("$inc", new Document("resources.$.upvoteCount", -1))
        );

        logger.info(String.format("User %d removed upvote from resource %d", user.getId(), resource.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addReviewFlag(Credentials user, Resource resource) {
        // Add an active review flag for the resource. We store who flagged it and when.
        Document flagDoc = new Document()
            .append("creatorId", user.getId())
            .append("active", true)
            .append("creationDate", new java.util.Date());

        com.mongodb.client.result.UpdateResult result = resources.updateOne(
            Filters.and(Filters.eq("id", user.getId()), Filters.eq("resources.id", resource.getId())),
            new Document("$push", new Document("resources.$.reviewFlags", flagDoc))
        );

        if (result.getModifiedCount() == 0) {
            logger.warn(String.format("Failed to add review flag from user %d to resource %d - resource not found", user.getId(), resource.getId()));
        }

        logger.info(String.format("User %d added review flag to resource %d", user.getId(), resource.getId()));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeReviewFlag(Credentials user, Resource resource) {
        // Delete the review flag created by this user for the matched resource
        com.mongodb.client.result.DeleteResult result = resources.deleteOne(
            Filters.and(
                Filters.eq("id", user.getId()),
                Filters.eq("resources.id", resource.getId()),
                Filters.eq("resources.reviewFlags.creatorId", user.getId())
            )
        );

        if (result.getDeletedCount() == 0) {
            logger.warn(String.format("Failed to remove review flag from user %d for resource %d - resource or flag not found", user.getId(), resource.getId()));
        }

        logger.info(String.format("User %d removed review flag from resource %d", user.getId(), resource.getId()));
    }

}