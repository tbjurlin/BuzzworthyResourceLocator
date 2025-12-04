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

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Resource class creates a Resource object that holds the title, 
 * description and URL of a resource as well as all fields from the Record 
 * class.
 * 
 * @author Dennis Shelby
 * @version 1.0
 */
public class Resource extends Record {
    private String title; 
    private String description; 
    private String url;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Comment> comments; 
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ReviewFlag> reviewFlags;
    @JsonIgnore
    private List<Upvote> upvotes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int upvoteCount;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean upvotedByCurrentUser;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int currentUserUpvoteId;

    private final XssSanitizer resourceSanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();

    /**
     * Constructs a new Resource object with default values.
     */
    public Resource() {
        super(); // Call the parent constructor (i.e. Record)
        resourceSanitizer = new XssSanitizerImpl(); // Initialize the sanitizer
    }


    /**
     * Validates and sanitizes the title for the resource before accepting and setting it. 
     * The title must not be null, empty, or exceed 64 characters, and should be at least 1 character.
     * 
     * @param title the title to validate
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

    /**
     * Gets the title of the resource.
     * 
     * @return the resource title
     */
    public String getTitle() {
        logger.debug("returning the title: " + title);
        return title;
    }

    /**
     * Sets the title of the resource.
     * <p>
     * The title will be validated and sanitized before being set.
     * 
     * @param title the resource title to set
     */
    public void setTitle(String title) {
        logger.debug("setting the title");
        this.title = validateTitle(title);
    }


    /** 
     * Validates and sanitizes the URL for the resource before accepting and setting it.
     * This includes checking that the URL is well-formed and uses either the HTTP or HTTPS protocol,
     * as well as making sure it is not null or empty.
     * 
     * @param url the URL to validate
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

    /**
     * Gets the URL of the resource.
     * 
     * @return the resource URL
     */
    public String getUrl() {
        logger.debug("returning the url: " + url);
        return url;
    }

    /**
     * Sets the URL of the resource.
     * <p>
     * The URL will be validated and sanitized before being set.
     * 
     * @param url the resource URL to set
     */
    public void setUrl(String url) {
        logger.debug("setting the url");
        this.url = validateUrl(url);
    }


    /**
     * Validates and sanitizes the description for the resource before accepting and setting it.
     * 
     * @param description the description to validate
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

    /**
     * Gets the description of the resource.
     * 
     * @return the resource description
     */
    public String getDescription() {
        logger.debug("returning the description");
        return description;
    }

    /**
     * Sets the description of the resource.
     * <p>
     * The description will be validated and sanitized before being set.
     * 
     * @param description the resource description to set
     */
    public void setDescription(String description) {
        logger.debug("setting the description");
        this.description = validateDescription(description);
    }


    /**
     * Ensures that the comments list is not null before setting it.
     * 
     * @param comments the list of comments to validate
     * @return the validated comments list
     */
    private List<Comment> validateComments(final List<Comment> comments) {
        if (comments == null) {
            logger.error("Comments must not be null");
            throw new IllegalArgumentException("Comments must not be null.");
        }
        return comments;
    }

    /**
     * Gets the list of comments associated with this resource.
     * 
     * @return the list of comments
     */
    public List<Comment> getComments() {
        logger.debug("returning the comment list");
        return comments;
    }

    /**
     * Sets the list of comments for this resource.
     * <p>
     * The comments list will be validated before being set.
     * 
     * @param comments the list of comments to set
     */
    public void setComments(List<Comment> comments) {
        logger.debug("setting the comment list");
        this.comments = validateComments(comments);
    }

    /**
     * Ensures that the reviewFlags list is not null before setting it.
     * 
     * @param reviewFlags the list of review flags to validate
     * @return the validated review flags list
     */
    private List<ReviewFlag> validateReviewFlags(final List<ReviewFlag> reviewFlags) {
        if (reviewFlags == null) {
            logger.error("Review flags must not be null");
            throw new IllegalArgumentException("ReviewFlags must not be null.");
        }
        return reviewFlags;
    }

    /**
     * Gets the list of review flags associated with this resource.
     * 
     * @return the list of review flags
     */
    public List<ReviewFlag> getReviewFlags() {
        logger.debug("returning the review flags list");
        return reviewFlags;
    }

    /**
     * Sets the list of review flags for this resource.
     * <p>
     * The review flags list will be validated before being set.
     * 
     * @param reviewFlags the list of review flags to set
     */
    public void setReviewFlags(List<ReviewFlag> reviewFlags) {
        logger.debug("setting the review flags list");
        this.reviewFlags = validateReviewFlags(reviewFlags);
    }

    /**
     * Ensures that the upvotes list is not null before setting it.
     * 
     * @param upvotes the list of upvotes to validate
     * @return the validated upvotes list
     */
    private List<Upvote> validateUpvotes(final List<Upvote> upvotes)
    {
        if (upvotes == null) {
            logger.error("Upvotes must not be null");
            throw new IllegalArgumentException("Upvotes must not be null.");
        }
        return upvotes;
    }

    /**
     * Gets the list of upvotes associated with this resource.
     * 
     * @return the list of upvotes
     */
    public List<Upvote> getUpvotes() {
        logger.debug("returning the upvotes list");
        return upvotes;
    }

    /**
     * Sets the list of upvotes for this resource.
     * <p>
     * The upvotes list will be validated before being set.
     * 
     * @param upvotes the list of upvotes to set
     */
    public void setUpvotes(List<Upvote> upvotes) {
        logger.debug("setting the upvotes list");
        this.upvotes = validateUpvotes(upvotes);
    }

    /**
     * Ensures that the upvote count is not negative before setting it.
     * 
     * @param upvoteCount the upvote count to validate
     * @return the validated upvote count
     */
    private int validateUpvoteCount(final int upvoteCount)
    {
        if (upvoteCount < 0) {
            logger.error("Upvote count must not be negative");
            throw new IllegalArgumentException("Upvote count must not be negative.");
        }
        return upvoteCount;
    }

    /**
     * Gets the total count of upvotes for this resource.
     * 
     * @return the upvote count
     */
    public int getUpvoteCount() {
        logger.debug("returning the upvote count");
        return upvoteCount;
    }

    /**
     * Sets the upvote count for this resource.
     * <p>
     * The upvote count will be validated before being set.
     * 
     * @param upvoteCount the upvote count to set
     */
    public void setUpvoteCount(int upvoteCount) {
        logger.debug("setting the upvote count");
        this.upvoteCount = validateUpvoteCount(upvoteCount);
    }

    /**
     * Increments the upvote count by one.
     */
    public void incrementUpvoteCount() {
        this.upvoteCount++;
    }

    /**
     * Decrements the upvote count by one.
     * <p>
     * If the upvote count is already zero, the operation is ignored and a warning is logged.
     */
    public void decrementUpvoteCount() {
        if (this.upvoteCount > 0) {
            this.upvoteCount--;
        } else {
            logger.warn("Attempted to decrement upvote count below zero. Operation ignored.");
        }
    }

    /**
     * Checks if the current user has upvoted this resource.
     * 
     * @return true if the current user upvoted this resource, false otherwise
     */
    public boolean getUpvotedByCurrentUser() {
        return upvotedByCurrentUser;
    }

    /**
     * Sets whether the current user has upvoted this resource.
     * 
     * @param upvotedByCurrentUser true if the current user upvoted, false otherwise
     */
    public void setUpvotedByCurrentUser(boolean upvotedByCurrentUser) {
        this.upvotedByCurrentUser = upvotedByCurrentUser;
    }

    /**
     * Ensures that the current user upvote ID is not negative before setting it.
     * 
     * @param currentUserUpvoteId the current user upvote ID to validate
     * @return the validated current user upvote ID
     */
    private int validateCurrentUserUpvoteId(final int currentUserUpvoteId)
    {
        if (currentUserUpvoteId < -1) {
            logger.error("Current user upvote ID must not be less than -1");
            throw new IllegalArgumentException("Current user upvote ID must not be less than -1.");
        }
        return currentUserUpvoteId;
    }

    /**
     * Gets the ID of the upvote created by the current user for this resource.
     * 
     * @return the current user's upvote ID, or -1 if not upvoted
     */
    public int getCurrentUserUpvoteId() {
        logger.debug("returning the current user upvote ID");
        return currentUserUpvoteId;
    }

    /**
     * Sets the ID of the upvote created by the current user for this resource.
     * <p>
     * The ID will be validated before being set.
     * 
     * @param currentUserUpvoteId the current user's upvote ID to set
     */
    public void setCurrentUserUpvoteId(int currentUserUpvoteId) {
        logger.debug("setting the current user upvote ID");
        this.currentUserUpvoteId = validateCurrentUserUpvoteId(currentUserUpvoteId);
    }
}