package com.george.vector.admin.edit_users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListUsersActivity extends AppCompatActivity {

    private static final String TAG = "ListUserActivity";

    RecyclerView recycler_view_list_users;
    Chip chip_admin, chip_user, chip_worker;
    ChipGroup chip_group;
    MaterialToolbar toolbar_list_users;
    SwipeRefreshLayout refresh_users;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    private UserAdapter adapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        chip_admin = findViewById(R.id.chip_admin);
        chip_user = findViewById(R.id.chip_user);
        chip_worker = findViewById(R.id.chip_worker);
        chip_group =findViewById(R.id.chip_group);
        recycler_view_list_users = findViewById(R.id.recycler_view_list_users);
        toolbar_list_users = findViewById(R.id.toolbar_list_users);
        refresh_users = findViewById(R.id.refresh_users);

        toolbar_list_users.setNavigationOnClickListener(v -> onBackPressed());

        setUpRecyclerView();

        chip_admin.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Admin checked");

                query = usersRef.whereEqualTo("role", "Администратор");

                FirestoreRecyclerOptions<User> AdminOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(AdminOptions);
            }

        });

        chip_user.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "user checked");

                query = usersRef.whereEqualTo("role", "Пользователь");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }

        });

        chip_worker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "executor checked");

                query = usersRef.whereEqualTo("role", "Исполнитель");

                FirestoreRecyclerOptions<User> ExecutorOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(ExecutorOptions);
            }
        });

    }

    private void setUpRecyclerView() {
        query = usersRef.whereEqualTo("role", "Администратор");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserAdapter(options);


        recycler_view_list_users.setHasFixedSize(true);
        recycler_view_list_users.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_list_users.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
           User user = documentSnapshot.toObject(User.class);
           String id = documentSnapshot.getId();
           String path = documentSnapshot.getReference().getPath();
            Toast.makeText(ListUsersActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra("user_id", id);
            startActivity(intent);

        });

    }

    @Override
    protected void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}