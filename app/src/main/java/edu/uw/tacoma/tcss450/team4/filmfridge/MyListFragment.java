package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.util.Log;

import java.util.List;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyListFragment() {

    }

    /**
     * Set the activity's title to match this fragment
     */
    @Override
    protected void updateTitle() {
        getActivity().setTitle(getString(R.string.my_list));
    }

    /**
     * Download the list of films in MyList
     */
    @Override
    protected void startDownloadTask() {
        String [] ids = mLocalSettings.getMyList()
                .toArray(new String[mLocalSettings.getMyList().size()]);
        new DownloadMyFilmsTask().execute(ids);
    }

    /**
     * Download a list of films and update the recycler view
     */
    private class DownloadMyFilmsTask extends AbstractDownloadFilmsTask {

        /**
         * Download just the films by id
         * @param ids the films to download
         * @return a list of those films
         */
        @Override
        protected List<Film> doInBackground(String... ids) {
            try {
                return tmdb.getByIds(ids);
            } catch (TMDBFetcher.TMDBException e) {
                Log.d(TAG, e.getMessage());
                return null;
            }
        }
    }
}
