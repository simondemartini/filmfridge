package edu.uw.tacoma.tcss450.team4.filmfridge.film;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This represents a film with all of its associated metadata.
 * Created by Simon DeMartini on 2/12/17.
 */

public class Film implements Serializable {

    private String mFilmId, mTitle, mOverview, mPosterPath, mBackdropPath, mContentRating;
    private Recommendation mRecommendation;
    private int mRating;
    private List<String> mGenres, mCast;
    private Date mReleaseDate;

    /**
     * Simple constructor that just requires an id
     * @param id a unique film id
     */
    public Film(String id) {
        this.mFilmId = id;
    }

    /**
     * An overloaded constructor that creates a film with lots of basic info found in a list
     * @param id a film id
     * @param title the title
     * @param overview a short description of the film
     * @param releaseDate the release date of the film
     * @param posterPath the path to find the poster on the API
     * @param backdropPath the path to find the backdrop on the API
     *                     //TODO Delete backdrop
     */
    public Film(String id,
                String title,
                String overview,
                String releaseDate,
                String posterPath,
                String backdropPath) {
        this.mFilmId = id;
        this.mTitle = title;
        this.mOverview = overview;
        setReleaseDate(releaseDate);
        this.mPosterPath = posterPath;
        this.mBackdropPath = backdropPath;
        this.mRating = -1; //Not set yet
    }

    public Bitmap getPoster(File cacheDir) {
        File file = new File(cacheDir, mPosterPath);

        Bitmap image = null;
        if(file.exists()) {
            try {
                image = BitmapFactory.decodeFile(file.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    /**
     * Format the Date object and make it pretty to fit the user's locale
     * @return a formatted date string
     */
    public String getReleaseDate() {
        DateFormat outFormat = SimpleDateFormat.getDateInstance();
        return outFormat.format(mReleaseDate);
    }

    /**
     * Parse the input date and store as a Date.
     * @param releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.mReleaseDate = format.parse(releaseDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse the date");
        }
    }

    /**
     * Set the recommendation based on the thresholds provided
     * @param atHomeThreshold the minimum threshold to be considered for seeing at home
     * @param inTheatersThreshold the minimum threshold to be considered for seeing in theaters
     */
    public void setRecommendation(int atHomeThreshold, int inTheatersThreshold) {
        if (mRating == -1) {
            throw new IllegalArgumentException("Must set rating first");
        } else if (mRating >= inTheatersThreshold) {
            mRecommendation = Recommendation.RECOMMENDED;
        } else if (mRating >= atHomeThreshold && mRating < inTheatersThreshold) {
            mRecommendation = Recommendation.SEE_AT_HOME;
        } else {
            mRecommendation = Recommendation.NOT_RECOMMENDED;
        }
    }

    /** The following are all simple getters and setters. Should be self-explanatory */

    public Recommendation getRecommendation() {
        return mRecommendation;
    }

    public String getId() {
        return mFilmId;
    }

    public void setId(String id) {
        this.mFilmId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.mBackdropPath = backdropPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public List<String> getCast() {
        return mCast;
    }

    public void setCast(List<String> cast) {
        this.mCast = cast;
    }

    public String getContentRating() {
        return mContentRating;
    }

    public void setContentRating(String contentRating) {
        this.mContentRating = contentRating;
    }

    public List<String> getGenres() {
        return mGenres;
    }

    public void setGenres(List<String> mGenres) {
        this.mGenres = mGenres;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    public int getRating() {
        return mRating;
    }

    /**
     * A simple enum to represent the types of recommendations
     */
    public enum Recommendation {
        RECOMMENDED, SEE_AT_HOME, NOT_RECOMMENDED;
    }
}
