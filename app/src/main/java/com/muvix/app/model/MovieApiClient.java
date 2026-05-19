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
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);

        int code = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream()
        ));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        if (code < 200 || code >= 300) {
            throw new Exception("API error: " + code);
        }

        JSONArray array = new JSONArray(response.toString());
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            String id = optString(obj, "id");
            String title = optString(obj, "title", "name", "judul", "movieTitle");
            String poster = optString(obj, "posterUrl", "poster", "image", "thumbnail", "cover");
            String banner = optString(obj, "bannerUrl", "banner", "backdrop", "image");
            String genre = optString(obj, "genre", "category", "type");
            String episode = optString(obj, "episode", "eps", "durationType");
            String duration = optString(obj, "duration", "time");
            String views = optString(obj, "views", "view");
            String description = optString(obj, "description", "synopsis", "desc");
            double rating = optDouble(obj, "rating", "score");

            if (title.isEmpty()) title = "Untitled Movie";
            if (episode.isEmpty()) episode = "Film";
            if (genre.isEmpty()) genre = "Movie";
            if (duration.isEmpty()) duration = "120 min";
            if (views.isEmpty()) views = "0 views";
            if (description.isEmpty()) description = "Film pilihan dari MuviX.";

            movies.add(new Movie(id, title, poster, banner, genre, episode, duration, views, description, rating));
        }

        return movies;
    }

    private String optString(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key) && !obj.isNull(key)) {
                String value = obj.optString(key, "");
                if (value != null && !value.trim().isEmpty()) return value.trim();
            }
        }
        return "";
    }

    private double optDouble(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key) && !obj.isNull(key)) {
                return obj.optDouble(key, 0.0);
            }
        }
        return 0.0;
    }
}
