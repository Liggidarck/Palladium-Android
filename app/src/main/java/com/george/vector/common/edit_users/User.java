package com.george.vector.common.edit_users;

public class User {

    private String name;
    private String last_name;
    private String patronymic;
    private String email;
    private String role;

    public User(){}

    public User(String  name, String last_name, String patronymic, String email, String role){
        this.name = name;
        this.last_name = last_name;
        this.patronymic = patronymic;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
