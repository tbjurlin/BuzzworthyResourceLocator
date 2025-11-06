package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;

@ExtendWith(MockitoExtension.class)
public class CounterDAOTest {
    
    @Mock
    MongoDatabase testDatabase;

    @Mock
    MongoCollection<Document> testCollection;

    CounterDAO counterDAO;

    @BeforeEach
    void setUpDatabase() {
        when(testDatabase.getCollection("counters")).thenReturn(testCollection);
        counterDAO = new CounterDAOImpl(testDatabase);
    }

    @Test
    void createsNewWhenFindsNullResource() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(null);

        int id = counterDAO.getNextResourceId();

        assertEquals(0, id);

        ArgumentCaptor<Document> argCaptor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection, times(2)).insertOne(argCaptor.capture());

        Assertions.assertThat(argCaptor.getAllValues().getFirst())
            .usingRecursiveComparison()
            .isEqualTo(
                new Document("_id", "resourceIdCounter").append("count", 1)
            );
    }

    @Test
    void returnsCorrectResourceId() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(new Document("_id", "resourceIdCounter").append("count", 5));

        int id = counterDAO.getNextResourceId();

        assertEquals(5, id);

        ArgumentCaptor<Document> argCaptor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection, times(1)).insertOne(argCaptor.capture());

        Assertions.assertThat(argCaptor.getValue())
            .usingRecursiveComparison()
            .isEqualTo(
                new Document("_id", 5)
                    .append("commentCount", 0)
                    .append("upvoteCount", 0)
                    .append("flagCount", 0)
            );
    }

    @Test
    void returnsCorrectCommentId() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(
                new Document("_id", 0)
                    .append("commentCount", 5)
                    .append("upvoteCount", 3)
                    .append("flagCount", 7));

        int id = counterDAO.getNextCommentId(0);

        assertEquals(5, id);
    }

    @Test
    void returnsCorrectUpvoteId() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(
                new Document("_id", 0)
                    .append("commentCount", 5)
                    .append("upvoteCount", 3)
                    .append("flagCount", 7));

        int id = counterDAO.getNextUpvoteId(0);

        assertEquals(3, id);
    }

    @Test
    void returnsCorrectFlagId() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(
                new Document("_id", 0)
                    .append("commentCount", 5)
                    .append("upvoteCount", 3)
                    .append("flagCount", 7));

        int id = counterDAO.getNextReviewFlagId(0);

        assertEquals(7, id);
    }

    @Test
    void commentHandlesNullRespose() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(null);

        assertThrows(RecordDoesNotExistException.class, () -> counterDAO.getNextCommentId(0));
    }

    @Test
    void upvoteHandlesNullRespose() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(null);

        assertThrows(RecordDoesNotExistException.class, () -> counterDAO.getNextUpvoteId(0));
    }

    @Test
    void flagHandlesNullRespose() {
        when(testCollection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
            .thenReturn(null);

        assertThrows(RecordDoesNotExistException.class, () -> counterDAO.getNextReviewFlagId(0));
    }
}