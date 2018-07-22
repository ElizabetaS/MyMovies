package com.elisp.mymovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.CastAdapter;
import com.elisp.mymovies.InfoAdapter;
import com.elisp.mymovies.Model.Crew;
import com.elisp.mymovies.Model.MovieDetail;
import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.MovieDetails;
import com.elisp.mymovies.R;
import com.elisp.mymovies.ShowAllCrew;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cast extends Fragment {
    RestApi api;
    Context context;
    MovieDetail model;
    @BindView(R.id.recView)
    ListView recyclerView;
    CastAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_cast, container, false);
        ButterKnife.bind(this, view);
        MovieDetails activity = (MovieDetails) getActivity();
        String movieId = activity.MovieId();
        mLayoutManager = new LinearLayoutManager(getContext());
        api = new RestApi(getContext());
        Call<MovieDetail> call = api.movieDetails(movieId);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    model = response.body();
                    adapter= new CastAdapter(getContext(),model);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setScrollContainer(false);

                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
