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

public class RegisterActivity extends AppCompatActivity {
    private AuthManager authManager;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authManager = new AuthManager(this);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        TextView btnGoLogin = findViewById(R.id.btnGoLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        btnGoLogin.setOnClickListener(v -> finishWithAnimation());
    }

    private void attemptRegister() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString();
        String confirm = inputConfirmPassword.getText().toString();

        if (name.length() < 2) {
            inputName.setError("Nama minimal 2 karakter");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Email tidak valid");
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("Password minimal 6 karakter");
            return;
        }

        if (!password.equals(confirm)) {
            inputConfirmPassword.setError("Konfirmasi password tidak sama");
            return;
        }

        boolean created = authManager.register(name, email, password);
        if (!created) {
            Toast.makeText(this, "Akun sudah ada atau data tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Register berhasil, silakan login.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.page_back_enter, R.anim.page_back_exit);
    }
}
