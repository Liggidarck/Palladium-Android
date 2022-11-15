package com.george.vector.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.Message;
import com.george.vector.network.model.Task;
import com.george.vector.network.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    final private TaskRepository repository;

    public TaskViewModel(@NonNull Application application, String token) {
        super(application);
        repository = new TaskRepository(token);
    }

    public MutableLiveData<Message> createTask(Task task) {
        return repository.createTask(task);
    }

    public MutableLiveData<Message> editTask(Task task, long id) {
        return repository.editTask(task, id);
    }

    public MutableLiveData<Message> countByZoneLikeAndStatusLike(String zone, String status) {
        return repository.countByZoneLikeAndStatusLike(zone, status);
    }

    public MutableLiveData<Message> countByZone(String zone) {
        return repository.countByZone(zone);
    }

    public MutableLiveData<List<Task>> getByZoneLikeAndStatusLikeAndExecutorId(String zone, String status, int executorId) {
        return repository.getByZoneLikeAndStatusLikeAndExecutorId(zone, status, executorId);
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

}
