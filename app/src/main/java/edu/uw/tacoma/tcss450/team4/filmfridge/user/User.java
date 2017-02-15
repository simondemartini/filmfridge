package edu.uw.tacoma.tcss450.team4.filmfridge.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by apple on 2/12/2017.
 */

public class User implements Serializable {

    private static final String USERNAME = "username", PASSWORD = "password"
            , F_NAME = "fname", L_NAME = "lname";

    private String mEmail;
    private String mPassword;
    private String mFname;
    private String mLname;

    public User(String email, String password, String fname, String lname) {
        this.mEmail = email;
        this.mPassword = password;
        this.mFname = fname;
        this.mLname = lname;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns user if success
     * @param courseJSON
     * @return reason or null if successful.
     */
    public static String parseCourseJSON(String courseJSON, User user) {
        String reason = null;
        if (courseJSON != null) {
            try {
                JSONObject obj = new JSONObject(courseJSON);
                user = new User(obj.getString(User.USERNAME), obj.getString(User.PASSWORD)
                        , obj.getString(User.F_NAME), obj.getString(User.L_NAME));

            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

    public String getmLname() {
        return mLname;
    }

    public void setmLname(String mLname) {
        this.mLname = mLname;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getfName() {
        return F_NAME;
    }

    public static String getlName() {
        return L_NAME;
    }

    public String getmUsername() {
        return mEmail;
    }

    public void setmUsername(String mUsername) {
        this.mEmail = mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmFname() {
        return mFname;
    }

    public void setmFname(String mFname) {
        this.mFname = mFname;
    }







}
