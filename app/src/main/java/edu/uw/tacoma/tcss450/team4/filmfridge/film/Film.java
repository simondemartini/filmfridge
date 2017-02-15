package edu.uw.tacoma.tcss450.team4.filmfridge.film;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * This represents a film with all of its associated metadata. It also includes a static JSON parser.
 * Created by Simon DeMartini on 2/12/17.
 */

public class Film implements Serializable {
    public static final String ID = "id", TITLE ="title", OVERVIEW = "overview",
            RELEASE_DATE = "release_date", POSTER_PATH = "poster_path",
            BACKDROP_PATH = "backdrop_path";

    private String mFilmId, mTitle, mOverview, mReleaseDate, mPosterPath, mBackdropPath;
    private Bitmap mPoster;

    public Film(String id,
                String title,
                String overview,
                String releaseDate,
                String posterPath,
                String backdropPath) {
        this.mFilmId = id;
        this.mTitle = title;
        this.mOverview = overview;
        this.mReleaseDate = releaseDate;
        this.mPosterPath = posterPath;
        this.mBackdropPath = backdropPath;
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
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

    public Bitmap getPoster() {
        return mPoster;
    }

    public void setPoster(Bitmap poster) {
        mPoster = poster;
    }

}
