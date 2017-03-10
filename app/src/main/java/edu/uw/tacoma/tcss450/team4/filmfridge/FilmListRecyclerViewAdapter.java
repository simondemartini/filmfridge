package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.AbstractFilmListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Film} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FilmListRecyclerViewAdapter extends RecyclerView.Adapter<FilmListRecyclerViewAdapter.ViewHolder> {

    private final List<Film> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Construct this adapter with the films and the listener
     * @param items the films
     * @param listener the listener
     */
    public FilmListRecyclerViewAdapter(List<Film> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Swap out the data in the current list
     * @param data the new data
     */
    public void swap(List<Film> data) {
        mValues.clear();
        mValues.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Create each individual list item
     * @return the inflated viewholder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_film_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Fill in the data of each individual film list item
     * @param holder the viewholder
     * @param position which item this is
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mReleaseDate.setText(holder.mItem.getReleaseDate());
        setupRecommendation(holder.mItem.getRecommendation(),
                holder.mView.getContext(),
                holder.mRecommendation);

        File cacheDir = holder.mView.getContext().getCacheDir();
        holder.mPoster.setImageBitmap(holder.mItem.getPoster(cacheDir));

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

    /**
     * Display a string of the film's recommendation and set the correctly colored background
     * @param rec the film's Recommendation
     * @param context any context
     * @param recView the view to change background
     */
    private void setupRecommendation(Film.Recommendation rec, Context context, TextView recView) {
        String text;
        if(rec == null) {
            text = "FUUUCKK";
        }
        if(rec.equals(Film.Recommendation.RECOMMENDED)) {
            text = context.getString(R.string.recommended);
            recView.setBackgroundResource(R.drawable.recommendation_badge);
        } else if (rec.equals(Film.Recommendation.SEE_AT_HOME)) {
            text = context.getString(R.string.see_at_home);
            recView.setBackgroundResource(R.drawable.see_at_home_badge);
        } else {
            text = context.getString(R.string.not_recommended);
            recView.setBackgroundResource(R.drawable.not_recommended_badge);
        }
        recView.setText(text);
    }

    /**
     * Return the current number of elements in this list
     * @return the size
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Our own custom view holder for the list items.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mReleaseDate;
        public final TextView mTitleView;
        public final TextView mRecommendation;
        public final ImageView mPoster;
        public Film mItem;

        /**
         * Construct a view holder.
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mReleaseDate = (TextView) view.findViewById(R.id.release_date);
            mPoster = (ImageView) view.findViewById(R.id.poster);
            mRecommendation = (TextView) view.findViewById(R.id.recommendation);
        }

        /**
         * You, the normal toString() method
         * @return the String, yo!
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
