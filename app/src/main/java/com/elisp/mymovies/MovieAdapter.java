package com.elisp.mymovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieDetail;
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
 * Created by elisp on 01.3.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    ArrayList<Movie> movieList;
    MovieList model = new MovieList();
    Context context;
    Listener listener;
    public MovieResponse mResponse;



    public MovieAdapter(Context context, MovieList model,Listener listener) {

        this.context = context;
        movieList=model.results;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
       final String sessionID = PreferencesManager.getSessionId(context);
        String slika = "https://image.tmdb.org/t/p/w500/" + movie.poster_path;
        Picasso.with(context).load(slika).fit().into(holder.slika);
        holder.title.setText(movie.title);
        holder.rejtingText.setText(movie.vote_average);
        holder.srce.setImageResource(R.mipmap.favourites_empty_hdpi);
        holder.znakwatchlist.setImageResource(R.mipmap.watchlist_add_hdpi);
        holder.addTowatch.setText("Add to watchlist");
        holder.rejting.setImageResource(R.mipmap.rate_empty_hdpi);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MovieDetails.class);
                intent.putExtra("Id", movie);
                context.startActivity(intent);
            }
        });
        model = PreferencesManager.getFavourite(context);
        if(model!=null) {
            for (int i = 0; i < model.results.size(); i++) {
                if (model.results.get(i).id.equals(movie.id)) {
                    model.results.get(i).isFav=true;
                    holder.srce.setImageResource(R.mipmap.favourites_full_hdpi);
                }
            }
        }
        model= PreferencesManager.getWatchlist(context);
        if(model!=null) {
            for (int i = 0; i < model.results.size(); i++) {
                if (model.results.get(i).id.equals(movie.id)) {
                    model.results.get(i).watchlist=true;
                    holder.znakwatchlist.setImageResource(R.mipmap.watchlist_remove_hdpi);
                    holder.addTowatch.setText("Saved to watchlist");
                }
            }
        }
        model= PreferencesManager.getRated(context);
        if(model!=null) {
            for (int i = 0; i < model.results.size(); i++) {
                if (model.results.get(i).id.equals(movie.id)) {
                    model.results.get(i).rated= true;
                    holder.rejting.setImageResource(R.mipmap.star);
                    holder.rejtingText.setText(model.results.get(i).rating);
                }
            }
        }
        holder.rejting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie.rated) {

                    final AlertDialog.Builder popDialog = new AlertDialog.Builder(context);
                    final RatingBar ratingBar = new RatingBar(context);
                    ratingBar.setNumStars(5);
                    ratingBar.setStepSize((float) 0.5);
                    ratingBar.setRating(Float.valueOf(movie.vote_average));
                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams parameters =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.FILL_PARENT,
                                    LinearLayout.LayoutParams.FILL_PARENT);
                    layout.setLayoutParams(parameters);
                    layout.setGravity(Gravity.CENTER);
                    layout.addView(ratingBar);

//Sets the parameters for the dialog: Icon, Title and the view
                    popDialog.setIcon(android.R.drawable.btn_star_big_on);
                    popDialog.setTitle("RATE");
                    popDialog.setView(layout);

//Defines the accept Buton
                    popDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.rejtingText.setText((ratingBar.getRating() *2)+ "");
                                    RestApi api = new RestApi(context);
                                    mResponse = new MovieResponse();
                                    mResponse.media_type = "movie";
                                    mResponse.media_id = Integer.valueOf(movie.id);
                                    mResponse.value = Float.valueOf(holder.rejtingText.getText().toString());
                                    Call<Movie> call = api.postRated(movie.id, sessionID, "application/json;charset=utf-8;", mResponse);
                                    call.enqueue(new Callback<Movie>() {
                                        @Override
                                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                                            if (response.isSuccessful()) {
                                                movie.rated = true;
                                                float r = ratingBar.getRating()*2;
                                                holder.rejting.setImageResource(R.mipmap.star);
                                                movie.rating = String.valueOf(r);
                                                model = PreferencesManager.getRated(context);
                                                if (model == null) {
                                                    model = new MovieList();
                                                }
                                                model.results.add(movie);
                                                notifyDataSetChanged();
                                                PreferencesManager.addMovieRated(model, context);
                                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Movie> call, Throwable t) {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })

                            .setNegativeButton("CANCEL",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();
                                        }
                                    });

                    popDialog.create();
                    popDialog.show();

                } else {
                    RestApi api = new RestApi(context);
                    Call<Movie> call = api.deleteRatingMovie(movie.id, "application/json;charset=utf-8;", sessionID);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            holder.rejting.setImageResource(R.mipmap.rate_empty_xxhdpi);
                            movie.rated = false;
                            model = PreferencesManager.getRated(context);
                            for(int i=0;i<model.results.size();i++)
                            {
                                if(model.results.get(i).id.equals(movie.id))
                                {
                                    model.results.remove(i);
                                    notifyDataSetChanged();
                                }
                            }
                            PreferencesManager.addMovieRated(model,context);
                            Toast.makeText(context, "Removed from rated", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {

                        }
                    });

                }
            }
            });






            holder.srce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movie.isFav)
                {
                    holder.srce.setImageResource(R.mipmap.favourites_full_hdpi);
                    movie.isFav=true;
                    RestApi api = new RestApi(context);
                    mResponse = new MovieResponse();
                    mResponse.media_type = "movie";
                    mResponse.media_id = Integer.valueOf(movie.id);
                    mResponse.favorite = true;
                    Call<Movie> call = api.postFavourite(sessionID,"application/json;charset=utf-8;", mResponse);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.isSuccessful()) {
                                movie.isFav=true;
                                model = PreferencesManager.getFavourite(context);
                                if(model==null)
                                {
                                    model = new MovieList();
                                }
                                model.results.add(movie);
                                notifyDataSetChanged();
                                PreferencesManager.addMovieFavourite(model, context);
                                Toast.makeText(context, "Movie added to favourite", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                   final int pos = position;
                    holder.srce.setImageResource(R.mipmap.favourites_empty_hdpi);
                    RestApi api = new RestApi(context);
                    mResponse = new MovieResponse();
                    mResponse.media_type = "movie";
                    mResponse.media_id = Integer.valueOf(movie.id);
                    mResponse.favorite = false;
                    Call<Movie> call = api.postFavourite(sessionID,"application/json;charset=utf-8;", mResponse);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.isSuccessful()) {
                                movie.isFav=false;
                                model = PreferencesManager.getFavourite(context);
                                for(int i=0;i<model.results.size();i++)
                                {
                                    if(model.results.get(i).id.equals(movie.id))
                                    {
                                        model.results.remove(i);
                                        notifyDataSetChanged();
                                    }
                                }
                                PreferencesManager.addMovieFavourite(model,context);
                                Toast.makeText(context, "Movie removed from favourite", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        holder.znakwatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movie.watchlist)
                {
                    holder.znakwatchlist.setImageResource(R.mipmap.watchlist_remove_hdpi);
                    movie.watchlist=true;
                    holder.addTowatch.setText("Saved to watchlist");
                    RestApi api = new RestApi(context);
                    mResponse = new MovieResponse();
                    mResponse.media_type = "movie";
                    mResponse.media_id = Integer.valueOf(movie.id);
                    mResponse.watchlist = true;
                    Call<Movie> call = api.postWatchlist(sessionID,"application/json;charset=utf-8;", mResponse);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.isSuccessful()) {
                                movie.watchlist=true;
                                model = PreferencesManager.getWatchlist(context);
                                if(model==null)
                                {
                                    model = new MovieList();
                                }
                                model.results.add(movie);
                                notifyDataSetChanged();
                                PreferencesManager.addMovieWatchlist(model,context);
                                Toast.makeText(context, "Movie added to watchlist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    holder.znakwatchlist.setImageResource(R.mipmap.watchlist_add_hdpi);
                    RestApi api = new RestApi(context);
                    movie.isFav=false;
                    holder.addTowatch.setText("Add to watchlist");
                    mResponse = new MovieResponse();
                    mResponse.media_type = "movie";
                    mResponse.media_id = Integer.valueOf(movie.id);
                    mResponse.watchlist = false;
                    Call<Movie> call = api.postWatchlist(sessionID,"application/json;charset=utf-8;", mResponse);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.isSuccessful()) {
                                movie.watchlist=false;
                                model = PreferencesManager.getWatchlist(context);
                                for(int i=0;i<model.results.size();i++)
                                {
                                    if(model.results.get(i).id.equals(movie.id))
                                    {
                                        model.results.remove(i);
                                        notifyDataSetChanged();
                                    }
                                }
                                PreferencesManager.addMovieWatchlist(model,context);
                                Toast.makeText(context, "Movie removed from watchlist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

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
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
