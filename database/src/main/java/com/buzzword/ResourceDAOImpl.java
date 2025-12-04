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
import java.util.HashMap;
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
            .append("dateCreated", resource.getCreationDate())
            .append("isUpdated", resource.getIsEdited());

        // Push the resource into the resources collection
        resources.insertOne(resourceDoc);

        logger.info(String.format("User %d inserted new resource %d", user.getId(), resource.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editResource(Credentials user, int id, Resource resource) {
        // Require an authenticated user to edit resources (simple policy).
        if (user == null || (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor")) {
            logger.error("Unauthenticated user denied permission to edit resource");
            throw new AuthorizationException("User must be authenticated to edit resources");
        }
        if (user.getSystemRole() == "Contributor") {
            // First, find the resource to check ownership
            Document doc = resources.find(
                Filters.eq("resourceId", id)).first();

            if (doc == null) {
                logger.warn(String.format("Failed to find resource %d for update by user %d", id, user.getId()));
                throw new RecordDoesNotExistException("Failed to find resource to update");
            }

            // Check if user has permission to edit this resource.
            // Allow editing only if the user is the original creator of the resource.
            Integer creatorId = doc.getInteger("creatorId");
            boolean isCreator = creatorId != null && creatorId.equals(user.getId());

            if (!isCreator) {
                logger.error(String.format("User %d with role %s denied permission to edit resource %d", 
                    user.getId(), user.getSystemRole(), id));
                throw new AuthorizationException("User does not have permission to edit this resource");
            }
        }

        Bson filter = Filters.eq("resourceId", id);
        Bson updateResource = Updates.combine(
            Updates.set("title", resource.getTitle()),
            Updates.set("description", resource.getDescription()),
            Updates.set("url", resource.getUrl()),
            Updates.set("isUpdated", true)
        );

        UpdateResult result = resources.updateOne(filter, updateResource);
        if(result.getMatchedCount() == 0) {
            logger.error(String.format("Resource %d not found for editing by user %d", id, user.getId()));
            throw new RecordDoesNotExistException("Resource not found for editing");
        } else {
            logger.info(String.format("User %d edited resource %d", user.getId(), id));
        }
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
            comments.deleteMany(Filters.eq("resourceId", id));
            upvotes.deleteMany(Filters.eq("resourceId", id));
            flags.deleteMany(Filters.eq("resourceId", id));
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

            if(user.getSystemRole() == "Admin" || resDoc.getInteger("creatorId") == user.getId()) {
                resource.setCurrentUserCanDelete(true);
            }

            resourceMap.put(resource.getId(), resource);
            System.out.println(resourceMap);
        });

        comments.find().forEach(commentDoc -> {

            Resource parent = resourceMap.get(commentDoc.getInteger("resourceId"));
            if (parent != null) {
                Comment comment = new Comment();
                comment.setId(commentDoc.getInteger("commentId"));
                comment.setCreatorId(commentDoc.getInteger("creatorId"));
                comment.setFirstName(commentDoc.getString("firstName"));
                comment.setLastName(commentDoc.getString("lastName"));
                comment.setCreationDate(commentDoc.getDate("dateCreated"));
                comment.setContents(commentDoc.getString("contents"));

                if(user.getSystemRole() == "Admin" || commentDoc.getInteger("creatorId") == user.getId()) {
                    comment.setCurrentUserCanDelete(true);
                }

                List<Comment> comments = parent.getComments();
                comments.add(comment);
            } else {
                logger.warn("Comment in database without a parent post.");
            }
        });

        flags.find().forEach(flagDoc -> {
            Resource parent = resourceMap.get(flagDoc.getInteger("resourceId"));
            if (parent != null) {
                ReviewFlag flag = new ReviewFlag();
                flag.setId(flagDoc.getInteger("flagId"));
                flag.setCreatorId(flagDoc.getInteger("creatorId"));
                flag.setFirstName(flagDoc.getString("firstName"));
                flag.setLastName(flagDoc.getString("lastName"));
                flag.setCreationDate(flagDoc.getDate("dateCreated"));
                flag.setContents(flagDoc.getString("contents"));

                if(user.getSystemRole() == "Admin" || flagDoc.getInteger("creatorId") == user.getId()) {
                    flag.setCurrentUserCanDelete(true);
                }

                List<ReviewFlag> flags = parent.getReviewFlags();
                flags.add(flag);
            } else {
                logger.warn("Flag in database without a parent post");
            }
        });

        upvotes.find().forEach(upvoteDoc -> {
            Resource parent = resourceMap.get(upvoteDoc.getInteger("resourceId"));
            if (parent != null) {
                Upvote upvote = new Upvote();
                upvote.setId(upvoteDoc.getInteger("upvoteId"));
                upvote.setCreatorId(upvoteDoc.getInteger("creatorId"));
                upvote.setFirstName(upvoteDoc.getString("firstName"));
                upvote.setLastName(upvoteDoc.getString("lastName"));
                upvote.setCreationDate(upvoteDoc.getDate("dateCreated"));

                parent.incrementUpvoteCount();
                if (upvoteDoc.getInteger("creatorId") == user.getId()) {
                    upvote.setCurrentUserCanDelete(true);
                    parent.setUpvotedByCurrentUser(true);
                    parent.setCurrentUserUpvoteId(upvoteDoc.getInteger("upvoteId"));
                }

                List<Upvote> upvotes = parent.getUpvotes();
                upvotes.add(upvote);
            } else {
                logger.warn("Upvote in database without a parent post");
            }
        });

        return new ArrayList<Resource>(resourceMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resource> listResourcesByKeywords(Credentials user, KeywordList keywords) {
        if (user.getSystemRole() != "Admin" && user.getSystemRole() != "Contributor" && user.getSystemRole() != "Commenter") {
            logger.error(String.format("User %d with role %s denied permission to retrieve resources: role is not valid.", 
                user.getId(), user.getSystemRole()));
            throw new AuthorizationException("User does not have a valid system role.");
        }


        Map<Integer, Resource> resourceMap = new LinkedHashMap<Integer, Resource>();

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

        TextSearchOptions searchOptions = new TextSearchOptions().caseSensitive(false);
        Document sortByWeight = new Document("weight", -1);
        Bson keywordFilter = Filters.text(keywords.toString(), searchOptions);

        resources.find(keywordFilter).sort(sortByWeight).forEach(resDoc -> {
            Resource resource = convertDocumentToResource(resDoc);
            resource.setComments(new ArrayList<Comment>());
            resource.setReviewFlags(new ArrayList<ReviewFlag>());
            resource.setUpvotes(new ArrayList<Upvote>());

            if(user.getSystemRole() == "Admin" || resDoc.getInteger("creatorId") == user.getId()) {
                resource.setCurrentUserCanDelete(true);
            }

            resourceMap.put(resource.getId(), resource);
            System.out.println(resourceMap);
        });

        comments.find().forEach(commentDoc -> {

            Resource parent = resourceMap.get(commentDoc.getInteger("resourceId"));
            if (parent != null) {
                Comment comment = new Comment();
                comment.setId(commentDoc.getInteger("commentId"));
                comment.setCreatorId(commentDoc.getInteger("creatorId"));
                comment.setFirstName(commentDoc.getString("firstName"));
                comment.setLastName(commentDoc.getString("lastName"));
                comment.setCreationDate(commentDoc.getDate("dateCreated"));
                comment.setContents(commentDoc.getString("contents"));

                if(user.getSystemRole() == "Admin" || commentDoc.getInteger("creatorId") == user.getId()) {
                    comment.setCurrentUserCanDelete(true);
                }

                List<Comment> comments = parent.getComments();
                comments.add(comment);
            } else {
                logger.warn("Comment in database without a parent post.");
            }
        });

        flags.find().forEach(flagDoc -> {
            Resource parent = resourceMap.get(flagDoc.getInteger("resourceId"));
            if (parent != null) {
                ReviewFlag flag = new ReviewFlag();
                flag.setId(flagDoc.getInteger("flagId"));
                flag.setCreatorId(flagDoc.getInteger("creatorId"));
                flag.setFirstName(flagDoc.getString("firstName"));
                flag.setLastName(flagDoc.getString("lastName"));
                flag.setCreationDate(flagDoc.getDate("dateCreated"));
                flag.setContents(flagDoc.getString("contents"));

                if(user.getSystemRole() == "Admin" || flagDoc.getInteger("creatorId") == user.getId()) {
                    flag.setCurrentUserCanDelete(true);
                }

                List<ReviewFlag> flags = parent.getReviewFlags();
                flags.add(flag);
            } else {
                logger.warn("Flag in database without a parent post");
            }
        });

        upvotes.find().forEach(upvoteDoc -> {
            Resource parent = resourceMap.get(upvoteDoc.getInteger("resourceId"));
            if (parent != null) {
                Upvote upvote = new Upvote();
                upvote.setId(upvoteDoc.getInteger("upvoteId"));
                upvote.setCreatorId(upvoteDoc.getInteger("creatorId"));
                upvote.setFirstName(upvoteDoc.getString("firstName"));
                upvote.setLastName(upvoteDoc.getString("lastName"));
                upvote.setCreationDate(upvoteDoc.getDate("dateCreated"));

                parent.incrementUpvoteCount();
                if (upvoteDoc.getInteger("creatorId") == user.getId()) {
                    upvote.setCurrentUserCanDelete(true);
                    parent.setUpvotedByCurrentUser(true);
                    parent.setCurrentUserUpvoteId(upvoteDoc.getInteger("upvoteId"));
                }

                List<Upvote> upvotes = parent.getUpvotes();
                upvotes.add(upvote);
            } else {
                logger.warn("Upvote in database without a parent post");
            }
        });

        return new ArrayList<Resource>(resourceMap.values());
    }
}