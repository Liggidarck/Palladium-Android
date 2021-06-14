package com.george.vector.admin.edit_users;

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
        holder.textViewName.setText(model.getName());
        holder.textViewLastName.setText(model.getLast_name());
        holder.textViewPatronymic.setText(model.getPatronymic());
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
        TextView textViewName, textViewLastName, textViewPatronymic, textViewEmail, textViewRole;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name_user);
            textViewLastName = itemView.findViewById(R.id.text_view_last_name_user);
            textViewPatronymic = itemView.findViewById(R.id.text_view_patronymic_user);
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
