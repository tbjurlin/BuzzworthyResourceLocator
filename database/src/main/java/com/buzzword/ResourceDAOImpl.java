package com.buzzword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class ResourceDAOImpl implements ResourceDAO {
    private final MongoCollection<Document> resources;
    private final MongoCollection<Document> comments;
    private final MongoCollection<Document> flags;
    private final MongoCollection<Document> upvotes;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    public ResourceDAOImpl(MongoDatabase db) {
        this.resources = db.getCollection("resources");
        this.comments = db.getCollection("comments");
        this.flags = db.getCollection("flags");
        this.upvotes = db.getCollection("upvotes");
        counterDAO = new CounterDAOImpl(db);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCounterDAO(CounterDAO counterDAO) {
        this.counterDAO = counterDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertResource(Credentials user, Resource resource) {
        // Require an authenticated user to insert resources (simple policy).
        if (user == null || (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor")) {
            logger.error("Unauthenticated user denied permission to insert resource");
            throw new AuthorizationException("User must be authenticated to insert resources");
        }

        Document resourceDoc = new Document()
            .append("resourceId", counterDAO.getNextResourceId())
            .append("title", resource.getTitle())
            .append("description", resource.getDescription())
            .append("url", resource.getUrl())
            .append("creatorId", user.getId())  // Track who created the resource
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("dateCreated", resource.getCreationDate());

        // Push the resource into the resources collection
        resources.insertOne(resourceDoc);

        logger.info(String.format("User %d inserted new resource %d", user.getId(), resource.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeResource(Credentials user, int id) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor") {
            logger.error(String.format("User %d with role %s denied permission to delete resource %d", 
                user.getId(), user.getSystemRole(), id));
            throw new AuthorizationException("User does not have permission to delete this resource");
        }
        if (user.getSystemRole() == "Contributor") {
            // First, find the resource to check ownership
            Document doc = resources.find(
                Filters.eq("resourceId", id)).first();

            if (doc == null) {
                logger.warn(String.format("Failed to find resource %d for removal by user %d", id, user.getId()));
                throw new RecordDoesNotExistException("Failed to find resource for removal");
            }

            // Check if user has permission to delete this resource.
            // Allow deletion only if the user is the original creator of the resource.
            Integer creatorId = doc.getInteger("creatorId");
            boolean isCreator = creatorId != null && creatorId.equals(user.getId());

            if (!isCreator) {
                logger.error(String.format("User %d with role %s denied permission to delete resource %d", 
                    user.getId(), user.getSystemRole(), id));
                throw new AuthorizationException("User does not have permission to delete this resource");
            }
        }

                // Delete the resource document
        DeleteResult result = resources.deleteOne(
            Filters.eq("resourceId", id)
        );
        
        boolean removed = result.getDeletedCount() > 0;
        if (removed) {
            counterDAO.removeResourceCounters(id);
            logger.info(String.format("User %d removed resource %d", user.getId(), id));
        } else {
            logger.warn(String.format("User %d failed to remove resource %d", user.getId(),id));
            throw new RecordDoesNotExistException("Failed to find resource for removal");
        }
    }

    private Resource convertDocumentToResource(Document doc) {
        Resource resource = new Resource();
        System.out.println(doc);
        resource.setId(doc.getInteger("resourceId"));
        resource.setTitle(doc.getString("title"));
        resource.setDescription(doc.getString("description"));
        resource.setUrl(doc.getString("url"));
        resource.setCreatorId(doc.getInteger("creatorId"));
        resource.setCreationDate(doc.getDate("dateCreated"));
        resource.setFirstName(doc.getString("firstName"));
        resource.setLastName(doc.getString("lastName"));
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resource> listAllResources(Credentials user) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with role %s denied permission to retrieve resources: role is not valid.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User does not have a valid system role.");
        }


        Map<Integer, Resource> resourceMap = new HashMap<Integer, Resource>();

        resources.find().forEach(resDoc -> {
            Resource resource = convertDocumentToResource(resDoc);
            resource.setComments(new ArrayList<Comment>());
            resource.setReviewFlags(new ArrayList<ReviewFlag>());
            resource.setUpvotes(new ArrayList<Upvote>());
            resourceMap.put(resource.getId(), resource);
            System.out.println(resourceMap);
        });

        comments.find().forEach(commentDoc -> {
            Comment comment = new Comment();
            comment.setId(commentDoc.getInteger("commentId"));
            comment.setCreatorId(commentDoc.getInteger("creatorId"));
            comment.setFirstName(commentDoc.getString("firstName"));
            comment.setLastName(commentDoc.getString("lastName"));
            comment.setCreationDate(commentDoc.getDate("dateCreated"));
            comment.setContents(commentDoc.getString("contents"));

            Resource parent = resourceMap.get(commentDoc.getInteger("resourceId"));
            List<Comment> comments = parent.getComments();
            comments.add(comment);
        });

        flags.find().forEach(flagDoc -> {
            ReviewFlag flag = new ReviewFlag();
            flag.setId(flagDoc.getInteger("flagId"));
            flag.setCreatorId(flagDoc.getInteger("creatorId"));
            flag.setFirstName(flagDoc.getString("firstName"));
            flag.setLastName(flagDoc.getString("lastName"));
            flag.setCreationDate(flagDoc.getDate("dateCreated"));
            flag.setContents(flagDoc.getString("contents"));

            Resource parent = resourceMap.get(flagDoc.getInteger("resourceId"));
            List<ReviewFlag> flags = parent.getReviewFlags();
            flags.add(flag);
        });

        upvotes.find().forEach(upvoteDoc -> {
            Upvote upvote = new Upvote();
            upvote.setId(upvoteDoc.getInteger("upvoteId"));
            upvote.setCreatorId(upvoteDoc.getInteger("creatorId"));
            upvote.setFirstName(upvoteDoc.getString("firstName"));
            upvote.setLastName(upvoteDoc.getString("lastName"));
            upvote.setCreationDate(upvoteDoc.getDate("dateCreated"));

            Resource parent = resourceMap.get(upvoteDoc.getInteger("resourceId"));
            List<Upvote> upvotes = parent.getUpvotes();
            upvotes.add(upvote);
        });

        return new ArrayList<Resource>(resourceMap.values());
    }
}