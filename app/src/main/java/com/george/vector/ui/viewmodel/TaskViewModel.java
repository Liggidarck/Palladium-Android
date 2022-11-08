package com.george.vector.ui.viewmodel;

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

import com.george.vector.network.model.Message;
import com.george.vector.network.model.Task;
import com.george.vector.network.repository.TaskRepository;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TaskViewModel extends AndroidViewModel {

    final TaskRepository repository;
    final FirebaseStorage firebaseStorage;
    final StorageReference storageReference;

    public TaskViewModel(@NonNull Application application, String token) {
        super(application);
        repository = new TaskRepository(token);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public MutableLiveData<Message> createTask(Task task) {
        return repository.createTask(task);
    }

    public MutableLiveData<Message> editTask(Task task, long id) {
        return repository.editTask(task, id);
    }

    public MutableLiveData<List<Task>> getTasksByExecutor(long id) {
        return repository.getTasksByExecutor(id);
    }

    public MutableLiveData<List<Task>> getTasksByCreator(long id) {
        return repository.getTasksByCreator(id);
    }

    public MutableLiveData<List<Task>> getTasksByStatus(String status) {
        return repository.getTasksByStatus(status);
    }

    public MutableLiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
    }

    public MutableLiveData<Task> getTaskById(long id) {
        return repository.getTaskById(id);
    }

    public MutableLiveData<List<Task>> getTasksByZone(String zone) {
        return repository.getTasksByZone(zone);
    }

    public MutableLiveData<List<Task>> getByZoneLikeAndStatusLike(String zone, String status) {
        return repository.getByZoneLikeAndStatusLike(zone, status);
    }

    public MutableLiveData<Message> deleteTask(long id) {
        return repository.deleteTask(id);
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
