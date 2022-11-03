package com.george.vector.data.preferences;

import com.george.vector.network.model.User;

public interface UserPreferencesBehaviour {

    void saveUser(User user);

    User getUser();

    void saveToken(String token);

    String getToken();

    boolean getNotifications();

    void setNotifications(boolean notifications);

}
