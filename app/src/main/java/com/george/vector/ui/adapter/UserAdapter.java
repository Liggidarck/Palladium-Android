package com.george.vector.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.vector.R;
import com.george.vector.network.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private onItemUserClickListener listener;
    private List<User> users = new ArrayList<>();

    public static final String TAG = UserAdapter.class.getSimpleName();

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = users.get(position);

        String name = user.getName();
        String lastName = user.getLastName();
        String patronymic = user.getPatronymic();
        String fullName = String.format("%s %s %s", lastName, name, patronymic);

        holder.textViewName.setText(fullName);
        holder.textViewEmail.setText(user.getEmail());
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(root);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        final TextView textViewName;
        final TextView textViewEmail;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name_user);
            textViewEmail = itemView.findViewById(R.id.text_view_email_user);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null)
                    listener.onItemClick(users.get(position), position);
            });

        }
    }

    public interface onItemUserClickListener {
        void onItemClick(User user, int position);
    }

    public void setOnItemClickListener(onItemUserClickListener listener) {
        this.listener = listener;
    }

}
