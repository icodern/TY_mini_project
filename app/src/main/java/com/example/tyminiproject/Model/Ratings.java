package com.example.tyminiproject.Model;

public class Ratings {

    String userId;
    String messId;
    String comments;
    String rating;

    public Ratings(String userId, String messId, String rating, String comments) {
        this.userId = userId;
        this.messId = messId;
        this.rating = rating;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Ratings{" +
                "userId='" + userId + '\'' +
                ", messId='" + messId + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }

    public Ratings(String userId, String messId, String rating) {
        this.userId = userId;
        this.messId = messId;
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
