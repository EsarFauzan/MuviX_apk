package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;
import com.muvix.app.view.adapter.SubscribeAdapter;

import java.util.ArrayList;

public class SubscribeActivity extends AppCompatActivity {
    private MovieController controller;
    private SubscribeAdapter adapter;
    private BottomNavigationView bottomNavigation;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        controller = new MovieController(this);

        RecyclerView rvSubscribe = findViewById(R.id.rvSubscribe);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        tvTotal = findViewById(R.id.tvTotal);

        adapter = new SubscribeAdapter(this::openDetail);

        rvSubscribe.setLayoutManager(new GridLayoutManager(this, 2));
        rvSubscribe.setHasFixedSize(false);
        rvSubscribe.setAdapter(adapter);

        setupNavigation();
        loadSubscribed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubscribed();
    }

    private void loadSubscribed() {
        controller.loadSubscribed(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                adapter.setMovies(movies);
                tvTotal.setText("Total (" + movies.size() + ")");

                if (movies.isEmpty()) {
                    Toast.makeText(
                            SubscribeActivity.this,
                            "Belum ada film yang disubscribe.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SubscribeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_subscribe);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                navigateTo(MainActivity.class);
                return true;
            } else if (id == R.id.nav_schedule) {
                navigateTo(ScheduleActivity.class);
                return true;
            } else if (id == R.id.nav_history) {
                navigateTo(HistoryActivity.class);
                return true;
            } else if (id == R.id.nav_subscribe) {
                return true;
            } else if (id == R.id.nav_profile) {
                navigateTo(ProfileActivity.class);
                return true;
            }

            return false;
        });
    }

    private void openDetail(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
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

    private void navigateTo(Class<?> target) {
        startActivity(new Intent(this, target));
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit);
    }
}
