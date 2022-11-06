package com.george.vector.ui.common.help.root.develop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.vector.R;

import java.util.List;

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {

    private final List<Release> releaseList;
    private final LayoutInflater inflater;

    ReleaseAdapter (Context context, List<Release> releaseList) {
        this.releaseList = releaseList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.develop_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Release release = releaseList.get(position);
        holder.title_version.setText(release.getTitle());
        holder.date_release.setText(release.getDate());
        holder.description_release.setText(release.getDescription());
    }

    @Override
    public int getItemCount() {
        return releaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title_version;
        final TextView date_release;
        final TextView description_release;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_version = itemView.findViewById(R.id.title_version);
            date_release = itemView.findViewById(R.id.date_release);
            description_release = itemView.findViewById(R.id.description_release);
        }
    }

}
