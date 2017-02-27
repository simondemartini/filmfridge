package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

public class FilmActivity extends AppCompatActivity implements
    UpcomingListFragment.OnListFragmentInteractionListener,
        FilmDetailFragment.OnDetailFragmentInteractionListener {

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
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
