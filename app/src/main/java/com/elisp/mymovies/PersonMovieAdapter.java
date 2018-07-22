package com.elisp.mymovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisp.mymovies.Model.Known;
import com.elisp.mymovies.Model.PeopleDetils;
import com.elisp.mymovies.Model.PeopleModel;
import com.elisp.mymovies.Model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by elisp on 03.3.2018.
 */

public class PersonMovieAdapter extends RecyclerView.Adapter<PersonMovieAdapter.ViewHolder> {

    Context context;
    ArrayList<Known> knownFor = new ArrayList<>();
    Listener onClickListener;
    public void setItems(ArrayList<Known> knownFor_){
        knownFor=knownFor_;
    }
    public PersonMovieAdapter(Context context_, Listener onClickListener_){
        context=context_;
        onClickListener=onClickListener_;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.people_img_movies, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Known knownFors = knownFor.get(position);
        if(knownFors.poster_path!=null) {
            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + knownFors.poster_path).fit()
                    .into(holder.mainImage);
        }
        else{
            Picasso.with(context)
                    .load(R.drawable.movie)
                    .fit()
                    .into(holder.mainImage);
        }


    }
    @Override
    public int getItemCount() {
        return knownFor.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img)
        ImageView mainImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
