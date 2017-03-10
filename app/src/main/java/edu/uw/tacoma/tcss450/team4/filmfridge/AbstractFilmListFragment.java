package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.FilmFilter;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.TMDBFetcher;
import edu.uw.tacoma.tcss450.team4.filmfridge.settings.LocalSettings;

/**
 * A fragment representing a list of Films.
 *
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public abstract class AbstractFilmListFragment extends Fragment {

    protected static final String ARG_COLUMN_COUNT = "column-count";

    protected int mColumnCount = 1;
    protected TMDBFetcher tmdb;
    protected OnListFragmentInteractionListener mListener;
    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressSpinner;
    protected FilmListRecyclerViewAdapter mFilmRecyclerViewAdapter;
    protected LocalSettings mLocalSettings;
    protected FilmFilter mFilmFilter;
    protected boolean isContentChanged = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AbstractFilmListFragment() {
        //mandatory empty constructor
    }

    /**
     * Part of the fragment lifecycle to inherit everything
     * @param savedInstanceState saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        tmdb = new TMDBFetcher(getActivity());
        mLocalSettings = new LocalSettings(getActivity());
        mFilmFilter = new FilmFilter();
        mFilmRecyclerViewAdapter = new FilmListRecyclerViewAdapter(new ArrayList<Film>(), mListener);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Create all the views and components. Part of the fragment lifecycle.
     * @return the created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);

        updateTitle();
        mProgressSpinner = (ProgressBar) getActivity().findViewById(R.id.progress_spinner);
        mProgressSpinner.setVisibility(View.GONE);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(mFilmRecyclerViewAdapter);

            if(mFilmRecyclerViewAdapter.getItemCount() == 0 || isContentChanged) {
                //TODO: Find a better way to limit re-downloading of info -- maybe a refresh button and a local DB?
                isContentChanged = false;
                mFilmFilter.setHiddenIds(mLocalSettings.getHiddenList());
                startDownloadTask();
            }
        }

        return view;
    }

    /**
     * Update the list if the data set has changed
     */
    public void notifyContentChanged() {
        isContentChanged = true;
    }

    /**
     * Create and start a new AsyncTask for getting the films.
     */
    protected abstract void startDownloadTask();

    /**
     * Update thte fragment title
     */
    protected abstract void updateTitle();

    /**
     * Called when attaching to an activity.
     * @param context the fragment's context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * Standard part of fragment lifecycle.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Film item);
    }


    /**
     * Download a list of films and update the recycler view
     */
    protected abstract class AbstractDownloadFilmsTask extends AsyncTask<String, Void, List<Film>> {

        /**
         * Hide list and show progress spinner
         */
        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            mProgressSpinner.setVisibility(View.VISIBLE);
        }

        /**
         * An abstract method that requires the sub classes to change what they can download
         * @return the result
         */
        @Override
        protected abstract List<Film> doInBackground(String... v);

        /**
         * Make sure everything went right then change out the data in the list
         * @param result the result of doInBackground()
         */
        @Override
        protected void onPostExecute(List<Film> result) {
            // Everything is good, show the list.
            if (result != null && !result.isEmpty()) {
                mFilmRecyclerViewAdapter.swap(mFilmFilter.filterFilms(result));
            } else if (result != null && result.isEmpty()){
                mFilmRecyclerViewAdapter.swap(result);
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.empty_list), Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.api_error), Toast.LENGTH_LONG)
                        .show();
            }
            mProgressSpinner.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
