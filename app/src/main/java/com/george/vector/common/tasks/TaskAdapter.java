package com.george.vector.common.tasks;

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

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskHolder> {

    private OnItemClickListener listener;


    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position, @NonNull Task model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewPriority.setText(model.getPriority());
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class TaskHolder extends RecyclerView.ViewHolder {

        final TextView textViewTitle;
        final TextView textViewDescription;
        final TextView textViewPriority;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_nate_task);
            textViewDescription = itemView.findViewById(R.id.text_view_address);
            textViewPriority = itemView.findViewById(R.id.date_create);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null)
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}