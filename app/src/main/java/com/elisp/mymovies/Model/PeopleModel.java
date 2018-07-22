package com.elisp.mymovies.Model;



import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elisp on 09.2.2018.
 */

public class PeopleModel implements Serializable {
    public String page;
    public String total_results;
    public String total_pages;
    public ArrayList<Person> results;
    public ArrayList<PeopleCast> cast = new ArrayList<>();
    public ArrayList<PeopleCrew> crew = new ArrayList<>();


}
