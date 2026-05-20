package com.muvix.app.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.muvix.app.model.AppDatabase;
import com.muvix.app.model.Movie;
import com.muvix.app.model.MovieApiClient;
import com.muvix.app.model.MovieDao;
import com.muvix.app.model.SampleData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieController {
    private final MovieApiClient apiClient;
    private final MovieDao movieDao;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public interface MovieCallback {
        void onSuccess(ArrayList<Movie> movies);
        void onError(String message);
    }

    public interface StateCallback {
        void onResult(boolean value);
    }

    public MovieController(Context context) {
        apiClient = new MovieApiClient();
        movieDao = AppDatabase.getInstance(context).movieDao();
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void loadMovies(MovieCallback callback) {
        executor.execute(() -> {
            try {
                ArrayList<Movie> movies = apiClient.fetchMovies();
                if (movies.isEmpty()) movies = SampleData.movies();
                ArrayList<Movie> finalMovies = movies;
                mainHandler.post(() -> callback.onSuccess(finalMovies));
            } catch (Exception e) {
                ArrayList<Movie> fallback = SampleData.movies();
                mainHandler.post(() -> {
                    callback.onSuccess(fallback);
                    callback.onError("API belum siap, pakai data contoh dulu.");
                });
            }
        });
    }

    public void saveToHistory(Movie movie) {
        executor.execute(() -> {
            Movie existing = movieDao.getMovieById(movie.id);
            movie.watchedAt = System.currentTimeMillis();
            if (existing != null) {
                movie.subscribed = existing.subscribed;
            }
            movieDao.saveMovie(movie);
        });
    }

    public void subscribe(Movie movie) {
        executor.execute(() -> {
            Movie existing = movieDao.getMovieById(movie.id);
            movie.subscribed = true;
            if (existing != null && existing.watchedAt > 0) {
                movie.watchedAt = existing.watchedAt;
            }
            movieDao.saveMovie(movie);
        });
    }

    public void unsubscribe(Movie movie) {
        executor.execute(() -> {
            Movie existing = movieDao.getMovieById(movie.id);
            if (existing != null) {
                existing.subscribed = false;
                movieDao.saveMovie(existing);
            }
        });
    }

    public void isSubscribed(String movieId, StateCallback callback) {
        executor.execute(() -> {
            Movie existing = movieDao.getMovieById(movieId);
            boolean result = existing != null && existing.subscribed;
            mainHandler.post(() -> callback.onResult(result));
        });
    }

    public void loadHistory(MovieCallback callback) {
        executor.execute(() -> {
            List<Movie> list = movieDao.getHistory();
            mainHandler.post(() -> callback.onSuccess(new ArrayList<>(list)));
        });
    }

    public void loadSubscribed(MovieCallback callback) {
        executor.execute(() -> {
            List<Movie> list = movieDao.getSubscribed();
            mainHandler.post(() -> callback.onSuccess(new ArrayList<>(list)));
        });
    }

    public void clearHistory(Runnable callback) {
        executor.execute(() -> {
            movieDao.clearHistory();

            if (callback != null) {
                mainHandler.post(callback);
            }
        });
    }
}
