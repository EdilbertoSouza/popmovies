package br.edu.uni7.edilberto.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.edu.uni7.edilberto.popmovies.domain.Movie;

public class MovieActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivFavorite;
    private ImageView ivMovie;
    private TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivFavorite = (ImageView) findViewById(R.id.iv_favorite);
        ivMovie = (ImageView) findViewById(R.id.iv_movie);
        tvOverview = (TextView) findViewById(R.id.tv_overview);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        tvTitle.setText(movie.getTitle() + " (" + movie.getRelease_date().substring(0,4) + ")");
        ivFavorite.setImageResource(movie.getIsFavoriteIcone());
        Picasso
                .get()
                .load("https://image.tmdb.org/t/p/w300/" + movie.getPoster_path())
                .into(ivMovie);
        tvOverview.setText(movie.getOverview());
    }

}
