package com.muvix.app.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private MovieController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        controller = new MovieController(this);
        movie = getMovieFromIntent();

        ImageView ivBanner = findViewById(R.id.ivBanner);
        TextView btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvMeta = findViewById(R.id.tvMeta);
        TextView tvDescription = findViewById(R.id.tvDescription);
        MaterialButton btnWatch = findViewById(R.id.btnWatch);
        MaterialButton btnSubscribe = findViewById(R.id.btnSubscribe);

        tvTitle.setText(movie.title);
        tvMeta.setText("⭐ " + movie.rating + " • " + movie.genre + " • " + movie.views);
        tvDescription.setText(movie.description);

        Glide.with(this)
                .load(movie.bannerUrl == null || movie.bannerUrl.isEmpty() ? movie.posterUrl : movie.bannerUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(ivBanner);

        btnBack.setOnClickListener(v -> finish());

        btnWatch.setOnClickListener(v -> {
            controller.saveToHistory(movie);
            Toast.makeText(this, "Masuk ke riwayat menonton.", Toast.LENGTH_SHORT).show();
        });

        btnSubscribe.setOnClickListener(v -> {
            controller.subscribe(movie);
            Toast.makeText(this, "Film disimpan ke Subscribe.", Toast.LENGTH_SHORT).show();
        });
    }

    private Movie getMovieFromIntent() {
        Movie m = new Movie();
        m.id = getIntent().getStringExtra("id");
        if (m.id == null || m.id.isEmpty()) m.id = String.valueOf(System.currentTimeMillis());
        m.title = getIntent().getStringExtra("title");
        m.posterUrl = getIntent().getStringExtra("posterUrl");
        m.bannerUrl = getIntent().getStringExtra("bannerUrl");
        m.genre = getIntent().getStringExtra("genre");
        m.episode = getIntent().getStringExtra("episode");
        m.duration = getIntent().getStringExtra("duration");
        m.views = getIntent().getStringExtra("views");
        m.description = getIntent().getStringExtra("description");
        m.rating = getIntent().getDoubleExtra("rating", 0.0);
        return m;
    }
}
