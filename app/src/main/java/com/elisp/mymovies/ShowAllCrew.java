package com.elisp.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Model.MovieDetail;
import com.elisp.mymovies.Model.MovieList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elisp on 02.3.2018.
 */

public class ShowAllCrew extends AppCompatActivity{
    MovieDetail model;
    InfoAdapter adapter;
    @BindView(R.id.recView)
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_crew);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("Movie_id")) {
            final String movieId = (String) intent.getSerializableExtra("Movie_id");
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            RestApi api = new RestApi(ShowAllCrew.this);
            Call<MovieDetail> call = api.movieDetails(movieId);
            call.enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                    if (response.isSuccessful()) {
                        model = response.body();
                        adapter= new InfoAdapter(ShowAllCrew.this,model);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<MovieDetail> call, Throwable t) {
                    Toast.makeText(ShowAllCrew.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
