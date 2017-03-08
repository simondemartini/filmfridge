package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uw.tacoma.tcss450.team4.filmfridge.AbstractFilmListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

import java.io.File;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Film} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FilmListRecyclerViewAdapter extends RecyclerView.Adapter<FilmListRecyclerViewAdapter.ViewHolder> {

    private final List<Film> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FilmListRecyclerViewAdapter(List<Film> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void swap(List<Film> data) {
        mValues.clear();
        mValues.addAll(data);
        notifyDataSetChanged();
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

    private void setupRecommendation(Film.Recommendation rec, Context context, TextView recView) {
        String text;
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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mReleaseDate;
        public final TextView mTitleView;
        public final TextView mRecommendation;
        public final ImageView mPoster;
        public Film mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mReleaseDate = (TextView) view.findViewById(R.id.release_date);
            mPoster = (ImageView) view.findViewById(R.id.poster);
            mRecommendation = (TextView) view.findViewById(R.id.recommendation);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
