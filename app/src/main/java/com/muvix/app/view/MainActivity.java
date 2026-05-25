package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.muvix.app.model.AuthManager;
import com.muvix.app.model.Movie;
import com.muvix.app.view.adapter.ContinueAdapter;
import com.muvix.app.view.adapter.HomeMovieAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MovieController controller;
    private AuthManager authManager;
    private HomeMovieAdapter movieAdapter;
    private ContinueAdapter continueAdapter;
    private final ArrayList<Movie> allMovies = new ArrayList<>();

    private ImageView ivHero;
    private TextView tvHeroTitle, tvHeroMeta, tvSectionTitle, tvEmptyState, tvFilmCount;
    private TextView tvHomeUserName, tvAvatarInitial;
    private View layoutLoadingHome;
    private EditText inputSearch;
    private RecyclerView rvMovies, rvContinue;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MovieController(this);
        authManager = new AuthManager(this);
        initViews();
        setupRecyclerViews();
        setupSearch();
        setupNavigation();
        bindUserHeader();
        loadHome();
    }

    private void initViews() {
        ivHero = findViewById(R.id.ivHero);
        tvHeroTitle = findViewById(R.id.tvHeroTitle);
        tvHeroMeta = findViewById(R.id.tvHeroMeta);
        tvSectionTitle = findViewById(R.id.tvSectionTitle);
        tvFilmCount = findViewById(R.id.tvFilmCount);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvHomeUserName = findViewById(R.id.tvHomeUserName);
        tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
        inputSearch = findViewById(R.id.inputSearch);
        rvMovies = findViewById(R.id.rvMovies);
        rvContinue = findViewById(R.id.rvContinue);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        layoutLoadingHome = findViewById(R.id.layoutLoadingHome);
    }

    private void bindUserHeader() {
        String name = authManager.getCurrentName();
        if (name == null || name.trim().isEmpty()) {
            name = "Explorer";
        }
        tvHomeUserName.setText("Halo, " + name);

        String initial = String.valueOf(Character.toUpperCase(name.charAt(0)));
        tvAvatarInitial.setText(initial);
    }

    private void setupRecyclerViews() {
        movieAdapter = new HomeMovieAdapter(this::openDetail);

        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setHasFixedSize(false);
        rvMovies.setNestedScrollingEnabled(false);
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
                navigateTo(ProfileActivity.class);
                return true;
            }

            return false;
        });
    }

    private void loadHome() {
        tvSectionTitle.setText("Explore Movies");
        layoutLoadingHome.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        controller.loadMovies(new MovieController.MovieCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                layoutLoadingHome.setVisibility(View.GONE);
                allMovies.clear();

                if (movies != null) {
                    allMovies.addAll(movies);
                }

                showMovies(allMovies, "Belum ada film tersedia");

                if (!allMovies.isEmpty()) {
                    setupHero(allMovies);

                    ArrayList<Movie> continueMovies = new ArrayList<>(
                            allMovies.subList(0, Math.min(4, allMovies.size()))
                    );
                    continueAdapter.setMovies(continueMovies);
                } else {
                    continueAdapter.setMovies(new ArrayList<>());
                }
            }

            @Override
            public void onError(String message) {
                layoutLoadingHome.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupHero(ArrayList<Movie> movies) {
        if (movies.isEmpty()) return;

        Movie hero = movies.get(0);
        tvHeroTitle.setText(hero.title);
        tvHeroMeta.setText(String.format(Locale.getDefault(), "Rating %.1f | %s | %s", hero.rating, hero.genre, hero.duration));

        Glide.with(this)
                .load(ImageSourceResolver.resolve(
                        this,
                        hero.bannerUrl == null || hero.bannerUrl.isEmpty() ? hero.posterUrl : hero.bannerUrl
                ))
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(ivHero);

        ivHero.setOnClickListener(v -> openDetail(hero));
    }

    private void filterMovies(String keyword) {
        String query = keyword.trim().toLowerCase();

        if (query.isEmpty()) {
            tvSectionTitle.setText("Explore Movies");
            showMovies(allMovies, "Belum ada film tersedia");
            return;
        }

        tvSectionTitle.setText("Hasil Pencarian");

        ArrayList<Movie> filtered = new ArrayList<>();

        for (Movie movie : allMovies) {
            String title = movie.title != null ? movie.title.toLowerCase() : "";
            String genre = movie.genre != null ? movie.genre.toLowerCase() : "";
            String episode = movie.episode != null ? movie.episode.toLowerCase() : "";

            if (title.contains(query) || genre.contains(query) || episode.contains(query)) {
                filtered.add(movie);
            }
        }

        showMovies(filtered, "Film \"" + keyword + "\" tidak ditemukan");
    }

    private void showMovies(ArrayList<Movie> movies, String emptyMessage) {
        movieAdapter.setMovies(movies);
        tvFilmCount.setText("(" + (movies != null ? movies.size() : 0) + " film)");

        if (movies == null || movies.isEmpty()) {
            rvMovies.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText(emptyMessage);
        } else {
            rvMovies.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }
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
        TabNavigationHelper.navigate(this, MainActivity.class, target);
    }
}
