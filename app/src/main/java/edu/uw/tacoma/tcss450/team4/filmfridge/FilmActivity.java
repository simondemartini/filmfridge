package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.uw.tacoma.tcss450.team4.filmfridge.authenticate.SignInActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

public class FilmActivity extends AppCompatActivity implements
        NowPlayingListFragment.OnListFragmentInteractionListener,
        FilmDetailFragment.OnDetailFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    private NowPlayingListFragment mNowPlayingListFragment;
    private MyListFragment mMyListFragment;
    private LocalSettings mLocalSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.film_fragment_container) == null) {
            mNowPlayingListFragment = new NowPlayingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.film_fragment_container, mNowPlayingListFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mLocalSettings = new LocalSettings(this);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upcoming) {
            if (mNowPlayingListFragment == null) {
                mNowPlayingListFragment = new NowPlayingListFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.film_fragment_container, mNowPlayingListFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_my_list) {
            if (mMyListFragment == null) {
                mMyListFragment = new MyListFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.film_fragment_container, mMyListFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.film_fragment_container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Film item) {
        FilmDetailFragment filmDetailFragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FilmDetailFragment.FILM_ITEM_SELECTED, item);
        filmDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.film_fragment_container, filmDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_film_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            mLocalSettings.setLoggedIn(false);
            mLocalSettings.setEmail(null);

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if (id == R.id.action_share) {
            //implemented in the fragment
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddToMyList(Film film) {
        mLocalSettings.addToMyList(film.getId());
        String success = film.getTitle() + " " + getString(R.string.add_to_my_list_successful);
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFromMyList(Film film) {
        mLocalSettings.removeFromMyList(film.getId());
        String success = film.getTitle() + " " + getString(R.string.remove_from_my_list_successful);
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
    }
}
