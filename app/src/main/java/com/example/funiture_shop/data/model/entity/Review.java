package com.example.funiture_shop.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reviews")
public class Review implements Serializable {
    @PrimaryKey
    @NonNull
    private String reviewID = "";
    private String creater = "";
    private int rating = 0;
    private String textReview = "";
    private String title = "";

    private String timeCreate = "";

    public Review(@NonNull String reviewID, String creater, int rating, String textReview, String title, String timeCreate) {
        this.reviewID = reviewID;
        this.creater = creater;
        this.rating = rating;
        this.textReview = textReview;
        this.title = title;
        this.timeCreate = timeCreate;
    }

    @NonNull
    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(@NonNull String reviewID) {
        this.reviewID = reviewID;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }
}
