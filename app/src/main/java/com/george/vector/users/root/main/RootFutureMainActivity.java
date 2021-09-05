package com.george.vector.users.root.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.root.main.fragments.FragmentProfile;
import com.george.vector.users.root.main.fragments.tasks.FragmentTasks;
import com.george.vector.users.root.main.fragments.home.FragmentHome;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RootFutureMainActivity extends AppCompatActivity {

    BottomNavigationView bottom_root_navigation;

    private static final String TAG = "RootMainActivity";
    String email;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_future_main);

        Bundle arguments = getIntent().getExtras();
        email = arguments.get(getString(R.string.email)).toString();

        // Дефолтный запущенный фрагмент home.
        if (savedInstanceState == null) {
            Fragment fragmentHome = new FragmentHome();
            Bundle email = new Bundle();
            email.putString(getString(R.string.email), this.email);
            fragmentHome.setArguments(email);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_future_main_root, fragmentHome).commit();
        }

        bottom_root_navigation = findViewById(R.id.bottom_root_navigation);
        bottom_root_navigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.d(TAG, "Start home root fragment");
                    selectedFragment = new FragmentHome();

                    Bundle email = new Bundle();
                    email.putString(getString(R.string.email), this.email);
                    selectedFragment.setArguments(email);

                    break;

                case R.id.nav_tasks:
                    Log.d(TAG, "Start tasks root fragment");
                    selectedFragment = new FragmentTasks();

                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.email), this.email);
                    selectedFragment.setArguments(bundle);

                    break;

                case R.id.nav_profile:
                    Log.d(TAG, "Start profile root fragment");
                    selectedFragment = new FragmentProfile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_future_main_root, selectedFragment).commit();

            return true;
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}