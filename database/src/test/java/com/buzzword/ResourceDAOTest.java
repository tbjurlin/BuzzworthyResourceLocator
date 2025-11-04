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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

@ExtendWith(MockitoExtension.class)
public class ResourceDAOTest {
    
    @Mock
    MongoDatabase testDatabase;

    @Mock
    MongoCollection<Document> testCollection;

    ResourceDAO resourceDAO;

    @BeforeEach
    void setUpDatabase() {
        when(testDatabase.getCollection("resources")).thenReturn(testCollection);
        resourceDAO = new ResourceDAOImpl(testDatabase);
    }

    @Test
    void developerMayInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        Resource mockResource = mock(Resource.class);
        when(mockResource.getId()).thenReturn(1);
        when(mockResource.getCreatorId()).thenReturn(1);
        when(mockResource.getCreatorFirstName()).thenReturn("Foo");
        when(mockResource.getCreatorLastName()).thenReturn("Bar");
        when(mockResource.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockResource.getTitle()).thenReturn("Title");
        when(mockResource.getDescription()).thenReturn("Description");
        when(mockResource.getUrl()).thenReturn("localhost");

        resourceDAO.insertResource(mockCredentials, mockResource);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");
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

        Resource mockResource = mock(Resource.class);
        when(mockResource.getId()).thenReturn(1);
        when(mockResource.getCreatorId()).thenReturn(1);
        when(mockResource.getCreatorFirstName()).thenReturn("Foo");
        when(mockResource.getCreatorLastName()).thenReturn("Bar");
        when(mockResource.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockResource.getTitle()).thenReturn("Title");
        when(mockResource.getDescription()).thenReturn("Description");
        when(mockResource.getUrl()).thenReturn("localhost");

        resourceDAO.insertResource(mockCredentials, mockResource);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("creatorId", 1)
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");
        Assertions.assertThat(capturedDoc)
            .usingRecursiveComparison()
            .isEqualTo(expectedDoc);
    }

    @Test
    void generalEmployeeMayNotInsert() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        Resource mockResource = mock(Resource.class);
        when(mockResource.getId()).thenReturn(1);
        when(mockResource.getCreatorId()).thenReturn(1);
        when(mockResource.getCreatorFirstName()).thenReturn("Foo");
        when(mockResource.getCreatorLastName()).thenReturn("Bar");
        when(mockResource.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockResource.getTitle()).thenReturn("Title");
        when(mockResource.getDescription()).thenReturn("Description");
        when(mockResource.getUrl()).thenReturn("localhost");

        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.insertResource(mockCredentials, mockResource);
        });

        verify(testCollection, never()).insertOne(any());

    }

    @Test
    void managerMayDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        resourceDAO.removeResource(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("recordId", 1);
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void developerMayDeleteIfCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        resourceDAO.removeResource(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("recordId", 1);
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void developerMayNotDeleteIfNotCreator() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Contributor");

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockIterable = (FindIterable<Document>) mock(FindIterable.class);
        Document targetDocument  = new Document()
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(testCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        resourceDAO.removeResource(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("recordId", 1);
        Assertions.assertThat(capturedFilter)
            .usingRecursiveComparison()
            .isEqualTo(expectedFilter);
    }

    @Test
    void testListsAllResources() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getFirstName()).thenReturn("Foo");
        when(mockCredentials.getLastName()).thenReturn("Bar");
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        resourceDAO.listAllResources(mockCredentials);


    }
}