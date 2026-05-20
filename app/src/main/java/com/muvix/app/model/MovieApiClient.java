package com.muvix.app.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieApiClient {
    private static final String API_URL = "https://68ff8dfbe02b16d1753e765d.mockapi.io/film";

    public ArrayList<Movie> fetchMovies() throws Exception {
        ArrayList<Movie> movies = new ArrayList<>();

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        JSONArray array = new JSONArray(response.toString());

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            movies.add(parseMovie(object));
        }

        return movies;
    }

    private Movie parseMovie(JSONObject object) {
        Movie movie = new Movie();

        movie.id = object.optString("id", String.valueOf(System.currentTimeMillis()));

        movie.title = getString(object, "title", "judul", "Untitled Movie");
        movie.description = getString(object, "description", "ringkasan", "Film pilihan dari MuviX.");
        movie.posterUrl = getString(object, "posterUrl", "gambar_poster", "");
        movie.bannerUrl = getString(object, "bannerUrl", "gambar_sampul", "");
        movie.genre = getString(object, "genre", "kategori", "Movie");

        movie.episode = getString(object, "episode", "episode", "Movie");
        movie.duration = getString(object, "duration", "durasi", "120 min");
        movie.views = getString(object, "views", "jumlah_view", "0 views");

        movie.rating = getRating(object);

        return movie;
    }

    private String getString(JSONObject object, String key1, String key2, String defaultValue) {
        String value1 = object.optString(key1, "");

        if (value1 != null && !value1.trim().isEmpty()) {
            return value1;
        }

        String value2 = object.optString(key2, "");

        if (value2 != null && !value2.trim().isEmpty()) {
            return value2;
        }

        return defaultValue;
    }

    private double getRating(JSONObject object) {
        Object value = object.opt("rating");

        if (value == null) {
            value = object.opt("skor_rating");
        }

        try {
            double rating = Double.parseDouble(String.valueOf(value));

            if (rating > 10) {
                rating = rating / 10.0;
            }

            return Math.round(rating * 10.0) / 10.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
