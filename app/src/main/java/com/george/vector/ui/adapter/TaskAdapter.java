package com.george.vector.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.vector.R;
import com.george.vector.network.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = tasks.get(position);

        holder.textViewNameTask.setText(task.getName());
        holder.textViewAddress.setText(task.getAddress());
        holder.textViewTimeCreate.setText(task.getDateCreate());
        holder.textViewTimeCreate.setText(task.getDateCreate());

        boolean visibleUrgentTask = task.isUrgent();
        if (visibleUrgentTask)
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

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void filterList(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        final TextView textViewNameTask;
        final TextView textViewAddress;
        final TextView textViewTimeCreate;
        final ImageView imageWarningTask;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameTask = itemView.findViewById(R.id.text_view_nate_task);
            textViewAddress = itemView.findViewById(R.id.text_view_address);
            textViewTimeCreate = itemView.findViewById(R.id.text_view_time_create);
            imageWarningTask = itemView.findViewById(R.id.image_warning_task);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null)
                    listener.onItemClick(tasks.get(position), position);
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}