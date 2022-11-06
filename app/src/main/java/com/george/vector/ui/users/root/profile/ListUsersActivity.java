package com.george.vector.ui.users.root.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityListUsersBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.adapter.UserAdapter;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListUsersActivity extends AppCompatActivity {

    private UserAdapter adapter = new UserAdapter();

    private ActivityListUsersBinding usersBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        usersBinding = ActivityListUsersBinding.inflate(getLayoutInflater());
        setContentView(usersBinding.getRoot());

        usersBinding.toolbarListUsers.setNavigationOnClickListener(v -> onBackPressed());

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> adapter.setUsers(users));



        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        usersBinding.recyclerViewListUsers.setHasFixedSize(true);
        usersBinding.recyclerViewListUsers.setLayoutManager(new LinearLayoutManager(this));
        usersBinding.recyclerViewListUsers.setAdapter(adapter);

        adapter.setOnItemClickListener((user, position) -> {
            long id = user.getId();
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra("user_id", id);
            startActivity(intent);
        });
    }

}