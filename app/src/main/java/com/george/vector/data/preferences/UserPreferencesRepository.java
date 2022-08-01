package com.george.vector.data.preferences;

import static com.george.vector.common.utils.consts.Keys.USER_NOTIFICATIONS_OPTIONS;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_PASSWORD;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_PERMISSION;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_ROLE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.george.vector.network.model.User;

public class UserPreferencesRepository implements UserPreferencesBehaviour{


    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public UserPreferencesRepository(Application application) {
        sharedPreferences = application.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void saveUser(User user) {
        editor.putString(USER_PREFERENCES_NAME, user.getName());
        editor.putString(USER_PREFERENCES_LAST_NAME, user.getLast_name());
        editor.putString(USER_PREFERENCES_PATRONYMIC, user.getPatronymic());
        editor.putString(USER_PREFERENCES_EMAIL, user.getEmail());
        editor.putString(USER_PREFERENCES_ROLE, user.getRole());
        editor.putString(USER_PREFERENCES_PERMISSION, user.getPermission());
        editor.putString(USER_PREFERENCES_PASSWORD, user.getPassword());
        editor.putBoolean(USER_NOTIFICATIONS_OPTIONS, false);
        editor.apply();
    }

    @Override
    public User getUser() {
        String name = sharedPreferences.getString(USER_PREFERENCES_NAME, "");
        String lastname = sharedPreferences.getString(USER_PREFERENCES_LAST_NAME, "");
        String patronymic = sharedPreferences.getString(USER_PREFERENCES_PATRONYMIC, "");
        String email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");
        String role = sharedPreferences.getString(USER_PREFERENCES_ROLE, "");
        String permission = sharedPreferences.getString(USER_PREFERENCES_PERMISSION, "");
        String password = sharedPreferences.getString(USER_PREFERENCES_PASSWORD, "");
        return new User(name, lastname, patronymic, email, role, permission, password);
    }

    @Override
    public boolean getNotifications() {
        return sharedPreferences.getBoolean(USER_NOTIFICATIONS_OPTIONS, false);
    }

    @Override
    public void setNotifications(boolean notifications) {
        editor.putBoolean(USER_NOTIFICATIONS_OPTIONS, notifications);
        editor.apply();
    }

}
