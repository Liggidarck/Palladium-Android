package com.george.vector.ui.users.root.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityListUsersBinding;
import com.george.vector.ui.adapter.UserAdapter;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class ListUsersActivity extends AppCompatActivity {

    private UserAdapter adapter = new UserAdapter();

    private UserViewModel userViewModel;

    private ActivityListUsersBinding usersBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        usersBinding = ActivityListUsersBinding.inflate(getLayoutInflater());
        setContentView(usersBinding.getRoot());

        usersBinding.toolbarListUsers.setNavigationOnClickListener(v -> onBackPressed());

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(UserViewModel.class);

        setUpList();

        setUpRecyclerView();

    }

    private void setUpList() {
        userViewModel.getAllUsers().observe(this, users -> adapter.setUsers(users));
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

    @Override
    protected void onStart() {
        super.onStart();
        setUpList();
    }
}