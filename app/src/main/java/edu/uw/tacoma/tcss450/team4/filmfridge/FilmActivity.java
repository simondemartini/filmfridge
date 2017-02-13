package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

public class FilmActivity extends AppCompatActivity implements
    FilmFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            FilmFragment courseFragment = new FilmFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.film_fragment_container, courseFragment)
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Film item) {

    }
}
