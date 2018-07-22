package com.elisp.mymovies.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.elisp.mymovies.CheckConnection;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieDetail;
import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.Model.MovieResponse;
import com.elisp.mymovies.Model.PeopleDetils;
import com.elisp.mymovies.Model.PeopleModel;
import com.elisp.mymovies.Model.User;
import com.elisp.mymovies.MovieImg;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
    Context activity;
    public static final int requste_max_time_in_seconds = 20;
    public RestApi(Context activity) {
        this.activity = activity;
    }

    public Retrofit getRetrofitInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .readTimeout(requste_max_time_in_seconds, TimeUnit.SECONDS)
                .connectTimeout(requste_max_time_in_seconds, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(ApiConstants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public ApiService request() {return getRetrofitInstance().create(ApiService.class);}
    //fragments api
    public Call<MovieList> getPopular() {
        return request().getPopular();
    }
    public Call<MovieList> getTopRated() {
        return request().getTopRated();
    }
    public Call<MovieList> getUpcoming() {
        return request().getUpcoming();
    }
    public Call<MovieList> getNowPlaying() {
        return request().getNowPlaying();
    }
    //account
    public Call<User> getRequestToken(String token) {
        return request().getRequestToken(token);
    }
    public Call<User> getSessionId(String token) {
        return request().getSessionId(token);
    }
    public Call<User> getAccountDetails(String sessionId) {return request().getAccountDetails(sessionId);}
    public Call<User> validateUser(String token,String session_id, String username, String password) {
        return request().validateUser(token,session_id,username, password);
    }
    //search

    public Call<MovieList> searchMovie(CharSequence id) {
        return request().searchMovie(id);
    }
    public Call<PeopleModel> searchPeople(CharSequence id) {
        return request().searchPeople(id);
    }
    //movie details
    public Call<MovieDetail> movieDetails(String id) {return request().movieDetails(id);}
    public Call<MovieList> getVideo(String id) {
        return request().getMovieVideo(id);
    }
    public Call<MovieDetail> getMovieGenre(String id) {return request().getMovieGenre(id);}

    //my movies

    public Call<Movie> postFavourite(String sessionId, String header, MovieResponse movieResponse) {
        return request().postFavourite(sessionId,header,movieResponse);}

    public Call<Movie> postWatchlist(String sessionId, String header, MovieResponse movieResponse) {
        return request().postWatchlist(sessionId,header,movieResponse);}
    public Call<Movie> postRated(String sessionId,String movieId, String header, MovieResponse movieResponse) {
        return request().postRating(sessionId,movieId,header,movieResponse);}

        //get movies
        public Call<MovieList> getFavourites(String session_id,String account_id) {
            return request().getFavouriteMovies(session_id,account_id);
        }
    public Call<MovieList> getWatchlist(String session_id,String account_id) {
        return request().getWatchList(session_id,account_id);
    }

    public Call<MovieList> getRated(String account_id,String session_id) {
        return request().getRated(account_id,session_id);
    }
    public Call<MovieImg> getImg(String path) {
        return request().getImage(path);
    }

    // person
    public Call<PeopleModel> getPerson() {
        return request().getPerson();
    }

    public Call<PeopleDetils> getPersonDetails(String person_id) {
        return request().getPersonDetails(person_id);
    }

    public Call<PeopleModel> getPersonMovies(String person_id) {
        return request().getPersonMovies(person_id);
    }
    public Call<Movie> deleteRatingMovie(String movie_id, String header, String session_id){
        return request().deleteRatingMovie(movie_id, header, session_id);
    }
    //check connection
    public void checkInternet(Runnable callback) {
        if (CheckConnection.CheckInternetConnectivity(activity, true, callback))
            callback.run();
    }

    public void checkInternet(Runnable callback, boolean showConnectionDialog) {
        if (CheckConnection.CheckInternetConnectivity(activity, showConnectionDialog, callback))
            callback.run();
        else {
            Toast.makeText(activity, "Connection failed, please check your connection settings", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkInternet(Runnable callback, boolean showConnectionDialog, String message) {
        if (CheckConnection.CheckInternetConnectivity(activity, showConnectionDialog, callback))
            callback.run();
        else {
            if (showConnectionDialog)
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            else
                Log.d("Connection failed", "" + message);
        }
    }

}