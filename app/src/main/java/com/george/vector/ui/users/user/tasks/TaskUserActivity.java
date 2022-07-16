package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.ui.tasks.FragmentImageTask;
import com.george.vector.databinding.ActivityTaskUserBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class TaskUserActivity extends AppCompatActivity {

    private static final String TAG = "TaskUserActivity";
    String id, collection, address, floor, cabinet, letter, name_task,
            comment, status, date_create, time_create, image, email_creator, full_name_creator, email_user;

    FirebaseFirestore firebase_firestore;
    FirebaseStorage firebase_storage;

    ActivityTaskUserBinding userBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBinding = ActivityTaskUserBinding.inflate(getLayoutInflater());
        setContentView(userBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        email_user = arguments.getString(EMAIL);

        firebase_firestore = FirebaseFirestore.getInstance();
        firebase_storage = FirebaseStorage.getInstance();

        setSupportActionBar(userBinding.topAppBarTaskUser);
        userBinding.topAppBarTaskUser.setNavigationOnClickListener(v -> onBackPressed());

        DocumentReference documentReference = firebase_firestore.collection(collection).document(id);

        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            letter = value.getString("litera");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            image = value.getString("image");
            email_creator = value.getString("email_creator");
            full_name_creator = value.getString("nameCreator");

            if (!letter.equals("-") && !letter.isEmpty())
                cabinet = String.format("%s%s", cabinet, letter);

            userBinding.textViewAddressTaskUser.setText(address);
            userBinding.textViewFloorTaskUser.setText(floor);
            userBinding.textViewCabinetTaskUser.setText(cabinet);
            userBinding.textViewNameTaskUser.setText(name_task);
            userBinding.textViewCommentTaskUser.setText(comment);
            userBinding.textViewStatusTaskUser.setText(status);
            userBinding.textViewEmailCreatorTaskUser.setText(email_creator);
            userBinding.textViewFullNameCreatorUser.setText(full_name_creator);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            userBinding.textViewDateCreateTaskUser.setText(date_create_text);

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
            } else {
                Fragment image_fragment = new FragmentImageTask();

                Bundle bundle = new Bundle();
                bundle.putString("image_id", image);
                bundle.putString(ID, id);
                bundle.putString(COLLECTION, collection);
                bundle.putString(LOCATION, collection);
                bundle.putString(EMAIL, "null");

                image_fragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_image_task_user, image_fragment)
                        .commit();
            }


        });

        documentReference.get().addOnCompleteListener(task -> userBinding.progressBarTaskUser.setVisibility(View.INVISIBLE));
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
            Snackbar.make(findViewById(R.id.coordinator_task_user), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }
}