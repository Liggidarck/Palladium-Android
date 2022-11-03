package com.george.vector.network.repository;

import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.BuildConfig;
import com.george.vector.common.notifications.SendNotification;
import com.george.vector.network.api.FluffyFoxyClient;
import com.george.vector.network.api.TaskInterface;
import com.george.vector.network.model.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private final TaskInterface taskInterface;

    public TaskRepository(String token) {
        taskInterface = FluffyFoxyClient.getFoxyTokenClient(token).create(TaskInterface.class);
    }

    public MutableLiveData<String> createTask(Task task) {
        MutableLiveData<String> taskMutableLiveData = new MutableLiveData<>();

        taskInterface.createTask(task).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    taskMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                taskMutableLiveData.setValue(null);
            }
        });

        return taskMutableLiveData;
    }

    public MutableLiveData<String> editTask(Task task, long id) {
        MutableLiveData<String> edit = new MutableLiveData<>();

        taskInterface.editTask(task, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    edit.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                edit.setValue(null);
            }
        });

        return edit;
    }

    public MutableLiveData<String> deleteTask(long id) {
        MutableLiveData<String> delete = new MutableLiveData<>();

        taskInterface.deleteTask(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                delete.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                delete.setValue(null);
            }
        });

        return delete;
    }


    public MutableLiveData<List<Task>> getTasksByExecutor(long id) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByExecutor(id).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getTasksByCreator(long id) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByCreator(id).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getTasksByStatus(String status) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByStatus(status).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getAllTasks() {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getAllTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
            }
        });

        return tasks;
    }

    public MutableLiveData<Task> getTaskById(long id) {
        MutableLiveData<Task> task = new MutableLiveData<>();

        taskInterface.getTaskById(id).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if (response.code() == 200) {
                    task.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                task.setValue(null);
            }
        });

        return task;
    }

    private void sendNotification(boolean urgent, String taskName, String taskId, String collection) {
        String title;

        if (urgent)
            title = "Созданна новая срочная заявка";
        else
            title = "Созданна новая заявка ";

        SendNotification sendNotification = new SendNotification();
        sendNotification.sendNotification(title, taskName, taskId, collection, TOPIC_NEW_TASKS_CREATE);
    }

}
