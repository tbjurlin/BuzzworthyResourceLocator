package com.buzzword;

public class Comment extends Record {
    private String contents;

    /* Constructor */
    public Resource() {
        // Construct the abstract record
        super();
    }

    public String getContents() {
        return contents;
    }

    public void setContents(string contents) {
        // TODO: Add validation for contents
        self.contents = contents;
    }
}
