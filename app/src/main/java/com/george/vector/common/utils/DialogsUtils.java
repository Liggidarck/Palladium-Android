package com.george.vector.common.utils;

import static com.george.vector.common.utils.consts.Keys.USERS;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.network.model.User;
import com.george.vector.ui.adapter.UserAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class DialogsUtils {

    Query query;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = firebaseFirestore.collection(USERS);

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
