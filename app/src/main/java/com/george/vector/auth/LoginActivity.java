package com.george.vector.auth;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LAST_NAME;
import static com.george.vector.common.consts.Keys.NAME;
import static com.george.vector.common.consts.Keys.PATRONYMIC;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.ROLE;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Keys.USER_DATA;
import static com.george.vector.common.consts.Keys.USER_DATA_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_ROLE;
import static com.george.vector.common.consts.Logs.TAG_LOGIN_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_VALIDATE_FILED;
import static com.george.vector.common.utils.Utils.validateEmail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email_login_text_layout, password_login_text_layout;
    Button btn_login;
    LinearProgressIndicator progress_bar_auth;
    CoordinatorLayout coordinator_login;

    String emailED, passwordED, userID;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        email_login_text_layout = findViewById(R.id.email_login_text_layout);
        password_login_text_layout = findViewById(R.id.password_login_text_layout);
        progress_bar_auth = findViewById(R.id.progress_bar_auth);
        coordinator_login = findViewById(R.id.coordinator_login_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btn_login.setOnClickListener(v -> {
            emailED = Objects.requireNonNull(email_login_text_layout.getEditText()).getText().toString();
            passwordED = Objects.requireNonNull(password_login_text_layout.getEditText()).getText().toString();

            if(isOnline()) {
                if (validateFields()) {
                    if(!validateEmail(emailED)) {
                        Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                        email_login_text_layout.setError("Не корректный формат e-mail");
                    } else {
                        progress_bar_auth.setVisibility(View.VISIBLE);
                        firebaseAuth.signInWithEmailAndPassword(emailED, passwordED).addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                progress_bar_auth.setVisibility(View.INVISIBLE);
                                Log.d(TAG_LOGIN_ACTIVITY, "Login success");

                                userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                                DocumentReference documentReference = firebaseFirestore.collection(USERS).document(userID);
                                documentReference.addSnapshotListener(this, (value, error) -> {
                                    assert value != null;

                                    String name = value.getString("name");
                                    String last_name = value.getString("last_name");
                                    String patronymic = value.getString("patronymic");
                                    String role = value.getString(ROLE);
                                    String email = value.getString(EMAIL);
                                    String permission = value.getString(PERMISSION);
                                    Log.d(TAG_LOGIN_ACTIVITY, String.format("permission - %s", permission));
                                    Log.d(TAG_LOGIN_ACTIVITY, String.format("role - %s", role));
                                    Log.d(TAG_LOGIN_ACTIVITY, String.format("email - %s", email));

                                    assert role != null;
                                    startApp(name, last_name, patronymic, role, email, permission);

                                });

                            }

                        }).addOnFailureListener(e -> {
                            progress_bar_auth.setVisibility(View.INVISIBLE);
                            Snackbar.make(coordinator_login, e.toString(), Snackbar.LENGTH_LONG).show();
                        });
                    }
                }
            } else
                onStart();

        });


    }

    void startApp(@NotNull String name, String last_name, String patronymic, String role, String email, String permission) {
        SharedPreferences mDataUser;
        mDataUser = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = mDataUser.edit();
        editor.putString(NAME, name);
        editor.putString(LAST_NAME, last_name);
        editor.putString(PATRONYMIC, patronymic);
        editor.putString(USER_DATA_EMAIL, email);
        editor.putString(USER_PERMISSION, permission);
        editor.putString(USER_ROLE, role);

        editor.apply();

        if (role.equals("Root")) {
            Intent intent = new Intent(this, RootMainActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        }

        if (role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);
        }

        if (role.equals("Исполнитель")) {
            Intent intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        }
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
            Snackbar.make(findViewById(R.id.coordinator_login_activity), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG_LOGIN_ACTIVITY, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(email_login_text_layout);
        utils.clear_error(password_login_text_layout);

        boolean checkEmail = utils.validate_field(emailED, email_login_text_layout);
        boolean checkPassword = utils.validate_field(passwordED, password_login_text_layout);
        return checkEmail & checkPassword;
    }

}