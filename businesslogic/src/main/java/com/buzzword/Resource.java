package com.buzzword;

public class Resource extends Record {
    private String title;
    private String description;
    private String url;
    private Comment[] comments;
    private Flag[] reviewFlags;
    private Flag[] upVoteFlags;

    /* Constructor */
    public Resource() {
        // Construct the abstract record
        Record();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {

    }

    public Comment getComments() {
        return comments;
    }

    public void setComments(Comment[] newComments) {

    }

    public Flag[] getReviewFlags() {
        return reviewFlags;
    }

    public void setReviewFlags(Flag[] newReviewFlags) {

    }

    public Flag[] getUpVoteFlags() {
        return upVoteFlags;
    }

    public void setUpVoteFlags(Flag[] newUpVoteFlags) {

    }

}
