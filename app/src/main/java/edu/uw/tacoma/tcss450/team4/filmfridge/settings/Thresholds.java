package edu.uw.tacoma.tcss450.team4.filmfridge.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Samantha Ong on 3/6/2017.
 */

public class Thresholds implements Serializable {

    public static final String AT_HOME = "athome", IN_THEATERS = "intheaters";


    public int mAtHome;
    public int mInTheaters;

    public Thresholds(int theAtHome, int theInTheaters) {
        mAtHome = theAtHome;
        mInTheaters = theInTheaters;
    }


    public int getmAtHome() {
        return mAtHome;
    }

    public void setmAtHome(int mAtHome) {
        this.mAtHome = mAtHome;
    }

    public int getmInTheaters() {
        return mInTheaters;
    }

    public void setmInTheaters(int mInTheaters) {
        this.mInTheaters = mInTheaters;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param thresholdsJSON
     * @return reason or null if successful.
     */
    public static String parseThresholdsJSON(String thresholdsJSON, List<Thresholds> thresholdsList) {
        String reason = null;
        if (thresholdsJSON != null) {
            try {
                JSONArray arr = new JSONArray(thresholdsJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Thresholds thresholds = new Thresholds(obj.getInt(Thresholds.AT_HOME),
                            obj.getInt(Thresholds.IN_THEATERS));
                    thresholdsList.add(thresholds);
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }
}
