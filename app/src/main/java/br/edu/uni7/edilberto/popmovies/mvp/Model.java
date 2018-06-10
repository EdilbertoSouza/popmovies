package br.edu.uni7.edilberto.popmovies.mvp;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import br.edu.uni7.edilberto.popmovies.domain.Movie;
import br.edu.uni7.edilberto.popmovies.network.JsonHttpRequest;


public class Model implements MVP.ModelImpl {
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private MVP.PresenterImpl presenter;

    public Model( MVP.PresenterImpl presenter ){
        this.presenter = presenter;
    }

    @Override
    public void retrieveMovies() {
        RequestParams requestParams = new RequestParams(JsonHttpRequest.METODO_KEY, "");
        asyncHttpClient.get( presenter.getContext(),
                JsonHttpRequest.URI,
                requestParams,
                new JsonHttpRequest( presenter ));
    }

    @Override
    public void updateIsFavoriteMovie(Movie movie) {

        final String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if (movie.isFavorite()) {
            firebaseDatabase
                    .getReference()
                    .child("favorites")
                    .child(uID)
                    .child(String.valueOf(movie.getId()))
                    .setValue(movie);
        } else {
            firebaseDatabase
                    .getReference()
                    .child("favorites")
                    .child(uID)
                    .child(String.valueOf(movie.getId()))
                    .setValue(null);
        }

    }

}
