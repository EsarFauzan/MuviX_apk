package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;
import com.muvix.app.view.adapter.HistoryAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private MovieController controller;
    private HistoryAdapter adapter;

    private RecyclerView rvHistory;
    private View layoutEmptyHistory;
    private TextView tvHistoryInfo;
    private TextView btnClearHistory;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        controller = new MovieController(this);

        rvHistory = findViewById(R.id.rvHistory);
        layoutEmptyHistory = findViewById(R.id.layoutEmptyHistory);
        tvHistoryInfo = findViewById(R.id.tvHistoryInfo);
        btnClearHistory = findViewById(R.id.btnClearHistory);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        setupRecyclerView();
        setupNavigation();
        setupActions();
        loadHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter(this::openPlayer);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setHasFixedSize(false);
        rvHistory.setAdapter(adapter);
    }

    private void setupActions() {
        btnClearHistory.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hapus riwayat?")
                    .setMessage("Semua riwayat menonton akan dikosongkan, tapi film subscribe tetap aman.")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        controller.clearHistory(() -> {
                            Toast.makeText(this, "Riwayat berhasil dihapus.", Toast.LENGTH_SHORT).show();
                            loadHistory();
                        });
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    private void loadHistory() {
        controller.loadHistory(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                adapter.setMovies(movies);

                int total = movies == null ? 0 : movies.size();
                tvHistoryInfo.setText("Total " + total + " film pernah ditonton");

                boolean isEmpty = total == 0;

                rvHistory.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                layoutEmptyHistory.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                btnClearHistory.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_history);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                navigateTo(MainActivity.class);
                return true;
            } else if (id == R.id.nav_schedule) {
                navigateTo(ScheduleActivity.class);
                return true;
            } else if (id == R.id.nav_history) {
                return true;
            } else if (id == R.id.nav_subscribe) {
                navigateTo(SubscribeActivity.class);
                return true;
            } else if (id == R.id.nav_profile) {
                navigateTo(ProfileActivity.class);
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
        startActivity(new Intent(this, target));
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit);
    }
}
