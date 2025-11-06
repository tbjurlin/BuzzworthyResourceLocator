package com.buzzword;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

/**
 * REST API Controller Endpoint for the Buzzworthy Resource Locator (BRL) Wiki.
 * 
 * @author Ben Edens
 * @version 1.0
 */
@RestController
@RequestMapping("wiki")
public class WikiEndpoint {

    private String authServerUrl = "";
    private final Logger logger = LoggerFactory.getEventLogger();
    private DatabaseConnectionPool databaseConnectionPool;

    /**
     * Constructor to initialize a new AuthenticatorImpl using the 
     * provided authServerUrl String.
     */
    @PostConstruct
    public void initialize() {
        try{
            databaseConnectionPool = DatabaseConnectionPool.getInstance();
        } catch(IOException | IllegalArgumentException e) {

        }
    }

    /**
     * GET Request.
     * Retrieve all resource records from the database as a JSON object
     * containing a list of Record objects.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @return A JSON-formatted HTTP response with a 200 response code and message, including a list of resources.
     */
    @GetMapping("resource")
    public ResponseEntity<String> retrieveAllResources(@Valid @RequestHeader("Bearer") String tokenStr) {
        logger.info("HTTP GET request (retrieveAllResources) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        List<Resource> resources = resourceDAO.listAllResources(userCredentials);
        if(resources == null) {
            throw new IllegalArgumentException();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String returnObj = objectMapper.writeValueAsString(resources);
        
            logger.info("Returning HTTP response code 200.");
            return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(returnObj);
        } catch(JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * POST Request. 
     * Insert a new resource record into the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resource A JSON-formatted resource record object from the HTTP request body.
     * @return
     */
    @PostMapping("resource")
    public ResponseEntity<String> addResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @RequestBody Resource resource) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        resourceDAO.insertResource(userCredentials, resource);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add resource\"}");
    }

    /**
     * POST Request.
     * Insert a new comment on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to add the comment to.
     * @param comment A JSON-formatted comment object from the HTTP request body.
     * @return
     */
    @PostMapping("resource/{resourceId}/comment")
    public ResponseEntity<String> addComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @RequestBody Comment comment) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        CommentDAO commentDAO = new CommentDAOImpl(databaseConnectionPool.getDatabaseConnection());
        commentDAO.addComment(userCredentials, comment, resourceId);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add comment to resource " + resourceId + "\"}");
    }

    /**
     * POST Request.
     * Insert a new upvote on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to add the upvote to.
     * @return
     */
    @PostMapping("resource/{resourceId}/upvote")
    public ResponseEntity<String> addUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        UpvoteDAO upvoteDAO = new UpvoteDAOImpl(databaseConnectionPool.getDatabaseConnection());
        upvoteDAO.addUpvote(userCredentials, new Upvote(), resourceId);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add upvote to resource " + resourceId + "\"}");
    }

    /**
     * POST Request.
     * Insert a new review flag on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to add the review flag to.
     * @param reviewFlag A JSON-formatted review flag object from the HTTP request body.
     * @return
     */
    @PostMapping("resource/{resourceId}/reviewFlag")
    public ResponseEntity<String> addReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @RequestBody ReviewFlag reviewFlag) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        FlagDAO flagDAO = new FlagDAOImpl(databaseConnectionPool.getDatabaseConnection());
        flagDAO.addReviewFlag(userCredentials, reviewFlag, resourceId);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add review flag to resource " + resourceId + "\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific resource record from the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to be deleted.
     * @return
     */
    @DeleteMapping("resource/{resourceId}")
    public ResponseEntity<String> removeResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        resourceDAO.removeResource(userCredentials, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove resource " + resourceId + "\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific comment from a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the comment to be deleted.
     * @param commentId The index of the comment to be deleted.
     * @return
     */
    @DeleteMapping("resource/{resourceId}/comment/{commentId}")
    public ResponseEntity<String> removeComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @PathVariable int commentId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        CommentDAO commentDAO = new CommentDAOImpl(databaseConnectionPool.getDatabaseConnection());
        commentDAO.removeComment(userCredentials, commentId, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove comment " + commentId + " from resource " + resourceId + "\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific upvote from a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the upvote to be deleted.
     * @param upvoteId The index of the upvote to be deleted.
     * @return
     */
    @DeleteMapping("resource/{resourceId}/upvote/{upvoteId}")
    public ResponseEntity<String> removeUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @PathVariable int upvoteId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        UpvoteDAO upvoteDAO = new UpvoteDAOImpl(databaseConnectionPool.getDatabaseConnection());
        upvoteDAO.removeUpvote(userCredentials, upvoteId, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove upvote " + upvoteId + " from resource " + resourceId + "\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific review flag from a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the review flag to be deleted.
     * @param flagId The index of the review flag to be deleted.
     * @return
     */
    @DeleteMapping("resource/{resourceId}/reviewFlag/{flagId}")
    public ResponseEntity<String> removeReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @PathVariable int flagId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        FlagDAO flagDAO = new FlagDAOImpl(databaseConnectionPool.getDatabaseConnection());
        flagDAO.removeReviewFlag(userCredentials, flagId, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove review flag " + flagId + " from resource " + resourceId + "\"}");
    }
}
