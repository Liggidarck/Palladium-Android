package com.george.vector.users.root.folders;

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
import com.george.vector.users.root.tasks.TaskRootActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksRoot extends Fragment {

    private static final String TAG = "fragmentTasksRoot";
    String location, folder, executed, email;

    TaskAdapter adapter;

    RecyclerView recyclerview_tasks_root;
    TextInputLayout text_input_search_root_tasks;
    Chip chip_all_tasks_root, chip_urgent_tasks_root, chip_old_school_tasks_root, chip_new_school_tasks_root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks_root, container, false);

        recyclerview_tasks_root = view.findViewById(R.id.recyclerview_school_ost_new_tasks);
        text_input_search_root_tasks = view.findViewById(R.id.text_input_search_root_tasks);
        chip_all_tasks_root = view.findViewById(R.id.chip_all_tasks_root);
        chip_urgent_tasks_root = view.findViewById(R.id.chip_urgent_tasks_root);
        chip_old_school_tasks_root = view.findViewById(R.id.chip_old_school_tasks_root);
        chip_new_school_tasks_root = view.findViewById(R.id.chip_new_school_tasks_root);

        Bundle args = getArguments();
        assert args != null;
        location = args.getString(getString(R.string.location));
        folder = args.getString(getString(R.string.folder));
        executed = args.getString("executed");
        email = args.getString(getString(R.string.email));

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.new_tasks)))
            ostSchoolNewTasks();

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.in_progress_tasks)))
            ostSchoolProgressTasks();

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.archive_tasks)))
            ostSchoolArchiveTasks();

        return view;
    }

    void setUpRecyclerView() {
        recyclerview_tasks_root.setHasFixedSize(true);
        recyclerview_tasks_root.setLayoutManager(new LinearLayoutManager(FragmentTasksRoot.this.getContext()));
        recyclerview_tasks_root.setAdapter(adapter);
    }

    void ostSchoolNewTasks() {
        queryOstSchoolNewTasks(executed);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_new));
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });
    }

    void queryOstSchoolNewTasks(String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_new");

        Query query = taskRef.whereEqualTo("status", "Новая заявка");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        adapter = new TaskAdapter(options);

        if(executed.equals("root")) {
            Log.d(TAG, "All Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked) {
                    Log.d(TAG, "Default Query");

                    Query query_all = taskRef.whereEqualTo("status", "Новая заявка");
                    FirestoreRecyclerOptions<TaskUi> all_tasks = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(all_tasks);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked){
                    Log.i(TAG, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true);

                    FirestoreRecyclerOptions<TaskUi> urgent_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_urgent, TaskUi.class)
                            .build();

                    adapter.updateOptions(urgent_options);
                }

            });

            chip_old_school_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Старое здание");

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Новое здание");

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }
            });

        }

        if(executed.equals("work")) {
            Log.d(TAG, "All Executed Tasks");

            Query query_all_default = taskRef.whereEqualTo("executor", email);
            FirestoreRecyclerOptions<TaskUi> executor_options_default = new FirestoreRecyclerOptions.Builder<TaskUi>()
                    .setQuery(query_all_default, TaskUi.class)
                    .build();

            adapter.updateOptions(executor_options_default);

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<TaskUi> executor_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(executor_options);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked){
                    Log.i(TAG, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_urgent, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_old_school_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Старое здание").whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Новое здание").whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }
            });

        }
    }


    void ostSchoolProgressTasks() {
        queryOstSchoolProgressTasks(executed);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_progress));
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });
    }

    void queryOstSchoolProgressTasks(String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_progress");

        Query query = taskRef.whereEqualTo("status", "В работе");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        adapter = new TaskAdapter(options);

        if(executed.equals("root")) {
            Log.d(TAG, "All Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked) {
                    Query query_all = taskRef.whereEqualTo("status", "Новая заявка");
                    FirestoreRecyclerOptions<TaskUi> all_tasks = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(all_tasks);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked){
                    Log.i(TAG, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true);

                    FirestoreRecyclerOptions<TaskUi> urgent_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_urgent, TaskUi.class)
                            .build();

                    adapter.updateOptions(urgent_options);
                }

            });

            chip_old_school_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Старое здание");

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Новое здание");

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }
            });

        }

        if(executed.equals("work")) {
            Log.d(TAG, "All Executed Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<TaskUi> executor_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(executor_options);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked){
                    Log.i(TAG, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_urgent, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_old_school_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Старое здание").whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Новое здание").whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }
            });

        }

    }


    void ostSchoolArchiveTasks() {
        queryOstSchoolArchiveTasks(executed);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_archive));
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });
    }

    void queryOstSchoolArchiveTasks(String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_archive");

        Query query = taskRef.whereEqualTo("status", "Архив");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        if(executed.equals("root")) {
            Log.d(TAG, "All Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked) {
                    Query query_all = taskRef.whereEqualTo("status", "Новая заявка");
                    FirestoreRecyclerOptions<TaskUi> all_tasks = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(all_tasks);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked){
                    Log.i(TAG, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true);

                    FirestoreRecyclerOptions<TaskUi> urgent_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_urgent, TaskUi.class)
                            .build();

                    adapter.updateOptions(urgent_options);
                }

            });

            chip_old_school_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Старое здание");

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Новое здание");

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }
            });

        }

        if(executed.equals("work")) {
            Log.d(TAG, "All Executed Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<TaskUi> executor_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(executor_options);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if(isChecked){
                    Log.i(TAG, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_urgent, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_old_school_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Старое здание").whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", "Улица Авиаторов дом 9. Новое здание").whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }
            });

        }

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
