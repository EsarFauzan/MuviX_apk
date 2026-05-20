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

public class ProfileRecentAdapter extends RecyclerView.Adapter<ProfileRecentAdapter.ProfileRecentViewHolder> {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final OnRecentClickListener listener;

    public interface OnRecentClickListener {
        void onMovieClick(Movie movie);
    }

    public ProfileRecentAdapter(OnRecentClickListener listener) {
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
    public ProfileRecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_recent, parent, false);
        return new ProfileRecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecentViewHolder holder, int position) {
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

    static class ProfileRecentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvMeta;

        ProfileRecentViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMeta = itemView.findViewById(R.id.tvMeta);
        }
    }
}
