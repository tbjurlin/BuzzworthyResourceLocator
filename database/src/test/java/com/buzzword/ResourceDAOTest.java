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
        when(mockResource.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockResource.getTitle()).thenReturn("Title");
        when(mockResource.getDescription()).thenReturn("Description");
        when(mockResource.getUrl()).thenReturn("localhost");

        resourceDAO.insertResource(mockCredentials, mockResource);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(testCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("creatorId", 1)
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
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        Resource mockResource = mock(Resource.class);

        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.insertResource(mockCredentials, mockResource);
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

        resourceDAO.removeResource(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(testCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("resourceId", 1L);
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
        Bson expectedFilter = Filters.eq("resourceId", 1L);
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
            .append("resourceId", 1)
            .append("creatorId", 2)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(testCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.removeResource(mockCredentials, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void generalEmployeeMayNotDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");


        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.removeResource(mockCredentials, 1);
        });

        verify(testCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void testListsAllResources() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        Document targetDocument1  = new Document()
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");

        Document targetDocument2  = new Document()
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "localhost");

        List<Document> testResponse = new ArrayList<Document>();
        
        testResponse.add(targetDocument1);
        testResponse.add(targetDocument2);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(testCollection.find()).thenReturn(mockFindIterable);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
                Consumer<Document> consumer = (Consumer<Document>) args[0];
                testResponse.iterator().forEachRemaining(consumer);
                return null;
            }
        }).when(mockFindIterable).forEach(any());

        List<Resource> results = resourceDAO.listAllResources(mockCredentials);

        List<Resource> expected = new ArrayList<Resource>();

        Resource targetResource1  = new Resource();
        targetResource1.setId(1);
        targetResource1.setCreatorId(1);
        targetResource1.setCreatorFirstName("Foo");
        targetResource1.setCreatorLastName("Bar");
        targetResource1.setTitle("Title");
        targetResource1.setDescription("Description");
        targetResource1.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetResource1.setUrl("localhost");

        Resource targetResource2  = new Resource();
        targetResource2.setId(2);
        targetResource2.setCreatorId(1);
        targetResource2.setCreatorFirstName("Foo");
        targetResource2.setCreatorLastName("Bar");
        targetResource2.setTitle("Title");
        targetResource2.setDescription("Description");
        targetResource2.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetResource2.setUrl("localhost");

        expected.add(targetResource1);
        expected.add(targetResource2);

        Assertions.assertThat(results)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void dontListAllIfNoSystemRole() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Some Invalid Role");

        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.listAllResources(mockCredentials);
        });
    }
}