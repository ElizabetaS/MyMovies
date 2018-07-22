package com.elisp.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elisp.mymovies.Model.Known;
import com.elisp.mymovies.Model.PeopleDetils;
import com.elisp.mymovies.Model.PeopleModel;
import com.elisp.mymovies.Model.Person;
import com.elisp.mymovies.Api.RestApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonDetails extends AppCompatActivity {

    @BindView(R.id.PersonImg)
    ImageView PersonImg;
    @BindView(R.id.PersonName)
    TextView PersonName;
    @BindView(R.id.bornOn)
    TextView born;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.biografija)
    TextView biografija;
    RestApi api;
    PeopleAdapter adapter;
    PersonMovieAdapter movieAdapter;
    String personId="";
    PeopleModel model;
    ArrayList<Known> knownFors;
    Listener listener;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_row);
        ButterKnife.bind(this);
        model = new PeopleModel();
        knownFors = new ArrayList<>();
        Intent intent = getIntent();
        if(intent.hasExtra("Popular"))
        {
           final Person popular = (Person) intent.getSerializableExtra("Popular");
           int pos = intent.getIntExtra("Pozicija",0);
           personId = popular.id;
            PersonName.setText(popular.name);
            String sl = "https://image.tmdb.org/t/p/w500/" + popular.profile_path;
            Picasso.with(PersonDetails.this).load(sl).into(PersonImg);
            api = new RestApi(PersonDetails.this);
            Call<PeopleDetils> call = api.getPersonDetails(popular.id);
            call.enqueue(new Callback<PeopleDetils>() {
                @Override
                public void onResponse(Call<PeopleDetils> call, Response<PeopleDetils> response) {
                    if (response.code() == 200) {
                        PeopleDetils personDetails = response.body();
                        biografija.setText(personDetails.biography);
                        if(personDetails.birthday !=null) {
                            String bday = getBirthday(personDetails.birthday);
                            born.setText("Born on " +bday);
                        }
                        else
                        {
                            born.setText("No information");
                        }
                        if(personDetails.place_of_birth != null) {
                            from.setText("From " + personDetails.place_of_birth);
                        }
                        else
                        {
                            from.setText("No information");
                        }
                    }
                }
                @Override
                public void onFailure(Call<PeopleDetils> call, Throwable t) {
                    Toast.makeText(PersonDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }

            });

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recView);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(PersonDetails.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManagaer);

            for (int i = 0; i < popular.known_for.size(); i++) {
                String poster="";
               // String title="";
                    poster+=popular.known_for.get(i).poster_path;
                  //  title = popular.known_for.get(i).title;
                    knownFors.add(new Known(poster));
                }
                movieAdapter = new PersonMovieAdapter(PersonDetails.this,listener);
                movieAdapter.setItems(knownFors);
                recyclerView.setAdapter(movieAdapter);
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

}
