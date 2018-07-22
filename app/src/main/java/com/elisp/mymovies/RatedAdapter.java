package com.elisp.mymovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.Model.MovieResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elisp on 08.2.2018.
 */

public class RatedAdapter extends RecyclerView.Adapter<RatedAdapter.ViewHolder>{
    ArrayList<Movie> movieList;
    MovieList viewPhotos = new MovieList();
    Context context;
    Listener listener;
    MovieList model;
    SwipeLayout swipeLayout;
    public RatedAdapter(Context context, MovieList photos, Listener listener) {

        this.context = context;
        movieList=photos.results;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mymovie_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    public void remove(int position)
    {
        movieList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        model = new MovieList();
        final String sessionId = PreferencesManager.getSessionId(context);
        String slika = "https://image.tmdb.org/t/p/w500/" + movie.poster_path;
        Picasso.with(context).load(slika).centerInside().fit().into(holder.slika);
        holder.title.setText(movie.title);
        holder.rejtingText.setText(movie.vote_average);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MovieDetails.class);
                intent.putExtra("Id", movie);
                context.startActivity(intent);
            }
        });
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });
        holder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie.isFav=false;
                MovieResponse mResponse = new MovieResponse();
                mResponse.media_type = "movie";
                mResponse.media_id = Integer.valueOf(movie.id);
                mResponse.value = Float.valueOf(movie.vote_average);
                RestApi api = new RestApi(context);
                Call<Movie> call = api.postFavourite(sessionId,"application/json;charset=utf-8;", mResponse);
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            model = PreferencesManager.getRated(context);
                            for(int i=0;i<model.results.size();i++)
                            {
                                if(model.results.get(i).id.equals(movie.id))
                                {
                                    model.results.remove(i);
                                }
                            }
                            PreferencesManager.addMovieRated(model,context);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Movie removed from watchlist", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        holder.srce.setImageResource(R.mipmap.favourites_empty_hdpi);
        holder.znakwatchlist.setImageResource(R.mipmap.watchlist_add_hdpi);
        holder.addTowatch.setText("Add to watchlist");
        holder.rejting.setImageResource(R.mipmap.rate_empty_hdpi);
        model= PreferencesManager.getFavourite(context);
        if(model!=null) {
            for (int i = 0; i < model.results.size(); i++) {
                if (model.results.get(i).id.equals(movie.id)) {
                    holder.srce.setImageResource(R.mipmap.favourites_full_hdpi);
                }
            }
        }
        model= PreferencesManager.getWatchlist(context);
        if(model!=null) {
            for (int i = 0; i < model.results.size(); i++) {
                if (model.results.get(i).id.equals(movie.id)) {
                    holder.znakwatchlist.setImageResource(R.mipmap.watchlist_remove_hdpi);
                    holder.addTowatch.setText("Saved to watchlist");

                }
            }
        }
        model= PreferencesManager.getRated(context);
        if(model!=null) {
            for (int i = 0; i < model.results.size(); i++) {
                if (model.results.get(i).id.equals(movie.id)) {
                    holder.rejting.setImageResource(R.mipmap.star);
                    holder.rejtingText.setText(model.results.get(i).rating);
                }
            }
        }

    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.slikaFilm)
        ImageView slika;
        @BindView(R.id.srceFilm)
        ImageView srce;
        @BindView(R.id.imeFilm)
        TextView title;
        @BindView(R.id.ratingbar)
        ImageView rejting;
        @BindView(R.id.rejting)
        TextView rejtingText;
        @BindView(R.id.znakcewathclist)
        ImageView znakwatchlist;
        @BindView(R.id.addtoWatchList)
        TextView addTowatch;
        @BindView(R.id.swipe)
        SwipeLayout swipeLayout;
        @BindView(R.id.delete)
        Button btn;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
