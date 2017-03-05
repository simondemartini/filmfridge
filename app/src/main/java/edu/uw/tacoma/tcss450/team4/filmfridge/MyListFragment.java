package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    protected boolean isContentChanged() {
        return  mFilmRecyclerViewAdapter.getItemCount() != mLocalSettings.getMyList().size();
    }

    @Override
    protected void updateTitle() {
        getActivity().setTitle(getString(R.string.my_list));
    }

    @Override
    protected void startDownloadTask() {
        String [] ids = mLocalSettings.getMyList()
                .toArray(new String[mLocalSettings.getMyList().size()]);
        new DownloadMyFilmsTask().execute(ids);
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
