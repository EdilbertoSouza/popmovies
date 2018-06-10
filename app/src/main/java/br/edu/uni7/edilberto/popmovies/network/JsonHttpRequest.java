package br.edu.uni7.edilberto.popmovies.network;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.edu.uni7.edilberto.popmovies.domain.Movie;
import br.edu.uni7.edilberto.popmovies.mvp.MVP;
import cz.msebera.android.httpclient.Header;


public class JsonHttpRequest extends JsonHttpResponseHandler {
    public static final String URI = "https://api.themoviedb.org/3/movie/popular?api_key=c08de78bb7f7291eb7ae7b3c368f1b7a&language=pt-BR&page=1";
    public static final String METODO_KEY = "";

    private MVP.PresenterImpl presenter;


    public JsonHttpRequest( MVP.PresenterImpl presenter ){
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        presenter.showProgressBar( true );
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            JSONArray results = response.getJSONArray("results");

            Gson gson = new Gson();
            final ArrayList<Movie> movies = new ArrayList<>();
            Movie m;

            for( int i = 0; i < results.length(); i++ ){
                m = gson.fromJson( results.getJSONObject( i ).toString(), Movie.class );
                movies.add( m );
            }

            final String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

            firebaseDatabase
                    .getReference()
                    .child("favorites")
                    .child(uID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Movie movieFavorite;
                            int i;
                            for( DataSnapshot child : dataSnapshot.getChildren()){
                                movieFavorite = child.getValue(Movie.class);

                                for (i=0; i < movies.size(); i++){
                                    if (movies.get(i).getId() == movieFavorite.getId()) {
                                        movies.get(i).setFavorite(true);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Error", "onCancelled", databaseError.toException());
                        }
                    });

            presenter.updateListRecycler(movies);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Gson gson = new Gson();
        ArrayList<Movie> movies = new ArrayList<>();
        Movie m;

        for( int i = 0; i < response.length(); i++ ){
            try{
                m = gson.fromJson( response.getJSONObject( i ).toString(), Movie.class );
                movies.add( m );
            }
            catch(JSONException e){}
        }
        presenter.updateListRecycler(movies);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        presenter.showToast( responseString );
    }

    @Override
    public void onFinish() {
        presenter.showProgressBar( false );
    }

}