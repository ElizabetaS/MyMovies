package com.elisp.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Fragments.Cast;
import com.elisp.mymovies.Fragments.InfoMovie;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieDetail;
import com.elisp.mymovies.Model.MovieList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity {
    @BindView(R.id.video)
    ImageView triler;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.slikaFilm)
    ImageView slikaFilm;
    @BindView(R.id.imeF)
    TextView ime;
    @BindView(R.id.zarn)
    TextView zarn;
    @BindView(R.id.pager)
    ViewPager myPager;
    @BindView(R.id.date)
    TextView date;
    MovieList model;
    String pomoshna="";
    String description="";
    MovieImg images;
    String s="";
    String i="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra("Id")) {
            final Movie movie = (Movie) intent.getSerializableExtra("Id");
            ime.setText(movie.original_title);
            String bday = getBirthday(movie.release_date);
            date.setText(bday);
            pomoshna=movie.id;
            description=movie.overview;
           setUpViewPager(myPager);
            RestApi api = new RestApi(MovieDetails.this);
            Call<MovieImg> img = api.getImg(movie.id);
            img.enqueue(new Callback<MovieImg>() {
                @Override
                public void onResponse(Call<MovieImg> call, Response<MovieImg> response) {
                    if (response.isSuccessful()) {
                        images = response.body();
                        if(!images.backdrops.get(0).file_path.isEmpty())
                        {
                         i = images.backdrops.get(0).file_path;
                            s = "https://image.tmdb.org/t/p/w500/" + i;
                            Picasso.with(MovieDetails.this).load(s).fit().into(triler);
                        }

                    }
                }
                @Override
                public void onFailure(Call<MovieImg> call, Throwable t) {
                    Toast.makeText(MovieDetails.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

                 s = "https://image.tmdb.org/t/p/w500/" + movie.poster_path;
                 Picasso.with(MovieDetails.this).load(s).centerInside().fit().into(slikaFilm);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RestApi api = new RestApi(MovieDetails.this);
                    Call<MovieList> call = api.getVideo(movie.id);
                    call.enqueue(new Callback<MovieList>() {
                        @Override
                        public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                            if (response.isSuccessful()) {
                                model = response.body();
                                int position = 0;
                                String uri = model.results.get(position).key;
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + uri)));
                            }
                        }
                        @Override
                        public void onFailure(Call<MovieList> call, Throwable t) {
                            Toast.makeText(MovieDetails.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            RestApi api2 = new RestApi(MovieDetails.this);
            Call<MovieDetail> call = api2.getMovieGenre(movie.id);
            call.enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                    if (response.isSuccessful()) {
                        MovieDetail genre = response.body();
                        for(int i=0;i<genre.genres.size();i++)
                        {
                            zarn.append(genre.genres.get(i).name + " ");
                        }

                    }
                }
                @Override
                public void onFailure(Call<MovieDetail> call, Throwable t) {
                    Toast.makeText(MovieDetails.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    public String getBirthday(String bday){
        String [] months;
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String current_month="";
        String official_month="";
        String current_day="";
        String official_day="";
        String official_year="";

        if(bday!=null && !bday.isEmpty() && bday.length()>4) {
            current_day=bday.substring(8, 10);
            if(current_day.substring(0, 1).equals("0")){
                current_day=bday.substring(9, 10);
            }
            else{
                current_day=bday.substring(8, 10);
            }
            official_day=current_day + " ";
            official_year=bday.substring(0, 4);
            current_month = bday.substring(5, 7);
            if (current_month.substring(0, 1).equals("0")) {
                current_month = bday.substring(6, 7);
            } else {
                current_month = bday.substring(5, 7);
            }
            for (int i = 1; i <= 12; i++) {
                int month = Integer.valueOf(current_month);
                if (i == month) {
                    int pos = i - 1;
                    for (int j = 0; j < months.length; j++) {
                        if (j == pos) {
                            official_month = months[j] + " ";
                        }
                    }
                }
            }
        }
        else if(bday!=null && bday.length()<5){
            official_day="";
            official_month=bday;
            official_year="";
        }
        else{
            official_day="";
            official_month="";
            official_year="";

        }
        return official_day +official_month +  official_year;
    }
    private void setUpViewPager(ViewPager mojpager) {

        PageAdapter adapter = new PageAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new InfoMovie(),"Info");
        adapter.addFragment(new Cast(),"Cast");
        mojpager.setAdapter(adapter);
    }
    public String MovieId() {
        return pomoshna;
    }
    public String MovieDescription() {
        return description;
    }
}
