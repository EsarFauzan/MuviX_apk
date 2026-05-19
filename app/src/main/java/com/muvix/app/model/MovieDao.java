package com.muvix.app.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE watchedAt > 0 ORDER BY watchedAt DESC")
    List<Movie> getHistory();

    @Query("SELECT * FROM movies WHERE subscribed = 1 ORDER BY title ASC")
    List<Movie> getSubscribed();

    @Query("DELETE FROM movies WHERE watchedAt > 0")
    void clearHistory();
}
