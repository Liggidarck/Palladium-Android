package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.tasks.FragmentUrgentRequest;
import com.george.vector.common.tasks.images.FragmentImageTask;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.databinding.ActivityTaskRootBinding;
import com.george.vector.notifications.SendNotification;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TaskRootActivity extends AppCompatActivity {

    private static final String TAG = "TaskActivityRoot";
    String id, collection, address, floor, cabinet, letter, nameTask, comment, status,
            dateCreate, timeCreate, location, email, image, emailCreator, emailExecutor,
            dateDone, fullNameExecutor, fullNameCreator;
    boolean confirmDelete, urgent;

    FirebaseFirestore firebaseFirestore;

    ActivityTaskRootBinding taskRootBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskRootBinding = ActivityTaskRootBinding.inflate(getLayoutInflater());
        setContentView(taskRootBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(ID).toString();
        collection = arguments.get(COLLECTION).toString();
        location = arguments.get(LOCATION).toString();
        email = arguments.get(EMAIL).toString();
        confirmDelete = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("confirm_before_deleting_root", true);

        firebaseFirestore = FirebaseFirestore.getInstance();

        setSupportActionBar(taskRootBinding.topAppBarTasksRoot);
        taskRootBinding.topAppBarTasksRoot.setNavigationOnClickListener(v -> onBackPressed());

        taskRootBinding.editTaskRootBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(LOCATION, location);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        load_data(collection, id);
    }

    void load_data(String collection, String id) {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            letter = value.getString("litera");
            nameTask = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            dateCreate = value.getString("date_create");
            timeCreate = value.getString("time_create");
            image = value.getString("image");

            Log.d(TAG, "image: " + image);

            emailCreator = value.getString("email_creator");
            emailExecutor = value.getString("executor");
            dateDone = value.getString("date_done");
            fullNameExecutor = value.getString("fullNameExecutor");
            fullNameCreator = value.getString("nameCreator");

            try {
                urgent = value.getBoolean("urgent");
                if (status.equals("Новая заявка"))
                    taskRootBinding.circleStatusRoot.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    taskRootBinding.circleStatusRoot.setImageResource(R.color.orange);

                if (status.equals("Архив") || status.equals("Завершенная заявка"))
                    taskRootBinding.circleStatusRoot.setImageResource(R.color.green);

                if (!letter.equals("-") && !letter.isEmpty())
                    cabinet = String.format("%s%s", cabinet, letter);

                if (image == null) {
                    Log.d(TAG, "No image, stop loading");
                } else {
                    Fragment image_fragment = new FragmentImageTask();

                    Bundle bundle = new Bundle();
                    bundle.putString("image_id", image);
                    bundle.putString(ID, id);
                    bundle.putString(COLLECTION, collection);
                    bundle.putString(LOCATION, location);
                    bundle.putString(EMAIL, email);

                    image_fragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_image_task, image_fragment)
                            .commit();
                }

                if (urgent) {
                    Log.d(TAG, "Срочная заявка");

                    Fragment urgent_fragment = new FragmentUrgentRequest();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_urgent_task, urgent_fragment)
                            .commit();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            taskRootBinding.textViewAddressTaskRoot.setText(address);
            taskRootBinding.textViewFloorTaskRoot.setText(floor);
            taskRootBinding.textViewCabinetTaskRoot.setText(cabinet);
            taskRootBinding.textViewNameTaskRoot.setText(nameTask);
            taskRootBinding.textViewCommentTaskRoot.setText(comment);
            taskRootBinding.textViewStatusTaskRoot.setText(status);

            String date_create_text = "Созданно: " + dateCreate + " " + timeCreate;
            taskRootBinding.textViewDateCreateTaskRoot.setText(date_create_text);

            taskRootBinding.textViewEmailCreatorTaskRoot.setText(emailCreator);

            if (fullNameCreator == null)
                taskRootBinding.textViewFullNameCreator.setText("Нет данных об этом пользователе");
            else
                taskRootBinding.textViewFullNameCreator.setText(fullNameCreator);

            if (fullNameExecutor == null)
                taskRootBinding.textViewFullNameExecutor.setText("Нет назначенного исполнителя");
            else
                taskRootBinding.textViewFullNameExecutor.setText(fullNameExecutor);

            if (emailExecutor == null)
                taskRootBinding.textViewEmailExecutorTaskRoot.setText("Нет данных");
            else
                taskRootBinding.textViewEmailExecutorTaskRoot.setText(emailExecutor);

            if (dateDone == null)
                taskRootBinding.textViewDateDoneTaskRoot.setText("Дата выполнения не назначена");
            else {
                String date_done_text = "Дата выполнения: " + dateDone;
                taskRootBinding.textViewDateDoneTaskRoot.setText(date_done_text);
            }

        });

        documentReference.get().addOnCompleteListener(task -> taskRootBinding.progressBarTaskRoot.setVisibility(View.INVISIBLE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_root_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.root_delete_task) {

            if (confirmDelete)
                show_dialog_delete();
            else
                delete_task();

        }

        if (item.getItemId() == R.id.root_share_task) {
            String IMAGE_URL;

            if (image == null)
                IMAGE_URL = "К заявке не прикреплено изображение";
            else
                IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image);

            String sharing_data = nameTask + "\n" + comment + "\n \n" +
                    address + "\n" + "Этаж: " + floor + "\n" + "Кабинет: " + cabinet + "\n \n" +
                    "Создатель заявки" + "\n" + fullNameCreator + "\n" + emailCreator + "\n \n" +
                    "Исполнитель" + "\n" + fullNameExecutor + "\n" + emailExecutor + "\n" + "Дата выполнения: " + dateDone + "\n \n" +
                    "Статус" + "\n" + status + "\n" + "Созданно: " + dateCreate + " " + timeCreate + "\n \n" +
                    "Изображение" + "\n" + IMAGE_URL;

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharing_data);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        }

        return true;
    }

    void show_dialog_delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_delete_task))
                .setPositiveButton(getText(R.string.delete), (dialog, id) -> delete_task())
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void delete_task() {
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        if (image != null) {
            String storageUrl = "images/" + image;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(storageUrl);
            storageReference.delete();
        }

        SendNotification sendNotification = new SendNotification();
        sendNotification.sendNotification(
                "Изменения по заявке",
                "Заявка " + nameTask + " удалена",
                TOPIC_NEW_TASKS_CREATE
        );
        onBackPressed();
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
            Snackbar.make(findViewById(R.id.task_root_coordinator), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}