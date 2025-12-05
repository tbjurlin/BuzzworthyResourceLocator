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

public class FlagDAOImpl implements FlagDAO {
    private final MongoCollection<Document> flags;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    /**
     * Constructs a FlagDAOImpl with the specified MongoDB database.
     * <p>
     * Initializes the flags collection and creates a counter DAO for managing flag IDs.
     * 
     * @param db the MongoDB database to use for data access
     * @throws IllegalArgumentException if db is null
     */
    public FlagDAOImpl(MongoDatabase db) {
        // Check for null database
        if (db == null) {
            logger.error("Attempted to construct FlagDAOImpl with null database.");
            throw new IllegalArgumentException("Database cannot be null.");
        }
        this.flags = db.getCollection("flags");
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
    public void addReviewFlag(Credentials user, ReviewFlag flag, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to add flag with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to add flags.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to add flags.");
        }
        // Check for null flag
        if (flag == null) {
            logger.error("Attempted to add null flag.");
            throw new IllegalArgumentException("Flag cannot be null.");
        }

        // All users can add flags (Admin, Contributor, Commenter)
        Document flagDoc = new Document()
            .append("flagId", counterDAO.getNextReviewFlagId(resourceId))
            .append("resourceId", resourceId)
            .append("creatorId", user.getId())
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("contents", flag.getContents())
            .append("dateCreated", flag.getCreationDate());

        // Insert the flag document into the collection
        flags.insertOne(flagDoc);

        logger.info(String.format("User %d added flag %d to resource %d.", user.getId(), flag.getId(), resourceId));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void editReviewFlag(Credentials user, int flagId, ReviewFlag flag, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to edit flag with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to edit flags.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to edit flags.");
        }
        // Check for null flag
        if (flag == null) {
            logger.error("Attempted to edit with null flag.");
            throw new IllegalArgumentException("Flag cannot be null.");
        }

        // Only the creator of a flag can edit it
        Document doc = flags.find(
            Filters.and(Filters.eq("flagId", flagId), Filters.eq("resourceId", resourceId))).first();
        if (doc == null) {
            logger.error(String.format("Failed to find flag %d for update by user %d.", flagId, user.getId()));
            throw new RecordDoesNotExistException("Failed to find flag to update.");
        }

        // Check if user has permission to edit this flag
        // Allow editing only if the user is the original creator of the flag
        int flagCreatorId = doc.getInteger("creatorId");

        if (flagCreatorId != user.getId()) {
            logger.error(String.format("User %d denied permission to edit flag %d because they are not the creator.", user.getId(), flagId));
            throw new AuthorizationException("User does not have permission to edit this flag because they are not the creator.");
        }

        Bson filter = Filters.and(
                        Filters.eq("flagId", flagId), 
                        Filters.eq("resourceId", resourceId));
        Bson updateFlag = Updates.combine(
                            Updates.set("contents", flag.getContents()),
                            Updates.set("isUpdated", true));

        // Update the flag document
        UpdateResult result = flags.updateOne(filter, updateFlag);

        // Check if update was successful
        if(result.getMatchedCount() == 0) {
            logger.error(String.format("Flag %d not found for editing by user %d.", flagId, user.getId()));
            throw new RecordDoesNotExistException("Flag not found for editing.");
        } else {
            logger.info(String.format("User %d edited flag %d on resource %d.", user.getId(), flagId, resourceId));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeReviewFlag(Credentials user, int flagId, int resourceId) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to delete flag with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to delete flags.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to delete flags.");
        }

        Document doc = flags.find(Filters.and(
                                        Filters.eq("flagId", flagId), 
                                        Filters.eq("resourceId", resourceId)))
                                .first();
        if (doc == null) {
            logger.error(String.format("Failed to find flag %d for removal by user %d.", flagId, user.getId()));
            throw new RecordDoesNotExistException("Failed to find flag for removal.");
        }

        // An admin may remove any flag; only do ownership check if they're a commenter or contributor
        if (!user.getSystemRole().equals("Admin")) {

            // Check if user has permission to delete this flag
            // Allow deletion only if the user is the original creator of the flag
            int flagCreatorId = doc.getInteger("creatorId");

            if (flagCreatorId != user.getId()) {
                logger.error(String.format("User %d denied permission to delete flag %d because they are not the creator.", user.getId(), flagId));
                throw new AuthorizationException("User does not have permission to delete this flag because they are not the creator.");
            }
        }

        // Delete the flag document
        DeleteResult result = flags.deleteOne(Filters.and(
                                                        Filters.eq("resourceId", resourceId),
                                                        Filters.eq("flagId", flagId)));

        // Check if deletion was successful
        if (result.getDeletedCount() > 0) {
            logger.info(String.format("User %d removed flag %d from resource %d.", user.getId(), flagId, resourceId));
        } else {
            logger.error(String.format("User %d failed to remove flag %d from resource %d.", user.getId(), flagId, resourceId));
            throw new RecordDoesNotExistException("Failed to remove flag.");
        }
    }
}