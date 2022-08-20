package com.george.vector.ui.viewmodel;

import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.common.notifications.SendNotification;
import com.george.vector.network.model.Task;
import com.george.vector.network.repository.TaskRepository;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TaskViewModel extends AndroidViewModel {

    final TaskRepository repository;
    final FirebaseStorage firebaseStorage;
    final StorageReference storageReference;

    public TaskViewModel(@NonNull Application application, String collection) {
        super(application);
        repository = new TaskRepository(collection);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public MutableLiveData<Task> getTask(String taskId) {
        return repository.getTask(taskId);
    }

    public void createTask(Task task) {
        repository.createTask(task);
        sendNotification(task.getUrgent(), task.getName_task());
    }

    public void updateTask(String id, Task task) {
        repository.updateTask(id, task);
    }

    public void deleteTask(String id, String imageId) {
        if (imageId != null) {
            String storageUrl = "images/" + imageId;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(storageUrl);
            storageReference.delete();
        }

        repository.deleteTask(id);
    }

    void sendNotification(boolean urgent, String taskName) {
        String title;

        if (urgent)
            title = "Созданна новая срочная заявка";
        else
            title = "Созданна новая заявка";

        SendNotification sendNotification = new SendNotification();
        sendNotification.sendNotification(title, taskName, TOPIC_NEW_TASKS_CREATE);
    }

    public String uploadImage(Uri fileUri, Context context) {
        String nameImage;
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();

        nameImage = UUID.randomUUID().toString();

        StorageReference ref = storageReference.child("images/" + nameImage);
        ref.putBytes(data)
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Прогресс: " + (int) progress + "%");
                });

        return nameImage;
    }


    public void setImage(String image, ProgressBar progressBar, ImageView imageView, int bufferSize) {
        StorageReference photoReference = storageReference.child("images/" + image);

        long BUFFER = bufferSize * (1024 * 1024);
        photoReference.getBytes(BUFFER).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(bmp);
        });

    }

}
