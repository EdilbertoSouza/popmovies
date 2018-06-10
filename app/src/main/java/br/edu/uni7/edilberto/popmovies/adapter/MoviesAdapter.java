package br.edu.uni7.edilberto.popmovies.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.uni7.edilberto.popmovies.MainActivity;
import br.edu.uni7.edilberto.popmovies.MovieActivity;
import br.edu.uni7.edilberto.popmovies.R;
import br.edu.uni7.edilberto.popmovies.domain.Movie;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private MainActivity activity;
    private ArrayList<Movie> movies;

    public MoviesAdapter(MainActivity activity, ArrayList<Movie> movies){
        this.activity = activity;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from( parent.getContext() )
                .inflate(R.layout.item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivMovie;
        private ImageView ivFavorite;
        private TextView tvTitle;

        private ViewHolder(View itemView) {
            super(itemView);

            ivMovie = (ImageView) itemView.findViewById(R.id.iv_movie);
            ivFavorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);

            tvTitle.setOnClickListener(tvTitleOnClickListener);
            ivFavorite.setOnClickListener(ivFavoriteOnClickListener);
        }

        private void setData(final Movie movie){
            tvTitle.setText(movie.getTitle());
            ivFavorite.setImageResource(movie.getIsFavoriteIcone());
        }

        @Override
        public void onClick(View view) {}

        public View.OnClickListener tvTitleOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = movies.get(getAdapterPosition());
                Intent i = new Intent(v.getContext(), MovieActivity.class);
                i.putExtra("movie", movie);
                activity.startActivity(i);
            }
        };

        public View.OnClickListener ivFavoriteOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = movies.get(getAdapterPosition());
                activity.updateIsFavoriteMovie(movie);
                ivFavorite.setImageResource(movie.getIsFavoriteIcone());
            }
        };

    }
}