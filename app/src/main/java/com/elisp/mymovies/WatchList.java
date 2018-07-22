package com.elisp.mymovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.elisp.mymovies.Api.RestApi;
import com.elisp.mymovies.Model.Known;
import com.elisp.mymovies.Model.Movie;
import com.elisp.mymovies.Model.MovieList;
import com.elisp.mymovies.Model.Person;
import com.elisp.mymovies.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.recView)
    RecyclerView recyclerView;
    User userActive = new User();
    public MovieList favourites;
    ArrayList<Movie> movie;
    private CircleImageView imgProfile;
    private TextView txtName, txtWebsite;
    private NavigationView navigationView;
    MovieList model;
    WatchListAdapter adapter;
    Listener listener;
    RestApi api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        String sessionID = PreferencesManager.getSessionId(WatchList.this);
        if (sessionID != null && !sessionID.isEmpty()) {
            RestApi api = new RestApi(WatchList.this);
            Call<User> call = api.getAccountDetails(sessionID);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        userActive = response.body();
                        imgProfile.setMaxWidth(70);
                        imgProfile.setMaxHeight(70);
                        Picasso.with(WatchList.this).load("http://www.gravatar.com/avatar/" + userActive.avatar.gravatar.hash)
                                .into(imgProfile);
                        PreferencesManager.addUser(userActive, WatchList.this);
                        PreferencesManager.setUserId(userActive.id, WatchList.this);
                        txtName.setText(userActive.name);
                        txtWebsite.setText(userActive.username);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                }
            });
        } else {

            Intent intent = new Intent(WatchList.this, LogInActivity.class);
            startActivity(intent);
        }
        userActive = PreferencesManager.getUser(WatchList.this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        txtName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.guest);
        txtWebsite = (TextView) navigationView.getHeaderView(0).findViewById(R.id.logIn);
        imgProfile = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchList.this, LogInActivity.class);
                startActivity(intent);
            }
        });


        //  myFavourites = new ArrayList<>();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        ButterKnife.bind(this);
        api = new RestApi(WatchList.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       model = PreferencesManager.getWatchlist(WatchList.this);
       if(model!=null)
       {
           adapter = new WatchListAdapter(WatchList.this,model,listener);
           recyclerView.setAdapter(adapter);
           adapter.notifyDataSetChanged();
       }
    }







    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.explore) {
            Intent intent = new Intent(WatchList.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.favourites) {
            Intent intent = new Intent(WatchList.this,Favourites.class);
            startActivity(intent);


        } else if (id == R.id.rated) {
            Intent intent = new Intent(WatchList.this,Rated.class);
            startActivity(intent);

        } else if (id == R.id.watchlist) {
            Intent intent = new Intent(WatchList.this,WatchList.class);
            startActivity(intent);

        } else if (id == R.id.people) {
            Intent intent = new Intent(WatchList.this,People.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(WatchList.this);
            builder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences settings = WatchList.this.getSharedPreferences("MySharedPreffsFile", WatchList.MODE_PRIVATE);
                            settings.edit().clear().commit();
                            Intent intent = new Intent(WatchList.this,MainActivity.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
