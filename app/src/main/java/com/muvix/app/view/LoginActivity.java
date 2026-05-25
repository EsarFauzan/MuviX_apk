package com.muvix.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.muvix.app.R;
import com.muvix.app.model.AuthManager;

public class LoginActivity extends AppCompatActivity {
    private AuthManager authManager;
    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);
        if (authManager.isLoggedIn()) {
            openMainAndFinish();
            return;
        }

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView btnGoRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.page_enter, R.anim.page_exit);
        });
    }

    private void attemptLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Email tidak valid");
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("Password minimal 6 karakter");
            return;
        }

        boolean success = authManager.login(email, password);
        if (!success) {
            Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
            return;
        }

        openMainAndFinish();
    }

    private void openMainAndFinish() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

