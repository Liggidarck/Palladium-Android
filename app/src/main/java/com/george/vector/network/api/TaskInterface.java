package com.george.vector.network.api;

import com.george.vector.network.model.Message;
import com.george.vector.network.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TaskInterface {

    @POST("/palladium/tasks/create")
    Call<Message> createTask(@Body Task task);

    @PUT("/palladium/tasks/edit")
    Call<Message> editTask(@Body Task task, @Query("id") long id);

    @DELETE("/palladium/tasks/delete")
    Call<Message> deleteTask(@Query("id") long id);

    @GET("/palladium/tasks/get/countByZoneAndStatus")
    Call<Message> countByZoneLikeAndStatusLike(@Query("zone") String zone,
                                               @Query("status") String status);

    @GET("/palladium/tasks/get/countByZone")
    Call<Message> countByZone(@Query("zone") String zone);

    @GET("/palladium/tasks/get/byExecutor")
    Call<List<Task>> getTasksByExecutor(@Query("id") long id);

    @GET("/palladium/tasks/get/byZoneAndStatusAndExecutorIdLike")
    Call<List<Task>> getByZoneLikeAndStatusLikeAndExecutorId(@Query("zone") String zone,
                                                             @Query("status") String status,
                                                             @Query("executorId") int executorId);

    @GET("/palladium/tasks/get/byCreator")
    Call<List<Task>> getTasksByCreator(@Query("id") long id);

    @GET("/palladium/tasks/get/byStatus")
    Call<List<Task>> getTasksByStatus(@Query("status") String status);

    @GET("/palladium/tasks/get/byZone")
    Call<List<Task>> getTasksByZone(@Query("zone") String zone);

    @GET("/palladium/tasks/get/byZoneAndStatus")
    Call<List<Task>> getByZoneLikeAndStatusLike(@Query("zone") String zone,
                                                @Query("status") String status);

    @GET("/palladium/tasks/get/all")
    Call<List<Task>> getAllTasks();

    @GET("/palladium/tasks/get/byId")
    Call<Task> getTaskById(@Query("id") long id);

}
