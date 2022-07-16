package com.george.vector.ui.users.user.main.fragments.home;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Logs.TAG_HOME_USER_FRAGMENT;

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
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.network.model.Task;
import com.george.vector.databinding.FragmentUserHomeBinding;
import com.george.vector.ui.users.user.tasks.AddTaskUserActivity;
import com.george.vector.ui.users.user.tasks.TaskUserActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentHome extends Fragment {

    FragmentUserHomeBinding homeBinding;
    String permission, collection, email;

    TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding = FragmentUserHomeBinding.inflate(inflater, container, false);
        View view = homeBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);
        permission = args.getString(PERMISSION);
        collection = args.getString(COLLECTION);

        Log.d(TAG_HOME_USER_FRAGMENT, "email: " + email);
        Log.d(TAG_HOME_USER_FRAGMENT, "permission: " + permission);
        Log.d(TAG_HOME_USER_FRAGMENT, "collection: " + collection);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference taskRef = db.collection(collection);

        homeBinding.homeToolbarUser.setNavigationOnClickListener(v -> {
            BottomSheetProfileUser bottomSheet = new BottomSheetProfileUser();
            bottomSheet.show(getParentFragmentManager(), "ProfileUserBottomSheet");
        });

        Query query = taskRef.whereEqualTo("email_creator", email);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        adapter = new TaskAdapter(options);

        homeBinding.recyclerviewViewUser.setHasFixedSize(true);
        homeBinding.recyclerviewViewUser.setLayoutManager(new LinearLayoutManager(FragmentHome.this.getContext()));
        homeBinding.recyclerviewViewUser.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_HOME_USER_FRAGMENT, String.format("Position: %d ID: %s", position, id));

            Intent intent = new Intent(FragmentHome.this.getContext(), TaskUserActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        homeBinding.createTaskUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentHome.this.getContext(), AddTaskUserActivity.class);
            intent.putExtra(PERMISSION, permission);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }
}
