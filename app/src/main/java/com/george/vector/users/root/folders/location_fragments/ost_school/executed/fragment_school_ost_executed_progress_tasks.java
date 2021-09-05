package com.george.vector.users.root.folders.location_fragments.ost_school.executed;

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
import com.george.vector.users.root.tasks.TaskRootActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_school_ost_executed_progress_tasks extends Fragment {

    private static final String TAG = "EXECUTED_PROGRESS";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_progress");

    private TaskAdapter adapter;
    private Query query;

    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerview_school_ost_progress_tasks;
    TextInputLayout text_input_search_school_ost_progress_tasks;
    Chip chip_old_school_ost_progress, chip_new_school_ost_progress, chip_all, chip_urgent_progress_tasks_ost_school;

    String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_ost_executed_progress_tasks, container, false);

        recyclerview_school_ost_progress_tasks = view.findViewById(R.id.recyclerview_school_ost_progress_tasks_executed);
        text_input_search_school_ost_progress_tasks = view.findViewById(R.id.text_input_search_school_ost_progress_tasks_executed);
        chip_old_school_ost_progress = view.findViewById(R.id.chip_old_school_ost_progress_executed);
        chip_new_school_ost_progress = view.findViewById(R.id.chip_new_school_ost_progress_executed);
        chip_urgent_progress_tasks_ost_school = view.findViewById(R.id.chip_urgent_progress_tasks_ost_school_executed);
        chip_all = view.findViewById(R.id.chip_all_progress_tasks_ost_school_executed);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(getString(R.string.email));
        Log.d(TAG, "Email: " + email);

        query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_school_ost_progress_tasks.setHasFixedSize(true);
        recyclerview_school_ost_progress_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_ost_executed_progress_tasks.this.getContext()));
        recyclerview_school_ost_progress_tasks.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, "Position: " + position + " id: " + id);

            Intent intent = new Intent(fragment_school_ost_executed_progress_tasks.this.getContext(), TaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_progress));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });

        Objects.requireNonNull(text_input_search_school_ost_progress_tasks.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_value = text_input_search_school_ost_progress_tasks.getEditText().getText().toString();

                if(search_value.isEmpty())
                    defaultQuery(email);
                else
                    search(search_value, email);

                return true;
            }
            return false;
        });

        chip_all.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "default checked");
                defaultQuery(email);
            }
        });

        chip_old_school_ost_progress.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "old checked");
                currentAddressTasks("Улица Авиаторов дом 9. Старое здание", email);
            }

        });

        chip_new_school_ost_progress.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "new checked");
                currentAddressTasks("Улица Авиаторов дом 9. Новое здание", email);
            }

        });

        chip_urgent_progress_tasks_ost_school.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "urgent checked");
                UrgentTasks(email);
            }
        });

        return view;
    }

    private void currentAddressTasks(String address, String email) {
        query = taskRef.whereEqualTo("address", address).whereEqualTo("executor", email);

        FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(search_options);
    }

    private void UrgentTasks(String email) {
        query = taskRef.whereEqualTo("urgent", true).whereEqualTo("executor", email);

        FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(search_options);
    }

    private void search(String query_text, String email) {
        query = taskRef.whereEqualTo("name_task", query_text).whereEqualTo("executor", email);

        FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(search_options);
    }

    private void defaultQuery(String email) {
        query = taskRef.whereEqualTo("executor", email);

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
