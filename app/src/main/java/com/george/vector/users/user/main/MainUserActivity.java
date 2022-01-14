package com.george.vector.users.user.main;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_ROLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityMainUserBinding;
import com.george.vector.users.user.main.fragments.FragmentHistory;
import com.george.vector.users.user.main.fragments.FragmentHome;
import com.george.vector.users.user.main.fragments.FragmentProfile;

public class MainUserActivity extends AppCompatActivity {

    ActivityMainUserBinding mainUserBinding;
    private static final String TAG = "MainUserActivity";
    String permission, email, collection;

    SharedPreferences mDataUser;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        mainUserBinding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(mainUserBinding.getRoot());

        mDataUser = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        String name_user = mDataUser.getString(USER_PREFERENCES_NAME, "");
        String last_name_user = mDataUser.getString(USER_PREFERENCES_LAST_NAME, "");
        String patronymic_user = mDataUser.getString(USER_PREFERENCES_PATRONYMIC, "");
        email = mDataUser.getString(USER_PREFERENCES_EMAIL, "");
        String role_user = mDataUser.getString(USER_PREFERENCES_ROLE, "");
        permission = mDataUser.getString(USER_PREFERENCES_PERMISSION, "");
        collection = null;

        if (permission.equals(OST_SCHOOL))
            collection = OST_SCHOOL_NEW;

        if (permission.equals(BAR_SCHOOL))
            collection = BAR_SCHOOL_NEW;

        Log.d(TAG, "email: " + email);

        if (savedInstanceState == null) {
            Fragment fragmentHome = new FragmentHome();
            Bundle data_home_fragment = new Bundle();
            data_home_fragment.putString(EMAIL, email);
            data_home_fragment.putString(PERMISSION, permission);
            data_home_fragment.putString(COLLECTION, collection);
            fragmentHome.setArguments(data_home_fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, fragmentHome).commit();
        }

        mainUserBinding.bottomUserNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.item_home_user:
                    Log.d(TAG, "Start home user fragment");
                    selectedFragment = new FragmentHome();

                    Bundle data_home_fragment = new Bundle();
                    data_home_fragment.putString(EMAIL, email);
                    data_home_fragment.putString(PERMISSION, permission);
                    data_home_fragment.putString(COLLECTION, collection);
                    selectedFragment.setArguments(data_home_fragment);

                    break;

                case R.id.item_history_user:
                    Log.d(TAG, "Start history user fragment");
                    selectedFragment = new FragmentHistory();

                    Bundle data_history_fragment = new Bundle();
                    data_history_fragment.putString(EMAIL, email);
                    data_history_fragment.putString(PERMISSION, permission);
                    selectedFragment.setArguments(data_history_fragment);

                    break;

                case R.id.item_profile_user:
                    Log.d(TAG, "Start profile user fragment");
                    selectedFragment = new FragmentProfile();

                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, selectedFragment).commit();

            return true;
        });

    }

}