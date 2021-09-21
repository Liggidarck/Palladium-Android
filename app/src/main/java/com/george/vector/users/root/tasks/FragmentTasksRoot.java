package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTED;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_ARCHIVE;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_PROGRESS;

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
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksRoot extends Fragment {

    private static final String TAG = "fragmentTasksRoot";
    String location, folder, executed, email;

    TaskAdapter adapter;

    RecyclerView recyclerview_tasks_root;
    Chip chip_all_tasks_root, chip_urgent_tasks_root, chip_old_school_tasks_root, chip_new_school_tasks_root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks_root, container, false);

        recyclerview_tasks_root = view.findViewById(R.id.recyclerview_school_ost_new_tasks);
        chip_all_tasks_root = view.findViewById(R.id.chip_all_tasks_root);
        chip_urgent_tasks_root = view.findViewById(R.id.chip_urgent_tasks_root);
        chip_old_school_tasks_root = view.findViewById(R.id.chip_old_school_tasks_root);
        chip_new_school_tasks_root = view.findViewById(R.id.chip_new_school_tasks_root);

        Bundle args = getArguments();
        assert args != null;
        location = args.getString(LOCATION);
        folder = args.getString(FOLDER);
        executed = args.getString(EXECUTED);
        email = args.getString(EMAIL);

        if (location.equals(OST_SCHOOL) && folder.equals(NEW_TASKS))
            ostSchoolNewTasks();

        if (location.equals(OST_SCHOOL) && folder.equals(IN_PROGRESS_TASKS))
            ostSchoolProgressTasks();

        if (location.equals(OST_SCHOOL) && folder.equals(ARCHIVE_TASKS))
            ostSchoolArchiveTasks();

        return view;
    }

    void setUpRecyclerView() {
        recyclerview_tasks_root.setHasFixedSize(true);
        recyclerview_tasks_root.setLayoutManager(new LinearLayoutManager(FragmentTasksRoot.this.getContext()));
        recyclerview_tasks_root.setAdapter(adapter);

        //Удаление по свайпу
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(FragmentTasksRoot.this.getContext());
//
//                builder.setTitle(getText(R.string.warning))
//                        .setMessage(getText(R.string.warning_delete_task))
//                        .setPositiveButton(getText(R.string.delete), (dialog, id) -> Toast.makeText(FragmentTasksRoot.this.getContext(), "start demo deleting...", Toast.LENGTH_SHORT).show())
//                        .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        }).attachToRecyclerView(recyclerview_tasks_root);
    }

    void ostSchoolNewTasks() {
        queryOstSchoolNewTasks(executed);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_NEW);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void queryOstSchoolNewTasks(String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection(OST_SCHOOL_NEW);

        Query query = taskRef.whereEqualTo("status", "Новая заявка");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        adapter = new TaskAdapter(options);

        if (executed.equals("root")) {
            Log.d(TAG, "All Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Log.d(TAG, "Default Query");

                    Query query_all = taskRef.whereEqualTo("status", "Новая заявка");
                    FirestoreRecyclerOptions<TaskUi> all_tasks = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(all_tasks);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
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
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text));

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text));

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }
            });

        }

        if (executed.equals("work")) {
            Log.d(TAG, "All Executed Tasks");

            Query query_all_default = taskRef.whereEqualTo("executor", email);
            FirestoreRecyclerOptions<TaskUi> executor_options_default = new FirestoreRecyclerOptions.Builder<TaskUi>()
                    .setQuery(query_all_default, TaskUi.class)
                    .build();

            adapter.updateOptions(executor_options_default);

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<TaskUi> executor_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(executor_options);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
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
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text)).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text)).whereEqualTo("executor", email);

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
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_PROGRESS);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void queryOstSchoolProgressTasks(String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection(OST_SCHOOL_PROGRESS);

        Query query = taskRef.whereEqualTo("status", "В работе");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();
        adapter = new TaskAdapter(options);

        if (executed.equals("root")) {
            Log.d(TAG, "All Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_all = taskRef.whereEqualTo("status", "Новая заявка");
                    FirestoreRecyclerOptions<TaskUi> all_tasks = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(all_tasks);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
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
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text));

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text));

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }
            });

        }

        if (executed.equals("work")) {
            Log.d(TAG, "All Executed Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<TaskUi> executor_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(executor_options);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
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
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text)).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text)).whereEqualTo("executor", email);

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
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_ARCHIVE);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void queryOstSchoolArchiveTasks(String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection(OST_SCHOOL_ARCHIVE);

        Query query = taskRef.whereEqualTo("status", "Архив");
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        if (executed.equals("root")) {
            Log.d(TAG, "All Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_all = taskRef.whereEqualTo("status", "Новая заявка");
                    FirestoreRecyclerOptions<TaskUi> all_tasks = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(all_tasks);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
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
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text));

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text));

                    FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(address_options);
                }
            });

        }

        if (executed.equals("work")) {
            Log.d(TAG, "All Executed Tasks");

            chip_all_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<TaskUi> executor_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_all, TaskUi.class)
                            .build();

                    adapter.updateOptions(executor_options);
                }

            });

            chip_urgent_tasks_root.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
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
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text)).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                            .setQuery(query_address, TaskUi.class)
                            .build();

                    adapter.updateOptions(search_options);
                }

            });

            chip_new_school_tasks_root.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text)).whereEqualTo("executor", email);

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
