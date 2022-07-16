package com.george.vector.ui.adapters;

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
    protected void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position, @NonNull Task model) {
        holder.textViewTitle.setText(model.getName_task());
        holder.textViewDescription.setText(model.getAddress());
        holder.textViewPriority.setText(model.getDate_create());
        holder.textViewTimeCreate.setText(model.getTime_create());

        boolean visible_urgent_task = model.getUrgent();
        if(visible_urgent_task)
            holder.image_warning_task.setVisibility(View.VISIBLE);
        else
            holder.image_warning_task.setVisibility(View.INVISIBLE);

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
        final TextView textViewTimeCreate;
        final ImageView image_warning_task;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_nate_task);
            textViewDescription = itemView.findViewById(R.id.text_view_address);
            textViewPriority = itemView.findViewById(R.id.date_create);
            textViewTimeCreate = itemView.findViewById(R.id.text_view_time_create);
            image_warning_task = itemView.findViewById(R.id.image_warning_task);

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