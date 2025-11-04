package com.buzzword;

public class Comment extends Record {
    private String contents;

    private final XssSanitizer commentSanitizer;

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
            throw new IllegalArgumentException("Contents must not be null.");
        }

        String sanitizedContents = commentSanitizer.sanitizeInput(contents);

        if (sanitizedContents.isEmpty()) {
            throw new IllegalArgumentException("Contents must not be empty.");
        }
        if(sanitizedContents.length() < minLength ) {
            throw new IllegalArgumentException("Contents must be at least 1 character long");
        }
        if (sanitizedContents.length() > maxLength ) {
            throw new IllegalArgumentException("Contents must not exceed 200 characters");
        }

        return sanitizedContents;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = validateContents(contents);
    }
}
