package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
public class UpvoteDAOTest {
    
    @Mock
    MongoDatabase testDatabase;

    @Mock
    MongoCollection<Document> testCollection;

    @Mock
    CounterDAO mockCounterDAO;

    UpvoteDAO upvoteDAO;

    @BeforeEach
    void setUpDatabase() {
        when(testDatabase.getCollection("upvotes")).thenReturn(testCollection);
        upvoteDAO = new UpvoteDAOImpl(testDatabase);
        upvoteDAO.setCounterDAO(mockCounterDAO);
    }

    @Test
    void developerMayInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");
        
        when(mockCounterDAO.getNextUpvoteId(1)).thenReturn(1);

        UpVote mockUpvote = mock(UpVote.class);
        when(mockUpvote.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);


        upvoteDAO.addUpvote(mockCredentials, mockUpvote, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("creatorId", 1)
            .append("resourceId", 1)
            .append("upvoteId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
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
        
        when(mockCounterDAO.getNextUpvoteId(1)).thenReturn(1);

        UpVote mockUpvote = mock(UpVote.class);
        when(mockUpvote.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
 
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);


        upvoteDAO.addUpvote(mockCredentials, mockUpvote, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("creatorId", 1)
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
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
        
        when(mockCounterDAO.getNextUpvoteId(1)).thenReturn(1);

        UpVote mockUpvote = mock(UpVote.class);
        when(mockUpvote.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);


        upvoteDAO.addUpvote(mockCredentials, mockUpvote, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("creatorId", 1)
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        Assertions.assertThat(capturedDoc)
            .usingRecursiveComparison()
            .isEqualTo(expectedDoc);
    }

    @Test
    void duplicateUpvoteMayNotBeSubmitted() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        UpVote mockUpvote = mock(UpVote.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(RecordAlreadyExistsException.class, () -> {
            upvoteDAO.addUpvote(mockCredentials, mockUpvote, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void invalidRoleMayNotInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");

        UpVote mockUpvote = mock(UpVote.class);

        assertThrows(AuthorizationException.class, () -> {
            upvoteDAO.addUpvote(mockCredentials, mockUpvote, 1);
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
        Document targetDocument  = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        upvoteDAO.removeUpvote(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("upvoteId", 1), Filters.eq("resourceId", 1));
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void managerMayNotDeleteIfNotCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");


        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            upvoteDAO.removeUpvote(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void developerMayDeleteIfCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        upvoteDAO.removeUpvote(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("upvoteId", 1), Filters.eq("resourceId", 1));
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
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            upvoteDAO.removeUpvote(mockCredentials, 1, 1);
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
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        upvoteDAO.removeUpvote(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("upvoteId", 1), Filters.eq("resourceId", 1));
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
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            upvoteDAO.removeUpvote(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void invalidRoleMayNotDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");


        assertThrows(AuthorizationException.class, () -> {
            upvoteDAO.removeUpvote(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }
}