package com.george.vector.common.edit_users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserHolder> {

    private onItemUserClickListener listener;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position, @NonNull User model) {
        String name = model.getName();
        String last_name = model.getLast_name();
        String patronymic = model.getPatronymic();
        String full_name = String.format("%s %s %s", name, last_name, patronymic);

        holder.textViewName.setText(full_name);
        holder.textViewEmail.setText(model.getEmail());
        holder.textViewRole.setText(model.getRole());
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(root);
    }

    public void deleteUser(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        final TextView textViewName;
        final TextView textViewEmail;
        final TextView textViewRole;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name_user);
            textViewEmail = itemView.findViewById(R.id.text_view_email_user);
            textViewRole = itemView.findViewById(R.id.text_view_role_user);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null)
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
            });

        }
    }

    interface onItemUserClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(onItemUserClickListener listener){
        this.listener = listener;
    }

}