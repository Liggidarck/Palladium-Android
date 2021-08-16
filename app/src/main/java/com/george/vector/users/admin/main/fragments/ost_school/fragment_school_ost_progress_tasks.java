package com.george.vector.users.admin.main.fragments.ost_school;

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
import com.george.vector.common.utils.Utils;
import com.george.vector.users.admin.tasks.TaskAdminActivity;
import com.george.vector.users.root.tasks.TaskRootActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_school_ost_progress_tasks extends Fragment {

    private static final String TAG = "ProgressTaskOstSchool";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_progress");

    private TaskAdapter adapter;
    private Query query;

    FirebaseFirestore firebaseFirestore;
    TextInputLayout text_input_search_school_ost_progress_tasks;
    Chip chip_today_school_ost_progress, chip_old_school_ost_progress, chip_new_school_ost_progress, chip_all;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_ost_progress_tasks, container, false);

        Utils utils = new Utils();

        RecyclerView recyclerview_school_ost_new_tasks = view.findViewById(R.id.recyclerview_school_ost_progress_tasks);
        text_input_search_school_ost_progress_tasks = view.findViewById(R.id.text_input_search_school_ost_progress_tasks);
        chip_today_school_ost_progress = view.findViewById(R.id.chip_today_school_ost_progress);
        chip_old_school_ost_progress = view.findViewById(R.id.chip_old_school_ost_progress);
        chip_new_school_ost_progress = view.findViewById(R.id.chip_new_school_ost_progress);
        chip_all = view.findViewById(R.id.chip_all_progress_tasks_ost_school);

        firebaseFirestore = FirebaseFirestore.getInstance();

        query = taskRef.whereEqualTo("status", "В работе");

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_school_ost_new_tasks.setHasFixedSize(true);
        recyclerview_school_ost_new_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_ost_progress_tasks.this.getContext()));
        recyclerview_school_ost_new_tasks.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_ost_progress_tasks.this.getContext(), TaskAdminActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_progress));
            intent.putExtra(getString(R.string.permission), getString(R.string.ost_school));

            startActivity(intent);
        });

        Objects.requireNonNull(text_input_search_school_ost_progress_tasks.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_value = text_input_search_school_ost_progress_tasks.getEditText().getText().toString();

                if(search_value.isEmpty())
                    defaultQuery();
                else
                    search(search_value);

                return true;
            }
            return false;
        });

        chip_all.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "default checked");
                defaultQuery();
            }
        });

        chip_today_school_ost_progress.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "today checked");
                String today = utils.getDate();
                todayTasks(today);
            }

        });

        chip_old_school_ost_progress.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "old checked");
                currentAddressTasks("Улица Авиаторов дом 9. Старое здание");
            }

        });

        chip_new_school_ost_progress.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "new checked");
                currentAddressTasks("Улица Авиаторов дом 9. Новое здание");
            }

        });


        return view;
    }

    private void currentAddressTasks(String address) {
        query = taskRef.whereEqualTo("address", address);

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


    private void search(String query_text) {
        query = taskRef.whereEqualTo("name_task", query_text);

        FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(search_options);
    }

    private void defaultQuery() {
        query = taskRef.whereEqualTo("status", "В работе");

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
