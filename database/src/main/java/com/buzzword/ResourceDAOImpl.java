package com.buzzword;

import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.client.MongoCollection;

public class ResourceDAOImpl implements ResourceDAO {
    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();

    public ResourceDAOImpl(MongoDatabase db) {
        this.resources = db.getCollection("users");
    }

    @Override
    public Void insertResource(Credentials user, Resource resource) {
        // Require an authenticated user to insert resources (simple policy).
        if (user == null || user.getId() == null) {
            logger.error("Unauthenticated user denied permission to insert resource");
            throw new AuthorizationException("User must be authenticated to insert resources");
        }

        // Build a sub-document for the resource and push it into the user's resources array
        Document resourceDoc = new Document()
            .append("id", resource.getId())
            .append("title", resource.getTitle())
            .append("description", resource.getDescription())
            .append("url", resource.getUrl())
            .append("creatorId", user.getId());  // Track who created the resource

        // Initialize embedded arrays and counters for consistent updates later
        resourceDoc.append("comments", new ArrayList<>())
                   .append("upVotes", new ArrayList<>())
                   .append("reviewFlags", new ArrayList<>())
                   .append("upvoteCount", 0)
                   .append("creationDate", new java.util.Date());

        // Push the resource into the resources collection
        resources.updateOne(
            Filters.eq("id", user.getId()),
            new Document("$push", new Document("resources", resourceDoc))
        );

        logger.info(String.format("User %d inserted new resource %d", user.getId(), resource.getId()));
        return null;
    }

    @Override
    public boolean removeResource(Credentials user, long id) {
        // First, find the resource to check ownership
        Document doc = resources.find(
            Filters.eq("resourceId", id)).first();

        if (doc == null) {
            logger.warn(String.format("Failed to find resource %d for removal by user %d", id, user.getId()));
            return false;  // Resource not found
        }

        // Check if user has permission to delete this resource.
        // Allow deletion only if the user is an Admin or the original creator of the resource.
        boolean isAdmin = "Admin".equals(user.getSystemRole());
        Integer creatorId = doc.getInteger("creatorId");
        boolean isCreator = creatorId != null && creatorId.equals(user.getId());

        if (!isAdmin && !isCreator) {
            logger.error(String.format("User %d with role %s denied permission to delete resource %d", 
                user.getId(), user.getSystemRole(), id));
            throw new AuthorizationException("User does not have permission to delete this resource");
        }

        // Pull (delete) the resource entirely from the user's resources array
        com.mongodb.client.result.UpdateResult result = resources.updateOne(
            Filters.elemMatch("resources", new Document("id", id)),
            new Document("$pull", new Document("resources", new Document("id", id)))
        );
        
        boolean removed = result.getModifiedCount() > 0;
        if (removed) {
            logger.info(String.format("User %d removed resource %d", user.getId(), id));
        } else {
            logger.warn(String.format("User %d failed to remove resource %d", user.getId(),id));
        }
        return removed;
    }

    public Resource convertDocumentToResource(Document doc) {
        Resource resource = new Resource();
        resource.setId(doc.getInteger("id"));
        resource.setTitle(doc.getString("title"));
        resource.setDescription(doc.getString("description"));
        resource.setUrl(doc.getString("url"));
        return resource;
    }

    @Override
    public List<Resource> listAllResources(Credentials user) {
        List<Resource> allResources = new ArrayList<>();

        resources.find().forEach(resDoc -> {
            Resource resource = convertDocumentToResource(resDoc);
            allResources.add(resource);
        });

        return allResources;
    }
}