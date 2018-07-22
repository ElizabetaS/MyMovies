package com.elisp.mymovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Model.MovieList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    RestApi api;
    MovieList model;
    MovieList model2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new RestApi(SplashActivity.this);
        String sessionId = PreferencesManager.getSessionId(SplashActivity.this);
        String token = PreferencesManager.getToken(SplashActivity.this);
        SharedPreferences preferences = getSharedPreferences("MySharedPreffsFile", 0);
        model = PreferencesManager.getFavourite(SplashActivity.this);
        if(model!=null) {
            preferences.edit().remove("Favourite").commit();
        }

        model = PreferencesManager.getWatchlist(SplashActivity.this);
        if(model!=null) {
            preferences.edit().remove("Watchlist").commit();
        }

        model = PreferencesManager.getRated(SplashActivity.this);
        if(model!=null) {
            preferences.edit().remove("Rated").commit();
        }
        if (token!=null) {
            Call<MovieList> call = api.getFavourites("account_id", sessionId);
            call.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (response.code() == 200) {
                        model = response.body();
                        if (model != null) {
                            for (int i = 0; i < model.results.size(); i++) {
                                model2 = PreferencesManager.getFavourite(SplashActivity.this);
                                if (model2 == null) {
                                    model2 = new MovieList();
                                }
                                model2.results.add(model.results.get(i));
                                PreferencesManager.addMovieFavourite(model2, SplashActivity.this);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }

            });
            Call<MovieList> call2 = api.getWatchlist("account_id", sessionId);
            call2.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (response.code() == 200) {
                        model = response.body();
                        if (model != null) {
                            for (int i = 0; i < model.results.size(); i++) {
                                model2 = PreferencesManager.getWatchlist(SplashActivity.this);
                                if (model2 == null) {
                                    model2 = new MovieList();
                                }
                                model2.results.add(model.results.get(i));
                                PreferencesManager.addMovieWatchlist(model2, SplashActivity.this);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }

            });
            Call<MovieList> call3 = api.getRated("account_id", sessionId);
            call3.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (response.code() == 200) {
                        model = response.body();
                        if (model != null) {
                            for (int i = 0; i < model.results.size(); i++) {
                                model2 = PreferencesManager.getRated(SplashActivity.this);
                                if (model2 == null) {
                                    model2 = new MovieList();
                                }
                                model2.results.add(model.results.get(i));
                                PreferencesManager.addMovieRated(model2, SplashActivity.this);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }

            });
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }


    }
}