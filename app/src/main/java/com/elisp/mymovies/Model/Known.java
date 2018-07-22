package com.elisp.mymovies.Model;

import java.io.Serializable;

/**
 * Created by elisp on 09.2.2018.
 */

public class Known implements Serializable {
    public String vote_average;
    public String vote_count;
    public String id;
    public boolean video;
    public String title;
    public String popularity;
    public String poster_path;
    public String original_language;
    public String original_title;
    public String overview;
public Known(String poster_path)
{
    this.poster_path = poster_path;

}


}
