package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Logs.TAG_TASK_EXECUTOR_FRAGMENT;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityTaskExecutorBinding;
import com.george.vector.ui.tasks.FragmentImageTask;
import com.george.vector.ui.tasks.FragmentUrgentRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskExecutorActivity extends AppCompatActivity {

    String id, collection, location, address, floor, cabinet, letter, nameTask, comment, status, dateCreate , timeCreate,
            emailExecutor, emailCreator, dateDone, image, fullNameCreator, fullNameExecutor, emailMainActivity;
    boolean urgent;

    FirebaseFirestore firebaseFirestore;
    ActivityTaskExecutorBinding taskExecutorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskExecutorBinding = ActivityTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(taskExecutorBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);
        emailMainActivity = arguments.getString(EMAIL);

        firebaseFirestore = FirebaseFirestore.getInstance();
        taskExecutorBinding.topAppBarTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        taskExecutorBinding.editTaskExecutorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskExecutorActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(LOCATION, location);
            intent.putExtra(EMAIL, emailMainActivity);
            startActivity(intent);
        });

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            taskExecutorBinding.progressBarTaskExecutor.setVisibility(View.VISIBLE);
            assert value != null;
            try {
                address = value.getString("address");
                floor = value.getString("floor");
                cabinet = value.getString("cabinet");
                letter = value.getString("litera");
                nameTask = value.getString("name_task");
                comment = value.getString("comment");
                status = value.getString("status");
                dateCreate = value.getString("date_create");
                timeCreate = value.getString("time_create");
                emailExecutor = value.getString("executor");
                dateDone = value.getString("date_done");
                image = value.getString("image");

                fullNameCreator = value.getString("nameCreator");
                emailCreator = value.getString("email_creator");

                fullNameExecutor = value.getString("fullNameExecutor");
                urgent = value.getBoolean("urgent");

                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "address: " + address);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "floor: " + floor);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "litera: " + letter);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "comment: " + comment);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "status: " + status);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "date_create: " + dateCreate);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "time_create: " + timeCreate);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "email_executor: " + emailExecutor);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "email_creator: " + emailCreator);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "date_done: " + dateDone);
                Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "image: " + image);

                if (status.equals("Новая заявка"))
                    taskExecutorBinding.circleStatusExecutor.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    taskExecutorBinding.circleStatusExecutor.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    taskExecutorBinding.circleStatusExecutor.setImageResource(R.color.green);

                if (!letter.equals("-") && !letter.isEmpty())
                    cabinet = String.format("%s%s", cabinet, letter);

                if (urgent) {
                    Log.d(TAG_TASK_EXECUTOR_FRAGMENT, "Срочная заявка");

                    Fragment urgent_fragment = new FragmentUrgentRequest();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_urgent_task_executor, urgent_fragment)
                            .commit();

                }

                if (image != null) {
                    Fragment image_fragment = new FragmentImageTask();

                    Bundle bundle = new Bundle();
                    bundle.putString("image_id", image);
                    bundle.putString(ID, id);
                    bundle.putString(COLLECTION, collection);
                    bundle.putString(LOCATION, location);
                    bundle.putString(EMAIL, "email");

                    image_fragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_image_task_executor, image_fragment)
                            .commit();
                }

            } catch (Exception e) {
                Log.e(TAG_TASK_EXECUTOR_FRAGMENT, "Error! " + e);
            }

            taskExecutorBinding.textViewAddressTaskExecutor.setText(address);
            taskExecutorBinding.textViewFloorTaskExecutor.setText(String.format("%s %s", getText(R.string.floor), floor));
            taskExecutorBinding.textViewCabinetTaskExecutor.setText(String.format("%s %s", getText(R.string.cabinet), cabinet));
            taskExecutorBinding.textViewNameTaskExecutor.setText(nameTask);
            taskExecutorBinding.textViewCommentTaskExecutor.setText(comment);
            taskExecutorBinding.textViewStatusTaskExecutor.setText(status);

            String date_create_text = String.format("Созданно: %s %s", dateCreate, timeCreate);
            taskExecutorBinding.textViewDateCreateTaskExecutor.setText(date_create_text);

            taskExecutorBinding.textViewFullNameCreatorExecutor.setText(fullNameCreator);
            taskExecutorBinding.textViewEmailCreatorTaskExecutor.setText(emailCreator);

            taskExecutorBinding.textViewFullNameExecutorEX.setText(fullNameExecutor);
            taskExecutorBinding.textViewEmailExecutorTaskExecutor.setText(emailExecutor);

            if (dateDone == null)
                taskExecutorBinding.textViewDateDoneTaskExecutor.setText("Дата выполнения не назначена");
            else {
                String date_done_text = "Дата выполнения: " + dateDone;
                taskExecutorBinding.textViewDateDoneTaskExecutor.setText(date_done_text);
            }

        });

        documentReference.get().addOnCompleteListener(task -> taskExecutorBinding.progressBarTaskExecutor.setVisibility(View.INVISIBLE));

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_task_executor), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        Log.i(TAG_TASK_EXECUTOR_FRAGMENT, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}