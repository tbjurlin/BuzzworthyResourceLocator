package com.buzzword;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

public class CounterDAOImpl implements CounterDAO {

    private MongoCollection<Document> counterCollection;

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
