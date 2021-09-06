package com.george.vector.users.root.folders.location_fragments.bar_school;

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
import com.george.vector.common.utils.Utils;
import com.george.vector.users.root.tasks.TaskRootActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_school_bar_new_tasks extends Fragment {

    private static final String TAG = "NewTaskBarSchool";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("bar_school_new");

    private TaskAdapter adapter;
    private Query query;

    RecyclerView recyclerview_school_bar_new_tasks;
    TextInputLayout text_input_search_bar_school_new_tasks;
    Chip chip_today_bar_school_today_new;

    FirebaseFirestore firebaseFirestore;

    String email;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_bar_new_tasks, container, false);

        recyclerview_school_bar_new_tasks = view.findViewById(R.id.recyclerview_school_bar_new_tasks);
        text_input_search_bar_school_new_tasks = view.findViewById(R.id.text_input_search_bar_school_new_tasks);
        chip_today_bar_school_today_new = view.findViewById(R.id.chip_today_bar_school_today_new);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Utils utils = new Utils();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(getString(R.string.email));
        Log.d(TAG, "Email: " + email);

        query = taskRef.whereEqualTo("status", "Новая заявка");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_school_bar_new_tasks.setHasFixedSize(true);
        recyclerview_school_bar_new_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_bar_new_tasks.this.getContext()));
        recyclerview_school_bar_new_tasks.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_bar_new_tasks.this.getContext(), TaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), getString(R.string.bar_school_new));
            intent.putExtra(getString(R.string.location), getString(R.string.bar_school));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });

        Objects.requireNonNull(text_input_search_bar_school_new_tasks.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_value = text_input_search_bar_school_new_tasks.getEditText().getText().toString();

                if(search_value.isEmpty())
                    defaultQuery();
                else
                    search(search_value);

                return true;
            }
            return false;
        });

        chip_today_bar_school_today_new.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "today checked");
                String today = utils.getDate();
                todayTasks(today);
            } else {
                Log.i(TAG, "today not-checked");
                defaultQuery();
            }

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

    private void todayTasks(String date) {
        query = taskRef.whereEqualTo("date_create", date);

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
