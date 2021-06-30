package com.george.vector.common.edit_users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    Chip chip_root, chip_zav, chip_admin, chip_user, chip_worker;
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

        chip_root = findViewById(R.id.chip_root);
        chip_zav = findViewById(R.id.chip_zav);
        chip_admin = findViewById(R.id.chip_admin);
        chip_user = findViewById(R.id.chip_user);
        chip_worker = findViewById(R.id.chip_worker);

        recycler_view_list_users = findViewById(R.id.recycler_view_list_users);
        toolbar_list_users = findViewById(R.id.toolbar_list_users);
        refresh_users = findViewById(R.id.refresh_users);

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

        chip_zav.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Root checked");

                query = usersRef.whereEqualTo("role", "Завхоз");

                FirestoreRecyclerOptions<User> AdminOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(AdminOptions);
            }

        });

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
        query = usersRef.whereEqualTo("role", "Root");

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