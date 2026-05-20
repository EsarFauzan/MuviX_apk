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
        movies.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.tvTitle.setText(movie.title);
        holder.tvEpisode.setText(movie.episode);
        holder.progressWatch.setProgress(70 + (position % 25));
        holder.tvDate.setText(formatDate(movie.watchedAt));

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

    private String formatDate(long time) {
        if (time <= 0) return "Baru saja";

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy • HH:mm", new Locale("id", "ID"));
        return format.format(new Date(time));
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvEpisode, tvDate;
        ProgressBar progressWatch;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEpisode = itemView.findViewById(R.id.tvEpisode);
            tvDate = itemView.findViewById(R.id.tvDate);
            progressWatch = itemView.findViewById(R.id.progressWatch);
        }
    }
}