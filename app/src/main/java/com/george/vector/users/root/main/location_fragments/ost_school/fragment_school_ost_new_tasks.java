package com.george.vector.users.root.main.location_fragments.ost_school;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.users.root.tasks.TaskRootActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_school_ost_new_tasks extends Fragment {

    private static final String TAG = "NewTaskOstSchool";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_new");

    private TaskAdapter adapter;
    private Query query;

    FirebaseFirestore firebaseFirestore;

    TextInputLayout text_input_search_new_tasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_ost_new_tasks, container, false);

        RecyclerView recyclerview_school_ost_new_tasks = view.findViewById(R.id.recyclerview_school_ost_new_tasks);
        text_input_search_new_tasks = view.findViewById(R.id.text_input_search_new_tasks);

        firebaseFirestore = FirebaseFirestore.getInstance();

        query = taskRef.whereEqualTo("status", "Новая заявка");

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_school_ost_new_tasks.setHasFixedSize(true);
        recyclerview_school_ost_new_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_ost_new_tasks.this.getContext()));
        recyclerview_school_ost_new_tasks.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_ost_new_tasks.this.getContext(), TaskRootActivity.class);
            intent.putExtra("id_task_root", id);
            intent.putExtra("collection", "ost_school_new");
            intent.putExtra("zone", "ost_school");
            startActivity(intent);

        });

        Objects.requireNonNull(text_input_search_new_tasks.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_value = text_input_search_new_tasks.getEditText().getText().toString();

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
        query = taskRef.whereEqualTo("status", "Новая заявка");

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
