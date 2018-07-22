package com.elisp.mymovies.Api;


import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieDetail;
import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.Model.MovieResponse;
import com.elisp.mymovies.Model.PeopleDetils;
import com.elisp.mymovies.Model.PeopleModel;
import com.elisp.mymovies.Model.User;
import com.elisp.mymovies.MovieDetails;
import com.elisp.mymovies.MovieImg;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //fragments
    @GET("movie/popular?" +ApiConstants.api_key)
    Call<MovieList> getPopular();
    @GET("movie/top_rated?"+ApiConstants.api_key)
    Call<MovieList> getUpcoming();
    @GET("movie/upcoming?"+ApiConstants.api_key)
    Call<MovieList> getTopRated();
    @GET("movie/now_playing?"+ApiConstants.api_key)
    Call<MovieList> getNowPlaying();

    //authentication

    @GET("authentication/{token}/new?"+ApiConstants.api_key)
    Call<User> getRequestToken(@Path("token")String token);
    @GET("authentication/session/new?"+ApiConstants.api_key)
    Call<User> getSessionId(@Query("request_token") String token);
    @GET("authentication/token/validate_with_login?"+ApiConstants.api_key)
    Call<User> validateUser(@Query("request_token")String token,
                            @Query("session_id")String sessionId,
                            @Query("username")String username,@Query("password")String password);
    @GET("authentication/guest_session/new?"+ApiConstants.api_key)
    Call<User> getGuestSession();

    //account
    @GET("account?" + ApiConstants.api_key)
    Call<User> getAccountDetails(@Query("session_id")String session_id);

    //search
    @GET("search/movie?" + ApiConstants.api_key)
    Call<MovieList> searchMovie(@Query("query") CharSequence movie);
    @GET("search/person?"+ApiConstants.api_key)
    Call<PeopleModel> searchPeople(@Query("query") CharSequence people);
    //movie details
    @GET("movie/{movie_id}/credits?" + ApiConstants.api_key)
    Call<MovieDetail> movieDetails(@Path("movie_id") String id);
    @GET("movie/{movie_id}/videos?"+ApiConstants.api_key)
    Call<MovieList> getMovieVideo(@Path("movie_id") String id);
    @GET("movie/{movie_id}?" + ApiConstants.api_key)
    Call<MovieDetail> getMovieGenre(@Path("movie_id") String id);
    //my movies
    @POST("account/account_id/favorite?" + ApiConstants.api_key)
    Call<Movie> postFavourite(@Query("session_id")String sessionId,@Header("json/application") String header,
                              @Body MovieResponse movieResponse);
    @POST("account/account_id/watchlist?"+ ApiConstants.api_key)
    Call<Movie> postWatchlist(@Query("session_id")String sessionId,@Header("json/application") String header,
                               @Body MovieResponse movieResponse);

    @POST("movie/{movie_id}/rating?"+ ApiConstants.api_key)
    Call<Movie> postRating(@Path("movie_id") String movie_id,@Query("session_id")String sessionId,@Header("json/application") String header,
                          @Body MovieResponse movieResponse);

//get movies
    @GET("account/{account_id}/favorite/movies?" + ApiConstants.api_key)
    Call<MovieList> getFavouriteMovies(@Path("account_id") String account_id,@Query("session_id")String session_id);
    @GET("account/{account_id}/watchlist/movies?" + ApiConstants.api_key)
    Call<MovieList> getWatchList(@Path("account_id") String account_id,@Query("session_id")String session_id );

    @GET("account/{account_id}/rated/movies?" + ApiConstants.api_key)
    Call<MovieList> getRated(@Path("account_id") String account_id,@Query("session_id")String session_id);
//get images

    @GET("movie/{movie_id}/images?" + ApiConstants.api_key)
    Call<MovieImg> getImage(@Path("movie_id") String movieId);
//person
    @GET("person/popular?" + ApiConstants.api_key)
    Call<PeopleModel> getPerson();
    @GET("person/{person_id}?" + ApiConstants.api_key)
    Call<PeopleDetils> getPersonDetails(@Path("person_id") String person_id);
    @GET("person/{person_id}/movie_credits" + ApiConstants.api_key)
    Call<PeopleModel> getPersonMovies(@Path("person_id") String person_id);
    @DELETE("movie/{movie_id}/rating?"+ApiConstants.api_key)
    Call<Movie> deleteRatingMovie(@Path("movie_id") String movie_id, @Header("json/application") String header, @Query("session_id") String session_id);


}
