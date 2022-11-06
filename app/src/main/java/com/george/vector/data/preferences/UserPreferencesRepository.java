package com.george.vector.data.preferences;

import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.TOKEN;
import static com.george.vector.common.utils.consts.Keys.USER_NOTIFICATIONS_OPTIONS;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_PASSWORD;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_ROLE;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_USERNAME;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_ZONE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.george.vector.network.model.Role;
import com.george.vector.network.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserPreferencesRepository implements UserPreferencesBehaviour{

    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public UserPreferencesRepository(Application application) {
        sharedPreferences = application.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void saveUser(User user) {
        List<Role> roles = user.getRole();

        editor.putString(USER_PREFERENCES_NAME, user.getName());
        editor.putString(USER_PREFERENCES_LAST_NAME, user.getLastName());
        editor.putString(USER_PREFERENCES_PATRONYMIC, user.getPatronymic());
        editor.putString(USER_PREFERENCES_ROLE, roles.get(0).getName());
        editor.putString(USER_PREFERENCES_EMAIL, user.getEmail());
        editor.putString(USER_PREFERENCES_ZONE, user.getZone());
        editor.putString(USER_PREFERENCES_PASSWORD, user.getPassword());
        editor.putString(USER_PREFERENCES_USERNAME, user.getUsername());

        editor.apply();
    }

    @Override
    public User getUser() {
        List<Role> roles = new ArrayList<>();

        String name = sharedPreferences.getString(USER_PREFERENCES_NAME, null);
        String lastName = sharedPreferences.getString(USER_PREFERENCES_LAST_NAME, null);
        String patronymic = sharedPreferences.getString(USER_PREFERENCES_PATRONYMIC, null);
        String role = sharedPreferences.getString(USER_PREFERENCES_ROLE, null);
        String email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, null);
        String zone = sharedPreferences.getString(USER_PREFERENCES_ZONE, null);
        String password = sharedPreferences.getString(USER_PREFERENCES_PASSWORD, null);
        String username = sharedPreferences.getString(USER_PREFERENCES_USERNAME, null);

        roles.add(new Role(0, role));

        return new User(zone, name, lastName, patronymic, email, password, username, roles);
    }

    @Override
    public void saveId(long id) {
        editor.putLong(ID, id);
        editor.apply();
    }

    @Override
    public long getId() {
        return sharedPreferences.getLong(ID, 0);
    }

    @Override
    public void saveToken(String token) {
        editor.putString(TOKEN, token);
        editor.apply();
    }

    @Override
    public String getToken() {
        return sharedPreferences.getString(TOKEN, null);
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
