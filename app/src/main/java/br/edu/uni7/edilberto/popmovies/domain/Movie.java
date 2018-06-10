package br.edu.uni7.edilberto.popmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import br.edu.uni7.edilberto.popmovies.R;

public class Movie implements Parcelable {
    public static final String ID_KEY = "id";
    public static final String IS_FAVORITE_KEY = "is-favorite";

    private int id;
    private String title;
    private boolean favorite;
    private boolean adult;
    private String poster_path;
    private String original_title;
    private String overview;
    private String release_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getIsFavoriteIcone(){
        if( isFavorite() ){
            return R.drawable.ic_favorite_marked;
        } else {
            return R.drawable.ic_favorite;
        }
    }

    public void setIsFavoriteIcone(int icone){
        if( R.drawable.ic_favorite_marked == icone ){
            Log.i("Info", "Marked");
        } else {
            Log.i("Info", "No Marked");
        }
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.favorite = in.readByte() != 0;
        this.adult = in.readByte() != 0;
        this.poster_path = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}