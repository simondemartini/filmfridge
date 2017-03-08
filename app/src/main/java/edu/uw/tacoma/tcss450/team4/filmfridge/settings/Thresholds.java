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

public class Thresholds implements Serializable {

    public static final String EMAIL = "email", AT_HOME = "athome", IN_THEATERS = "intheaters";


    public Thresholds(int theAtHome, int theInTheaters) {

    }



    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param thresholdsJSON
     * @return reason or null if successful.
     */
    public static String parseThresholdsJSON(String thresholdsJSON, LocalSettings theLocalSettings) {
        String reason = null;
        if (thresholdsJSON != null) {
            try {
                JSONArray arr = new JSONArray(thresholdsJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    if(obj.getString(EMAIL).equals(theLocalSettings.getEmail())) {
                        theLocalSettings.setAtHomeThreshold(obj.getInt(AT_HOME));
                        theLocalSettings.setInTheatersThreshold(obj.getInt(IN_THEATERS));
                    }
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }
}
