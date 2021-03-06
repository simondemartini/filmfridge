package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import edu.uw.tacoma.tcss450.team4.filmfridge.authenticate.SignInActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;
import edu.uw.tacoma.tcss450.team4.filmfridge.settings.LocalSettings;
import edu.uw.tacoma.tcss450.team4.filmfridge.settings.SettingsFragment;

/**
 * This is the main activity for the app that holds the lists, details, and settings fragments and
 * their methods.
 */
public class FilmActivity extends AppCompatActivity implements
        NowPlayingListFragment.OnListFragmentInteractionListener,
        FilmDetailFragment.OnDetailFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnSettingsInteractionListener {

    private NowPlayingListFragment mNowPlayingListFragment;
    private MyListFragment mMyListFragment;
    private LocalSettings mLocalSettings;
    private NavigationView mNavigationView;

    /**
     * Lifecycle method: Create the activity get most things up and running.
     * @param savedInstanceState the saved state bundle
     */
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

        mLocalSettings = new LocalSettings(this);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().getItem(0).setChecked(true);

        TextView navEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_email);
        navEmail.setText(mLocalSettings.getEmail());
    }

    /**
     * Close the navigation drawer if it is open, and set the selected item to be correct when
     * navigating with the back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            FragmentManager manager = getSupportFragmentManager();
            //update selected nav menu item on back press
            Fragment currentFragment = manager.findFragmentById(R.id.film_fragment_container);
            if(currentFragment instanceof NowPlayingListFragment){
                mNavigationView.getMenu().getItem(0).setChecked(true);
            }
            else if(currentFragment instanceof MyListFragment){
                mNavigationView.getMenu().getItem(1).setChecked(true);
            }
            else if(currentFragment instanceof SettingsFragment){
                mNavigationView.getMenu().getItem(2).setChecked(true);
            }
        }
    }

    /**
     * Handle the switching of fragments from the navigation drawer.
     * @param item the selected item
     * @return boolean whether we handled this request
     */
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

    /**
     * Inflate the options menu
     * @param menu the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_film_list, menu);
        return true;
    }

    /**
     * Handle when a menuitem is selected and run the corresponding action
     * @param item the selected item
     * @return whether this method could handle the item
     */
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
        } else if (id == R.id.action_unhide) {
            mLocalSettings.resetHiddenList();
            if(mNowPlayingListFragment != null) mNowPlayingListFragment.notifyContentChanged();
            if(mMyListFragment != null) mMyListFragment.notifyContentChanged();

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.film_fragment_container);
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(f)
                    .attach(f)
                    .commit();

            return true;
        } else if (id == R.id.action_filter_genres) {
            //implemented in fragment
            return false;
        } else if (id == R.id.action_share) {
            //implemented in the fragment
            return false;
        } else if (id == R.id.hide_forever) {
            //implemented in fragment
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When selecting an item in the list, create and switch to the film detail fragment
     * @param item the selected film item
     */
    @Override
    public void onListFragmentInteraction(Film item) {
        FilmDetailFragment filmDetailFragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FilmDetailFragment.FILM_ITEM_SELECTED, item);
        filmDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.film_fragment_container, filmDetailFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    /**
     * A listener method to add a film to My List and tell the list its content has changed
     * @param film the film to add
     */
    @Override
    public void onAddToMyList(Film film) {
        mLocalSettings.addToMyList(film.getId());
        if(mMyListFragment != null) mMyListFragment.notifyContentChanged();
        String success = film.getTitle() + " " + getString(R.string.add_to_my_list_successful);
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
    }

    /**
     * A listener method to remove a film to My List and tell the list its content has changed
     * @param film the film to remove
     */
    @Override
    public void onRemoveFromMyList(Film film) {
        mLocalSettings.removeFromMyList(film.getId());
        if(mMyListFragment != null) mMyListFragment.notifyContentChanged();
        String success = film.getTitle() + " " + getString(R.string.remove_from_my_list_successful);
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
    }

    /**
     * Listener method to notify the lists that the settings for viewing them have changed.
     */
    @Override
    public void onSettingsChange() {
        if(mNowPlayingListFragment != null) mNowPlayingListFragment.notifyContentChanged();
        if(mMyListFragment != null) mMyListFragment.notifyContentChanged();
    }

    /**
     * A listener method to hide a film, and notify the lists that their contents have changed.
     * @param film the film to hide
     */
    @Override
    public void onHideForever(Film film) {
        mLocalSettings.addToHiddenList(film.getId());
        if(mNowPlayingListFragment != null) mNowPlayingListFragment.notifyContentChanged();
        if(mMyListFragment != null) mMyListFragment.notifyContentChanged();
        String success = film.getTitle() + " " + getString(R.string.hide_forever_successful);
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
    }
}
