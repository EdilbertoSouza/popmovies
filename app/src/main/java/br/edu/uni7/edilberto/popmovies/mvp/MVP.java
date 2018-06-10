package br.edu.uni7.edilberto.popmovies.mvp;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import br.edu.uni7.edilberto.popmovies.domain.Movie;


public interface MVP {
    interface ModelImpl{
        public void retrieveMovies();
        public void updateIsFavoriteMovie(Movie movie);
    }

    interface PresenterImpl{
        public void retrieveMovies(Bundle savedInstanceState);
        public void retrieveMovies();
        public void retrieveFavoriteMovies();
        public void updateIsFavoriteMovie(Movie movie);
        public void showToast(String message);
        public void showProgressBar(boolean status);
        public void setView(MVP.ViewImpl view);
        public Context getContext();
        public void updateListRecycler(ArrayList<Movie> m);
        public void updateItemRecycler(Movie m);
        public ArrayList<Movie> getMovies();
    }

    interface ViewImpl{
        String MOVIES_KEY = "movies";

        public void showToast(String message);
        public void showProgressBar(int visibility);
        public void updateListRecycler();
        public void updateItemRecycler(int position);
    }
}
