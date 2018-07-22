package com.elisp.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elisp on 07.2.2018.
 */

public class Movie implements Serializable {
 public String vote_count;
 public String id;
 public boolean video;
 public String vote_average;
 public String title;
 public String popularity;
 public String poster_path;
 public String original_language;
 public String original_title;
 public String backdrop_path;
 public boolean adult;
 public String overview;
 public String release_date;
 public String key;
 public String rating;
 public transient boolean isFav;
 public transient boolean watchlist;
 public transient boolean rated;


}
