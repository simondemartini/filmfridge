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
import edu.uw.tacoma.tcss450.team4.filmfridge.film.TMDBFetcher;

/**
 * A fragment representing a list of Films.
 *
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public abstract class AbstractFilmListFragment extends Fragment {

    protected static final String ARG_COLUMN_COUNT = "column-count";

    protected TMDBFetcher tmdb;

    protected int mColumnCount = 1;
    protected OnListFragmentInteractionListener mListener;
    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressSpinner;
    protected FilmListRecyclerViewAdapter mFilmRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AbstractFilmListFragment() {
    }

    /**
     * Part of the fragment lifecycle to inherit everything
     * @param savedInstanceState saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        tmdb = new TMDBFetcher(getActivity());
        mFilmRecyclerViewAdapter = new FilmListRecyclerViewAdapter(new ArrayList<Film>(), mListener);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Create all the views and components. Part of the fragment lifecycle.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);

        mProgressSpinner = (ProgressBar) getActivity().findViewById(R.id.progress_spinner);
        mProgressSpinner.setVisibility(View.GONE);

        updateTitle();

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

            if(mFilmRecyclerViewAdapter.getItemCount()==0) {
                startDownloadTask();
            }

        }
        return view;
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
    protected abstract class DownloadFilmsTask extends AsyncTask<String, Void, List<Film>> {

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            mProgressSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected abstract List<Film> doInBackground(String... v);

        @Override
        protected void onPostExecute(List<Film> result) {
            // Everything is good, show the list.
            if (result != null && !result.isEmpty()) {
                mFilmRecyclerViewAdapter.swap(result);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.api_error), Toast.LENGTH_LONG)
                        .show();
            }
            mProgressSpinner.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
