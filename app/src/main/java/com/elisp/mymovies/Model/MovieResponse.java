package com.elisp.mymovies.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elisp on 21.2.2018.
 */

public class MovieResponse {
    @SerializedName("media_type")
    public String media_type;
    @SerializedName("media_id")
    public int  media_id;
    @SerializedName("favorite")
    public boolean favorite;
    @SerializedName("value")
    public float value;
    @SerializedName("watchlist")
    public boolean watchlist;

}