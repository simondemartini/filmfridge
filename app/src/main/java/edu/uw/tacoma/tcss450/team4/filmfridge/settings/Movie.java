package edu.uw.tacoma.tcss450.team4.filmfridge.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.LocalSettings;

/**
 * Created by Samantha Ong on 3/6/2017.
 */
public class Movie implements Serializable {

    public static final String EMAIL = "email", MOVIE_ID = "movieid";

    public String mEmail;
    public String mMovieID;

    public Movie(String mEmail, String mMovieID) {
        this.mEmail = mEmail;
        this.mMovieID = mMovieID;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param movieJSON
     * @return reason or null if successful.
     */
    public static String parseMovieJSON(String movieJSON, LocalSettings theLocalSettings) {
        String reason = null;
        if (movieJSON != null) {
            try {
                JSONArray arr = new JSONArray(movieJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    if(obj.getString(EMAIL).equals(theLocalSettings.getEmail())) {
                        theLocalSettings.addToMyList(obj.getString(MOVIE_ID));
                    }

                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmMovieID() {
        return mMovieID;
    }

    public void setmMovieID(String mMovieID) {
        this.mMovieID = mMovieID;
    }
}
