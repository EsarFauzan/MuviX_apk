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
    private String dayMode = "Hari Ini";

    public interface OnScheduleClickListener {
        void onMovieClick(Movie movie);
    }

    public ScheduleAdapter(OnScheduleClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(ArrayList<Movie> data, String dayMode) {
        movies.clear();

        if (data != null) {
            movies.addAll(data);
        }

        this.dayMode = dayMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_movie, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String title = movie.title != null && !movie.title.isEmpty()
                ? movie.title
                : "Untitled Movie";

        String genre = movie.genre != null && !movie.genre.isEmpty()
                ? movie.genre
                : "Movie";

        String duration = movie.duration != null && !movie.duration.isEmpty()
                ? movie.duration
                : "120 min";

        holder.tvTitle.setText(title);
        holder.tvMeta.setText(genre + " | " + duration);
        holder.tvScheduleTime.setText(dayMode + " | " + getTime(position) + " WITA");

        if (dayMode.equals("Hari Ini") && position % 3 == 0) {
            holder.tvStatus.setText("Sudah Tayang");
        } else {
            holder.tvStatus.setText("Akan Tayang");
        }

        Glide.with(holder.itemView.getContext())
                .load(movie.posterUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .error(R.drawable.bg_card)
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private String getTime(int position) {
        String[] times = {
                "10:00", "13:30", "16:00", "19:00", "21:30", "23:00"
        };

        return times[position % times.length];
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvMeta, tvScheduleTime, tvStatus;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            tvScheduleTime = itemView.findViewById(R.id.tvScheduleTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
