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

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.SubscribeViewHolder> {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final OnSubscribeClickListener listener;

    public interface OnSubscribeClickListener {
        void onMovieClick(Movie movie);
    }

    public SubscribeAdapter(OnSubscribeClickListener listener) {
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
    public SubscribeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscribe_movie, parent, false);
        return new SubscribeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribeViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String title = movie.title != null && !movie.title.isEmpty()
                ? movie.title
                : "Untitled Movie";

        String episode = movie.episode != null && !movie.episode.isEmpty()
                ? movie.episode
                : "Movie";

        String views = movie.views != null && !movie.views.isEmpty()
                ? movie.views
                : "0 views";

        double rating = movie.rating > 0 ? movie.rating : 8.0;

        holder.tvTitle.setText(title);
        holder.tvEpisode.setText(episode);
        holder.tvViews.setText("Views: " + views);
        holder.tvRating.setText(String.format(java.util.Locale.getDefault(), "Rate %.1f", rating));

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

    static class SubscribeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvEpisode, tvViews, tvRating;

        SubscribeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEpisode = itemView.findViewById(R.id.tvEpisode);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}