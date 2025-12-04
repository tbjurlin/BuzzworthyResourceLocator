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
    public void editReviewFlag(Credentials user, int flagId, ReviewFlag flag, int resourceId) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with invalid system role %s prevented from editing a flag", user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User with missing or invalid system role attempted to edit a flag.");
        }
        // Only the creator of a flag can edit it.
        Document doc = resources.find(
            Filters.and(Filters.eq("flagId", flagId), Filters.eq("resourceId", resourceId))).first();
        if (doc == null) {
            throw new RecordDoesNotExistException("Failed to find flag for update");
        }

        // Check if user has permission to edit this flag.
        // Allow update only if the user is the original creator of the flag.
        int flagCreatorId = doc.getInteger("creatorId");

        if (flagCreatorId != user.getId()) {
            logger.error(String.format("User %d has attempted to edit a flag that they did not create.", user.getId()));
            throw new AuthorizationException("User does not have permission to edit this flag");
        }

        Bson filter = Filters.and(Filters.eq("flagId", flagId), Filters.eq("resourceId", resourceId));
        Bson updateFlag = Updates.set("contents", flag.getContents());

        UpdateResult result = resources.updateOne(filter, updateFlag);
        if(result.getMatchedCount() == 0) {
            logger.warn(String.format("User %d failed to edit flag %d on resource %d", user.getId(), flagId, resourceId));
            throw new RecordDoesNotExistException("Failed to find flag for editing");
        } else {
            logger.info(String.format("User %d edited flag %d on resource %d", user.getId(), flagId, resourceId));
        }
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