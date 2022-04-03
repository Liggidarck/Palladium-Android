package com.george.vector.users.user.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Logs.TAG_TASK_USER_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.databinding.FragmentTasksUserBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksUser extends Fragment {

    TaskAdapter taskAdapter;
    FragmentTasksUserBinding userBinding;

    String folder, email, permission, collection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userBinding = FragmentTasksUserBinding.inflate(inflater, container, false);
        View view = userBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);
        permission = args.getString(PERMISSION);
        collection = args.getString(COLLECTION);
        folder = args.getString(FOLDER);

        Log.d(TAG_TASK_USER_ACTIVITY, "email: " + email);
        Log.d(TAG_TASK_USER_ACTIVITY, "permission: " + permission);
        Log.d(TAG_TASK_USER_ACTIVITY, "collection: " + collection);
        Log.d(TAG_TASK_USER_ACTIVITY, "folder: " + folder);

        if(permission.equals(BAR_SCHOOL)) {
            userBinding.chipNewSchoolTasksUser.setVisibility(View.INVISIBLE);
            userBinding.chipOldSchoolTasksUser.setVisibility(View.INVISIBLE);
        }

        setUpTasks(collection, email);

        return view;
    }

    void setUpRecyclerView() {
        userBinding.recyclerviewUserTasks.setHasFixedSize(true);
        userBinding.recyclerviewUserTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksUser.this.getContext()));
        userBinding.recyclerviewUserTasks.setAdapter(taskAdapter);
    }

    void setUpTasks(String collection, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference taskRef = db.collection(collection);

        Query query = taskRef.whereEqualTo("email_creator", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        taskAdapter = new TaskAdapter(options);

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_USER_ACTIVITY, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksUser.this.getContext(), TaskUserActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EMAIL, email);
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
