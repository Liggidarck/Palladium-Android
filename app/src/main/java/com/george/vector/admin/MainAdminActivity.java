package com.george.vector.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.george.vector.R;
import com.george.vector.admin.tasks.AddTaskAdminActivity;
import com.george.vector.admin.tasks.sort_by_category.FolderActivity;
import com.george.vector.common.ProfileBottomSheet;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainAdminActivity extends AppCompatActivity {

    private static final String TAG = "MainAdminActivity";

    BottomAppBar bottomAppBar;
    FloatingActionButton add;
    CardView new_tasks_card, in_progress_tasks_card, archive_tasks_card;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        new_tasks_card = findViewById(R.id.new_tasks_card);
        in_progress_tasks_card = findViewById(R.id.in_progress_tasks_card);
        archive_tasks_card = findViewById(R.id.archive_tasks_card);
        add = findViewById(R.id.fab_add);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(v -> {
            AdminBottomSheet bottomSheet = new AdminBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "AdminBottomSheet");
        });

        add.setOnClickListener(v -> startActivity(new Intent(this, AddTaskAdminActivity.class)));

        new_tasks_card.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderActivity.class);
            intent.putExtra("section", "new tasks");
            startActivity(intent);
        });

        in_progress_tasks_card.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderActivity.class);
            intent.putExtra("section", "in progress tasks");
            startActivity(intent);
        });

        archive_tasks_card.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderActivity.class);
            intent.putExtra("section", "archive tasks");
            startActivity(intent);
        });


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
        Log.i(TAG, "nope");
    }
}