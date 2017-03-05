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
public class NowPlayingListFragment extends AbstractFilmListFragment {

    private static final String TAG = "NowPlayingListFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NowPlayingListFragment() {

    }

    @Override
    protected boolean isContentChanged() {
        return false;
    }

    @Override
    protected void updateTitle() {
        getActivity().setTitle(getString(R.string.now_playing_films));
    }

    @Override
    protected void startDownloadTask() {
        DownloadUpcomingFilmsTask task = new DownloadUpcomingFilmsTask();
        task.execute();
    }

    /**
     * Download a list of films and update the recycler view
     */
    private class DownloadUpcomingFilmsTask extends DownloadFilmsTask {

        @Override
        protected List<Film> doInBackground(String... v) {
            try {
                return tmdb.getUpcoming();
            } catch (TMDBFetcher.TMDBException e) {
                Log.d(TAG, e.getMessage());
                return null;
            }
        }
    }
}
