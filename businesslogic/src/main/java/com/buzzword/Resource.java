package com.buzzword;


import java.util.List;


public class Resource extends Record {
    private String title;
    private String description;
    private String url;
    private List<Comment> comments;
    private List<ReviewFlag> reviewFlags;
    private List<UpVote> upVoteFlags;

    /* Constructor */
    public Resource() {
        // Construct the abstract record
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // TODO: Add validation for title
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        // TODO: Add validation for url
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        // TODO: Add validation for description
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        // TODO: Add validation for comments
        this.comments = comments;
    }

    public List<ReviewFlag> getReviewFlags() {
        return reviewFlags;
    }

    public void setReviewFlags(List<ReviewFlag> reviewFlags) {
        // TODO: Add validation for reviewFlags
        this.reviewFlags = reviewFlags;
    }

    public List<UpVote> getUpVoteFlags() {
        return upVoteFlags;
    }

    public void setUpVoteFlags(List<UpVote> upVoteFlags) {
        // TODO: Add validation for upVoteFlags
        this.upVoteFlags = upVoteFlags;
    }

}
