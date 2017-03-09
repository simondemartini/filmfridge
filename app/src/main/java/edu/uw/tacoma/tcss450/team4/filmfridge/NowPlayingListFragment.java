package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
public class NowPlayingListFragment extends AbstractFilmListFragment {

    private static final String TAG = "NowPlayingListFragment";
    private static final String ARG_GENRES = "Genres";
    private static final String ARG_HIDDEN_GENRES = "HiddenGenres";

    private ArrayList<String> mGenres;
    private MenuItem mFilterButton;
    private DialogFragment mChooseGenresDialog;
    private boolean[] mSelectedGenres;
    private Set<String> mHiddenGenres;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NowPlayingListFragment() {

    }

    /**
     * Part of the fragment lifecycle to inherit everything
     * @param savedInstanceState saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGenres = getArguments().getStringArrayList(ARG_GENRES);
            mHiddenGenres = new HashSet<>(Arrays.asList(getArguments().getString(ARG_HIDDEN_GENRES)));
        } else {
            mHiddenGenres = new HashSet<>();
            new DownloadGenresTask().execute();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //disable filter until list is retrieved
        mFilterButton = menu.findItem(R.id.action_filter_genres);
        if(mGenres != null) {
            mFilterButton.setVisible(true);
            mFilterButton.setEnabled(true);
        } else {
            mFilterButton.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_filter_genres) {
            createGenresListDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createGenresListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] genres = mGenres.toArray(new CharSequence[mGenres.size()]);

        // Set the dialog title
        builder.setTitle(R.string.hide_genres_dialog)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(genres, mSelectedGenres,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    notifyContentChanged();
                                    mHiddenGenres.add(mGenres.get(which));
                                    mSelectedGenres[which] = true;
                                } else if (mHiddenGenres.contains(mGenres.get(which))) {
                                    // Else, if the item is already in the array, remove it
                                    notifyContentChanged();
                                    mHiddenGenres.remove(mGenres.get(which));
                                    mSelectedGenres[which] = false;
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        if(isContentChanged) {
                            mFilmFilter.setHiddenGenres(mHiddenGenres);
                            startDownloadTask();
                        }
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(ARG_GENRES, mGenres);
        outState.putStringArray(ARG_HIDDEN_GENRES, mHiddenGenres.toArray(new String[mHiddenGenres.size()]));
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

    /**
     * Download a list of films and update the recycler view
     */
    protected class DownloadGenresTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<String> doInBackground(String... ids) {
            try {
                return tmdb.getGenres();
            } catch (TMDBFetcher.TMDBException e) {
                Log.d(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null && !result.isEmpty()) {
                mGenres = result;
                mSelectedGenres = new boolean[mGenres.size()];
                mFilterButton.setEnabled(true);
                mFilterButton.setVisible(true);
            } else if (result != null && result.isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.empty_list), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
