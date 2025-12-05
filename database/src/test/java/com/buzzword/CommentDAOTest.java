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
public class CommentDAOTest {
    
    @Mock
    MongoDatabase testDatabase;

    @Mock
    MongoCollection<Document> testCollection;

    @Mock
    CounterDAO mockCounterDAO;

    CommentDAO commentDAO;

    @BeforeEach
    void setUpDatabase() {
        when(testDatabase.getCollection("comments")).thenReturn(testCollection);
        commentDAO = new CommentDAOImpl(testDatabase);
        commentDAO.setCounterDAO(mockCounterDAO);
    }

    @Test
    void cannotRemovingMissingComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(RecordDoesNotExistException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1, 1);
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

        when(mockCounterDAO.getNextCommentId(1)).thenReturn(1);

        Comment mockComment = mock(Comment.class);
        when(mockComment.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockComment.getContents()).thenReturn("Thanks for the suggestion!");

        commentDAO.addComment(mockCredentials, mockComment, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("creatorId", 1)
            .append("resourceId", 1)
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

        when(mockCounterDAO.getNextCommentId(1)).thenReturn(1);

        Comment mockComment = mock(Comment.class);
        when(mockComment.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockComment.getContents()).thenReturn("Thanks for the suggestion!");

        commentDAO.addComment(mockCredentials, mockComment, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("commentId", 1)
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

        when(mockCounterDAO.getNextCommentId(1)).thenReturn(1);

        Comment mockComment = mock(Comment.class);
        when(mockComment.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockComment.getContents()).thenReturn("Thanks for the suggestion!");

        commentDAO.addComment(mockCredentials, mockComment, 1);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("commentId", 1)
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

        Comment mockComment = mock(Comment.class);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.addComment(mockCredentials, mockComment, 1);
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
            .append("commentId", 1)
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

        commentDAO.removeComment(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("resourceId", 1), Filters.eq("commentId", 1));
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

        commentDAO.removeComment(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("resourceId", 1), Filters.eq("commentId", 1));
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
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1, 1);
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

        commentDAO.removeComment(mockCredentials, 1, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.and(Filters.eq("resourceId", 1), Filters.eq("commentId", 1));
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
            .append("commentId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Thanks for the suggestion!");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void invalidRoleMayNotDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");


        assertThrows(AuthorizationException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    // ============ Edit Comment Tests ============

    @Test
    void adminMayEditComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        Comment mockComment = mock(Comment.class);
        when(mockComment.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("commentId", 1)
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

        commentDAO.editComment(mockCredentials, 1, mockComment, 1);

        verify(testCollection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void contributorMayEditOwnComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        Comment mockComment = mock(Comment.class);
        when(mockComment.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("commentId", 1)
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

        commentDAO.editComment(mockCredentials, 1, mockComment, 1);

        verify(testCollection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void contributorMayNotEditOthersComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        Comment mockComment = mock(Comment.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("commentId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.editComment(mockCredentials, 1, mockComment, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void commenterMayEditOwnComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        Comment mockComment = mock(Comment.class);
        when(mockComment.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("commentId", 1)
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

        commentDAO.editComment(mockCredentials, 1, mockComment, 1);

        verify(testCollection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void commenterMayNotEditOthersComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        Comment mockComment = mock(Comment.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("commentId", 1)
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "Original content");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.editComment(mockCredentials, 1, mockComment, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void cannotEditNonexistentComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        Comment mockComment = mock(Comment.class);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(mockIterable.first()).thenReturn(null);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(RecordDoesNotExistException.class, () -> {
            commentDAO.editComment(mockCredentials, 1, mockComment, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void cannotEditCommentWhenUpdateFails() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        Comment mockComment = mock(Comment.class);
        when(mockComment.getContents()).thenReturn("Updated content");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument = new Document()
            .append("commentId", 1)
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
            commentDAO.editComment(mockCredentials, 1, mockComment, 1);
        });
    }

    @Test
    void invalidRoleMayNotEditComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");

        Comment mockComment = mock(Comment.class);

        assertThrows(AuthorizationException.class, () -> {
            commentDAO.editComment(mockCredentials, 1, mockComment, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    // ============ Null Parameter Tests ============

    @Test
    void addCommentThrowsOnNullUser() {
        Comment mockComment = mock(Comment.class);

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.addComment(null, mockComment, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void addCommentThrowsOnNullComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.addComment(mockCredentials, null, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void addCommentThrowsOnNullRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn(null);

        Comment mockComment = mock(Comment.class);

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.addComment(mockCredentials, mockComment, 1);
        });

        verify(testCollection, never()).insertOne(any());
    }

    @Test
    void editCommentThrowsOnNullUser() {
        Comment mockComment = mock(Comment.class);

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.editComment(null, 1, mockComment, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void editCommentThrowsOnNullComment() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.editComment(mockCredentials, 1, null, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void editCommentThrowsOnNullRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn(null);

        Comment mockComment = mock(Comment.class);

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.editComment(mockCredentials, 1, mockComment, 1);
        });

        verify(testCollection, never()).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void removeCommentThrowsOnNullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.removeComment(null, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void removeCommentThrowsOnNullRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.removeComment(mockCredentials, 1, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void setCounterDAOThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            commentDAO.setCounterDAO(null);
        });
    }

    @Test
    void constructorThrowsOnNullDatabase() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CommentDAOImpl(null);
        });
    }
}