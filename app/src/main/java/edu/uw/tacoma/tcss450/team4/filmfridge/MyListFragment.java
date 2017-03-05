package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.TMDBFetcher;

/**
 * A fragment representing a list of Films.
 *
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyListFragment extends AbstractFilmListFragment {

    private static final String TAG = "MyListFragment";
    private static SharedPreferences mSharedPreferences;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyListFragment() {

    }

    @Override
    protected void updateTitle() {
        getActivity().setTitle(getString(R.string.my_list));
    }

    @Override
    protected void startDownloadTask() {

        mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        //create dummy data
        HashSet<String> demo = new HashSet<String>();
        demo.add("263115");
        demo.add("283995");
        mSharedPreferences.edit()
                .putStringSet(getString(R.string.MY_LIST_IDS), demo)
                .commit();

        Set<String> myList = mSharedPreferences.getStringSet(getString(R.string.MY_LIST_IDS), null);

        DownloadMyFilmsTask task = new DownloadMyFilmsTask();
        task.execute(myList.toArray(new String[myList.size()]));
    }

    /**
     * Download a list of films and update the recycler view
     */
    private class DownloadMyFilmsTask extends DownloadFilmsTask {

        @Override
        protected List<Film> doInBackground(String... v) {
            try {
                return tmdb.getByIds();
            } catch (TMDBFetcher.TMDBException e) {
                Log.d(TAG, e.getMessage());
                return null;
            }
        }
    }
}
