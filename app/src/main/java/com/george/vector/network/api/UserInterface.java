package com.george.vector.network.api;

import com.george.vector.network.model.Role;
import com.george.vector.network.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserInterface {

    @GET("/palladium/users/get/all")
    Call<List<User>> getAllUsers();

    @GET("/palladium/users/get/all/roles")
    Call<List<Role>> getAllRoles();

    @GET("palladium/users/get/all/users")
    Call<List<User>> getUsersByRoleName(@Query("name") String name);

    @GET("palladium/users/get/all/users/get/user")
    Call<User> getUserById(@Query("id") long id);

    @PUT("palladium/users/edit")
    Call<String> editUser(@Body User user, @Query("id") long id);

    @DELETE("palladium/users/delete")
    Call<String> deleteUser(@Query("id") long id);

}
