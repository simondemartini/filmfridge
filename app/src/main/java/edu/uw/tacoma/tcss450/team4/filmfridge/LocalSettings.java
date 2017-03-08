package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * This class manages the writing and getting to a shared prefs file for movie filter settings.
 *
 * Created by Simon DeMartini on 3/5/17.
 * Edited by Samantha Ong
 */

public class LocalSettings {

    private final static String ID_MY_LIST = "edu.uw.tacoma.tcss422.filmfridge.MY_LIST_IDS";
    private final static String ID_AT_HOME = "edu.uw.tacoma.tcss422.filmfridge.AT_HOME";
    private final static String ID_IN_THEATERS = "edu.uw.tacoma.tcss422.filmfridge.ID_IN_THEATERS";
    private final static String ID_LOGGED_IN = "edu.uw.tacoma.tcss422.filmfridge.ID_LOGGED_IN";
    private final static String ID_EMAIL = "edu.uw.tacoma.tcss422.filmfridge.ID_EMAIL";


    private final static int DEFAULT_AT_HOME_VALUE = 60;
    private final static int DEFAULT_IN_THEATERS_VALUE = 70;

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    public LocalSettings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
    }

    public Set<String> getMyList() {
        return new HashSet<>(mSharedPreferences.getStringSet(mContext.getString(R.string.MY_LIST_IDS),
                new HashSet<String>()));
    }

    /**
     * Add an film to my list by its id number
     * @param id the film id
     */
    public void addToMyList(String id) {
        Set<String> list = getMyList();
        list.add(id);

        mSharedPreferences.edit()
                .putStringSet(ID_MY_LIST, list)
                .commit();
    }

    /**
     * Remove an film to my list by its id number
     * @param id the film id
     */
    public void removeFromMyList(String id) {
        Set<String> list = getMyList();
        list.remove(id);

        mSharedPreferences.edit()
                .putStringSet(ID_MY_LIST, list)
                .commit();
    }

    /**
     * Set at home movie threshold
     */
    public void setAtHomeThreshold(int atHomeThreshold) {
        mSharedPreferences.edit()
                .putInt(ID_AT_HOME, atHomeThreshold)
                .commit();
    }

    /**
     * Set at in theaters movie threshold
     */
    public void setInTheatersThreshold(int inTheatersThreshold) {
        mSharedPreferences.edit()
                .putInt(ID_IN_THEATERS, inTheatersThreshold)
                .commit();
    }

    /**
     * Get the At Home Threshold or the deafult value if it doesn't exist
     */
    public int getAtHomeThreshold() {
        return mSharedPreferences.getInt(ID_AT_HOME, DEFAULT_AT_HOME_VALUE);
    }

    /**
     * Get the in theaters threshold, or the default value if it doesn't exist
     */
    public int getInTheatersThreshold() {
        return mSharedPreferences.getInt(ID_IN_THEATERS, DEFAULT_IN_THEATERS_VALUE);
    }

    /**
     * Set whether a user is logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        mSharedPreferences.edit()
                .putBoolean(ID_LOGGED_IN, loggedIn)
                .commit();
    }

    /**
     * Get whether the user is logged in. False is default if it doesn't exist
     */
    public boolean getLoggedIn() {
        return mSharedPreferences.getBoolean(ID_LOGGED_IN, false);
    }

    /**
     * Set whether a user's logged in email
     */
    public void setEmail(String email) {
        mSharedPreferences.edit()
                .putString(ID_EMAIL, email)
                .commit();
    }

    /**
     * Get whether the user is logged in. Defaults to null if not logged in
     */
    public String getEmail() {
        return mSharedPreferences.getString(ID_EMAIL, null);
    }
}
