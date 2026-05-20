package com.muvix.app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muvix.app.R;
import com.muvix.app.model.Movie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final OnHistoryClickListener listener;

    public interface OnHistoryClickListener {
        void onMovieClick(Movie movie);
    }

    public HistoryAdapter(OnHistoryClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(ArrayList<Movie> data) {
        movies.clear();

        if (data != null) {
            movies.addAll(data);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_movie, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
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
        holder.tvDate.setText("Terakhir ditonton: " + formatDate(movie.watchedAt));

        int progress = getFakeProgress(movie.id);
        holder.progressWatch.setProgress(progress);

        Glide.with(holder.itemView.getContext())
                .load(movie.posterUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .error(R.drawable.bg_card)
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
        holder.tvPlay.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private String formatDate(long time) {
        if (time <= 0) return "-";

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("id", "ID"));
        return formatter.format(new Date(time));
    }

    private int getFakeProgress(String id) {
        if (id == null) return 65;

        int hash = Math.abs(id.hashCode());
        return 35 + (hash % 60);
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvMeta, tvDate, tvPlay;
        ProgressBar progressWatch;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPlay = itemView.findViewById(R.id.tvPlay);
            progressWatch = itemView.findViewById(R.id.progressWatch);
        }
    }
}