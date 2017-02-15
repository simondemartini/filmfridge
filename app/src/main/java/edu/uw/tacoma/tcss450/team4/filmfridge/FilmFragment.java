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
import android.widget.Toast;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.FilmList;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.TMDBFetcher;

/**
 * A fragment representing a list of Films.
 *
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FilmFragment extends Fragment {

    private static final String TAG = "FilmFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";

    private TMDBFetcher tmdb;

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FilmFragment() {
    }

    public static FilmFragment newInstance(int columnCount) {
        FilmFragment fragment = new FilmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tmdb = new TMDBFetcher(getActivity());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DownloadFilmsTask task = new DownloadFilmsTask();
            task.execute(getString(R.string.tmdb_now_playing_url) + getString(R.string.tmdb_api_key));
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Film item);
    }

    /**
     * Download a list of films and update the recycler view
     */
    private class DownloadFilmsTask extends AsyncTask<String, Void, FilmList> {

        @Override
        protected FilmList doInBackground(String... v) {
            try {
                return tmdb.getList(getString(R.string.tmdb_now_playing_url));
            } catch (TMDBFetcher.TMDBException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(FilmList result) {
            // Everything is good, show the list.
            if (result != null && !result.isEmpty()) {
                mRecyclerView.setAdapter(new MyFilmRecyclerViewAdapter(result, mListener));
            }
        }
    }
}
