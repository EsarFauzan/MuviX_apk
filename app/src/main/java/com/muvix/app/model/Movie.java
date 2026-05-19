package com.muvix.app.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    @NonNull
    public String id = "";

    public String title;
    public String posterUrl;
    public String bannerUrl;
    public String genre;
    public String episode;
    public String duration;
    public String views;
    public String description;
    public double rating;
    public boolean subscribed;
    public long watchedAt;

    public Movie() {
    }

    public Movie(@NonNull String id, String title, String posterUrl, String bannerUrl,
                 String genre, String episode, String duration, String views,
                 String description, double rating) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.bannerUrl = bannerUrl;
        this.genre = genre;
        this.episode = episode;
        this.duration = duration;
        this.views = views;
        this.description = description;
        this.rating = rating;
        this.subscribed = false;
        this.watchedAt = 0;
    }
}
