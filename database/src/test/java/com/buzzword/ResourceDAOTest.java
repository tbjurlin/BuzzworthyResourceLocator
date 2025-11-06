package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    MongoCollection<Document> resourceCollection;

    @Mock
    MongoCollection<Document> commentCollection;

    @Mock
    MongoCollection<Document> flagCollection;

    @Mock
    MongoCollection<Document> upvoteCollection;

    @Mock
    CounterDAO mockCounterDAO;

    ResourceDAO resourceDAO;

    @BeforeEach
    void setUpDatabase() {

        when(testDatabase.getCollection("comments")).thenReturn(commentCollection);

        when(testDatabase.getCollection("flags")).thenReturn(flagCollection);

        when(testDatabase.getCollection("upvotes")).thenReturn(upvoteCollection);

        when(testDatabase.getCollection("resources")).thenReturn(resourceCollection);
        resourceDAO = new ResourceDAOImpl(testDatabase);
        resourceDAO.setCounterDAO(mockCounterDAO);
    }

    @Test
    void cannotRemovingMissingResource() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(0L);
        when(resourceCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        assertThrows(RecordDoesNotExistException.class, () -> {
            resourceDAO.removeResource(mockCredentials, 1);
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

        when(mockCounterDAO.getNextResourceId()).thenReturn(1);

        Resource mockResource = mock(Resource.class);
        when(mockResource.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockResource.getTitle()).thenReturn("Title");
        when(mockResource.getDescription()).thenReturn("Description");
        when(mockResource.getUrl()).thenReturn("http://example.com");

        resourceDAO.insertResource(mockCredentials, mockResource);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(resourceCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("creatorId", 1)
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "http://example.com");
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

        when(mockCounterDAO.getNextResourceId()).thenReturn(1);

        Resource mockResource = mock(Resource.class);
        when(mockResource.getCreationDate()).thenReturn(Date.from(Instant.ofEpochSecond(946684800)));
        when(mockResource.getTitle()).thenReturn("Title");
        when(mockResource.getDescription()).thenReturn("Description");
        when(mockResource.getUrl()).thenReturn("http://example.com");

        resourceDAO.insertResource(mockCredentials, mockResource);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(resourceCollection).insertOne(captor.capture());

        Document capturedDoc = captor.getValue();
        Document expectedDoc = new Document()
            .append("resourceId", 1)
            .append("firstName", "Foo")
            .append("creatorId", 1)
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "http://example.com");
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

        verify(resourceCollection, never()).insertOne(any());

    }

    @Test
    void managerMayDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Admin");

        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(resourceCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        resourceDAO.removeResource(mockCredentials, 1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(resourceCollection).deleteOne(captor.capture());

        verify(mockCounterDAO).removeResourceCounters(1);

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("resourceId", 1);
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
            .append("url", "http://example.com");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(resourceCollection.find(any(Bson.class))).thenReturn(mockIterable);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockResult.getDeletedCount()).thenReturn(1L);
        when(resourceCollection.deleteOne(any(Bson.class))).thenReturn(mockResult);

        resourceDAO.removeResource(mockCredentials, 1);

        verify(mockCounterDAO).removeResourceCounters(1);

        ArgumentCaptor<Bson> captor = ArgumentCaptor.forClass(Bson.class);
        verify(resourceCollection).deleteOne(captor.capture());

        Bson capturedFilter = captor.getValue();
        Bson expectedFilter = Filters.eq("resourceId", 1);
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
            .append("url", "http://example.com");
        when(mockIterable.first()).thenReturn(targetDocument);
        when(resourceCollection.find(any(Bson.class))).thenReturn(mockIterable);

        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.removeResource(mockCredentials, 1);
        });

        verifyNoInteractions(mockCounterDAO);

        verify(resourceCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void generalEmployeeMayNotDelete() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(1);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");


        assertThrows(AuthorizationException.class, () -> {
            resourceDAO.removeResource(mockCredentials, 1);
        });

        verifyNoInteractions(mockCounterDAO);

        verify(resourceCollection, never()).deleteOne(any(Bson.class));
    }

    @Test
    void testListsAllResources() {
        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getSystemRole()).thenReturn("Commenter");

        Document resourceDocument1  = new Document()
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "http://example.com");

        Document resourceDocument2  = new Document()
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("title", "Title")
            .append("description", "Description")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("url", "http://example.com");

        List<Document> resourceResponse = new ArrayList<Document>();
        
        resourceResponse.add(resourceDocument1);
        resourceResponse.add(resourceDocument2);

        @SuppressWarnings("unchecked")
        FindIterable<Document> resourceFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(resourceCollection.find()).thenReturn(resourceFindIterable);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
                Consumer<Document> consumer = (Consumer<Document>) args[0];
                resourceResponse.iterator().forEachRemaining(consumer);
                return null;
            }
        }).when(resourceFindIterable).forEach(any());

        Document commentDocument1  = new Document()
            .append("commentId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "What a comment");

        Document commentDocument2  = new Document()
            .append("commentId", 2)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "What a comment.");

        Document commentDocument3  = new Document()
            .append("commentId", 3)
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "What a comment?");

        Document commentDocument4  = new Document()
            .append("commentId", 4)
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)))
            .append("contents", "What a comment!");

        List<Document> commentResponse = new ArrayList<Document>();
        
        commentResponse.add(commentDocument1);
        commentResponse.add(commentDocument2);
        commentResponse.add(commentDocument3);
        commentResponse.add(commentDocument4);

        @SuppressWarnings("unchecked")
        FindIterable<Document> commentFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(commentCollection.find()).thenReturn(commentFindIterable);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
                Consumer<Document> consumer = (Consumer<Document>) args[0];
                commentResponse.iterator().forEachRemaining(consumer);
                return null;
            }
        }).when(commentFindIterable).forEach(any());

        Document flagDocument1  = new Document()
            .append("flagId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));

        Document flagDocument2  = new Document()
            .append("flagId", 2)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));

        Document flagDocument3  = new Document()
            .append("flagId", 3)
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));

        List<Document> flagResponse = new ArrayList<Document>();
        
        flagResponse.add(flagDocument1);
        flagResponse.add(flagDocument2);
        flagResponse.add(flagDocument3);

        @SuppressWarnings("unchecked")
        FindIterable<Document> flagFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(flagCollection.find()).thenReturn(flagFindIterable);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
                Consumer<Document> consumer = (Consumer<Document>) args[0];
                flagResponse.iterator().forEachRemaining(consumer);
                return null;
            }
        }).when(flagFindIterable).forEach(any());

        Document upvoteDocument1  = new Document()
            .append("upvoteId", 1)
            .append("resourceId", 1)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));

        Document upvoteDocument2  = new Document()
            .append("upvoteId", 2)
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));

        Document upvoteDocument3  = new Document()
            .append("upvoteId", 3)
            .append("resourceId", 2)
            .append("creatorId", 1)
            .append("firstName", "Foo")
            .append("lastName", "Bar")
            .append("dateCreated", Date.from(Instant.ofEpochSecond(946684800)));

        List<Document> upvoteResponse = new ArrayList<Document>();
        
        upvoteResponse.add(upvoteDocument1);
        upvoteResponse.add(upvoteDocument2);
        upvoteResponse.add(upvoteDocument3);

        @SuppressWarnings("unchecked")
        FindIterable<Document> upvoteFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        when(upvoteCollection.find()).thenReturn(upvoteFindIterable);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
                Consumer<Document> consumer = (Consumer<Document>) args[0];
                upvoteResponse.iterator().forEachRemaining(consumer);
                return null;
            }
        }).when(upvoteFindIterable).forEach(any());

        List<Resource> results = resourceDAO.listAllResources(mockCredentials);

        List<Resource> expected = new ArrayList<Resource>();

        Resource targetResource1  = new Resource();
        targetResource1.setId(1);
        targetResource1.setCreatorId(1);
        targetResource1.setFirstName("Foo");
        targetResource1.setLastName("Bar");
        targetResource1.setTitle("Title");
        targetResource1.setDescription("Description");
        targetResource1.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetResource1.setUrl("http://example.com");

        List<Comment> r1Comments = new ArrayList<Comment>();

        Comment targetComment1  = new Comment();
        targetComment1.setId(1);
        targetComment1.setCreatorId(1);
        targetComment1.setFirstName("Foo");
        targetComment1.setLastName("Bar");
        targetComment1.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetComment1.setContents("What a comment");

        Comment targetComment2  = new Comment();
        targetComment2.setId(2);
        targetComment2.setCreatorId(1);
        targetComment2.setFirstName("Foo");
        targetComment2.setLastName("Bar");
        targetComment2.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetComment2.setContents("What a comment.");

        r1Comments.add(targetComment1);
        r1Comments.add(targetComment2);

        List<ReviewFlag> r1Flags = new ArrayList<ReviewFlag>();

        ReviewFlag targetFlag1  = new ReviewFlag();
        targetFlag1.setId(1);
        targetFlag1.setCreatorId(1);
        targetFlag1.setFirstName("Foo");
        targetFlag1.setLastName("Bar");
        targetFlag1.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));

        ReviewFlag targetFlag2  = new ReviewFlag();
        targetFlag2.setId(2);
        targetFlag2.setCreatorId(1);
        targetFlag2.setFirstName("Foo");
        targetFlag2.setLastName("Bar");
        targetFlag2.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));

        r1Flags.add(targetFlag1);
        r1Flags.add(targetFlag2);

        List<Upvote> r1Upvotes = new ArrayList<Upvote>();

        Upvote targetUpvote1  = new Upvote();
        targetUpvote1.setId(1);
        targetUpvote1.setCreatorId(1);
        targetUpvote1.setFirstName("Foo");
        targetUpvote1.setLastName("Bar");
        targetUpvote1.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));

        r1Upvotes.add(targetUpvote1);

        targetResource1.setComments(r1Comments);
        targetResource1.setReviewFlags(r1Flags);
        targetResource1.setUpvotes(r1Upvotes);

        Resource targetResource2  = new Resource();
        targetResource2.setId(2);
        targetResource2.setCreatorId(1);
        targetResource2.setFirstName("Foo");
        targetResource2.setLastName("Bar");
        targetResource2.setTitle("Title");
        targetResource2.setDescription("Description");
        targetResource2.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetResource2.setUrl("http://example.com");

        List<Comment> r2Comments = new ArrayList<Comment>();

        Comment targetComment3  = new Comment();
        targetComment3.setId(3);
        targetComment3.setCreatorId(1);
        targetComment3.setFirstName("Foo");
        targetComment3.setLastName("Bar");
        targetComment3.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetComment3.setContents("What a comment?");

        Comment targetComment4  = new Comment();
        targetComment4.setId(4);
        targetComment4.setCreatorId(1);
        targetComment4.setFirstName("Foo");
        targetComment4.setLastName("Bar");
        targetComment4.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));
        targetComment4.setContents("What a comment!");

        r2Comments.add(targetComment3);
        r2Comments.add(targetComment4);

        List<ReviewFlag> r2Flags = new ArrayList<ReviewFlag>();

        ReviewFlag targetFlag3  = new ReviewFlag();
        targetFlag3.setId(3);
        targetFlag3.setCreatorId(1);
        targetFlag3.setFirstName("Foo");
        targetFlag3.setLastName("Bar");
        targetFlag3.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));

        r2Flags.add(targetFlag3);

        List<Upvote> r2Upvotes = new ArrayList<Upvote>();

        Upvote targetUpvote2  = new Upvote();
        targetUpvote2.setId(2);
        targetUpvote2.setCreatorId(1);
        targetUpvote2.setFirstName("Foo");
        targetUpvote2.setLastName("Bar");
        targetUpvote2.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));

        Upvote targetUpvote3  = new Upvote();
        targetUpvote3.setId(3);
        targetUpvote3.setCreatorId(1);
        targetUpvote3.setFirstName("Foo");
        targetUpvote3.setLastName("Bar");
        targetUpvote3.setCreationDate(Date.from(Instant.ofEpochSecond(946684800)));

        r2Upvotes.add(targetUpvote2);
        r2Upvotes.add(targetUpvote3);

        targetResource2.setComments(r2Comments);
        targetResource2.setReviewFlags(r2Flags);
        targetResource2.setUpvotes(r2Upvotes);

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