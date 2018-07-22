package com.elisp.mymovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisp.mymovies.Model.Crew;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieDetail;
import com.elisp.mymovies.Model.MovieList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by elisp on 01.3.2018.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>{
   ArrayList<Crew> crew;
    MovieDetail model;
    Context context;
    public InfoAdapter(Context context, MovieDetail model) {

        this.context = context;
        crew=model.crew;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.crew_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Crew c = crew.get(position);
        String s = "https://image.tmdb.org/t/p/w500/" + c.profile_path;
        if(c.profile_path!=null)
        {
            Picasso.with(context).load(s).centerInside().fit().into(holder.slika);
        }
        holder.name.setText(c.name);
        holder.job.setText(c.job);


    }

    @Override
    public int getItemCount() {
        return crew.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView slika;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.job)
        TextView job;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
