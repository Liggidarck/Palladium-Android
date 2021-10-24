package com.george.vector.users.user.main.fragments;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.PERMISSION;

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
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.user.tasks.AddTaskUserActivity;
import com.george.vector.users.user.tasks.TaskUserActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentHome extends Fragment {

    ExtendedFloatingActionButton create_task_btn;
    RecyclerView recyclerview_view_user;

    private static final String TAG = "MainUserFragment";
    String permission, collection, email;

    TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        create_task_btn = view.findViewById(R.id.create_task_user);
        recyclerview_view_user = view.findViewById(R.id.recyclerview_view_user);

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);
        permission = args.getString(PERMISSION);
        collection = args.getString(COLLECTION);

        Log.d(TAG, "email: " + email);
        Log.d(TAG, "permission: " + permission);
        Log.d(TAG, "collection: " + collection);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference taskRef = db.collection(collection);

        Query query = taskRef.whereEqualTo("email_creator", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        adapter = new TaskAdapter(options);

        recyclerview_view_user.setHasFixedSize(true);
        recyclerview_view_user.setLayoutManager(new LinearLayoutManager(FragmentHome.this.getContext()));
        recyclerview_view_user.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentHome.this.getContext(), TaskUserActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });

        create_task_btn.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentHome.this.getContext(), AddTaskUserActivity.class);
            intent.putExtra(PERMISSION, permission);
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
}
