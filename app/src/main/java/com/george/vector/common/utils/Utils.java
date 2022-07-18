package com.george.vector.common.utils;

import static com.george.vector.common.consts.Keys.USERS;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.network.model.User;
import com.george.vector.ui.adapter.UserAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    Query query;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = firebaseFirestore.collection(USERS);

    public boolean validateField(@NonNull String text, TextInputLayout inputLayout) {
        if (text.isEmpty()) {
            inputLayout.setError("Это поле не может быть пустым");
            inputLayout.setErrorEnabled(true);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void validateNumberField(String text, TextInputLayout inputLayout, ExtendedFloatingActionButton actionButton, int length) {
        if (!validateNumberText(text)) {
            inputLayout.setError("Ошибка! Проверьте правильность написания");
            actionButton.setClickable(false);
            inputLayout.setErrorEnabled(true);
        } else if (text.length() > length) {
            actionButton.setClickable(false);
            inputLayout.setErrorEnabled(true);
        } else {
            actionButton.setClickable(true);
            inputLayout.setErrorEnabled(false);
        }
    }

    public void clearError(@NonNull TextInputLayout inputLayout) {
        Objects.requireNonNull(inputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                inputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    Date currentDate = new Date();

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public String getTime() {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(currentDate);
    }

    public static final Pattern VALID_NUMBER =
            Pattern.compile("^[1-9]", Pattern.CASE_INSENSITIVE);

    public static boolean validateNumberText(String emailStr) {
        Matcher matcher = VALID_NUMBER.matcher(emailStr);
        return matcher.find();
    }

    public void showAddExecutorDialog(Context context,
                               TextInputLayout textLayoutExecutorEmail,
                               TextInputLayout textLayoutFullNameExecutor) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recyclerViewListExecutors = dialog.findViewById(R.id.recyclerViewListExecutors);
        Chip chipRootDialog = dialog.findViewById(R.id.chip_root_dialog);
        Chip chipExecutorsDialog = dialog.findViewById(R.id.chip_executors_dialog);

        query = usersRef.whereEqualTo("role", "Root");
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        UserAdapter adapter = new UserAdapter(options);

        recyclerViewListExecutors.setHasFixedSize(true);
        recyclerViewListExecutors.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewListExecutors.setAdapter(adapter);

        chipRootDialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                query = usersRef.whereEqualTo("role", "Root");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        chipExecutorsDialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                query = usersRef.whereEqualTo("role", "Исполнитель");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(id);
            documentReference.addSnapshotListener((value, error) -> {
                assert value != null;
                String nameExecutor = value.getString("name");
                String lastNameExecutor = value.getString("last_name");
                String patronymicExecutor = value.getString("patronymic");
                String emailExecutor = value.getString("email");
                String fullNameExecutor = String.format("%s %s %s", lastNameExecutor, nameExecutor, patronymicExecutor);

                Objects.requireNonNull(textLayoutExecutorEmail.getEditText()).setText(emailExecutor);
                Objects.requireNonNull(textLayoutFullNameExecutor.getEditText()).setText(fullNameExecutor);

                adapter.stopListening();
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

}
