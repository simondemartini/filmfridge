package edu.uw.tacoma.tcss450.team4.filmfridge.film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by simon on 3/7/17.
 */

public class FilmFilter {

    private Set<String> mHiddenIds;
    private Set<String> mHiddenGenres;

    public FilmFilter() {
        mHiddenIds = new HashSet<>();
        mHiddenGenres = new HashSet<>();
    }

    /**
     * Add a set of films that are never supposed to be shown.
     * @param hiddenIds
     */
    public void addHiddenIds(Set<String> hiddenIds ) {
        mHiddenIds.addAll(hiddenIds);
    }

    /**
     * Remove everything in the the input list that matches the rules set up in this filter.
     * @param oldList a list to be filtered
     * @return a new filtered list
     */
    public List<Film> filterFilms(List<Film> oldList) {
        List<Film> newList = new ArrayList<>();
        for(Film f: oldList) {
            if(!mHiddenIds.contains(f.getId())){
                newList.add(f);
            }
        }
        return newList;
    }
}
