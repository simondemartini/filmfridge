package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;
import edu.uw.tacoma.tcss450.team4.filmfridge.settings.LocalSettings;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDetailFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilmDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmDetailFragment extends Fragment {

    public static final String FILM_ITEM_SELECTED = "film_selected";

    private static final int MAX_CAST = 15;
    private static final String TAG = "FilmDetailFragment";

    private Film mFilm;
    private ImageView mPoster;
    private Button mMyListModifier;
    private LocalSettings mLocalSettings;
    private OnDetailFragmentInteractionListener mListener;
    private TextView mDescriptionTV,
            mReleaseDateTV,
            mCastTV,
            mContentRatingTV,
            mTitleTV,
            mGenresTV,
            mRecommendation,
            mCriticRating,
            mCriticRatingLabel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FilmDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param item The film.
     * @return A new instance of fragment FilmDetailFragment.
     */
    public static FilmDetailFragment newInstance(Film item) {
        FilmDetailFragment fragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FilmDetailFragment.FILM_ITEM_SELECTED, item);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Lifecycle method: Get data from saved state, and init the menu and local settings
     * @param savedInstanceState the saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFilm = (Film) getArguments().getSerializable(FILM_ITEM_SELECTED);
        }
        setHasOptionsMenu(true);
        mLocalSettings = new LocalSettings(getActivity());
    }

    /**
     * Lifecycle method: Inflate the view & add button listeners
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        mTitleTV = (TextView) view.findViewById(R.id.title);
        mReleaseDateTV = (TextView) view.findViewById(R.id.release_date);
        mDescriptionTV = (TextView) view.findViewById(R.id.description);
        mCastTV = (TextView) view.findViewById(R.id.cast);
        mContentRatingTV = (TextView) view.findViewById(R.id.content_rating);
        mGenresTV = (TextView) view.findViewById(R.id.genres);
        mPoster = (ImageView) view.findViewById(R.id.poster);
        mRecommendation = (TextView) view.findViewById(R.id.recommendation);
        mCriticRating = (TextView) view.findViewById(R.id.critic_rating);
        mCriticRatingLabel = (TextView) view.findViewById(R.id.label_critic_rating);


        mMyListModifier = (Button) view.findViewById(R.id.add_to_my_list);
        mMyListModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddToMyList(mFilm);
            }
        });

        Button hideForever = (Button) view.findViewById(R.id.hide_forever);
        hideForever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onHideForever(mFilm);
            }
        });

        return view;
    }

    /**
     * Fill in the views with data about the film.
     * @param film
     */
    public void updateView(Film film) {
        if (film != null) {
            mTitleTV.setText(film.getTitle());
            mReleaseDateTV.setText(film.getReleaseDate());
            mDescriptionTV.setText(film.getOverview());
            mContentRatingTV.setText(film.getContentRating());
            mPoster.setImageBitmap(film.getPoster(getContext().getCacheDir()));
            mGenresTV.setText(listToString(film.getGenres()));
            mCriticRating.setText(String.format(getString(R.string.critic_rating), film.getRating()));
            int maxCast = MAX_CAST;
            if(film.getCast().size() < MAX_CAST) {
                maxCast = film.getCast().size();
            }
            mCastTV.setText(listToString(film.getCast().subList(0, maxCast)));

            //Change button to remove or add from my list
            setMyListButton(mLocalSettings.getMyList().contains(film.getId()));

            //setup Recommendation
            setupRecommendation(film.getRecommendation(), getContext(), mRecommendation);

        }
    }

    /**
     * Display a string of the film's recommendation and set the correctly colored background
     * @param rec the film's Recommendation
     * @param context any context
     * @param recView the view to change background
     */
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

    /**
     * A helper method to swap the content and listener of the button that adds to MyList
     * @param isInMyList whether the film is in the list
     */
    private void setMyListButton(boolean isInMyList) {
        if(isInMyList) {
            mMyListModifier.setText(getString(R.string.remove_from_my_list));
            mMyListModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRemoveFromMyList(mFilm);
                    setMyListButton(false);
                }
            });
        } else {
            mMyListModifier.setText(getString(R.string.add_to_my_list));
            mMyListModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddToMyList(mFilm);
                    setMyListButton(true);
                }
            });
        }
    }

    /**
     * A helper method to get the toString of a list but remove the "[" and "]" around it
     * @param list a list
     * @return a clean string
     */
    private String listToString(List list) {
        if(list != null && list.toString().length() > 1) {
            String str = list.toString();
            return str.substring(1, str.length() - 1);
        } else {
            return null;
        }

    }

    /**
     * Lifecycle method: load up arguments
     */
    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((Film) args.getSerializable(FILM_ITEM_SELECTED));
        }
    }

    /**
     * Lifecycle method: When being added to the activity, attach the required listener
     * @param context the context it's being attached to.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailFragmentInteractionListener");
        }
    }

    /**
     * Lifecycle method: Remove listeners when being removed from parent activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Add the share & reveal buttons to the toolbar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem reveal = menu.findItem(R.id.action_reveal);
        share.setVisible(true);
        reveal.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * What to do when the menu items are selected
     * @param item the item clicked
     * @return whether this method handled the item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_share) {
            shareFilm();
            return true;
        } else if (id == R.id.action_reveal) {
            revealScore();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method takes care of creating a new intent to share the data.
     */
    private void shareFilm() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        String shareContent = mFilm.getTitle();
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareContent);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_chooser)));
    }

    /**
     * This method reveals the actual critic rating of a film
     */
    private void revealScore() {
        mCriticRating.setVisibility(View.VISIBLE);
        mCriticRatingLabel.setVisibility(View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDetailFragmentInteractionListener {
        void onAddToMyList(Film film);
        void onRemoveFromMyList(Film film);
        void onHideForever(Film film);
    }
}
