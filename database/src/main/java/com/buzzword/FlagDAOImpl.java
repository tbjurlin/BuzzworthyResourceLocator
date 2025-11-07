package com.buzzword;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class FlagDAOImpl implements FlagDAO {
    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    public FlagDAOImpl(MongoDatabase db) {
        this.resources = db.getCollection("flags");
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
    public void addReviewFlag(Credentials user, ReviewFlag flag, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with invalid system role %s prevented from adding a flag", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to add a flag.");
        }

        // All users can add flags (General User, Developer, Manager)
        Document flagDoc = new Document()
            .append("flagId", counterDAO.getNextReviewFlagId(resourceId))
            .append("resourceId", resourceId)
            .append("creatorId", user.getId())
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("contents", flag.getContents())
            .append("dateCreated", flag.getCreationDate());  // Set current timestamp

        // Push the flag into the resource's flags array
        resources.insertOne(flagDoc);

        logger.info(String.format("User %d added flag %d to resource %d", user.getId(), flag.getId(), flag.getCreatorId()));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeReviewFlag(Credentials user, int flagId, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with invalid system role %s prevented from deleting a flag", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to delete a flag.");
        }
        if (user.getSystemRole() != "Admin") {        
            //An admin may remove the flag, only do find if they're a flagor or contributor.
            Document doc = resources.find(
                Filters.and(Filters.eq("flagId", flagId), Filters.eq("resourceId", resourceId))).first();
            if (doc == null) {
                throw new RecordDoesNotExistException("Failed to find flag for removal");
            }

            // Check if user has permission to delete this flag.
            // Allow deletion only if the user is an Admin or the original creator of the flag.
            int flagCreatorId = doc.getInteger("creatorId");

            if (flagCreatorId != user.getId()) {
                logger.error(String.format("User %d has attempted to delete a flag that they did not create.", user.getId()));
                throw new AuthorizationException("User does not have permission to delete this flag");
            }
        }
        // Delete the flag document
        com.mongodb.client.result.DeleteResult result = resources.deleteOne(
            Filters.and(
                Filters.eq("resourceId", resourceId),
                Filters.eq("flagId", flagId)
            )
        );

        boolean removed = result.getDeletedCount() > 0;
        if (removed) {
            logger.info(String.format("User %d removed flag %d from resource %d", user.getId(), flagId, resourceId));
        } else {
            logger.warn(String.format("User %d failed to remove flag %d from resource %d", user.getId(), flagId, resourceId));
            throw new RecordDoesNotExistException("Failed to find flag for removal");
        }
    }
}