package br.edu.uni7.edilberto.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.uni7.edilberto.popmovies.adapter.MoviesAdapter;
import br.edu.uni7.edilberto.popmovies.domain.Movie;
import br.edu.uni7.edilberto.popmovies.mvp.MVP;
import br.edu.uni7.edilberto.popmovies.mvp.Presenter;

public class MainActivity extends AppCompatActivity implements MVP.ViewImpl {

    private MoviesAdapter adapter;
    private static MVP.PresenterImpl presenter;

    private EditText login;
    private EditText password;
    private Button btLogin;
    private Button btCreate;
    private LinearLayout llLogin;
    private LinearLayout llListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        btCreate = findViewById(R.id.bt_create);
        llLogin = findViewById(R.id.ll_login);
        llListMovies = findViewById(R.id.ll_list_movies);

        btLogin.setOnClickListener(btLoginOnClickListener);
        btCreate.setOnClickListener(btCreateOnClickListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);

        if( presenter == null ){
            presenter = new Presenter();
        }
        presenter.setView( this );
        presenter.retrieveMovies( savedInstanceState );
    }

    @Override
    protected void onStart() {
        super.onStart();

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new MoviesAdapter( this, presenter.getMovies() );
        rvMovies.setAdapter( adapter );
        rvMovies.setLayoutManager( layoutManager );
    }

    public View.OnClickListener btLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signIn(login.getText().toString(), password.getText().toString());
        }
    };

    public View.OnClickListener btCreateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createAccount(login.getText().toString(), password.getText().toString());
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_KEY, presenter.getMovies());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);

        switch (item.getItemId()) {
            case R.id.action_favorite:
                presenter.retrieveFavoriteMovies();
                adapter = new MoviesAdapter( this, presenter.getMovies() );
                rvMovies.setAdapter( adapter );
                rvMovies.setLayoutManager( layoutManager );
                return true;
            case R.id.action_popular:
                presenter.retrieveMovies();
                adapter = new MoviesAdapter( this, presenter.getMovies() );
                rvMovies.setAdapter( adapter );
                rvMovies.setLayoutManager( layoutManager );
                return true;
            case R.id.action_signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateIsFavoriteMovie(Movie movie){
        presenter.updateIsFavoriteMovie(movie);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar(int Visibility){
        findViewById(R.id.pb_loading).setVisibility(Visibility);
    }

    public void updateListRecycler(){
        adapter.notifyDataSetChanged();
    }

    public void updateItemRecycler(int position){
        adapter.notifyItemChanged(position);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = login.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showToast("Login Required");
            valid = false;
        }

        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            showToast("Password Required");
            valid = false;
        }

        return valid;
    }

    private void signIn(String login, String password) {
        if (!validateForm()) return;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.v("firebase", "onSuccess");
                        llLogin.setVisibility(View.GONE);
                        llListMovies.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("firebase", "onFailure");
                        showToast("Authentication failed.");
                    }
                });
    }

    private void createAccount(String login, String password) {
        if (!validateForm()) return;

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.v("Firebase", "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            llLogin.setVisibility(View.GONE);
                            llListMovies.setVisibility(View.VISIBLE);
                        } else {
                            Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                            showToast("Authentication failed.");
                        }
                    }
                });
    }

    private void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        llLogin.setVisibility(View.VISIBLE);
        llListMovies.setVisibility(View.GONE);
    }

}
