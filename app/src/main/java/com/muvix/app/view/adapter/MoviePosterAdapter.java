package com.muvix.app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muvix.app.R;
import com.muvix.app.model.Movie;

import java.util.ArrayList;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MoviePosterAdapter(OnMovieClickListener listener) {
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
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_poster, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
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

        holder.tvTitle.setText(title);
        holder.tvEpisode.setText(episode);
        holder.tvViews.setText(views);
        holder.tvRating.setText("â­ " + (movie.rating == 0 ? "-" : movie.rating));

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

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvEpisode, tvViews, tvRating;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEpisode = itemView.findViewById(R.id.tvEpisode);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
