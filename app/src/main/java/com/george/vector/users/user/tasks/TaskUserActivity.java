package com.george.vector.users.user.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class TaskUserActivity extends AppCompatActivity {

    private static final String TAG = "TaskUserActivity";
    MaterialToolbar toolbar;
    TextView text_view_address_task_user, text_view_floor_task_user, text_view_cabinet_task_user,
            text_view_name_task_user, text_view_comment_task_user, text_view_status_task_user,
            text_view_date_create_task_user;
    LinearProgressIndicator progress_bar_task_user;
    ImageView image_user_task;

    String id, permission, collection, address, floor, cabinet, litera, name_task, comment, status, date_create, time_create, image;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_user);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(getString(R.string.id)).toString();
        permission = arguments.getString(getString(R.string.permission));

        toolbar = findViewById(R.id.topAppBar_task_user);
        text_view_address_task_user = findViewById(R.id.text_view_address_task_user);
        text_view_floor_task_user = findViewById(R.id.text_view_floor_task_user);
        text_view_cabinet_task_user = findViewById(R.id.text_view_cabinet_task_user);
        text_view_name_task_user = findViewById(R.id.text_view_name_task_user);
        text_view_comment_task_user = findViewById(R.id.text_view_comment_task_user);
        text_view_status_task_user = findViewById(R.id.text_view_status_task_user);
        text_view_date_create_task_user = findViewById(R.id.text_view_date_create_task_user);
        progress_bar_task_user = findViewById(R.id.progress_bar_task_user);
        image_user_task = findViewById(R.id.image_user_task);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

         setSupportActionBar(toolbar);
         toolbar.setNavigationOnClickListener(v -> onBackPressed());

         if(permission.equals(getString(R.string.ost_school)))
             collection = getString(R.string.ost_school_new);

         if(permission.equals(getString(R.string.bar_school)))
             collection = getString(R.string.bar_school_new);

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_user.setVisibility(View.VISIBLE);
            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            litera = value.getString("litera");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                progress_bar_task_user.setVisibility(View.INVISIBLE);
                image_user_task.setImageResource(R.drawable.no_image);
            } else {

                String IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image);
                Picasso.with(this)
                        .load(IMAGE_URL)
                        .into(image_user_task, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress_bar_task_user.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                Log.e(TAG, "Error on download");
                            }
                        });
            }

            if (!litera.equals("-") && !litera.isEmpty())
                cabinet = String.format("%s%s", cabinet, litera);

            text_view_address_task_user.setText(address);
            text_view_floor_task_user.setText(floor);
            text_view_cabinet_task_user.setText(cabinet);
            text_view_name_task_user.setText(name_task);
            text_view_comment_task_user.setText(comment);
            text_view_status_task_user.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_user.setText(date_create_text);

        });
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_task_user), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }
}