package com.elisp.mymovies;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elisp on 03.3.2018.
 */

public class MovieImg implements Serializable {
    public String id;
    public ArrayList<Img> backdrops = new ArrayList<>();
}
