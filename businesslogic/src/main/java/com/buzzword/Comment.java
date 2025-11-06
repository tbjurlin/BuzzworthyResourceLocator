package com.buzzword;

/**
 * The Comment class creates a comment object that holds the content of a 
 * comment as well as all fields from the Record class.
 */
public class Comment extends Record {
    private String contents;

    private final XssSanitizer commentSanitizer;

    private final Logger logger = LoggerFactory.getEventLogger();

    /* Constructor */
    public Comment() {
        super();
        commentSanitizer = new XssSanitizerImpl();
    }

    /**
     * <p>
     * Compared to Resource.java, this method will focus on validating the contents of the comment.
     * The contents must not be null, must have a minimum length of 1 character,
     * and must not exceed 200 characters.
     * </p>
     * @return
     */
    private String validateContents(final String contents) {
        final int minLength = 1;
        final int maxLength = 200;

        if (contents == null) {
            logger.error("Contents must not be null");
            throw new IllegalArgumentException("Contents must not be null.");
        }

        String sanitizedContents = commentSanitizer.sanitizeInput(contents);

        if (sanitizedContents.isEmpty()) {
            logger.error("Contents must not be empty");
            throw new IllegalArgumentException("Contents must not be empty.");
        }
        if(sanitizedContents.length() < minLength ) {
            logger.error("Contents must be at least 1 character long");
            throw new IllegalArgumentException("Contents must be at least 1 character long");
        }
        if (sanitizedContents.length() > maxLength ) {
            logger.error("Contents must not exceed 200 characters");
            throw new IllegalArgumentException("Contents must not exceed 200 characters");
        }

        return sanitizedContents;
    }
    public String getContents() {
        logger.debug("returning the contents");
        return contents;
    }
    public void setContents(String contents) {
        logger.debug("setting contents");
        this.contents = validateContents(contents);
    }
}
