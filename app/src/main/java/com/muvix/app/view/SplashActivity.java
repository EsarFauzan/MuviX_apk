package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.muvix.app.R;
import com.muvix.app.model.AuthManager;

public class SplashActivity extends AppCompatActivity {
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        authManager = new AuthManager(this);

        ImageView logo = findViewById(R.id.ivSplashLogo);
        logo.setAlpha(0f);
        logo.setScaleX(0.9f);
        logo.setScaleY(0.9f);
        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Class<?> target = authManager.isLoggedIn() ? MainActivity.class : LoginActivity.class;
            startActivity(new Intent(SplashActivity.this, target));
            finish();
        }, 1500);
    }
}
