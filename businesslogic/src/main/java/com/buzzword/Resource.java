package com.buzzword;

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Resource class creates a REsource object that holds the title, 
 * descripiton and URL of a resource as well as all fields from the Record 
 * class.
 */
public class Resource extends Record {
    private String title; 
    private String description; 
    private String url;
    @JsonIgnore
    private List<Comment> comments; 
    @JsonIgnore
    private List<ReviewFlag> reviewFlags;
    @JsonIgnore
    private List<Upvote> upVotes;

    private final XssSanitizer resourceSanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();

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
            logger.error("Title must not be null");
            throw new IllegalArgumentException("Title must not be null.");
        }

        String sanitizedTitle = resourceSanitizer.sanitizeInput(title);

        if (sanitizedTitle.isEmpty()) {
            logger.error("Title must not be empty");
            throw new IllegalArgumentException("Title must not be empty.");
        }
        if(sanitizedTitle.length() < minLength ) {
            logger.error("Title must be at least 1 character long");
            throw new IllegalArgumentException("Title must be at least 1 character long");
        }
        if (sanitizedTitle.length() > maxLength ) {
            logger.error("Title must not exceed 64 characters");
            throw new IllegalArgumentException("Title must not exceed 64 characters");
        }
        
        return sanitizedTitle;
    }
    public String getTitle() {
        logger.debug("returning the title: " + title);
        return title;
    }
    public void setTitle(String title) {
        logger.debug("setting the title");
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
            logger.error("URL must not be null");
            throw new IllegalArgumentException("URL must not be null.");
        }

        if (url.trim().isEmpty()) {
            logger.error("URL must not be empty");
            throw new IllegalArgumentException("URL must not be empty.");
        }

        String sanitizedUrl = resourceSanitizer.sanitizeInput(url);

        // Create URL validator that only allows http and https
        UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
        if (!urlValidator.isValid(sanitizedUrl)) {
            logger.error("URL is not valid. Must be a valid HTTP or HTTPS URL");
            throw new IllegalArgumentException("URL is not valid. Must be a valid HTTP or HTTPS URL.");
        }
        return sanitizedUrl;
    }
    public String getUrl() {
        logger.debug("returning the url: " + url);
        return url;
    }
    public void setUrl(String url) {
        logger.debug("setting the url");
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
            logger.error("Description must not be null");
            throw new IllegalArgumentException("Description must not be null.");
        }

        String sanitizedDescription = resourceSanitizer.sanitizeInput(description);

        if (sanitizedDescription.isEmpty()) {
            logger.error("Description must not be empty");
            throw new IllegalArgumentException("Description must not be empty.");
        }

        return sanitizedDescription;
    }
    public String getDescription() {
        logger.debug("returning the description");
        return description;
    }
    public void setDescription(String description) {
        logger.debug("setting the description");
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
            logger.error("Comments must not be null");
            throw new IllegalArgumentException("Comments must not be null.");
        }
        return comments;
    } 
    public List<Comment> getComments() {
        logger.debug("returning the comment list");
        return comments;
    }
    public void setComments(List<Comment> comments) {
        logger.debug("setting the comment list");
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
            logger.error("Review flags must not be null");
            throw new IllegalArgumentException("ReviewFlags must not be null.");
        }
        return reviewFlags;
    }
    public List<ReviewFlag> getReviewFlags() {
        logger.debug("returning the review flags list");
        return reviewFlags;
    }
    public void setReviewFlags(List<ReviewFlag> reviewFlags) {
        logger.debug("setting the review flags list");
        this.reviewFlags = validateReviewFlags(reviewFlags);
    }

    /**
     * <p>
     * Ensures that the upVotes list is not null before setting it.
     * </p>
     * 
     * @param upVotes
     */
    private List<Upvote> validateUpvotes(final List<Upvote> upVotes)
    {
        if (upVotes == null) {
            logger.error("Upvotes must not be null");
            throw new IllegalArgumentException("Upvotes must not be null.");
        }
        return upVotes;
    }
    public List<Upvote> getUpvotes() {
        logger.debug("returning the upvotes list");
        return upVotes;
    }
    public void setUpvotes(List<Upvote> upVotes) {
        logger.debug("setting the upvotes list");
        this.upVotes = validateUpvotes(upVotes);
    }

}
