package com.george.vector.users.user.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.PERMISSION;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.common.tasks.fragmentImageTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class TaskUserActivity extends AppCompatActivity {

    private static final String TAG = "TaskUserActivity";
    MaterialToolbar toolbar;
    TextView text_view_address_task_user, text_view_floor_task_user, text_view_cabinet_task_user,
            text_view_name_task_user, text_view_comment_task_user, text_view_status_task_user,
            text_view_date_create_task_user, text_view_full_name_creator_user, text_view_email_creator_task_user;
    LinearProgressIndicator progress_bar_task_user;

    String id, permission, collection, address, floor, cabinet, letter, name_task,
            comment, status, date_create, time_create, image, email_creator, full_name_creator;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_user);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(ID).toString();
        permission = arguments.getString(PERMISSION);

        toolbar = findViewById(R.id.topAppBar_task_user);
        text_view_address_task_user = findViewById(R.id.text_view_address_task_user);
        text_view_floor_task_user = findViewById(R.id.text_view_floor_task_user);
        text_view_cabinet_task_user = findViewById(R.id.text_view_cabinet_task_user);
        text_view_name_task_user = findViewById(R.id.text_view_name_task_user);
        text_view_comment_task_user = findViewById(R.id.text_view_comment_task_user);
        text_view_status_task_user = findViewById(R.id.text_view_status_task_user);
        text_view_date_create_task_user = findViewById(R.id.text_view_date_create_task_user);
        progress_bar_task_user = findViewById(R.id.progress_bar_task_user);
        text_view_full_name_creator_user = findViewById(R.id.text_view_full_name_creator_user);
        text_view_email_creator_task_user = findViewById(R.id.text_view_email_creator_task_user);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (permission.equals(OST_SCHOOL))
            collection = OST_SCHOOL_NEW;

        if (permission.equals(BAR_SCHOOL))
            collection = BAR_SCHOOL_NEW;

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);

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

            text_view_address_task_user.setText(address);
            text_view_floor_task_user.setText(floor);
            text_view_cabinet_task_user.setText(cabinet);
            text_view_name_task_user.setText(name_task);
            text_view_comment_task_user.setText(comment);
            text_view_status_task_user.setText(status);
            text_view_email_creator_task_user.setText(email_creator);
            text_view_full_name_creator_user.setText(full_name_creator);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_user.setText(date_create_text);

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
            } else {
                Fragment image_fragment = new fragmentImageTask();

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

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_user.setVisibility(View.INVISIBLE));
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