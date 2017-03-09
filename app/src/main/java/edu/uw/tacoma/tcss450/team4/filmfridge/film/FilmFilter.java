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
     * Set the set of films that are never supposed to be shown.
     * @param hiddenIds
     */
    public void setHiddenIds(Set<String> hiddenIds ) {
        mHiddenIds.clear();
        mHiddenIds.addAll(hiddenIds);
    }

    /**
     * If a film contains one of these genres, it will never be shown
     * @param hiddenGenres
     */
    public void setHiddenGenres(Set<String> hiddenGenres ) {
        mHiddenGenres.clear();
        mHiddenGenres.addAll(hiddenGenres);
    }

    /**
     * Remove everything in the the input list that matches the rules set up in this filter.
     * @param oldList a list to be filtered
     * @return a new filtered list
     */
    public List<Film> filterFilms(List<Film> oldList) {
        List<Film> newList = new ArrayList<>();
        for(Film f: oldList) {
            boolean hasBannedGenre = false;
            boolean hasBannedId = false;
            if(mHiddenIds.contains(f.getId())){
                hasBannedId = true;
            } else {
                for (String fGenre : f.getGenres()) {
                    if (mHiddenGenres.contains(fGenre)) {
                        hasBannedGenre = true;
                    }
                }
            }
            if(!hasBannedGenre && !hasBannedId) {
                newList.add(f);
            }
        }
        return newList;
    }
}
