package com.muvix.app.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private MovieController controller;
    private MaterialButton btnSubscribe;
    private boolean isSubscribed = false;

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
        btnSubscribe = findViewById(R.id.btnSubscribe);

        tvTitle.setText(movie.title);
        tvMeta.setText(String.format(Locale.getDefault(), "Rating %.1f | %s | %s", movie.rating, movie.genre, movie.views));
        tvDescription.setText(movie.description);

        Glide.with(this)
                .load(movie.bannerUrl == null || movie.bannerUrl.isEmpty() ? movie.posterUrl : movie.bannerUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(ivBanner);

        btnBack.setOnClickListener(v -> finishWithAnimation());

        btnWatch.setOnClickListener(v -> openPlayer());

        btnSubscribe.setOnClickListener(v -> toggleSubscribe());

        checkSubscribeStatus();
    }

    private void checkSubscribeStatus() {
        controller.isSubscribed(movie.id, value -> {
            isSubscribed = value;
            updateSubscribeButton();
        });
    }

    private void toggleSubscribe() {
        if (isSubscribed) {
            controller.unsubscribe(movie);
            isSubscribed = false;
            Toast.makeText(this, "Subscribe dibatalkan.", Toast.LENGTH_SHORT).show();
        } else {
            controller.subscribe(movie);
            isSubscribed = true;
            Toast.makeText(this, "Film disimpan ke Subscribe.", Toast.LENGTH_SHORT).show();
        }

        updateSubscribeButton();
    }

    private void updateSubscribeButton() {
        if (isSubscribed) {
            btnSubscribe.setText("Saved");
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.accent_gold)
            ));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.bg_dark));
        } else {
            btnSubscribe.setText("Save");
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.surface_soft)
            ));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }
    }

    private void openPlayer() {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("id", movie.id);
        intent.putExtra("title", movie.title);
        intent.putExtra("posterUrl", movie.posterUrl);
        intent.putExtra("bannerUrl", movie.bannerUrl);
        intent.putExtra("genre", movie.genre);
        intent.putExtra("episode", movie.episode);
        intent.putExtra("duration", movie.duration);
        intent.putExtra("views", movie.views);
        intent.putExtra("description", movie.description);
        intent.putExtra("rating", movie.rating);
        startActivity(intent);
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit);
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.page_back_enter, R.anim.page_back_exit);
    }

    private Movie getMovieFromIntent() {
        Movie m = new Movie();

        m.id = getIntent().getStringExtra("id");
        if (m.id == null || m.id.isEmpty()) {
            m.id = String.valueOf(System.currentTimeMillis());
        }

        m.title = getIntent().getStringExtra("title");
        m.posterUrl = getIntent().getStringExtra("posterUrl");
        m.bannerUrl = getIntent().getStringExtra("bannerUrl");
        m.genre = getIntent().getStringExtra("genre");
        m.episode = getIntent().getStringExtra("episode");
        m.duration = getIntent().getStringExtra("duration");
        m.views = getIntent().getStringExtra("views");
        m.description = getIntent().getStringExtra("description");
        m.rating = getIntent().getDoubleExtra("rating", 0.0);

        if (m.title == null) m.title = "Untitled Movie";
        if (m.genre == null) m.genre = "Movie";
        if (m.views == null) m.views = "0 views";
        if (m.description == null) m.description = "Film pilihan dari MuviX.";
        if (m.duration == null || m.duration.isEmpty()) m.duration = "120 min";

        return m;
    }
}
