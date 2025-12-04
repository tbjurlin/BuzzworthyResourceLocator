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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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

    private String authServerUrl;
    private final Logger logger = LoggerFactory.getEventLogger();
    private DatabaseConnectionPool databaseConnectionPool;

    /**
     * Constructor to initialize a new AuthenticatorImpl using the 
     * provided authServerUrl String.
     */
    @PostConstruct
    public void initialize() {
        AuthServerConfiguration config = new AuthServerConfigurationImpl(ConfigurationManagerImpl.getInstance());
        authServerUrl = config.getAuthServerConnectionString();
        try{
            databaseConnectionPool = DatabaseConnectionPool.getInstance();
        } catch(IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot get instance of database connection pool.");
        }
    }

    /**
     * Cleans up resources before the application is shut down.
     */
    @PreDestroy
    public void cleanup() {
        if (databaseConnectionPool != null) {
            databaseConnectionPool.close();
        }
    }

    /**
     * GET Request.
     * Retrieve all resource records from the database as a JSON object
     * containing a list of Record objects.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @return ResponseEntity containing a JSON array of resources and HTTP status 200.
     */
    @GetMapping("resource")
    public ResponseEntity<String> retrieveAllResources(@Valid @RequestHeader("Bearer") String tokenStr) {
        logger.info("HTTP GET request (retrieveAllResources) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        List<Resource> resources = resourceDAO.listAllResources(userCredentials);
        if(resources == null) {
            logger.error("Cannot return a null list of resources.");
            throw new NullPointerException("Cannot return a null list of resources.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String returnObj = objectMapper.writeValueAsString(resources);
        
            logger.info("Returning HTTP response code 200.");
            return ResponseEntity.ok()
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(returnObj);
        } catch(JsonProcessingException e) {
            throw new NullPointerException("Unable to parse JSON from list of resources.");
        }
    }

    /**
     * GET Request.
     * Retrieve all resource records from the database as a JSON object
     * containing a list of Record objects.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param keywords A JSON-formatted list of keywords from the HTTP request body.
     * @return ResponseEntity containing a JSON array of filtered resources and HTTP status 200.
     */
    @PostMapping("resource-filtered")
    public ResponseEntity<String> retrieveResourcesByKeywords(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @RequestBody KeywordList keywords) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        List<Resource> resources = resourceDAO.listResourcesByKeywords(userCredentials, keywords);
        if(resources == null) {
            logger.error("Cannot return a null list of resources.");
            throw new NullPointerException("Cannot return a null list of resources.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String returnObj = objectMapper.writeValueAsString(resources);
        
            logger.info("Returning HTTP response code 200.");
            return ResponseEntity.ok()
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(returnObj);
        } catch(JsonProcessingException e) {
            throw new NullPointerException("Unable to parse JSON from list of resources.");
        }
    }
    
    /**
     * POST Request. 
     * Insert a new resource record into the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resource A JSON-formatted resource record object from the HTTP request body.
     * @return ResponseEntity containing a success message and HTTP status 201.
     */
    @PostMapping("resource")
    public ResponseEntity<String> addResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @RequestBody Resource resource) {
        logger.info("HTTP POST request (addResource) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        resourceDAO.insertResource(userCredentials, resource);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully added a new resource.\"}");
    }

    /**
     * POST Request.
     * Insert a new comment on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to add the comment to.
     * @param comment A JSON-formatted comment object from the HTTP request body.
     * @return ResponseEntity containing a success message and HTTP status 201.
     */
    @PostMapping("resource/{resourceId}/comment")
    public ResponseEntity<String> addComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @RequestBody Comment comment) {
        logger.info("HTTP POST request (addComment) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        CommentDAO commentDAO = new CommentDAOImpl(databaseConnectionPool.getDatabaseConnection());
        commentDAO.addComment(userCredentials, comment, resourceId);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully added comment to resource " + resourceId + ".\"}");
    }

    /**
     * POST Request.
     * Insert a new upvote on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to add the upvote to.
     * @return ResponseEntity containing a success message and HTTP status 201.
     */
    @PostMapping("resource/{resourceId}/upvote")
    public ResponseEntity<String> addUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId) {
        logger.info("HTTP POST request (addUpvote) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        UpvoteDAO upvoteDAO = new UpvoteDAOImpl(databaseConnectionPool.getDatabaseConnection());
        upvoteDAO.addUpvote(userCredentials, new Upvote(), resourceId);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully added new upvote to resource " + resourceId + ".\"}");
    }

    /**
     * POST Request.
     * Insert a new review flag on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to add the review flag to.
     * @param reviewFlag A JSON-formatted review flag object from the HTTP request body.
     * @return ResponseEntity containing a success message and HTTP status 201.
     */
    @PostMapping("resource/{resourceId}/reviewFlag")
    public ResponseEntity<String> addReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @RequestBody ReviewFlag reviewFlag) {
        logger.info("HTTP POST request (addReviewFlag) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        FlagDAO flagDAO = new FlagDAOImpl(databaseConnectionPool.getDatabaseConnection());
        flagDAO.addReviewFlag(userCredentials, reviewFlag, resourceId);
        logger.info("Returning HTTP response code 201.");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully added new review flag to resource " + resourceId + ".\"}");
    }

    /**
     * PUT Request.
     * Edit an existing resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to edit.
     * @param resource A JSON-formatted resource record object from the HTTP request body.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @PutMapping("resource/{resourceId}")
    public ResponseEntity<String> editResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @RequestBody Resource resource) {
        logger.info("HTTP PUT request (editResource) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        resourceDAO.editResource(userCredentials, resourceId, resource);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully edited resource " + resourceId + ".\"}");
    }

    /**
     * PUT Request.
     * Edit an existing comment on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the comment to edit.
     * @param commentId The index of the comment to edit.
     * @param comment A JSON-formatted comment object from the HTTP request body.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @PutMapping("resource/{resourceId}/comment/{commentId}")
    public ResponseEntity<String> editComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @PathVariable int commentId, @Valid @RequestBody Comment comment) {
        logger.info("HTTP PUT request (editComment) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        CommentDAO commentDAO = new CommentDAOImpl(databaseConnectionPool.getDatabaseConnection());
        commentDAO.editComment(userCredentials, commentId, comment, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully edited comment " + commentId + " on resource " + resourceId + ".\"}");
    }

    /**
     * PUT Request.
     * Edit an existing review flag on a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the review flag to edit.
     * @param flagId The index of the review flag to edit.
     * @param reviewFlag A JSON-formatted review flag object from the HTTP request body.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @PutMapping("resource/{resourceId}/reviewFlag/{flagId}")
    public ResponseEntity<String> updateReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @Valid @PathVariable int flagId, @Valid @RequestBody ReviewFlag reviewFlag) {
        logger.info("HTTP PUT request (updateReviewFlag) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        FlagDAO flagDAO = new FlagDAOImpl(databaseConnectionPool.getDatabaseConnection());
        flagDAO.editReviewFlag(userCredentials, flagId, reviewFlag, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully edited review flag " + flagId + " on resource " + resourceId + ".\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific resource record from the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record to be deleted.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @DeleteMapping("resource/{resourceId}")
    public ResponseEntity<String> removeResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId) {
        logger.info("HTTP DELETE request (removeResource) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        ResourceDAO resourceDAO = new ResourceDAOImpl(databaseConnectionPool.getDatabaseConnection());
        resourceDAO.removeResource(userCredentials, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully removed resource " + resourceId + ".\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific comment from a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the comment to be deleted.
     * @param commentId The index of the comment to be deleted.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @DeleteMapping("resource/{resourceId}/comment/{commentId}")
    public ResponseEntity<String> removeComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @PathVariable int commentId) {
        logger.info("HTTP DELETE request (removeComment) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        CommentDAO commentDAO = new CommentDAOImpl(databaseConnectionPool.getDatabaseConnection());
        commentDAO.removeComment(userCredentials, commentId, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully removed comment " + commentId + " from resource " + resourceId + ".\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific upvote from a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the upvote to be deleted.
     * @param upvoteId The index of the upvote to be deleted.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @DeleteMapping("resource/{resourceId}/upvote/{upvoteId}")
    public ResponseEntity<String> removeUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @PathVariable int upvoteId) {
        logger.info("HTTP DELETE request (removeUpvote) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        UpvoteDAO upvoteDAO = new UpvoteDAOImpl(databaseConnectionPool.getDatabaseConnection());
        upvoteDAO.removeUpvote(userCredentials, upvoteId, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully removed upvote " + upvoteId + " from resource " + resourceId + ".\"}");
    }

    /**
     * DELETE Request. 
     * Remove a specific review flag from a specific resource record in the database.
     * 
     * @param tokenStr A string representation of the user's Java Web Token (JWT).
     * @param resourceId The index of the resource record containing the review flag to be deleted.
     * @param flagId The index of the review flag to be deleted.
     * @return ResponseEntity containing a success message and HTTP status 200.
     */
    @DeleteMapping("resource/{resourceId}/reviewFlag/{flagId}")
    public ResponseEntity<String> removeReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable int resourceId, @PathVariable int flagId) {
        logger.info("HTTP DELETE request (removeReviewFlag) received.");
        Token token = new Token();
        token.setToken(tokenStr);
        Authenticator auth = new AuthenticatorImpl(authServerUrl);
        Credentials userCredentials = auth.authenticate(token);
        FlagDAO flagDAO = new FlagDAOImpl(databaseConnectionPool.getDatabaseConnection());
        flagDAO.removeReviewFlag(userCredentials, flagId, resourceId);
        logger.info("Returning HTTP response code 200.");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Successfully removed review flag " + flagId + " from resource " + resourceId + ".\"}");
    }
}
