package com.muvix.app.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;
import com.muvix.app.view.adapter.ScheduleAdapter;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private MovieController controller;
    private ScheduleAdapter adapter;

    private RecyclerView rvSchedule;
    private View layoutLoadingSchedule;
    private View layoutEmptySchedule;
    private BottomNavigationView bottomNavigation;

    private TextView btnToday, btnTomorrow, btnWeek;
    private TextView tvScheduleInfo;

    private final ArrayList<Movie> allMovies = new ArrayList<>();
    private String selectedMode = "Hari Ini";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        controller = new MovieController(this);

        rvSchedule = findViewById(R.id.rvSchedule);
        layoutLoadingSchedule = findViewById(R.id.layoutLoadingSchedule);
        layoutEmptySchedule = findViewById(R.id.layoutEmptySchedule);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        btnToday = findViewById(R.id.btnToday);
        btnTomorrow = findViewById(R.id.btnTomorrow);
        btnWeek = findViewById(R.id.btnWeek);
        tvScheduleInfo = findViewById(R.id.tvScheduleInfo);

        setupRecyclerView();
        setupFilter();
        setupNavigation();
        loadSchedule();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_schedule);
    }

    private void setupRecyclerView() {
        adapter = new ScheduleAdapter(this::openDetail);

        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.setHasFixedSize(false);
        rvSchedule.setAdapter(adapter);
    }

    private void setupFilter() {
        btnToday.setOnClickListener(v -> {
            selectedMode = "Hari Ini";
            updateFilterUi();
            showFilteredSchedule();
        });

        btnTomorrow.setOnClickListener(v -> {
            selectedMode = "Besok";
            updateFilterUi();
            showFilteredSchedule();
        });

        btnWeek.setOnClickListener(v -> {
            selectedMode = "Minggu Ini";
            updateFilterUi();
            showFilteredSchedule();
        });
    }

    private void loadSchedule() {
        layoutLoadingSchedule.setVisibility(View.VISIBLE);
        rvSchedule.setVisibility(View.GONE);
        layoutEmptySchedule.setVisibility(View.GONE);

        controller.loadMovies(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                layoutLoadingSchedule.setVisibility(View.GONE);
                allMovies.clear();

                if (movies != null) {
                    allMovies.addAll(movies);
                }

                showFilteredSchedule();
            }

            @Override
            public void onError(String message) {
                layoutLoadingSchedule.setVisibility(View.GONE);
                Toast.makeText(ScheduleActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFilteredSchedule() {
        ArrayList<Movie> result = new ArrayList<>();

        if (selectedMode.equals("Hari Ini")) {
            int limit = Math.min(4, allMovies.size());
            for (int i = 0; i < limit; i++) {
                result.add(allMovies.get(i));
            }

            tvScheduleInfo.setText("Jadwal tayang hari ini");
        } else if (selectedMode.equals("Besok")) {
            int start = Math.min(2, allMovies.size());
            int end = Math.min(start + 4, allMovies.size());

            for (int i = start; i < end; i++) {
                result.add(allMovies.get(i));
            }

            tvScheduleInfo.setText("Jadwal tayang besok");
        } else {
            result.addAll(allMovies);
            tvScheduleInfo.setText("Semua jadwal tayang minggu ini");
        }

        adapter.setMovies(result, selectedMode);

        boolean isEmpty = result.isEmpty();
        rvSchedule.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        layoutEmptySchedule.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        layoutLoadingSchedule.setVisibility(View.GONE);
    }

    private void updateFilterUi() {
        setChip(btnToday, selectedMode.equals("Hari Ini"));
        setChip(btnTomorrow, selectedMode.equals("Besok"));
        setChip(btnWeek, selectedMode.equals("Minggu Ini"));
    }

    private void setChip(TextView chip, boolean active) {
        int bgColor = ContextCompat.getColor(this, active ? R.color.primary_blue : R.color.surface_soft);
        int textColor = ContextCompat.getColor(this, active ? R.color.text_primary : R.color.text_secondary);

        chip.setBackgroundTintList(ColorStateList.valueOf(bgColor));
        chip.setTextColor(textColor);
    }

    private void setupNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_schedule);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                navigateTo(MainActivity.class);
                return true;
            } else if (id == R.id.nav_schedule) {
                return true;
            } else if (id == R.id.nav_history) {
                navigateTo(HistoryActivity.class);
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
