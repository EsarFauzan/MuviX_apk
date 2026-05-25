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
import com.muvix.app.view.ImageSourceResolver;

import java.util.ArrayList;

public class ContinueAdapter extends RecyclerView.Adapter<ContinueAdapter.ContinueViewHolder> {
    private final ArrayList<Movie> movies = new ArrayList<>();

    public void setMovies(ArrayList<Movie> data) {
        movies.clear();
        movies.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContinueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_continue, parent, false);
        return new ContinueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContinueViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.title);
        holder.progressWatch.setProgress(65);

        Glide.with(holder.itemView.getContext())
                .load(ImageSourceResolver.resolve(
                        holder.itemView.getContext(),
                        movie.bannerUrl == null || movie.bannerUrl.isEmpty() ? movie.posterUrl : movie.bannerUrl
                ))
                .centerCrop()
                .placeholder(R.drawable.bg_card)
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ContinueViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;
        ProgressBar progressWatch;

        ContinueViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            progressWatch = itemView.findViewById(R.id.progressWatch);
        }
    }
}
