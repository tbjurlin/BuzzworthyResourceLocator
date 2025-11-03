package com.buzzword;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceDAOImpl implements ResourceDAO {
    private final MongoCollection<Document> usersCollection;
    private final SecurityDAO securityDAO;
    private final Logger logger = LoggerFactory.getEventLogger();

    public ResourceDAOImpl(MongoCollection<Document> usersCollection, SecurityDAO securityDAO) {
        this.usersCollection = usersCollection;
        this.securityDAO = securityDAO;
    }

    @Override
    public Void insertResource(Credentials user, Resource resource) {
        // Check if user has permission to insert resources
        if (!securityDAO.canInsertResource(user.getSystemRole())) {
            logger.error(String.format("User %d with role %s denied permission to insert resource", user.getId(), user.getSystemRole()));
            throw new SecurityException("User does not have permission to insert resources");
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
        usersCollection.updateOne(
            Filters.eq("id", user.getId()),
            new Document("$push", new Document("resources", resourceDoc))
        );

        logger.info(String.format("User %d inserted new resource %d", user.getId(), resource.getId()));
        return null;
    }

    @Override
    public Boolean removeResource(Credentials user, Resource resource) {
        // First, find the resource to check ownership
        Document doc = usersCollection.find(
            Filters.elemMatch("resources", new Document("id", resource.getId()))
        ).first();

        if (doc == null) {
            logger.warn(String.format("Failed to find resource %d for removal by user %d", resource.getId(), user.getId()));
            return false;  // Resource not found
        }

        // Find the resource in the array to get its creatorId
        List<Document> resources = doc.getList("resources", Document.class);
        Document resourceDoc = resources.stream()
            .filter(r -> r.getInteger("id") == resource.getId())
            .findFirst()
            .orElse(null);

        if (resourceDoc == null) {
            return false;  // Resource not found
        }

        // Check if user has permission to delete this resource
        if (!securityDAO.canDeleteResource(user.getSystemRole(), resourceDoc.getInteger("creatorId"), user.getId())) {
            logger.error(String.format("User %d with role %s denied permission to delete resource %d", 
                user.getId(), user.getSystemRole(), resource.getId()));
            throw new SecurityException("User does not have permission to delete this resource");
        }

        // Pull (delete) the resource entirely from the user's resources array
        com.mongodb.client.result.UpdateResult result = usersCollection.updateOne(
            Filters.elemMatch("resources", new Document("id", resource.getId())),
            new Document("$pull", new Document("resources", new Document("id", resource.getId())))
        );
        
        boolean removed = result.getModifiedCount() > 0;
        if (removed) {
            logger.info(String.format("User %d removed resource %d", user.getId(), resource.getId()));
        } else {
            logger.warn(String.format("User %d failed to remove resource %d", user.getId(), resource.getId()));
        }
        return removed;
    }

    private Resource convertDocumentToResource(Document doc) {
        Resource resource = new Resource();
        resource.setId(doc.getInteger("id"));
        resource.setTitle(doc.getString("title"));
        resource.setDescription(doc.getString("description"));
        resource.setUrl(doc.getString("url"));
        return resource;
    }

    @Override
    public List<Resource> searchResources(String query) {
        // Create empty list to store matching resources
        List<Resource> matchingResources = new ArrayList<>();
        
        // If no query provided, return all resources
        if (query == null || query.trim().isEmpty()) {
            usersCollection.find(Filters.exists("resources"))
                .forEach(doc -> {
                    List<Document> resources = doc.getList("resources", Document.class);
                    if (resources != null) {
                        for (Document resourceDoc : resources) {
                            matchingResources.add(convertDocumentToResource(resourceDoc));
                        }
                    }
                });
            return matchingResources;
        }

        // Search resources where title or description contains query (case insensitive)
        Document searchFilter = new Document("$or", Arrays.asList(
            new Document("resources.title", new Document("$regex", query).append("$options", "i")),
            new Document("resources.description", new Document("$regex", query).append("$options", "i"))
        ));

        usersCollection.find(searchFilter).forEach(doc -> {
            List<Document> resources = doc.getList("resources", Document.class);
            if (resources != null) {
                for (Document resourceDoc : resources) {
                    // Only add resources that match our criteria
                    String title = resourceDoc.getString("title");
                    String description = resourceDoc.getString("description");
                    if ((title != null && title.toLowerCase().contains(query.toLowerCase())) ||
                        (description != null && description.toLowerCase().contains(query.toLowerCase()))) {
                        matchingResources.add(convertDocumentToResource(resourceDoc));
                    }
                }
            }
        });
        
        return matchingResources;
    }
}