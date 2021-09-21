package com.george.vector.users.user.main;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.PERMISSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.announcements.fragment_news;
import com.george.vector.common.bottom_sheets.ProfileBottomSheet;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.user.tasks.AddTaskUserActivity;
import com.george.vector.users.user.tasks.TaskUserActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class MainUserActivity extends AppCompatActivity {

    private static final String TAG = "MainUserActivity";
    ExtendedFloatingActionButton create_task_user;
    MaterialToolbar toolbar_main_user;
    Chip chip_today_user;

    FirebaseFirestore db;
    CollectionReference taskRef;

    private TaskAdapter adapter;
    private Query query;

    FirebaseFirestore firebaseFirestore;

    String permission;
    boolean show_news_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        Utils utils = new Utils();
        Bundle arguments = getIntent().getExtras();
        String email = arguments.get(EMAIL).toString();
        permission = arguments.getString(PERMISSION);
        String collection = null;

        create_task_user = findViewById(R.id.create_task_user);
        toolbar_main_user = findViewById(R.id.toolbar_main_user);
        chip_today_user = findViewById(R.id.chip_today_user);

        setSupportActionBar(toolbar_main_user);
        toolbar_main_user.setNavigationOnClickListener(v -> {
            SettingsUserBottomSheet bottomSheet = new SettingsUserBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "SettingsUserBottomSheet");
        });

        db = FirebaseFirestore.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection("news").document("news_fragment");
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            show_news_fragment = Objects.requireNonNull(value.getBoolean("show"));
            Log.d(TAG, String.format("show_news_fragment: %s", show_news_fragment));

            if(show_news_fragment)
                show_news_fragment();
        });

        create_task_user.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskUserActivity.class);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);
        });

        if (permission.equals(OST_SCHOOL))
            collection = OST_SCHOOL_NEW;

        if (permission.equals(BAR_SCHOOL))
            collection = BAR_SCHOOL_NEW;

        setUpRecyclerView(email, collection);

        chip_today_user.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "today checked");
                String today = utils.getDate();
                todayTasks(today, email);
            } else {
                Log.i(TAG, "today not-checked");
                defaultQuery(email);
            }

        });

    }

    void show_news_fragment() {
        Fragment fragment_news = new fragment_news();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_user_frame, fragment_news)
                .commit();
    }

    private void todayTasks(String date, String email) {
        query = taskRef.whereEqualTo("date_create", date).whereEqualTo("email_creator", email);

        FirestoreRecyclerOptions<TaskUi> search_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(search_options);
    }

    private void defaultQuery(String email) {
        query = taskRef.whereEqualTo("email_creator", email);

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter.updateOptions(options);
    }

    private void setUpRecyclerView(String email, String collection) {
        taskRef = db.collection(collection);

        query = taskRef.whereEqualTo("email_creator", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_view_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(this, TaskUserActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);

        });

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile_item) {
            ProfileBottomSheet bottomSheet = new ProfileBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ProfileBottomSheet");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_main_user), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "nope!");
    }
}