package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Logs.TAG_TASK_USER_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentTasksUserBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksUser extends Fragment {

    TaskAdapter taskAdapter;
    FragmentTasksUserBinding userBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userBinding = FragmentTasksUserBinding.inflate(inflater, container, false);
        View view = userBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        String email = userDataViewModel.getUser().getEmail();
        String permission = userDataViewModel.getUser().getPermission();
        String folder = args.getString(FOLDER);

        if(permission.equals(BAR_SCHOOL)) {
            userBinding.chipNewSchoolTasksUser.setVisibility(View.INVISIBLE);
            userBinding.chipOldSchoolTasksUser.setVisibility(View.INVISIBLE);
        }

        setUpTasks(permission, email, folder);

        return view;
    }

    void setUpRecyclerView() {
        userBinding.recyclerviewUserTasks.setHasFixedSize(true);
        userBinding.recyclerviewUserTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksUser.this.getContext()));
        userBinding.recyclerviewUserTasks.setAdapter(taskAdapter);
    }

    void setUpTasks(String collection, String email, String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(collection);

        Query query = collectionReference
                .whereEqualTo("email_creator", email)
                .whereEqualTo("status", status);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        taskAdapter = new TaskAdapter(options);

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_USER_ACTIVITY, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksUser.this.getContext(), TaskUserActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            startActivity(intent);

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userBinding = null;
    }

}
