package com.elisp.mymovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elisp.mymovies.Model.Cast;
import com.elisp.mymovies.Model.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by elisp on 01.3.2018.
 */

public class CastAdapter extends ArrayAdapter<Cast> {
    ArrayList<Cast> cast;
    MovieDetail model;
    Context context;

    public CastAdapter(Context context, MovieDetail model) {
        super(context, R.layout.cast_row, model.cast);
        this.context = context;
        cast = model.cast;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.cast_row, parent, false);
            holder = new ViewHolder(convertView);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        final Cast c = cast.get(position);
        String s = "https://image.tmdb.org/t/p/w500/" + c.profile_path;
        if (c.profile_path != null) {
            Picasso.with(context).load(s).centerInside().fit().into(holder.slika);
        }
        holder.name.setText(c.name);
        holder.job.setText(c.character);
        return result;
    }



    public class ViewHolder {
        @BindView(R.id.img)
        ImageView slika;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.job)
        TextView job;
        @BindView(R.id.itemView)
        LinearLayout itemView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
