package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.SharedPreferences;
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

    @Override
    protected void updateTitle() {
        getActivity().setTitle(getString(R.string.my_list));
    }

    @Override
    protected void startDownloadTask() {
        LocalSettings ls = new LocalSettings(getContext());
        ls.addToMyList("263115");
        ls.addToMyList("283995");

        DownloadMyFilmsTask task = new DownloadMyFilmsTask();
        task.execute(ls.getMyList().toArray(new String[ls.getMyList().size()]));
    }

    /**
     * Download a list of films and update the recycler view
     */
    private class DownloadMyFilmsTask extends DownloadFilmsTask {

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
