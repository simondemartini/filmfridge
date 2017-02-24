package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uw.tacoma.tcss450.team4.filmfridge.UpcomingListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Film} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyFilmRecyclerViewAdapter extends RecyclerView.Adapter<MyFilmRecyclerViewAdapter.ViewHolder> {

    private final List<Film> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyFilmRecyclerViewAdapter(List<Film> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_film_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mReleaseDate.setText(holder.mItem.getReleaseDate());
        holder.mPoster.setImageBitmap(holder.mItem.getPoster());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mReleaseDate;
        public final TextView mTitleView;
        public final ImageView mPoster;
        public Film mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mReleaseDate = (TextView) view.findViewById(R.id.release_date);
            mPoster = (ImageView) view.findViewById(R.id.poster);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
