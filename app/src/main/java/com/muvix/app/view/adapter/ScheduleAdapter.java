package com.muvix.app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muvix.app.R;
import com.muvix.app.model.Movie;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final OnScheduleClickListener listener;

    private final String[] times = {
            "02:08", "01:26", "00:17", "23:41", "21:30", "19:10", "17:45"
    };

    public interface OnScheduleClickListener {
        void onMovieClick(Movie movie);
    }

    public ScheduleAdapter(OnScheduleClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(ArrayList<Movie> data) {
        movies.clear();
        movies.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.tvTime.setText(times[position % times.length]);
        holder.tvTitle.setText(movie.title);
        holder.tvEpisode.setText(movie.episode);
        holder.tvMeta.setText("⭐ " + movie.rating + " • " + movie.duration);

        if (position < 3) {
            holder.tvStatus.setText("● Sudah Tayang");
        } else {
            holder.tvStatus.setText("● Menunggu Update");
        }

        Glide.with(holder.itemView.getContext())
                .load(movie.posterUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTime, tvTitle, tvEpisode, tvMeta, tvStatus;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEpisode = itemView.findViewById(R.id.tvEpisode);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}