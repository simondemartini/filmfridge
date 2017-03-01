package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.uw.tacoma.tcss450.team4.filmfridge.authenticate.SignInActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

public class FilmActivity extends AppCompatActivity implements
    UpcomingListFragment.OnListFragmentInteractionListener,
        FilmDetailFragment.OnDetailFragmentInteractionListener {

    private FilmDetailFragment mFilmDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            UpcomingListFragment courseFragment = new UpcomingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.film_fragment_container, courseFragment)
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Film item) {
        mFilmDetailFragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FilmDetailFragment.FILM_ITEM_SELECTED, item);
        mFilmDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.film_fragment_container, mFilmDetailFragment)
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
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if(id == R.id.action_share) {
            //implemented in the fragment
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
