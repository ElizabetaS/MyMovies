package com.elisp.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elisp on 09.2.2018.
 */

public class Person implements Serializable {
   public String popularity;
   public String id;
   public String profile_path;
   public String name;
   public ArrayList<Known> known_for = new ArrayList<>();



}
