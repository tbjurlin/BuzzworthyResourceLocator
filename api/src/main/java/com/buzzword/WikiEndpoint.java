package com.buzzword;

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

    String authServerUrl = "http://";
    Authenticator auth = null;
    private final Logger logger = LoggerFactory.getEventLogger();

    /**
     * Constructor to initialize a new AuthenticatorImpl using the 
     * provided authServerUrl String.
     */
    @PostConstruct
    public void initialize() {
        auth = new AuthenticatorImpl(authServerUrl);
    }

    /**
     * Setter for injecting a custom Authenticator.
     * 
     * @param auth An Authenticator object.
     */
    public void setAuthenticator(Authenticator auth) {
        this.auth = auth;
        if(this.auth == null) {
            auth = new AuthenticatorImpl(authServerUrl);
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
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /*
        UserDAO dao = new UserDAOImpl();
        List<resource> resources = dao.SearchByAll("");
        Objectmapper mapper = new Objectmapper(); */
        String returnObj = ""; //mapper.writeValueAsString(resources);
        return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(returnObj);
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
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertResource(userCredentials, resource);
         */
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
    public ResponseEntity<String> addComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @Valid @RequestBody Comment comment) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertComment(userCredentials, resourceId, comment);
         */
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
    public ResponseEntity<String> addUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertUpvote(userCredentials, resourceId, upvote);
         */
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
    public ResponseEntity<String> addReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @Valid @RequestBody ReviewFlag reviewFlag) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertReviewFlag(userCredentials, resourceId, reviewFlag);
         */
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
    public ResponseEntity<String> removeResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteResource(userCredentials, resourceId);
         */
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
    public ResponseEntity<String> removeComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @PathVariable Long commentId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteComment(userCredentials, resourceId, commentId);
         */
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
    public ResponseEntity<String> removeUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @PathVariable Long upvoteId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteUpvote(userCredentials, resourceId, upvoteId);
         */
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
    public ResponseEntity<String> removeReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @PathVariable Long flagId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteReviewFlag(userCredentials, resourceId, flagId);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove review flag " + flagId + " from resource " + resourceId + "\"}");
    }
}
