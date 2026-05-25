package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.AuthManager;
import com.muvix.app.model.Movie;
import com.muvix.app.view.adapter.ProfileRecentAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private MovieController controller;
    private AuthManager authManager;
    private BottomNavigationView bottomNavigation;

    private TextView tvMinutes;
    private TextView tvHistoryCount;
    private TextView tvSubscribeCount;
    private TextView tvAvatarInitial;
    private TextView tvProfileName;
    private TextView tvProfileEmail;

    private RecyclerView rvRecentActivity;
    private View layoutEmptyRecent;
    private ProfileRecentAdapter recentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        controller = new MovieController(this);
        authManager = new AuthManager(this);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        tvMinutes = findViewById(R.id.tvMinutes);
        tvHistoryCount = findViewById(R.id.tvHistoryCount);
        tvSubscribeCount = findViewById(R.id.tvSubscribeCount);
        tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        rvRecentActivity = findViewById(R.id.rvRecentActivity);
        layoutEmptyRecent = findViewById(R.id.layoutEmptyRecent);
        View btnLogout = findViewById(R.id.btnLogout);

        setupRecentRecycler();
        setupNavigation();
        setupProfileIdentity();
        btnLogout.setOnClickListener(v -> logout());
        loadStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!authManager.isLoggedIn()) {
            openLoginAndFinish();
            return;
        }
        setupProfileIdentity();
        loadStats();
    }

    private void setupProfileIdentity() {
        String name = authManager.getCurrentName();
        String email = authManager.getCurrentEmail();

        tvProfileName.setText(name);
        tvProfileEmail.setText(email.isEmpty() ? "Premium Member" : email);

        String initial = "M";
        if (!name.isEmpty()) {
            initial = String.valueOf(Character.toUpperCase(name.charAt(0)));
        }
        tvAvatarInitial.setText(initial);
    }

    private void setupRecentRecycler() {
        recentAdapter = new ProfileRecentAdapter(this::openPlayer);

        rvRecentActivity.setLayoutManager(new LinearLayoutManager(this));
        rvRecentActivity.setNestedScrollingEnabled(false);
        rvRecentActivity.setAdapter(recentAdapter);
    }

    private void loadStats() {
        controller.loadHistory(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                int historyCount = movies == null ? 0 : movies.size();
                int minutes = historyCount * 24;

                tvHistoryCount.setText(String.valueOf(historyCount));
                tvMinutes.setText(String.valueOf(minutes));

                if (historyCount == 0) {
                    recentAdapter.setMovies(new ArrayList<>());
                    rvRecentActivity.setVisibility(View.GONE);
                    layoutEmptyRecent.setVisibility(View.VISIBLE);
                } else {
                    ArrayList<Movie> recentMovies = new ArrayList<>();

                    int limit = Math.min(5, movies.size());
                    for (int i = 0; i < limit; i++) {
                        recentMovies.add(movies.get(i));
                    }

                    recentAdapter.setMovies(recentMovies);
                    rvRecentActivity.setVisibility(View.VISIBLE);
                    layoutEmptyRecent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
            }
        });

        controller.loadSubscribed(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                int subscribeCount = movies == null ? 0 : movies.size();
                tvSubscribeCount.setText(String.valueOf(subscribeCount));
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    private void setupNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

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
                navigateTo(SubscribeActivity.class);
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }

            return false;
        });
    }

    private void openPlayer(Movie movie) {
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

    private void navigateTo(Class<?> target) {
        TabNavigationHelper.navigate(this, ProfileActivity.class, target);
    }

    private void logout() {
        authManager.logout();
        openLoginAndFinish();
    }

    private void openLoginAndFinish() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

