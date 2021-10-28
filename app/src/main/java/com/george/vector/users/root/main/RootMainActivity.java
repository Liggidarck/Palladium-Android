package com.george.vector.users.root.main;

import static com.george.vector.common.consts.Keys.EMAIL;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityRootMainBinding;
import com.george.vector.users.root.main.fragments.FragmentProfile;
import com.george.vector.users.root.main.fragments.home.FragmentHome;
import com.george.vector.users.root.main.fragments.notifications.FragmentNotifications;
import com.george.vector.users.root.main.fragments.tasks.FragmentTasks;

public class RootMainActivity extends AppCompatActivity {

    private static final String TAG = "RootMainActivity";
    String email;

    ActivityRootMainBinding rootMainBinding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);

        rootMainBinding = ActivityRootMainBinding.inflate(getLayoutInflater());
        setContentView(rootMainBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        email = arguments.getString(EMAIL);

        if (savedInstanceState == null) {
            Fragment fragmentHome = new FragmentHome();
            Bundle email = new Bundle();
            email.putString(EMAIL, this.email);
            fragmentHome.setArguments(email);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, fragmentHome).commit();
        }

        rootMainBinding.bottomRootNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.d(TAG, "Start home root fragment");
                    selectedFragment = new FragmentHome();

                    Bundle email = new Bundle();
                    email.putString(EMAIL, this.email);
                    selectedFragment.setArguments(email);

                    break;

                case R.id.nav_tasks:
                    Log.d(TAG, "Start tasks root fragment");
                    selectedFragment = new FragmentTasks();

                    Bundle bundle = new Bundle();
                    bundle.putString(EMAIL, this.email);
                    selectedFragment.setArguments(bundle);

                    break;

                case R.id.nav_notifications:
                    Log.d(TAG, "Start notifications root fragment");
                    selectedFragment = new FragmentNotifications();
                    break;

                case R.id.nav_profile:
                    Log.d(TAG, "Start profile root fragment");
                    selectedFragment = new FragmentProfile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, selectedFragment).commit();

            return true;
        });

    }

    @Override
    public void onBackPressed() {

    }
}