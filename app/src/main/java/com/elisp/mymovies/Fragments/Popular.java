package com.elisp.mymovies.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.MainActivity;
import com.elisp.mymovies.Model.Known;
import com.elisp.mymovies.Model.Person;
import com.elisp.mymovies.MovieAdapter;
import com.elisp.mymovies.Listener;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.MovieDetails;
import com.elisp.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elisp on 07.2.2018.
 */

public class Popular extends android.support.v4.app.Fragment {
    RestApi api = new RestApi(getActivity());
    MovieList model;
    @BindView(R.id.recView)
    RecyclerView recycler;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    MovieAdapter adapter;
    Listener listener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(lm);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0) {
                    searchMovies(s);

                } else {
                    refreshApi();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        refreshApi();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!search.getText().toString().isEmpty()){
                    swipeRefreshLayout.setRefreshing(false);
                    searchMovies(search.getText().toString());
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                    refreshApi();
                }
            }
        });

        return view;
    }
    public void refreshApi() {
        Call<MovieList> call = api.getPopular();
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.code() == 200) {
                    model = response.body();
                    adapter = new MovieAdapter(getContext(), model, new Listener() {
                        @Override
                        public void onMovieLayoutClick(Movie movie, int position) {
                            Intent intent = new Intent(getContext(), MovieDetails.class);
                            intent.putExtra("Id", movie);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(Movie movie, int position) {

                        }
                        @Override
                        public void onKnownClick(Known movie, int position) {

                        }

                        @Override
                        public void onPersonClick(Person person, int position) {

                        }


                    });
                    recycler.setAdapter(adapter);
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Popular!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(getContext(), "Popular!", Toast.LENGTH_LONG).show();
            }

        });
    }
 void searchMovies(CharSequence sequence) {
        Call<MovieList> call = api.searchMovie(sequence);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                model = response.body();
                recycler.setHasFixedSize(true);
                recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                adapter = new MovieAdapter(getContext(), model,listener);
                recycler.setAdapter(adapter);
                Log.w("MyTag", "requestSuccesfull");
            }
            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
 }

}
