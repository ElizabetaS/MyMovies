package com.elisp.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elisp on 14.2.2018.
 */

public class MovieDetail implements Serializable {
    public String id;
    public ArrayList<Crew> crew = new ArrayList<>();
    public ArrayList<Cast> cast = new ArrayList<>();
    public ArrayList<Genre> genres = new ArrayList<>();

}
