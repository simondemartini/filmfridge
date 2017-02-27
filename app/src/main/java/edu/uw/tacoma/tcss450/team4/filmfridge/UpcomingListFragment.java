package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class UpcomingListFragment extends Fragment {

    private static final String TAG = "UpcomingListFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";

    private TMDBFetcher tmdb;

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressSpinner;
    private MyFilmRecyclerViewAdapter mFilmRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UpcomingListFragment() {
    }

    public static UpcomingListFragment newInstance(int columnCount) {
        UpcomingListFragment fragment = new UpcomingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tmdb = new TMDBFetcher(getActivity());
        mFilmRecyclerViewAdapter = new MyFilmRecyclerViewAdapter(new ArrayList<Film>(), mListener);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);

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

            //TODO: Find a better way to limit re-downloading of info -- maybe a refresh button and a local DB?
            if (mFilmRecyclerViewAdapter.getItemCount() == 0){
                DownloadFilmsTask task = new DownloadFilmsTask();
                task.execute(getString(R.string.tmdb_now_playing_url) + getString(R.string.tmdb_api_key));
            }
        }
        return view;
    }


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
    private class DownloadFilmsTask extends AsyncTask<String, Void, List<Film>> {

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            mProgressSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Film> doInBackground(String... v) {
            try {
                return tmdb.getList(getString(R.string.tmdb_now_playing_url));
            } catch (TMDBFetcher.TMDBException e) {
                Log.d(TAG, e.getMessage());
                return null;
            }
        }

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
