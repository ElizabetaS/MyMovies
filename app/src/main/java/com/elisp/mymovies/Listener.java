package com.elisp.mymovies;


import com.elisp.mymovies.Model.Known;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.Person;

/**
 * Created by elisp on 13.2.2018.
 */

public interface Listener {
    public void onMovieLayoutClick(Movie movie, int position);
    public void onLongClick(Movie movie, int position);
    public void onKnownClick(Known movie, int position);
    public void onPersonClick(Person person, int position);

}
