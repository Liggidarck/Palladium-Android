package com.george.vector.ui.users.root.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityListUsersBinding;
import com.george.vector.ui.adapter.UserAdapter;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class ListUsersActivity extends AppCompatActivity {

    private final UserAdapter userAdapter = new UserAdapter();

    private UserViewModel userViewModel;

    private ActivityListUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityListUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarListUsers.setNavigationOnClickListener(v -> onBackPressed());

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(UserViewModel.class);

        setUpList("ROLE_ADMIN");

        binding.chipAdmin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUpList("ROLE_ADMIN");
            }
        });

        binding.chipWorker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUpList("ROLE_EXECUTOR");
            }
        });

        binding.chipUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUpList("ROLE_USER");
            }
        });

        binding.chipDeveloper.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUpList("ROLE_DEVELOPER");
            }
        });

        binding.recyclerViewListUsers.setHasFixedSize(true);
        binding.recyclerViewListUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewListUsers.setAdapter(userAdapter);

        userAdapter.setOnItemClickListener((user, position) -> {
            long id = user.getId();
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra("user_id", id);
            startActivity(intent);
        });

    }

    private void setUpList(String role) {
        binding.progressUsers.setVisibility(View.VISIBLE);
        userViewModel.getUsersByRoleName(role).observe(this, users -> {
            if(users == null) {
                binding.progressUsers.setVisibility(View.INVISIBLE);
                return;
            }

            userAdapter.setUsers(users);
            binding.progressUsers.setVisibility(View.INVISIBLE);

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        setUpList("ROLE_ADMIN");
    }
}