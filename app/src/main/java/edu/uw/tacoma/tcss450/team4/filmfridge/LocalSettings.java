package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * This class manages the writing and getting to a shared prefs file for movie filter settings.
 *
 * Created by Simon DeMartini on 3/5/17.
 */

public class LocalSettings {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private Set<String> mMyList;

    public LocalSettings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mMyList = mSharedPreferences.getStringSet(mContext.getString(R.string.MY_LIST_IDS),
                new HashSet<String>());
    }

    /**
     * Add an film to my list by its id number
     * @param id the film id
     */
    public void addToMyList(String id) {
        mMyList.add(id);
        mSharedPreferences.edit()
                .putStringSet(mContext.getString(R.string.MY_LIST_IDS), mMyList)
                .commit();
    }

    /**
     * Read and get all of the films in MyList
     * @return
     */
    public Set<String> getMyList() {
        return mMyList;
    }
}
