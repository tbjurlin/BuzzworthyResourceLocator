package com.buzzword;

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
import jakarta.validation.Valid;

@RestController
@RequestMapping("wiki")
public class WikiEndpoint {

    String authServerUrl = "url here";
    private final Logger logger = LoggerFactory.getEventLogger();

    @GetMapping("resource")
    public ResponseEntity<String> retrieveAllResources(@Valid @RequestHeader("Bearer") String tokenStr) {
        try {
            Token token = new Token();
            token.setToken(tokenStr);
            Authentication auth = new AuthenticationImpl(authServerUrl);
            Credentials userCredentials = auth.Authenticate(token);
            if(userCredentials == null) {
                throw new Exception("No");
            }
            /* Pseudo code:
            * DAO dbAccess = new DAOImpl(Config.Instance());
            * String obj = dbAccess.getResources(userCredentials);
            */
            return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("{\"msg\": \"Retrieve all resources\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
    
    @PostMapping("resource")
    public ResponseEntity<String> addResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @RequestBody Resource resource) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertResource(userCredentials, resource);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add resource\"}");
    }

    @PostMapping("resource/{resourceId}/comment")
    public ResponseEntity<String> addComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @Valid @RequestBody Comment comment) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertComment(userCredentials, resourceId, comment);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add comment to resource " + resourceId + "\"}");
    }

    @PostMapping("resource/{resourceId}/upvote")
    public ResponseEntity<String> addUpVote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @Valid @RequestBody UpVote upvote) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertUpVote(userCredentials, resourceId, upvote);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add upvote to resource " + resourceId + "\"}");
    }

    @PostMapping("resource/{resourceId}/reviewFlag")
    public ResponseEntity<String> addReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @Valid @RequestBody ReviewFlag reviewFlag) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertReviewFlag(userCredentials, resourceId, reviewFlag);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add review flag to resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}")
    public ResponseEntity<String> removeResource(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteResource(userCredentials, resourceId);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}/comment/{commentId}")
    public ResponseEntity<String> removeComment(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @PathVariable Long commentId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteComment(userCredentials, resourceId, commentId);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove comment " + commentId + " from resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}/upvote/{upvoteId}")
    public ResponseEntity<String> removeUpvote(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @PathVariable Long upvoteId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
        Credentials userCredentials = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.deleteUpvote(userCredentials, resourceId, upvoteId);
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove upvote " + upvoteId + " from resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}/reviewFlag/{flagId}")
    public ResponseEntity<String> removeReviewFlag(@Valid @RequestHeader("Bearer") String tokenStr, @Valid @PathVariable Long resourceId, @PathVariable Long flagId) {
        Token token = new Token();
        token.setToken(tokenStr);
        Authentication auth = new AuthenticationImpl(authServerUrl);
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
