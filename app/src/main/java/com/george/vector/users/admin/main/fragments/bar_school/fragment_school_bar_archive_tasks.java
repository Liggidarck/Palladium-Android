package com.george.vector.users.admin.main.fragments.bar_school;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.users.admin.tasks.TaskAdminActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_school_bar_archive_tasks extends Fragment {

    private static final String TAG = "ArchiveTaskBarSchool";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("bar_school_archive");

    private TaskAdapter adapter;
    private Query query;

    TextInputLayout text_input_search_bar_school_archive_tasks;

    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_bar_archive_tasks, container, false);

        RecyclerView recyclerview_school_bar_new_tasks = view.findViewById(R.id.recyclerview_school_bar_archive_tasks);
        text_input_search_bar_school_archive_tasks = view.findViewById(R.id.text_input_search_bar_school_archive_tasks);

        firebaseFirestore = FirebaseFirestore.getInstance();

        query = taskRef.whereEqualTo("status", "Архив");

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_school_bar_new_tasks.setHasFixedSize(true);
        recyclerview_school_bar_new_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_bar_archive_tasks.this.getContext()));
        recyclerview_school_bar_new_tasks.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_bar_archive_tasks.this.getContext(), TaskAdminActivity.class);
            intent.putExtra((String) getText(R.string.id), id);
            intent.putExtra((String) getText(R.string.collection), getText(R.string.bar_school_archive));
            intent.putExtra((String) getText(R.string.permission), getText(R.string.bar_school));
            startActivity(intent);

        });

        Objects.requireNonNull(text_input_search_bar_school_archive_tasks.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_value = text_input_search_bar_school_archive_tasks.getEditText().getText().toString();

                if(search_value.isEmpty())
                    defaultQuery();
                else
                    search(search_value);

                return true;
            }
            return false;
        });

        return view;
    }

    private void search(String query_text) {
        query = taskRef.whereEqualTo("name_task", query_text);

        FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(search_options);
    }

    private void defaultQuery() {
        query = taskRef.whereEqualTo("status", "Архив");

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(options);
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
