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
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

public class CounterDAOImpl implements CounterDAO {

    private MongoCollection<Document> counterCollection;

    /**
     * Constructs a CounterDAOImpl with the specified MongoDB database.
     * <p>
     * Initializes the counters collection for managing record IDs.
     * 
     * @param db the MongoDB database to use for data access
     */
    public CounterDAOImpl(MongoDatabase db) {
        counterCollection = db.getCollection("counters");
    }

    private Logger logger = LoggerFactory.getEventLogger();

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNextResourceId() {
        Document previous = counterCollection.findOneAndUpdate(
            Filters.eq("_id", "resourceIdCounter"),
            Updates.inc("count", 1),
            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE)
        );
        int count = 0;
        if (previous == null) {
            logger.debug("Resource counter did not exists, creating it now");
            counterCollection.insertOne(
                new Document("_id", "resourceIdCounter")
                    .append("count", 1)
                );
        } else {
            count = previous.getInteger("count");
        }
        counterCollection.insertOne(
            new Document("_id", count)
                .append("commentCount", 0)
                .append("upvoteCount", 0)
                .append("flagCount", 0)
        );
        logger.debug(String.format("Returned next resource ID %d", count));
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNextCommentId(int resourceId) {
        Document counters = counterCollection.findOneAndUpdate(
            Filters.eq("_id", resourceId),
            Updates.inc("commentCount", 1),
            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE)
        );
        if (counters == null) {
            logger.error(String.format("Attempted to get id counters for non-existent resource %d", resourceId));
            throw new RecordDoesNotExistException("Attempted to get id counters for non-existent resource");
        } else {
            return counters.getInteger("commentCount");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNextReviewFlagId(int resourceId) {
        Document counters =  counterCollection.findOneAndUpdate(
            Filters.eq("_id", resourceId),
            Updates.inc("flagCount", 1),
            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE)
        );
        if (counters == null) {
            logger.error(String.format("Attempted to get id counters for non-existent resource %d", resourceId));
            throw new RecordDoesNotExistException("Attempted to get id counters for non-existent resource");
        } else {
            return counters.getInteger("flagCount");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNextUpvoteId(int resourceId) {
        Document counters =  counterCollection.findOneAndUpdate(
            Filters.eq("_id", resourceId),
            Updates.inc("upvoteCount", 1),
            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE)
        );
        if (counters == null) {
            logger.error(String.format("Attempted to get id counters for non-existent resource %d", resourceId));
            throw new RecordDoesNotExistException("Attempted to get id counters for non-existent resource");
        } else {
            return counters.getInteger("upvoteCount");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeResourceCounters(int resourceId) {
        counterCollection.deleteOne(Filters.eq("_id", resourceId));
    }
    
}
