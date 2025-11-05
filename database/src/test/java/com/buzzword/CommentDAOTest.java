package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

@ExtendWith(MockitoExtension.class)
public class CommentDAOTest {
    
    @Mock
    MongoDatabase testDatabase;

    @Mock
    MongoCollection<Document> testCollection;

    CommentDAO commentDAO;

    @BeforeEach
    void setUpDatabase() {
        when(testDatabase.getCollection("comments")).thenReturn(testCollection);
        commentDAO = new CommentDAOImpl(testDatabase);
    }

    @Test
    void developerMayInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        Comment mockComment = mock(Comment.class);
        when(mockComment.getId()).thenReturn(1);
        when(mockComment.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockComment.getContents()).thenReturn("Thanks for the suggestion!");

        commentDAO.addComment(mockCredentials, mockComment);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("creatorId", 1)
            .append("commentId", 1)
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

        Comment mockComment = mock(Comment.class);
        when(mockComment.getId()).thenReturn(1);
        when(mockComment.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockComment.getContents()).thenReturn("Thanks for the suggestion!");

        commentDAO.addComment(mockCredentials, mockComment);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("commentId", 1)
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

        Comment mockComment = mock(Comment.class);
        when(mockComment.getId()).thenReturn(1);
        when(mockComment.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockComment.getContents()).thenReturn("Thanks for the suggestion!");

        commentDAO.addComment(mockCredentials, mockComment);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("commentId", 1)
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

        Comment mockComment = mock(Comment.class);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.addComment(mockCredentials, mockComment);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void managerMayDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        commentDAO.removeComment(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("commentId", 1L);
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
            .append("commentId", 1)
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

        commentDAO.removeComment(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("commentId", 1L);
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
            .append("commentId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("description", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1);
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
            .append("commentId", 1)
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

        commentDAO.removeComment(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("commentId", 1L);
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void generalEmployeeMayNotDeleteIfNotCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("commenter");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("commentId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("description", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void invalidRoleMayNotDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");


        assertThrows(AuthorizationException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }
}