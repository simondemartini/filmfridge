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

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;
    private Set<String> mMyList;

    public LocalSettings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mMyList = new HashSet<>(mSharedPreferences.getStringSet(mContext.getString(R.string.MY_LIST_IDS),
                new HashSet<String>()));
    }

    private void readMyList() {
        mMyList = new HashSet<>(mSharedPreferences.getStringSet(mContext.getString(R.string.MY_LIST_IDS),
                new HashSet<String>()));
    }

    /**
     * Add an film to my list by its id number
     * @param id the film id
     */
    public void addToMyList(String id) {
        readMyList();
        mMyList.add(id);

        mSharedPreferences.edit()
                .putStringSet(mContext.getString(R.string.MY_LIST_IDS), mMyList)
                .commit();
    }

    /**
     * Remove an film to my list by its id number
     * @param id the film id
     */
    public void removeFromMyList(String id) {
        readMyList();
        mMyList.remove(id);

        mSharedPreferences.edit()
                .putStringSet(mContext.getString(R.string.MY_LIST_IDS), mMyList)
                .commit();
    }

    /**
     * Read and get all of the films in MyList
     * @return
     */
    public Set<String> getMyList() {
        readMyList();
        return mMyList;
    }
}
