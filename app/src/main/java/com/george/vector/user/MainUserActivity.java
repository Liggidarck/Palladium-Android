package com.george.vector.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.ActivityProfile;
import com.george.vector.common.Task;
import com.george.vector.common.TaskAdapter;
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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef = db.collection("new tasks");

    private TaskAdapter adapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    static String MAIN_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        bottomAppBar = findViewById(R.id.bottomAppBarUser);
        fab_add_user = findViewById(R.id.fab_add_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setSupportActionBar(bottomAppBar);
        fab_add_user.setOnClickListener(v -> startActivity(new Intent(this, AddTaskUserActivity.class)));

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Bundle arguments = getIntent().getExtras();
        MAIN_EMAIL = arguments.get("email").toString();
        Log.i(TAG, "email - " + MAIN_EMAIL);

        Query query = taskRef.whereEqualTo("email_creator", MAIN_EMAIL);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_view_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Task task = documentSnapshot.toObject(Task.class);
            String id = documentSnapshot.getId();
            String path = documentSnapshot.getReference().getPath();
            Toast.makeText(MainUserActivity.this, "Position: " + position + "ID: " + id, Toast.LENGTH_SHORT).show();
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
        if(item.getItemId() == R.id.profile_item)
            startActivity(new Intent(this, ActivityProfile.class));

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "nope!");
    }
}