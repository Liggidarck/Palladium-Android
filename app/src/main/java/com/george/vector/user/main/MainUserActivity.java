package com.george.vector.user.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.bottom_sheets.ProfileBottomSheet;
import com.george.vector.common.bottom_sheets.SettingsUserBottomSheet;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.user.tasks.AddTaskUserActivity;
import com.george.vector.user.tasks.TaskUserActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainUserActivity extends AppCompatActivity {

    private static final String TAG = "MainUserActivity";
    FloatingActionButton fab_add_user;
    BottomAppBar bottomAppBar;

    FirebaseFirestore db;
    CollectionReference taskRef;

    private TaskAdapter adapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        Bundle arguments = getIntent().getExtras();
        String email = arguments.get("email").toString();
        permission = arguments.getString("permission");
        String collection = null;

        bottomAppBar = findViewById(R.id.bottomAppBarUser);
        fab_add_user = findViewById(R.id.fab_add_user);

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setSupportActionBar(bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(v -> {
            SettingsUserBottomSheet bottomSheet = new SettingsUserBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "SettingsUserBottomSheet");
        });

        fab_add_user.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskUserActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        });

        if(permission.equals("ost_school"))
            collection = "ost_school_new";

        if(permission.equals("bar_school"))
            collection = "bar_school_new";

        setUpRecyclerView(email, collection);
    }

    private void setUpRecyclerView(String email, String collection) {
        taskRef = db.collection(collection);

        Query query = taskRef.whereEqualTo("email_creator", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_view_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            TaskUi task = documentSnapshot.toObject(TaskUi.class);
            String id = documentSnapshot.getId();
            String path = documentSnapshot.getReference().getPath();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(this, TaskUserActivity.class);
            intent.putExtra("id_task", id);
            intent.putExtra("permission", permission);
            startActivity(intent);

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile_item){
            ProfileBottomSheet bottomSheet = new ProfileBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ProfileBottomSheet");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "nope!");
    }
}