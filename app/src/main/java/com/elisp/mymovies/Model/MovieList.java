package com.elisp.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elisp on 07.2.2018.
 */

public class MovieList implements Serializable {

    public String page;
    public String total_results;
    public String total_pages;
    public ArrayList<Movie> results = new ArrayList<>();


}
