package com.buzzword;

import java.net.http.HttpHeaders;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("wiki")
public class WikiEndpoint {

    @GetMapping("resource")
    public ResponseEntity<String> retrieveAllResources(@Valid @RequestHeader("Bearer") Token token) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.retrieveAll();
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Retrieve all resources\"}");
    }
    
    @PostMapping("resource")
    public ResponseEntity<String> addResource(@Valid @RequestHeader("Bearer") Token token, @Valid @RequestBody Resource resource) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        /* Pseudo code:
         * DAO dbAccess = new DAOImpl(Config.Instance());
         * String obj = dbAccess.insertResource(userCredresource, );
         */
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add resource\"}");
    }

    @PostMapping("resource/{resourceId}/comment")
    public ResponseEntity<String> addComment(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add comment to resource " + resourceId + "\"}");
    }

    @PostMapping("resource/{resourceId}/upvote")
    public ResponseEntity<String> addUpvote(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add upvote to resource " + resourceId + "\"}");
    }

    @PostMapping("resource/{resourceId}/reviewFlag")
    public ResponseEntity<String> addReviewFlag(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Add review flag to resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}")
    public ResponseEntity<String> removeResource(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}/comment/{commentId}")
    public ResponseEntity<String> removeComment(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId, @PathVariable Long commentId) {
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove comment " + commentId + " from resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}/upvote/{upvoteId}")
    public ResponseEntity<String> removeUpvote(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId, @PathVariable Long upvoteId) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove upvote " + upvoteId + " from resource " + resourceId + "\"}");
    }

    @DeleteMapping("resource/{resourceId}/reviewFlag/{flagId}")
    public ResponseEntity<String> removeReviewFlag(@Valid @RequestHeader("Bearer") Token token, @Valid @PathVariable Long resourceId, @PathVariable Long flagId) {
        Authentication auth = new AuthenticationImpl("Hello");
        Credentials userCred = auth.Authenticate(token);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body("{\"msg\": \"Remove review flag " + flagId + " from resource " + resourceId + "\"}");
    }
}
