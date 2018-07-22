package com.elisp.mymovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;

import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.Model.PeopleModel;
import com.elisp.mymovies.Model.User;
import com.google.gson.Gson;


/**
 * Created by tasev on 2/1/18.
 */

public class PreferencesManager {

    private static final String USERID = "USERID";
    private static final String USER = "USER";
    private static final String SessionId = "SessionId";
    private static final String Token = "Token";


    private static SharedPreferences getPreferences(Context c) {
        return c.getApplicationContext().getSharedPreferences("MySharedPreffsFile", Activity.MODE_PRIVATE);
    }

    public static void addUser(User user, Context c) {
        Gson gson = new Gson();
        String mapString = gson.toJson(user);
        getPreferences(c).edit().putString(USER, mapString).apply();
    }

    public static void addFavourite(PeopleModel user, Context c) {
        Gson gson = new Gson();
        String mapString = gson.toJson(user);
        getPreferences(c).edit().putString("Model", mapString).apply();
    }
    public static PeopleModel getModel(Context c) {
        return new Gson().fromJson(getPreferences(c).
                getString("Model", ""), PeopleModel.class);
    }
    public static User getUser(Context c) {
        return new Gson().fromJson(getPreferences(c).
                getString(USER, ""), User.class);
    }
    public static void addMovieFavourite(MovieList model, Context c) {
        Gson gson = new Gson();
        String mapString = gson.toJson(model);
        getPreferences(c).edit().putString("Favourite", mapString).apply();
    }
    public static MovieList getFavourite(Context c) {
        return new Gson().fromJson(getPreferences(c).
                getString("Favourite", ""), MovieList.class);
    }
    public static void removeFavourite(Context c) {
        getPreferences(c).edit().remove("Favourite").apply();
    }
    public static void addMovieRated(MovieList model, Context c) {
        Gson gson = new Gson();
        String mapString = gson.toJson(model);
        getPreferences(c).edit().putString("Rated", mapString).apply();
    }
    public static MovieList getRated(Context c) {
        return new Gson().fromJson(getPreferences(c).
                getString("Rated", ""), MovieList.class);
    }
    public static void removeRated(Context c) {
        getPreferences(c).edit().remove("Rated").apply();
    }
    public static void addMovieWatchlist(MovieList model, Context c) {
        Gson gson = new Gson();
        String mapString = gson.toJson(model);
        getPreferences(c).edit().putString("Watchlist", mapString).apply();
    }
    public static MovieList getWatchlist(Context c) {
        return new Gson().fromJson(getPreferences(c).
                getString("Watchlist", ""), MovieList.class);
    }
    public static void removeWatchlist(Context c) {
        getPreferences(c).edit().remove("Watchlist").apply();
    }
    public static void removeUser(Context c) {
        getPreferences(c).edit().remove(USER).apply();
    }

    public static void setToken(String request_token, Context c) {
        getPreferences(c).edit().putString(Token, request_token).apply();
    }

    public static void setSessionId(String sessionId, Context c) {
        getPreferences(c).edit().putString(SessionId, sessionId).apply();
    }

    public static void setUserId(String id, Context c) {
        getPreferences(c).edit().putString(USERID, id).apply();
    }

    public static String getToken(Context c) {
        return getPreferences(c).getString(Token, "");
    }

    public static String getSessionId(Context c) {
        return getPreferences(c).getString(SessionId, "");
    }

    public static String getUserID(Context c) {
        return getPreferences(c).getString(USERID, "");
    }

    public static void removeUserID(Context c) {
        getPreferences(c).edit().remove(USERID).apply();
    }

    public static void removeSessionId(Context c) {
        getPreferences(c).edit().remove(SessionId).apply();
    }
    public static void removeToken(Context c) {
        getPreferences(c).edit().remove(Token).apply();
    }


}
