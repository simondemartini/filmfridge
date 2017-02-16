package edu.uw.tacoma.tcss450.team4.filmfridge.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Samantha Ong on 2/12/2017.
 */

public class User implements Serializable {

    private static final String USERNAME = "username", PASSWORD = "password"
            , F_NAME = "fname", L_NAME = "lname";

    /**
     * the users email.
     */
    private String mEmail;
    /**
     * the users password.
     */
    private String mPassword;
    /**
     * the users first name.
     */
    private String mFname;
    /**
     * the users last name.
     */
    private String mLname;

    /**
     * User constructors.
     * @param email the email.
     * @param password the password.
     * @param fname the first name.
     * @param lname the last name.
     */
    public User(String email, String password, String fname, String lname) {
        this.mEmail = email;
        this.mPassword = password;
        this.mFname = fname;
        this.mLname = lname;
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
