package com.muvix.app.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.muvix.app.R;
import com.muvix.app.controller.MovieController;
import com.muvix.app.model.Movie;
import com.muvix.app.model.SampleData;

import java.util.Locale;

public class PlayerActivity extends AppCompatActivity {
    private static final String SWAPPED_DRIVE_PREVIEW_URL =
            "https://drive.google.com/file/d/198fLbZ0bnRfQT3Ibahzkqm-slH-UMdTs/preview";

    private Movie movie;
    private MovieController controller;

    private ProgressBar progressPlayer;
    private TextView tvPlayerTime;
    private TextView btnPlayPause;
    private WebView webPlayer;
    private View simulationInfo;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private int progress = 0;
    private boolean isPlaying = true;

    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying && progress < 100) {
                progress++;
                progressPlayer.setProgress(progress);
                tvPlayerTime.setText(formatProgress(progress) + " / " + movie.duration);
            }

            handler.postDelayed(this, 700);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        controller = new MovieController(this);
        movie = getMovieFromIntent();

        ImageView ivPlayerBanner = findViewById(R.id.ivPlayerBanner);
        TextView btnBack = findViewById(R.id.btnBack);
        TextView tvPlayerTitle = findViewById(R.id.tvPlayerTitle);
        TextView tvPlayerMeta = findViewById(R.id.tvPlayerMeta);
        MaterialButton btnFinishWatch = findViewById(R.id.btnFinishWatch);
        simulationInfo = findViewById(R.id.tvPlayerInfo);

        progressPlayer = findViewById(R.id.progressPlayer);
        tvPlayerTime = findViewById(R.id.tvPlayerTime);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        webPlayer = findViewById(R.id.webPlayer);

        tvPlayerTitle.setText(movie.title);
        tvPlayerMeta.setText(String.format(Locale.getDefault(), "Rating %.1f | %s | %s", movie.rating, movie.genre, movie.duration));
        tvPlayerTime.setText("00:00 / " + movie.duration);

        Glide.with(this)
                .load(ImageSourceResolver.resolve(
                        this,
                        movie.bannerUrl == null || movie.bannerUrl.isEmpty() ? movie.posterUrl : movie.bannerUrl
                ))
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(ivPlayerBanner);

        controller.saveToHistory(movie);

        btnBack.setOnClickListener(v -> finishWithAnimation());

        btnPlayPause.setOnClickListener(v -> {
            isPlaying = !isPlaying;

            if (isPlaying) {
                btnPlayPause.setText("||");
            } else {
                btnPlayPause.setText(">");
            }
        });

        btnFinishWatch.setOnClickListener(v -> finishWithAnimation());

        if (SampleData.SWAPPED_ID.equals(movie.id)) {
            setupDrivePlayer(ivPlayerBanner);
        } else {
            handler.post(progressRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(progressRunnable);
        if (webPlayer != null) {
            webPlayer.loadUrl("about:blank");
            webPlayer.stopLoading();
            webPlayer.destroy();
            webPlayer = null;
        }
    }

    private void setupDrivePlayer(ImageView ivPlayerBanner) {
        ivPlayerBanner.setVisibility(View.GONE);
        btnPlayPause.setVisibility(View.GONE);
        progressPlayer.setVisibility(View.GONE);
        tvPlayerTime.setVisibility(View.GONE);
        simulationInfo.setVisibility(View.GONE);

        webPlayer.setVisibility(View.VISIBLE);
        WebSettings settings = webPlayer.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webPlayer.setWebChromeClient(new WebChromeClient());
        webPlayer.loadUrl(SWAPPED_DRIVE_PREVIEW_URL);
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.page_back_enter, R.anim.page_back_exit);
    }

    private String formatProgress(int value) {
        int totalMinutes = value;
        int minute = totalMinutes / 60;
        int second = totalMinutes % 60;

        return String.format("%02d:%02d", minute, second);
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

        if (m.duration == null || m.duration.isEmpty()) {
            m.duration = "120 min";
        }

        return m;
    }
}
