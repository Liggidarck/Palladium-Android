package com.george.vector.admin.edit_users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.user.MainUserActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListUsersActivity extends AppCompatActivity {

    RecyclerView recycler_view_list_users;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query = usersRef.whereEqualTo("name", "Георгий");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserAdapter(options);

        recycler_view_list_users = findViewById(R.id.recycler_view_list_users);
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
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}