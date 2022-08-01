package com.george.vector.network.repository;

import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.PERMISSION;
import static com.george.vector.common.utils.consts.Keys.ROLE;

import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    final CollectionReference collectionReference;
    DocumentReference documentReference;
    String collection;

    public UserRepository(String collection) {
        collectionReference = FirebaseFirestore.getInstance().collection(collection);
        this.collection = collection;
    }

    public void updateUser(String id, User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());
        userMap.put("last_name", user.getLast_name());
        userMap.put("patronymic", user.getPatronymic());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole());
        userMap.put("password", user.getPassword());
        collectionReference
                .document(id)
                .update(userMap);
    }

    public MutableLiveData<User> getUser(String id) {
        MutableLiveData<User> user = new MutableLiveData<>();
        documentReference = FirebaseFirestore.getInstance().collection(collection).document(id);
        documentReference.addSnapshotListener((value, error) -> {
            String name = value.getString("name");
            String lastName = value.getString("last_name");
            String patronymic = value.getString("patronymic");
            String role = value.getString(ROLE);
            String email = value.getString(EMAIL);
            String permission = value.getString(PERMISSION);
            String password = value.getString("password");
            user.setValue(new User(name, lastName, patronymic, email, role, permission, password));
        });
        return user;
    }

}
