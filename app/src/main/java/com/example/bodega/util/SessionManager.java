package com.example.bodega.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "BodegaSessionPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_ROLE_ID = "roleId";
    private static final String KEY_ROLE_NAME = "roleName";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String username, String fullName, int roleId, String roleName) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putInt(KEY_ROLE_ID, roleId);
        editor.putString(KEY_ROLE_NAME, roleName);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, "");
    }

    public int getRoleId() {
        return pref.getInt(KEY_ROLE_ID, -1);
    }

    public String getRoleName() {
        return pref.getString(KEY_ROLE_NAME, "Operador");
    }

    public boolean isAdmin() {
        String role = getRoleName();
        return role != null && role.equalsIgnoreCase("Administrador");
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
