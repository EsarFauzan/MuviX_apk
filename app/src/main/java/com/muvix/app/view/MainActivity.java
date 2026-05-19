package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;
import com.muvix.app.view.adapter.ContinueAdapter;
import com.muvix.app.view.adapter.MoviePosterAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MovieController controller;
    private MoviePosterAdapter movieAdapter;
    private ContinueAdapter continueAdapter;
    private ArrayList<Movie> allMovies = new ArrayList<>();

    private ImageView ivHero;
    private TextView tvHeroTitle, tvHeroMeta, tvSectionTitle;
    private EditText inputSearch;
    private RecyclerView rvMovies, rvContinue;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MovieController(this);
        initViews();
        setupRecyclerViews();
        setupSearch();
        setupNavigation();
        loadHome();
    }

    private void initViews() {
        ivHero = findViewById(R.id.ivHero);
        tvHeroTitle = findViewById(R.id.tvHeroTitle);
        tvHeroMeta = findViewById(R.id.tvHeroMeta);
        tvSectionTitle = findViewById(R.id.tvSectionTitle);
        inputSearch = findViewById(R.id.inputSearch);
        rvMovies = findViewById(R.id.rvMovies);
        rvContinue = findViewById(R.id.rvContinue);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecyclerViews() {
        movieAdapter = new MoviePosterAdapter(movie -> {
            controller.saveToHistory(movie);
            openDetail(movie);
        });

        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(movieAdapter);

        continueAdapter = new ContinueAdapter();
        rvContinue.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvContinue.setAdapter(continueAdapter);
    }

    private void setupSearch() {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadHome();
                return true;
            } else if (id == R.id.nav_schedule) {
                tvSectionTitle.setText("Jadwal Tayang");
                movieAdapter.setMovies(allMovies);
                Toast.makeText(this, "Jadwal memakai data film sementara.", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_history) {
                tvSectionTitle.setText("Riwayat Menonton");
                controller.loadHistory(simpleCallback());
                return true;
            } else if (id == R.id.nav_subscribe) {
                tvSectionTitle.setText("Subscribed Movie");
                controller.loadSubscribed(simpleCallback());
                return true;
            } else if (id == R.id.nav_profile) {
                tvSectionTitle.setText("Profil MuviX");
                Toast.makeText(this, "Halaman profil akan kita buat di tahap berikutnya.", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    private MovieController.MovieCallback simpleCallback() {
        return new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                movieAdapter.setMovies(movies);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void loadHome() {
        tvSectionTitle.setText("New Update Film");
        controller.loadMovies(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                allMovies = movies;
                movieAdapter.setMovies(movies);
                continueAdapter.setMovies(new ArrayList<>(movies.subList(0, Math.min(4, movies.size()))));
                setupHero(movies);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupHero(ArrayList<Movie> movies) {
        if (movies.isEmpty()) return;

        Movie hero = movies.get(0);
        tvHeroTitle.setText(hero.title);
        tvHeroMeta.setText("⭐ " + hero.rating + " • " + hero.genre + " • " + hero.duration);

        Glide.with(this)
                .load(hero.bannerUrl == null || hero.bannerUrl.isEmpty() ? hero.posterUrl : hero.bannerUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(ivHero);

        ivHero.setOnClickListener(v -> openDetail(hero));
    }

    private void filterMovies(String keyword) {
        if (keyword.trim().isEmpty()) {
            movieAdapter.setMovies(allMovies);
            return;
        }

        ArrayList<Movie> filtered = new ArrayList<>();
        String lower = keyword.toLowerCase();

        for (Movie movie : allMovies) {
            if (movie.title != null && movie.title.toLowerCase().contains(lower)) {
                filtered.add(movie);
            }
        }

        movieAdapter.setMovies(filtered);
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
    }
}
