package com.elisp.mymovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.Model.User;
import com.elisp.mymovies.Api.RestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogInActivity extends AppCompatActivity {

    private static final String TAG = LogInActivity.class.getSimpleName();
    private EditText username, password;
    User user;
    @BindView(R.id.logInBtn)
    Button btnlogIn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    MovieList model;
    MovieList model2;
    RestApi api = new RestApi(LogInActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);

    }
    @OnClick(R.id.logInBtn)
    public void btnLogIn() {

        if(username.getText().toString().isEmpty())
        {
            username.setError("Please input username");
        }
        else if(password.getText().toString().isEmpty())
        {
            password.setError("Please input password");
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            validation();
        }


    }

     void validation()
    {
        Call<User> call = api.getRequestToken("token");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    user = response.body();
                    Call<User> call2 = api.validateUser(user.request_token,user.session_id,username.getText().toString(),password.getText().toString());
                    call2.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                user = response.body();
                                PreferencesManager.setToken(user.request_token, LogInActivity.this);
                                PreferencesManager.addUser(user,LogInActivity.this);
                                RestApi api = new RestApi(LogInActivity.this);
                                Call<User> call3 = api.getSessionId(user.request_token);
                                call3.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.code() == 200) {
                                            user = response.body();
                                            PreferencesManager.setSessionId(user.session_id, LogInActivity.this);

                                            String sessionId = PreferencesManager.getSessionId(LogInActivity.this);
                                            RestApi api = new RestApi(LogInActivity.this);
                                            Call<MovieList> call5 = api.getFavourites("account_id", sessionId);
                                            call5.enqueue(new Callback<MovieList>() {
                                                @Override
                                                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                                                    if (response.code() == 200) {
                                                        model = response.body();
                                                        if (model != null) {
                                                            for (int i = 0; i < model.results.size(); i++) {
                                                                model2 = PreferencesManager.getFavourite(LogInActivity.this);
                                                                if (model2 == null) {
                                                                    model2 = new MovieList();
                                                                }
                                                                model2.results.add(model.results.get(i));
                                                                PreferencesManager.addMovieFavourite(model2, LogInActivity.this);
                                                            }
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MovieList> call, Throwable t) {
                                                    Toast.makeText(LogInActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                                }

                                            });
                                            Call<MovieList> call6 = api.getWatchlist("account_id", sessionId);
                                            call6.enqueue(new Callback<MovieList>() {
                                                @Override
                                                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                                                    if (response.code() == 200) {
                                                        model = response.body();
                                                        if (model != null) {
                                                            for (int i = 0; i < model.results.size(); i++) {
                                                                model2 = PreferencesManager.getWatchlist(LogInActivity.this);
                                                                if (model2 == null) {
                                                                    model2 = new MovieList();
                                                                }
                                                                model2.results.add(model.results.get(i));
                                                                PreferencesManager.addMovieWatchlist(model2, LogInActivity.this);
                                                            }
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MovieList> call, Throwable t) {
                                                    Toast.makeText(LogInActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                                }

                                            });
                                            Call<MovieList> call7 = api.getRated("account_id", sessionId);
                                            call7.enqueue(new Callback<MovieList>() {
                                                @Override
                                                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                                                    if (response.code() == 200) {
                                                        model = response.body();
                                                        if (model != null) {
                                                            for (int i = 0; i < model.results.size(); i++) {
                                                                model2 = PreferencesManager.getRated(LogInActivity.this);
                                                                if (model2 == null) {
                                                                    model2 = new MovieList();
                                                                }
                                                                model2.results.add(model.results.get(i));
                                                                PreferencesManager.addMovieRated(model2, LogInActivity.this);
                                                            }
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MovieList> call, Throwable t) {
                                                    Toast.makeText(LogInActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                                }

                                            });

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LogInActivity.this, "Failed validation", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(LogInActivity.this, "Failed validation", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LogInActivity.this, "Failed token", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
