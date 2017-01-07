package com.udacity.nd801.course.popularmoviesapp.utils;

/**
 * Created by dnbhatia on 12/20/2016.
 */

public class Reviews {
    private String author;
    private String content;
    private String reviewUrl;

    public Reviews(String author,String content,String reviewUrl){
        this.author=author;
        this.content=content;
        this.reviewUrl=reviewUrl;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }
}
