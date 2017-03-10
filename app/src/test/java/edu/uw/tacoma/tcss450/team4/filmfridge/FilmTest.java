package edu.uw.tacoma.tcss450.team4.filmfridge;

import org.junit.Test;

import edu.uw.tacoma.tcss450.team4.filmfridge.film.Film;

import static org.junit.Assert.*;

/**
 * Thoroughly test the Film class.
 * Created by Simon DeMartini on 3/9/17.
 */

public class FilmTest {

    @Test
    public void testConstructorID() {
        String id = "1231324";
        Film f = new Film(id);
        assertEquals("Constructor ID broken", f.getId(), id);
    }

    @Test
    public void testConstructorOverloaded() {
        String id = "1231324";
        String title = "Big Title";
        String overview = "Something happened, somewhere";
        String release = "2017-12-2";
        String poster = "/1234.png";
        String backdrop = "/56789.png";
        Film f = new Film(id, title, overview, release, poster, backdrop);

        assertEquals(f.getId(), id);
        assertEquals(f.getTitle(), title);
        assertEquals(f.getOverview(), overview);
        assertEquals(f.getReleaseDate(), "Dec 2, 2017");
        assertEquals(f.getPosterPath(), poster);
        assertEquals(f.getBackdropPath(), backdrop);
    }

    @Test
    public void testRecommendGreaterThanInTheaters() {
        Film f = new Film("1");
        f.setRating(95);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.RECOMMENDED, f.getRecommendation());
    }

    @Test
    public void testRecommendEqualToInTheaters() {
        Film f = new Film("1");
        f.setRating(85);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.RECOMMENDED, f.getRecommendation());
    }

    @Test
    public void testRecommendGreaterThanAtHome() {
        Film f = new Film("1");
        f.setRating(75);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.SEE_AT_HOME, f.getRecommendation());
    }

    @Test
    public void testRecommendEqualToAtHome() {
        Film f = new Film("1");
        f.setRating(65);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.SEE_AT_HOME, f.getRecommendation());
    }

    @Test
    public void testRecommendLessThanAtHome() {
        Film f = new Film("1");
        f.setRating(55);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.NOT_RECOMMENDED, f.getRecommendation());
    }

    @Test
    public void testRecommendWithZero() {
        Film f = new Film("1");
        f.setRating(0);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.NOT_RECOMMENDED, f.getRecommendation());
    }

    @Test
    public void testRecommendWithMaxInt() {
        Film f = new Film("1");
        f.setRating(Integer.MAX_VALUE);
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.RECOMMENDED, f.getRecommendation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRecommendWithNoRating() {
        Film f = new Film("1");
        f.setRecommendation(65, 85);
        assertEquals(Film.Recommendation.NOT_RECOMMENDED, f.getRecommendation());
    }

    @Test
    public void testSetReleaseDateValid() {
        Film f = new Film("1");
        f.setReleaseDate("2015-05-05");
        assertEquals("May 5, 2015", f.getReleaseDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReleaseDateWrongFormat1() {
        Film f = new Film("1");
        f.setReleaseDate("2015/05/05");
        assertEquals("May 5, 2015", f.getReleaseDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReleaseDateWrongFormat2() {
        Film f = new Film("1");
        f.setReleaseDate("May 5, 2015");
        assertEquals("May 5, 2015", f.getReleaseDate());
    }
}
