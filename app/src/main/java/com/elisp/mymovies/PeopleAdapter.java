package com.elisp.mymovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisp.mymovies.Model.PeopleDetils;
import com.elisp.mymovies.Model.PeopleModel;
import com.elisp.mymovies.Model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by elisp on 09.2.2018.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    List<Person> personList = new ArrayList<>();
    Context context;
    Person popular;
    int brojac=0;
    PeopleDetils personDetails;
    PeopleModel model;
    Listener listener;
    public PeopleAdapter(Context context, PeopleModel model,Listener listener) {
        this.context = context;
        personList =model.results;
        this.listener = listener;
    }


    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.people_row,parent,false);
        PeopleAdapter.ViewHolder viewHolder = new PeopleAdapter.ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final PeopleAdapter.ViewHolder holder, final int position) {

        final Person popular = personList.get(position);
        String slika ="https://image.tmdb.org/t/p/w500/" + popular.profile_path;
        Picasso.with(context).load(slika).centerInside().fit().into(holder.slika);
        brojac=position+1;
        holder.broj.setText(brojac + "");
        holder.broj.setTextColor(Color.RED);
        holder.ime.setText(popular.name);
        holder.ime.setTextColor(Color.WHITE);
        holder.movies.setText("");
        ArrayList<String> movies = new ArrayList<>();
        for(int i=0;i<popular.known_for.size();i++)
        {
            movies.add(popular.known_for.get(i).title);
        }
        for(int i=0;i<movies.size();i++)
        {
            holder.movies.setText(holder.movies.getText().toString() + " " +movies.get(i));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PersonDetails.class);
                intent.putExtra("Popular",popular);
                intent.putExtra("Pozicija",position);
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.slika)
        ImageView slika;
        @BindView(R.id.ime)
        TextView ime;
        @BindView(R.id.broj)
        TextView broj;
        @BindView(R.id.movies)
        TextView movies;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
