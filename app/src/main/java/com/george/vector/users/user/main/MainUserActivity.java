package com.george.vector.users.user.main;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.PERMISSION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.users.user.main.fragments.FragmentHistory;
import com.george.vector.users.user.main.fragments.FragmentHome;
import com.george.vector.users.user.main.fragments.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserActivity extends AppCompatActivity {

    private static final String TAG = "MainUserActivity";

    String permission, email, collection;
    BottomNavigationView bottom_user_navigation;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        bottom_user_navigation = findViewById(R.id.bottom_user_navigation);

        Bundle arguments = getIntent().getExtras();
        email = arguments.getString(EMAIL);
        permission = arguments.getString(PERMISSION);
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

        bottom_user_navigation.setOnNavigationItemSelectedListener(item -> {
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

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "nope!");
    }

}