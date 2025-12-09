package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class ResourceDAOImpl implements ResourceDAO {
    private final MongoCollection<Document> resources;
    private final MongoCollection<Document> comments;
    private final MongoCollection<Document> flags;
    private final MongoCollection<Document> upvotes;
    private final Logger logger = LoggerFactory.getEventLogger();
    private CounterDAO counterDAO;

    /**
     * Constructs a ResourceDAOImpl with the specified MongoDB database.
     * <p>
     * Initializes the collections for resources, comments, flags, and upvotes,
     * and creates a counter DAO for managing record IDs.
     * 
     * @param db the MongoDB database to use for data access
     * @throws IllegalArgumentException if db is null
     */
    public ResourceDAOImpl(MongoDatabase db) {
        // Check for null database
        if (db == null) {
            logger.error("Attempted to construct ResourceDAOImpl with null database.");
            throw new IllegalArgumentException("Database cannot be null.");
        }

        // Get the collections
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
        // Check for null CounterDAO
        if (counterDAO == null) {
            logger.error("Attempted to set null CounterDAO.");
            throw new IllegalArgumentException("CounterDAO cannot be null.");
        }
        this.counterDAO = counterDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insertResource(Credentials user, Resource resource) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to insert resource with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor")) {
            logger.error(String.format("User %d with role %s denied permission to insert resources.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to insert resources.");
        }
        // Check for null resource
        if (resource == null) {
            logger.error("Attempted to insert null resource.");
            throw new IllegalArgumentException("Resource cannot be null.");
        }

        // Create the resource document
        Document resourceDoc = new Document()
            .append("resourceId", counterDAO.getNextResourceId())
            .append("title", resource.getTitle())
            .append("description", resource.getDescription())
            .append("url", resource.getUrl())
            .append("creatorId", user.getId())  // Track who created the resource
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("dateCreated", resource.getCreationDate())
            .append("isEdited", resource.getIsEdited());

        // Push the resource into the resources collection
        resources.insertOne(resourceDoc);

        logger.info(String.format("User %d inserted new resource %d", user.getId(), resource.getId()));
        return resourceDoc.getInteger("resourceId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editResource(Credentials user, int id, Resource resource) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to edit resource with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor")) {
            logger.error(String.format("User %d with role %s denied permission to edit resources.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to edit resources.");
        }
        // Check for null resource
        if (resource == null) {
            logger.error("Attempted to edit with null resource.");
            throw new IllegalArgumentException("Resource cannot be null.");
        }

        // First, find the resource to check ownership
        Document doc = resources.find(Filters.eq("resourceId", id)).first();
        if (doc == null) {
            logger.error(String.format("Failed to find resource %d for update by user %d.", id, user.getId()));
            throw new RecordDoesNotExistException("Failed to find resource to update.");
        }

        // Check ownership for Contributors; Admins can edit any resource
        if (user.getSystemRole().equals("Contributor")) {
            // Allow editing only if the user is the original creator of the resource.
            Integer creatorId = doc.getInteger("creatorId");
            boolean isCreator = creatorId != null && creatorId.equals(user.getId());

            if (!isCreator) {
                logger.error(String.format("User %d denied permission to edit resource %d because they are not the creator.", 
                    user.getId(), id));
                throw new AuthorizationException("User does not have permission to edit this resource because they are not the creator.");
            }
        }

        // Update the resource document
        Bson filter = Filters.eq("resourceId", id);
        Bson updateResource = Updates.combine(
            Updates.set("title", resource.getTitle()),
            Updates.set("description", resource.getDescription()),
            Updates.set("url", resource.getUrl()),
            Updates.set("isEdited", true)
        );
        UpdateResult result = resources.updateOne(filter, updateResource);

        // Check if the update was successful
        if(result.getMatchedCount() == 0) {
            logger.error(String.format("Resource %d not found for editing by user %d.", id, user.getId()));
            throw new RecordDoesNotExistException("Resource not found for editing.");
        } else {
            logger.info(String.format("User %d edited resource %d.", user.getId(), id));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeResource(Credentials user, int id) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to delete resource with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor")) {
            logger.error(String.format("User %d with role %s denied permission to delete resources.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User is not authorized to delete resources.");
        }

        // First, find the resource to check ownership
        Document doc = resources.find(Filters.eq("resourceId", id)).first();
        if (doc == null) {
            logger.error(String.format("Failed to find resource %d for removal by user %d.", id, user.getId()));
            throw new RecordDoesNotExistException("Failed to find resource for removal.");
        }

        // Check ownership for Contributors; Admins can delete any resource
        if (user.getSystemRole().equals("Contributor")) {
            // Allow deletion only if the user is the original creator of the resource.
            Integer creatorId = doc.getInteger("creatorId");
            boolean isCreator = creatorId != null && creatorId.equals(user.getId());

            if (!isCreator) {
                logger.error(String.format("User %d denied permission to delete resource %d because they are not the creator.", 
                    user.getId(), id));
                throw new AuthorizationException("User does not have permission to delete this resource because they are not the creator.");
            }
        }

        // Delete the resource document
        DeleteResult result = resources.deleteOne(Filters.eq("resourceId", id));

        // Check if the deletion was successful; also remove associated comments, flags, and upvotes
        if (result.getDeletedCount() > 0) {
            counterDAO.removeResourceCounters(id);
            comments.deleteMany(Filters.eq("resourceId", id));
            upvotes.deleteMany(Filters.eq("resourceId", id));
            flags.deleteMany(Filters.eq("resourceId", id));
            logger.info(String.format("User %d removed resource %d.", user.getId(), id));
        } else {
            logger.error(String.format("User %d failed to remove resource %d.", user.getId(), id));
            throw new RecordDoesNotExistException("Failed to find resource for removal.");
        }
    }

    /**
     * Converts a MongoDB Document to a Resource object.
     * <p>
     * Extracts resource data from the document and populates a Resource instance.
     * 
     * @param doc the MongoDB document to convert
     * @return a Resource object populated with data from the document
     * @throws IllegalArgumentException if the document is null
     */
    private Resource convertDocumentToResource(Document doc) {
        // Check for null document
        if(doc == null) {
            logger.error("Attempted to convert null Document to Resource.");
            throw new IllegalArgumentException("Document cannot be null.");
        }

        // Populate and return the Resource object
        Resource resource = new Resource();
        resource.setId(doc.getInteger("resourceId"));
        resource.setTitle(doc.getString("title"));
        resource.setDescription(doc.getString("description"));
        resource.setUrl(doc.getString("url"));
        resource.setCreatorId(doc.getInteger("creatorId"));
        resource.setCreationDate(doc.getDate("dateCreated"));
        resource.setFirstName(doc.getString("firstName"));
        resource.setLastName(doc.getString("lastName"));
        resource.setIsEdited(doc.getBoolean("isEdited", false));
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resource> listAllResources(Credentials user) {
        // List resources using listResources helper with empty filters (lists all in default order)
        return listResources(user, new Document(), new Document());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getResourceById(Credentials user, int id) {
        Bson findById = Filters.eq("resourceId", id);
        return listResources(user, findById, new Document())
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    logger.error(String.format("Resource %d not found for retrieval by user %d.", id, user.getId()));
                    return new RecordDoesNotExistException("Resource not found.");
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resource> listResourcesByKeywords(Credentials user, KeywordList keywords) {
        // Check for null or empty keywords
        if(keywords == null || keywords.getKeywords().isEmpty()) {
            return listAllResources(user);
        }
        // Create new text index if not already present
        createTextIndex();
        // Create filters (text search with case insensitivity, sorted by weight)
        TextSearchOptions searchOptions = new TextSearchOptions().caseSensitive(false);
        Bson findByKeyword = Filters.text(keywords.toString(), searchOptions);
        Document sortByWeight = new Document("weight", -1);
        // List resources using listResources helper with the constructed filters
        return listResources(user, findByKeyword, sortByWeight);
    }

    /**
     * Helper method to list resources based on a filter and sort criteria.
     * <p>
     * This method retrieves resources from the database that match the specified filter,
     * sorts them according to the provided sort criteria, and populates associated comments,
     * flags, and upvotes. It also sets front-end permission flags based on the user's
     * system role and ownership of the resources.
     * @param user the credentials of the user requesting the resources
     * @param findFilter the filter criteria to apply when retrieving resources
     * @param sortFilter the sort criteria to apply when retrieving resources
     * @return
     */
    private List<Resource> listResources(Credentials user, Bson findFilter, Document sortFilter) {
        // Check for valid authentication and authorization
        if (user == null || user.getSystemRole() == null) {
            logger.error("Attempted to list resources with null user credentials.");
            throw new IllegalArgumentException("User credentials cannot be null.");
        }
        if (!user.getSystemRole().equals("Admin") && !user.getSystemRole().equals("Contributor") && !user.getSystemRole().equals("Commenter")) {
            logger.error(String.format("User %d with role %s denied permission to retrieve resources.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User does not have a valid system role.");
        }
        // Check for null filters
        if (findFilter == null) {
            logger.error("Attempted to list resources with null findFilter.");
            throw new IllegalArgumentException("Find filter cannot be null.");
        }
        if (sortFilter == null) {
            logger.error("Attempted to list resources with null sortFilter.");
            throw new IllegalArgumentException("Sort filter cannot be null.");
        }

        // Linked hash map to maintain insertion order while allowing quick access by resource ID
        Map<Integer, Resource> resourceMap = new LinkedHashMap<Integer, Resource>();

        // Load resources based on the provided filters
        resources.find(findFilter).sort(sortFilter).forEach(resDoc -> {
            Resource resource = convertDocumentToResource(resDoc);
            resource.setComments(new ArrayList<Comment>());
            resource.setReviewFlags(new ArrayList<ReviewFlag>());
            resource.setUpvotes(new ArrayList<Upvote>());

            // Set front end flags for current user
            if(user.getSystemRole().equals("Admin") || resDoc.getInteger("creatorId") == user.getId()) {
                resource.setCurrentUserCanDelete(true);
                resource.setCurrentUserCanEdit(true);
            }

            resourceMap.put(resource.getId(), resource);
        });

        // If no resources found, return empty list without searching comments, flags, or upvotes
        if (resourceMap.isEmpty()) {
            return new ArrayList<Resource>();
        }

        // Load comments, flags, and upvotes for the retrieved resources
        Bson resourceIdFilter = Filters.in("resourceId", resourceMap.keySet());

        // Load comments
        comments.find(resourceIdFilter).forEach(commentDoc -> {
            Resource parent = resourceMap.get(commentDoc.getInteger("resourceId"));
            if (parent != null) {
                Comment comment = new Comment();
                comment.setId(commentDoc.getInteger("commentId"));
                comment.setCreatorId(commentDoc.getInteger("creatorId"));
                comment.setFirstName(commentDoc.getString("firstName"));
                comment.setLastName(commentDoc.getString("lastName"));
                comment.setIsEdited(commentDoc.getBoolean("isEdited", false));
                comment.setCreationDate(commentDoc.getDate("dateCreated"));
                comment.setContents(commentDoc.getString("contents"));

                // Set front end flags for current user
                if(user.getSystemRole().equals("Admin") || commentDoc.getInteger("creatorId") == user.getId()) {
                    comment.setCurrentUserCanDelete(true);
                    comment.setCurrentUserCanEdit(commentDoc.getInteger("creatorId") == user.getId());
                }

                List<Comment> comments = parent.getComments();
                comments.add(comment);
            } else {
                logger.warn("Comment in database without a parent post.");
            }
        });

        // Load flags
        flags.find(resourceIdFilter).forEach(flagDoc -> {
            Resource parent = resourceMap.get(flagDoc.getInteger("resourceId"));
            if (parent != null) {
                ReviewFlag flag = new ReviewFlag();
                flag.setId(flagDoc.getInteger("flagId"));
                flag.setCreatorId(flagDoc.getInteger("creatorId"));
                flag.setFirstName(flagDoc.getString("firstName"));
                flag.setLastName(flagDoc.getString("lastName"));
                flag.setIsEdited(flagDoc.getBoolean("isEdited", false));
                flag.setCreationDate(flagDoc.getDate("dateCreated"));
                flag.setContents(flagDoc.getString("contents"));

                // Set front end flags for current user
                if(user.getSystemRole().equals("Admin") || flagDoc.getInteger("creatorId") == user.getId()) {
                    flag.setCurrentUserCanDelete(true);
                    flag.setCurrentUserCanEdit(flagDoc.getInteger("creatorId") == user.getId());
                }

                List<ReviewFlag> flags = parent.getReviewFlags();
                flags.add(flag);
            } else {
                logger.warn("Flag in database without a parent post.");
            }
        });

        // Load upvotes
        upvotes.find(resourceIdFilter).forEach(upvoteDoc -> {
            Resource parent = resourceMap.get(upvoteDoc.getInteger("resourceId"));
            if (parent != null) {
                Upvote upvote = new Upvote();
                upvote.setId(upvoteDoc.getInteger("upvoteId"));
                upvote.setCreatorId(upvoteDoc.getInteger("creatorId"));
                upvote.setFirstName(upvoteDoc.getString("firstName"));
                upvote.setLastName(upvoteDoc.getString("lastName"));
                upvote.setCreationDate(upvoteDoc.getDate("dateCreated"));

                // Set front end flags for current user and upvote count
                parent.incrementUpvoteCount();
                if (upvoteDoc.getInteger("creatorId") == user.getId()) {
                    upvote.setCurrentUserCanDelete(true);
                    parent.setUpvotedByCurrentUser(true);
                    parent.setCurrentUserUpvoteId(upvoteDoc.getInteger("upvoteId"));
                }

                List<Upvote> upvotes = parent.getUpvotes();
                upvotes.add(upvote);
            } else {
                logger.warn("Upvote in database without a parent post.");
            }
        });

        return new ArrayList<Resource>(resourceMap.values());
    }

    /**
     * Creates a text index on the resources collection for efficient keyword searching.
     * <p>
     * The index is created on the title, description, and URL fields with different weights.
     * If a text index already exists, no action is taken.
     */
    private void createTextIndex() {
        // Check for null resources collection
        if(resources == null) {
            logger.error("Cannot create text index: resources collection is null.");
            throw new IllegalStateException("Resources collection is not initialized.");
        }

        // Check for pre-existing text index
        boolean textIndexExists = false;
        for(Document index: resources.listIndexes()) {
            if (index.containsKey("weights") || (index.containsKey("key") && index.get("key", Document.class).containsKey("$**"))) {
                logger.info("Text index already exists on resources collection.");
                textIndexExists = true;
                break;
            }
        }

        // Create text index if it does not already exist
        if(!textIndexExists)
        {
            logger.info("No text index found on resources collection. Creating new text index.");
            Document weights = new Document()
                .append("title", 10)
                .append("description", 5)
                .append("url", 2);
            IndexOptions indexOptions = new IndexOptions().weights(weights);
            resources.createIndex(Indexes.compoundIndex(Indexes.text("title"), Indexes.text("description"), Indexes.text("url")), indexOptions);
        }
    }
}