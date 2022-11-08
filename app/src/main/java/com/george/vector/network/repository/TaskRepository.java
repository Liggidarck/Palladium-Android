package com.george.vector.network.repository;

import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.Message;
import com.george.vector.network.notifications.SendNotification;
import com.george.vector.network.api.FluffyFoxyClient;
import com.george.vector.network.api.TaskInterface;
import com.george.vector.network.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private final TaskInterface taskInterface;

    public static final String TAG = TaskRepository.class.getSimpleName();

    public TaskRepository(String token) {
        taskInterface = FluffyFoxyClient.getFoxyTokenClient(token).create(TaskInterface.class);
    }

    public MutableLiveData<Message> createTask(Task task) {
        MutableLiveData<Message> taskMutableLiveData = new MutableLiveData<>();

        taskInterface.createTask(task).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "Task create code: " + response.code());
                if (response.code() == 200) {
                    taskMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                taskMutableLiveData.setValue(null);
                Log.e(TAG, "onFailure: create task: ", t);
            }
        });

        return taskMutableLiveData;
    }

    public MutableLiveData<Message> editTask(Task task, long id) {
        MutableLiveData<Message> edit = new MutableLiveData<>();

        taskInterface.editTask(task, id).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: edit task: " + response.code());
                if (response.code() == 200) {
                    edit.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                edit.setValue(null);
                Log.e(TAG, "onFailure: edit task: ", t);
            }
        });

        return edit;
    }

    public MutableLiveData<Message> deleteTask(long id) {
        MutableLiveData<Message> delete = new MutableLiveData<>();

        taskInterface.deleteTask(id).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: delete task code: " + response.code());
                if (response.code() == 200) {
                    delete.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                delete.setValue(null);
                Log.e(TAG, "onFailure: delete task", t);
            }
        });

        return delete;
    }


    public MutableLiveData<List<Task>> getTasksByExecutor(long id) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByExecutor(id).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "getTasksByExecutor code: " + response.code());
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
                Log.e(TAG, "getTasksByExecutor: ", t);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getTasksByCreator(long id) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByCreator(id).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "getTasksByCreator: " + response.body());
                tasks.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
                Log.e(TAG, "getTasksByCreator: ", t);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getTasksByZone(String zone) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByZone(zone).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "getTasksByZone: " + response.code());
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
                Log.e(TAG, "getTasksByZone: ", t);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getByZoneLikeAndStatusLike(String zone, String status) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getByZoneLikeAndStatusLike(zone, status).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "getByZoneLikeAndStatusLike: " + response.code());
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
                Log.e(TAG, "getByZoneLikeAndStatusLike: ", t);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getTasksByStatus(String status) {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getTasksByStatus(status).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "getTasksByStatus: " + response.code());
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
                Log.e(TAG, "getTasksByStatus: ", t);
            }
        });

        return tasks;
    }

    public MutableLiveData<List<Task>> getAllTasks() {
        MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

        taskInterface.getAllTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "getAllTasks: " + response.code());
                if (response.code() == 200) {
                    tasks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                tasks.setValue(null);
                Log.e(TAG, "getAllTasks: ", t);
            }
        });

        return tasks;
    }

    public MutableLiveData<Task> getTaskById(long id) {
        MutableLiveData<Task> task = new MutableLiveData<>();

        taskInterface.getTaskById(id).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                Log.d(TAG, "getTaskById: " + response.code());
                if (response.code() == 200) {
                    task.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                Log.e(TAG, "getTaskById: ", t);
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
