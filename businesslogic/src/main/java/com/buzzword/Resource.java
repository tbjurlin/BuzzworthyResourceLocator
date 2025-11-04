package com.buzzword;

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Resource extends Record {
    private String title; 
    private String description; 
    private String url;
    @JsonIgnore
    private List<Comment> comments; 
    @JsonIgnore
    private List<ReviewFlag> reviewFlags;
    @JsonIgnore
    private List<UpVote> upVotes;

    private final XssSanitizer resourceSanitizer;

    /* Constructor */
    public Resource() {
        super(); // Call the parent constructor (i.e. Record)
        resourceSanitizer = new XssSanitizerImpl(); // Initialize the sanitizer
    }


    /**
     * <p>
     * Will validate and sanitize the Title for the resource before accepting and setting it. 
     * The Title must not be null, empty, or exceed 64 characters, and should be at least 1 character.
     * </p>
     * 
     * @return sanitized and validated title string    
     */
    private String validateTitle(final String title) {
        final int minLength = 1;
        final int maxLength = 64;

        if (title == null) {
            throw new IllegalArgumentException("Title must not be null.");
        }

        String sanitizedTitle = resourceSanitizer.sanitizeInput(title);

        if (sanitizedTitle.isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty.");
        }
        if(sanitizedTitle.length() < minLength ) {
            throw new IllegalArgumentException("Title must be at least 1 character long");
        }
        if (sanitizedTitle.length() > maxLength ) {
            throw new IllegalArgumentException("Title must not exceed 64 characters");
        }
        
        return sanitizedTitle;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = validateTitle(title);
    }


    /** 
     *<p>
     Will validate and sanitize the Url for the resource before accepting and setting it.
     *This includes checking that the URL is well-formed and uses either the HTTP or HTTPS protocol. 
     As well as making sure it is not null or empty.
     * </p> 
     * 
     * @return sanitized and validated URL string
    */
    private String validateUrl(final String url) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null.");
        }

        if (url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be empty.");
        }

        String sanitizedUrl = resourceSanitizer.sanitizeInput(url);

        // Create URL validator that only allows http and https
        UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
        if (!urlValidator.isValid(sanitizedUrl)) {
            throw new IllegalArgumentException("URL is not valid. Must be a valid HTTP or HTTPS URL.");
        }
        return sanitizedUrl;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = validateUrl(url);
    }


    /**
     * <p>
     * Will validate and sanitize the Description for the resource before accepting and setting it.
     * </p>
     * 
     * @return sanitized and validated description string
     */
    private String validateDescription(final String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description must not be null.");
        }

        String sanitizedDescription = resourceSanitizer.sanitizeInput(description);

        if (sanitizedDescription.isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty.");
        }

        return sanitizedDescription;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = validateDescription(description);
    }


    /**
     * <p>
     * Ensures that the comments list is not null before setting it.
     * </p>
     * 
     * @param comments
     * @return
     */
    private List<Comment> validateComments(final List<Comment> comments) {
        if (comments == null) {
            throw new IllegalArgumentException("Comments must not be null.");
        }
        return comments;
    } 
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = validateComments(comments);
    }

    /**
     * <p>
     * Ensures that the reviewFlags list is not null before setting it.
     * </p>
     * 
     * @param reviewFlags
     */
    private List<ReviewFlag> validateReviewFlags(final List<ReviewFlag> reviewFlags) {
        if (reviewFlags == null) {
            throw new IllegalArgumentException("ReviewFlags must not be null.");
        }
        return reviewFlags;
    }
    public List<ReviewFlag> getReviewFlags() {
        return reviewFlags;
    }
    public void setReviewFlags(List<ReviewFlag> reviewFlags) {
        this.reviewFlags = validateReviewFlags(reviewFlags);
    }

    /**
     * <p>
     * Ensures that the upVotes list is not null before setting it.
     * </p>
     * 
     * @param upVotes
     */
    private List<UpVote> validateUpVotes(final List<UpVote> upVotes)
    {
        if (upVotes == null) {
            throw new IllegalArgumentException("Upvotes must not be null.");
        }
        return upVotes;
    }
    public List<UpVote> getUpVotes() {
        return upVotes;
    }
    public void setUpVotes(List<UpVote> upVotes) {
        this.upVotes = validateUpVotes(upVotes);
    }

}
