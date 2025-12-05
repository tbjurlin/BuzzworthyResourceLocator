package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

@ExtendWith(MockitoExtension.class)
public class FlagDAOTest {
    
    @Mock
    MongoDatabase testDatabase;

    @Mock
    MongoCollection<Document> testCollection;

    @Mock
    CounterDAO mockCounterDAO;

    FlagDAO flagDAO;

    @BeforeEach
    void setUpDatabase() {
        when(testDatabase.getCollection("flags")).thenReturn(testCollection);
        flagDAO = new FlagDAOImpl(testDatabase);
        flagDAO.setCounterDAO(mockCounterDAO);
    }

    @Test
    void cannotRemovingMissingFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(RecordDoesNotExistException.class, () -> {
            flagDAO.removeReviewFlag(mockCredentials, 1, 1);
        });

        verifyNoInteractions(mockCounterDAO);
    }

    @Test
    void developerMayInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        when(mockCounterDAO.getNextReviewFlagId(1)).thenReturn(1);

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockFlag.getContents()).thenReturn("Thanks for the suggestion!");

        flagDAO.addReviewFlag(mockCredentials, mockFlag, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("creatorId", 1)
            .append("resourceId", 1)
            .append("flagId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        Assertions.assertThat(capturedDoc)
            .usingRecursiveComparison()
            .isEqualTo(expectedDoc);
    }

    @Test
    void managerMayInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        when(mockCounterDAO.getNextReviewFlagId(1)).thenReturn(1);

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockFlag.getContents()).thenReturn("Thanks for the suggestion!");

        flagDAO.addReviewFlag(mockCredentials, mockFlag, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("creatorId", 1)
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        Assertions.assertThat(capturedDoc)
            .usingRecursiveComparison()
            .isEqualTo(expectedDoc);
    }

    @Test
    void generalEmployeeMayInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        when(mockCounterDAO.getNextReviewFlagId(1)).thenReturn(1);

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockFlag.getContents()).thenReturn("Thanks for the suggestion!");

        flagDAO.addReviewFlag(mockCredentials, mockFlag, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("creatorId", 1)
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        Assertions.assertThat(capturedDoc)
            .usingRecursiveComparison()
            .isEqualTo(expectedDoc);
    }

    @Test
    void invalidRoleMayNotInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        assertThrows(AuthorizationException.class, () -> {
            flagDAO.addReviewFlag(mockCredentials, mockFlag, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void managerMayDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        flagDAO.removeReviewFlag(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("resourceId", 1), Filters.eq("flagId", 1));
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void developerMayDeleteIfCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        flagDAO.removeReviewFlag(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("resourceId", 1), Filters.eq("flagId", 1));
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void developerMayNotDeleteIfNotCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            flagDAO.removeReviewFlag(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void generalEmployeeMayDeleteIfCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        flagDAO.removeReviewFlag(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("resourceId", 1), Filters.eq("flagId", 1));
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void generalEmployeeMayNotDeleteIfNotCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            flagDAO.removeReviewFlag(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void invalidRoleMayNotDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");


        assertThrows(AuthorizationException.class, () -> {
            flagDAO.removeReviewFlag(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    // ============ Edit Flag Tests ============

    @Test
    void adminMayEditFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        com.mongodb.client.result.UpdateResult mockResult = mock(com.mongodb.client.result.UpdateResult.class);
        when(mockResult.getMatchedCount()).thenReturn(1L);
        when(testCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(mockResult);

        flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);

        verify(testCollection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void contributorMayEditOwnFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        com.mongodb.client.result.UpdateResult mockResult = mock(com.mongodb.client.result.UpdateResult.class);
        when(mockResult.getMatchedCount()).thenReturn(1L);
        when(testCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(mockResult);

        flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);

        verify(testCollection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void contributorMayNotEditOthersFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void commenterMayEditOwnFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        com.mongodb.client.result.UpdateResult mockResult = mock(com.mongodb.client.result.UpdateResult.class);
        when(mockResult.getMatchedCount()).thenReturn(1L);
        when(testCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(mockResult);

        flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);

        verify(testCollection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void commenterMayNotEditOthersFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void cannotEditNonexistentFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(RecordDoesNotExistException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void cannotEditFlagWhenUpdateFails() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        ReviewFlag mockFlag = mock(ReviewFlag.class);
        when(mockFlag.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        com.mongodb.client.result.UpdateResult mockResult = mock(com.mongodb.client.result.UpdateResult.class);
        when(mockResult.getMatchedCount()).thenReturn(0L);
        when(testCollection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(mockResult);

        assertThrows(RecordDoesNotExistException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);
        });
    }

    @Test
    void invalidRoleMayNotEditFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        assertThrows(AuthorizationException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    // ============ Null Parameter Tests ============

    @Test
    void addFlagThrowsOnNullUser() {
        ReviewFlag mockFlag = mock(ReviewFlag.class);

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.addReviewFlag(null, mockFlag, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void addFlagThrowsOnNullFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.addReviewFlag(mockCredentials, null, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void addFlagThrowsOnNullRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn(null);

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.addReviewFlag(mockCredentials, mockFlag, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void editFlagThrowsOnNullUser() {
        ReviewFlag mockFlag = mock(ReviewFlag.class);

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.editReviewFlag(null, 1, mockFlag, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void editFlagThrowsOnNullFlag() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, null, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void editFlagThrowsOnNullRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn(null);

        ReviewFlag mockFlag = mock(ReviewFlag.class);

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.editReviewFlag(mockCredentials, 1, mockFlag, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void removeFlagThrowsOnNullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.removeReviewFlag(null, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void removeFlagThrowsOnNullRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.removeReviewFlag(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void setCounterDAOThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            flagDAO.setCounterDAO(null);
        });
    }

    @Test
    void constructorThrowsOnNullDatabase() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FlagDAOImpl(null);
        });
    }
}