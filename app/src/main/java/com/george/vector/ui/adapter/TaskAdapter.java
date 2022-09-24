package com.george.vector.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.network.model.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskHolder> {

    private OnItemClickListener listener;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position, @NonNull Task task) {
        holder.textViewTitle.setText(task.getNameTask());
        holder.textViewDescription.setText(task.getAddress());
        holder.textViewPriority.setText(task.getDateCreate());
        holder.textViewTimeCreate.setText(task.getTimeCreate());

        boolean visibleUrgentTask = task.getUrgent();
        if(visibleUrgentTask)
            holder.imageWarningTask.setVisibility(View.VISIBLE);
        else
            holder.imageWarningTask.setVisibility(View.INVISIBLE);

    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskHolder(view);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        final TextView textViewTitle;
        final TextView textViewDescription;
        final TextView textViewPriority;
        final TextView textViewTimeCreate;
        final ImageView imageWarningTask;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_nate_task);
            textViewDescription = itemView.findViewById(R.id.text_view_address);
            textViewPriority = itemView.findViewById(R.id.date_create);
            textViewTimeCreate = itemView.findViewById(R.id.text_view_time_create);
            imageWarningTask = itemView.findViewById(R.id.image_warning_task);

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