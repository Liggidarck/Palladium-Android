package com.george.vector.users.root.folders.location_fragments.ost_school.full_access;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.users.root.tasks.TaskRootActivity;
import com.google.android.material.chip.Chip;
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

    String email;
    boolean confirm_delete;

    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerview_school_ost_new_tasks;
    TextInputLayout text_input_search_new_tasks;
    Chip chip_old_school_ost_new, chip_new_school_ost_new, chip_all, chip_urgent_new_tasks_ost_school;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_ost_new_tasks, container, false);

        recyclerview_school_ost_new_tasks = view.findViewById(R.id.recyclerview_school_ost_new_tasks);
        text_input_search_new_tasks = view.findViewById(R.id.text_input_search_new_tasks);
        chip_old_school_ost_new = view.findViewById(R.id.chip_old_school_ost_new);
        chip_new_school_ost_new = view.findViewById(R.id.chip_new_school_ost_new);
        chip_urgent_new_tasks_ost_school = view.findViewById(R.id.chip_urgent_new_tasks_ost_school);
        chip_all = view.findViewById(R.id.chip_all_new_tasks_ost_school);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(getString(R.string.email));
        confirm_delete = PreferenceManager.getDefaultSharedPreferences(fragment_school_ost_new_tasks.this.getContext()).getBoolean("confirm_before_deleting_root", true);
        Log.d(TAG, "Email: " + email);

        query = taskRef.whereEqualTo("status", "Новая заявка");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        adapter = new TaskAdapter(options);

        recyclerview_school_ost_new_tasks.setHasFixedSize(true);
        recyclerview_school_ost_new_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_ost_new_tasks.this.getContext()));
        recyclerview_school_ost_new_tasks.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(fragment_school_ost_new_tasks.this.getContext());

                builder.setTitle(getText(R.string.warning))
                        .setMessage(getText(R.string.warning_delete_task))
                        .setPositiveButton(getText(R.string.delete), (dialog, id) -> Toast.makeText(fragment_school_ost_new_tasks.this.getContext(), "start demo deleteng...", Toast.LENGTH_SHORT).show())
                        .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(recyclerview_school_ost_new_tasks);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_ost_new_tasks.this.getContext(), TaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_new));
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.email), email);
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

        chip_all.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "default checked");
                defaultQuery();
            }
        });

        chip_old_school_ost_new.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "old checked");
                currentAddressTasks("Улица Авиаторов дом 9. Старое здание");
            }

        });

        chip_new_school_ost_new.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "new checked");
                currentAddressTasks("Улица Авиаторов дом 9. Новое здание");
            }

        });

        chip_urgent_new_tasks_ost_school.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "urgent checked");
                UrgentTasks();
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

    private void UrgentTasks() {
        query = taskRef.whereEqualTo("urgent", true);

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
