package com.elisp.mymovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Model.*;
import com.elisp.mymovies.MovieDetails;
import com.elisp.mymovies.R;
import com.elisp.mymovies.ShowAllCrew;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoMovie extends Fragment {
    RestApi api;
    Context context;
    MovieList model;
    @BindView(R.id.director)
    TextView direktor;
    @BindView(R.id.writter)
    TextView writter;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.showAll)
    TextView showAll;
    @BindView(R.id.stars)
    TextView stars;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_movie, container, false);
        ButterKnife.bind(this, view);
        MovieDetails activity = (MovieDetails) getActivity();
        final String movieId = activity.MovieId();
        String d = activity.MovieDescription();
        description.setText(d);
        RestApi api = new RestApi(context);
        final Call<MovieDetail> call = api.movieDetails(movieId);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail movies = response.body();

                    ArrayList<Crew> crew = new ArrayList<>();
                    ArrayList<com.elisp.mymovies.Model.Cast> cast = new ArrayList<>();

                    for (int i = 0; i < movies.crew.size(); i++) {
                            crew.add(movies.crew.get(i));
                    }
                    for (int i = 0; i < movies.cast.size(); i++) {
                        cast.add(movies.cast.get(i));
                    }
                    for (int i = 0; i < crew.size(); i++) {
                        if (crew.get(i).job.equals("Director")) {
                            direktor.append(crew.get(i).name + " ");
                        }
                    }
                        for (int i = 0; i < crew.size(); i++) {
                            if (crew.get(i).department.equals("Writing")) {
                                writter.append(crew.get(i).name + " ");
                            }
                        }
                    for (int i = 0; i < crew.size(); i++) {
                        if(i==0 || i == 1) {
                            stars.append(crew.get(i).name + " ");
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
            }

        });
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllCrew.class);
                intent.putExtra("Movie_id",movieId);
                startActivity(intent);

            }
        });
        return view;
    }
}
