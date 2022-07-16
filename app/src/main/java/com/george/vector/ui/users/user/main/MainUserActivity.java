package com.george.vector.ui.users.user.main;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.ui.auth.LoginActivity;
import com.george.vector.databinding.ActivityMainUserBinding;
import com.george.vector.ui.users.user.main.fragments.FragmentHistory;
import com.george.vector.ui.users.user.main.fragments.home.FragmentHome;
import com.george.vector.ui.users.user.main.fragments.FragmentHelp;
import com.george.vector.ui.users.user.main.fragments.home.BottomSheetProfileUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainUserActivity extends AppCompatActivity implements BottomSheetProfileUser.StateListener {

    FirebaseAuth firebase_auth;

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

        firebase_auth = FirebaseAuth.getInstance();

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
                    selectedFragment = new FragmentHelp();

                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, selectedFragment).commit();

            return true;
        });

    }

    @Override
    public void getButton(String button) {
        mDataUser = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mDataUser.edit();

        if (button.equals("logoutBtnUser")) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("ok", (dialog1, which) -> {
                        firebase_auth.signOut();

                        editor.putString(USER_PREFERENCES_NAME, "");
                        editor.putString(USER_PREFERENCES_LAST_NAME, "");
                        editor.putString(USER_PREFERENCES_PATRONYMIC, "");
                        editor.putString(USER_PREFERENCES_EMAIL, "");
                        editor.putString(USER_PREFERENCES_ROLE, "");
                        editor.putString(USER_PREFERENCES_PERMISSION, "");
                        editor.apply();

                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();
        }
    }
}