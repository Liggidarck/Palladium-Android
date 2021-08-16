package com.george.vector.users.admin.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.george.vector.R;
import com.george.vector.users.admin.tasks.AddTaskAdminActivity;
import com.george.vector.common.bottom_sheets.ConsoleBottomSheet;
import com.george.vector.common.bottom_sheets.ProfileBottomSheet;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainAdminActivity extends AppCompatActivity {

    private static final String TAG = "MainAdminActivity";

    BottomAppBar bottomAppBar;
    FloatingActionButton add;
    CardView new_tasks_card, in_progress_tasks_card, archive_tasks_card;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Bundle arguments = getIntent().getExtras();
        permission = arguments.get( getString(R.string.permission)).toString();
        Log.d(TAG, String.format("permission: %s", permission));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        new_tasks_card = findViewById(R.id.new_tasks_card);
        in_progress_tasks_card = findViewById(R.id.in_progress_tasks_card);
        archive_tasks_card = findViewById(R.id.archive_tasks_card);
        add = findViewById(R.id.fab_add);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        setSupportActionBar(bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(v -> {
            ConsoleBottomSheet bottomSheet = new ConsoleBottomSheet();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.permission), "admin");
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), "ConsoleBottomSheet");
        });

        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskAdminActivity.class);
            intent.putExtra(getString(R.string.permission), permission);
            startActivity(intent);
        });

        new_tasks_card.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderAdminActivity.class);
            intent.putExtra(getString(R.string.folder), getString(R.string.new_tasks));
            intent.putExtra(getString(R.string.permission), permission);
            startActivity(intent);
        });

        in_progress_tasks_card.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderAdminActivity.class);
            intent.putExtra(getString(R.string.folder), getString(R.string.in_progress_tasks));
            intent.putExtra(getString(R.string.permission), permission);
            startActivity(intent);
        });

        archive_tasks_card.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderAdminActivity.class);
            intent.putExtra(getString(R.string.folder), getString(R.string.archive_tasks));
            intent.putExtra(getString(R.string.permission), permission);
            startActivity(intent);
        });

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_main_admin), "Ошибка! Проверьте подключение к интернету", Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> Log.i(TAG, "Update status: " + isOnline()))
                    .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile_item) {
            ProfileBottomSheet bottomSheet = new ProfileBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ProfileBottomSheet");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
    }
}