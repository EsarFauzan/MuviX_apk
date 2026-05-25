package com.muvix.app.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

public class AuthManager {
    private static final String PREF_NAME = "muvix_auth";
    private static final String KEY_USERS = "users";
    private static final String KEY_SESSION_EMAIL = "session_email";

    private final SharedPreferences prefs;

    public AuthManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean register(String name, String email, String password) {
        String safeName = normalize(name);
        String safeEmail = normalize(email).toLowerCase();
        String safePassword = normalize(password);

        if (safeName.isEmpty() || safeEmail.isEmpty() || safePassword.length() < 6) {
            return false;
        }

        try {
            JSONObject users = getUsersJson();
            if (users.has(safeEmail)) {
                return false;
            }

            JSONObject user = new JSONObject();
            user.put("name", safeName);
            user.put("password", safePassword);
            users.put(safeEmail, user);

            prefs.edit().putString(KEY_USERS, users.toString()).apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean login(String email, String password) {
        String safeEmail = normalize(email).toLowerCase();
        String safePassword = normalize(password);

        try {
            JSONObject users = getUsersJson();
            JSONObject user = users.optJSONObject(safeEmail);
            if (user == null) return false;

            String storedPassword = user.optString("password", "");
            if (!storedPassword.equals(safePassword)) return false;

            prefs.edit().putString(KEY_SESSION_EMAIL, safeEmail).apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoggedIn() {
        String email = prefs.getString(KEY_SESSION_EMAIL, "");
        return email != null && !email.trim().isEmpty();
    }

    public void logout() {
        prefs.edit().remove(KEY_SESSION_EMAIL).apply();
    }

    public String getCurrentName() {
        String email = getCurrentEmail();
        if (email.isEmpty()) return "Guest";

        try {
            JSONObject user = getUsersJson().optJSONObject(email);
            if (user == null) return "Guest";
            return user.optString("name", "Guest");
        } catch (Exception e) {
            return "Guest";
        }
    }

    public String getCurrentEmail() {
        String email = prefs.getString(KEY_SESSION_EMAIL, "");
        return email == null ? "" : email.trim().toLowerCase();
    }

    private JSONObject getUsersJson() {
        String raw = prefs.getString(KEY_USERS, "");
        if (raw == null || raw.trim().isEmpty()) {
            return new JSONObject();
        }

        try {
            return new JSONObject(raw);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}

