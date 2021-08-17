package com.george.vector.users.executor.main.fragments_location.ost_school;

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
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.executor.tasks.TaskExecutorActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class fragment_school_ost_new_tasks extends Fragment {

    RecyclerView recycler_view_new_tasks_executor;
    private static final String TAG = "NewTaskOstSchoolExec";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_new");

    TextInputLayout search_executor_ost_school_new_tasks;
    Chip chip_today_school_ost_new, chip_old_school_ost_new, chip_new_school_ost_new, chip_all;

    TaskAdapter adapter;
    private Query query;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_ost_new_tasks, container, false);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(getString(R.string.email));

        Utils utils = new Utils();

        recycler_view_new_tasks_executor = view.findViewById(R.id.recyclerview_school_ost_new_tasks);
        search_executor_ost_school_new_tasks = view.findViewById(R.id.text_input_search_new_tasks);
        chip_today_school_ost_new = view.findViewById(R.id.chip_today_school_ost_new);
        chip_old_school_ost_new = view.findViewById(R.id.chip_old_school_ost_new);
        chip_new_school_ost_new = view.findViewById(R.id.chip_new_school_ost_new);
        chip_all = view.findViewById(R.id.chip_all_new_tasks_ost_school);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recycler_view_new_tasks_executor.setHasFixedSize(true);
        recycler_view_new_tasks_executor.setLayoutManager(new LinearLayoutManager(fragment_school_ost_new_tasks.this.getContext()));
        recycler_view_new_tasks_executor.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_ost_new_tasks.this.getContext(), TaskExecutorActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_new));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });

        Objects.requireNonNull(search_executor_ost_school_new_tasks.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_value = search_executor_ost_school_new_tasks.getEditText().getText().toString();

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

        chip_today_school_ost_new.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "today checked");
                String today = utils.getDate();
                todayTasks(today, email);
            }

        });

        chip_old_school_ost_new.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "old checked");
                currentAddressTasks("Улица Авиаторов дом 9. Старое здание", email);
            }

        });

        chip_new_school_ost_new.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "new checked");
                currentAddressTasks("Улица Авиаторов дом 9. Новое здание", email);
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

    private void todayTasks(String date, String email) {
        query = taskRef.whereEqualTo("date_create", date).whereEqualTo("executor", email);

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
