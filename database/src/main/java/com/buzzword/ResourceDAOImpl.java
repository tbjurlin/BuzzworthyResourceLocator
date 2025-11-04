package com.buzzword;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class ResourceDAOImpl implements ResourceDAO {
    private final MongoCollection<Document> resources;
    private final Logger logger = LoggerFactory.getEventLogger();

    public ResourceDAOImpl(MongoDatabase db) {
        this.resources = db.getCollection("resources");
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

        // Build a sub-document for the resource and push it into the user's resources array
        Document resourceDoc = new Document()
            .append("resourceId", resource.getId())
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
    public boolean removeResource(Credentials user, long id) {
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
                return false;  // Resource not found
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
            logger.info(String.format("User %d removed resource %d", user.getId(), id));
        } else {
            logger.warn(String.format("User %d failed to remove resource %d", user.getId(),id));
        }
        return removed;
    }

    private Resource convertDocumentToResource(Document doc) {
        Resource resource = new Resource();
        resource.setId(doc.getInteger("resourceId"));
        resource.setTitle(doc.getString("title"));
        resource.setDescription(doc.getString("description"));
        resource.setUrl(doc.getString("url"));
        resource.setCreatorId(doc.getInteger("creatorId"));
        resource.setCreationDate(doc.getDate("dateCreated"));
        resource.setCreatorFirstName(doc.getString("firstName"));
        resource.setCreatorLastName(doc.getString("lastName"));
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

        List<Resource> allResources = new ArrayList<>();

        resources.find().forEach(resDoc -> {
            Resource resource = convertDocumentToResource(resDoc);
            allResources.add(resource);
        });

        return allResources;
    }
}