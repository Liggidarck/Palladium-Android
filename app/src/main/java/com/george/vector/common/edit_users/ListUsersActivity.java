package com.george.vector.common.edit_users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListUsersActivity extends AppCompatActivity {

    private static final String TAG = "ListUserActivity";

    RecyclerView recycler_view_list_users;
    Chip chip_root, chip_user, chip_worker;
    MaterialToolbar toolbar_list_users;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    private UserAdapter adapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        chip_root = findViewById(R.id.chip_root);
        chip_user = findViewById(R.id.chip_user);
        chip_worker = findViewById(R.id.chip_worker);

        recycler_view_list_users = findViewById(R.id.recycler_view_list_users);
        toolbar_list_users = findViewById(R.id.toolbar_list_users);

        toolbar_list_users.setNavigationOnClickListener(v -> onBackPressed());

        setUpRecyclerView();

        chip_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Root checked");

                query = usersRef.whereEqualTo("role", "Root");

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
        query = usersRef.whereEqualTo("role", "Root");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserAdapter(options);

        recycler_view_list_users.setHasFixedSize(true);
        recycler_view_list_users.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_list_users.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
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