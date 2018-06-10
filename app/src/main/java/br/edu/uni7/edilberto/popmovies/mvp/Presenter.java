package br.edu.uni7.edilberto.popmovies.mvp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.stream.Collectors;

import br.edu.uni7.edilberto.popmovies.domain.Movie;


public class Presenter implements MVP.PresenterImpl {

    private MVP.ModelImpl model;
    private MVP.ViewImpl view;
    private ArrayList<Movie> movies = new ArrayList<Movie>();

    public Presenter(){
        model = new Model( this );
    }

    @Override
    public void setView( MVP.ViewImpl view ){
        this.view = view;
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void retrieveMovies(Bundle savedInstanceState) {
        if( savedInstanceState != null ){
            movies = savedInstanceState.getParcelableArrayList( MVP.ViewImpl.MOVIES_KEY );
            return;
        }
        model.retrieveMovies();
    }

    @Override
    public void retrieveMovies() {
        model.retrieveMovies();
    }

    @Override
    public void retrieveFavoriteMovies() {
        ArrayList<Movie> favoriteMovies = new ArrayList<Movie>();

        int i;
        for (i=0; i < movies.size(); i++){
            if (movies.get(i).isFavorite()) {
                favoriteMovies.add(movies.get(i));
            }
        }
        updateListRecycler(favoriteMovies);
    }

    @Override
    public void updateIsFavoriteMovie(Movie movie) {
        movie.setFavorite(!movie.isFavorite());
        model.updateIsFavoriteMovie(movie);
    }

    @Override
    public void showToast(String message) {
        view.showToast( message );
    }

    @Override
    public void showProgressBar(boolean status) {
        int visibility = status ? View.VISIBLE : View.GONE;
        view.showProgressBar( visibility );
    }

    @Override
    public void updateListRecycler(ArrayList<Movie> m) {
        movies.clear();
        movies.addAll( m );
        view.updateListRecycler();
    }

    @Override
    public void updateItemRecycler(Movie m) {
        for(int i = 0; i < movies.size(); i++ ){
            if( movies.get(i).getId() == m.getId() ){
                movies.get(i).setFavorite( m.isFavorite() );
                view.updateItemRecycler( i );
                break;
            }
        }
    }

    @Override
    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
