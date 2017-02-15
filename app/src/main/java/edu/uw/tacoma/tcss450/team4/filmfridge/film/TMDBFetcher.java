package edu.uw.tacoma.tcss450.team4.filmfridge.film;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.MyFilmRecyclerViewAdapter;
import edu.uw.tacoma.tcss450.team4.filmfridge.R;

/**
 * Download data from TMDb to some kind of FilmList
 * Created by Simon DeMartini on 2/14/17.
 */

public final class TMDBFetcher {

    private static final String TAG = "TMDBFetcher";

    private static final String URL_CONFIG
            = "https://api.themoviedb.org/3/configuration?api_key=";
    private static final String URL_NOWPLAYING
            = "https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1&api_key=";
    private static final String URL_IMAGE_BASE
            = "https://image.tmdb.org/t/p/";
    private static final String URL_IMAGE_SIZE
            = "w342";

    private static Context mContext;

    public TMDBFetcher(Context context) {
        mContext = context;
        //get API config

    }

    private Bitmap fetchImage(String url) throws TMDBException {
        String response = "";
        HttpURLConnection urlConnection = null;
        Bitmap bitmap;

        try {
            URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            InputStream content = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(content);
        } catch (Exception e) {
            throw new TMDBException(e);
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return bitmap;
    }

    /** Download posters for each of the films in the list */
    private void fetchPosters(FilmList list) throws TMDBException{
        //TODO Caches posters to limit API Requests
        try {
            for (Film f : list) {
                String url = URL_IMAGE_BASE + URL_IMAGE_SIZE + f.getPosterPath();
                f.setPoster(fetchImage(url));
            }
        } catch (TMDBException e) {
            throw new TMDBException(e);
        }
    }

    /** Get more details for a specific movie **/
    public void fetchDetails(Film film) {
        //check if already has them

    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param filmJSON, courseList
     * @return reason or null if successful.
     */
    public static String parseFilmListJSON (String filmJSON, List<Film> filmList) {
        String reason = null;
        if (filmJSON != null) {
            try {
                JSONObject all = new JSONObject(filmJSON);
                JSONArray arr = all.getJSONArray("results");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Film film = new Film(obj.getString(Film.ID),
                            obj.getString(Film.TITLE),
                            obj.getString(Film.OVERVIEW),
                            obj.getString(Film.RELEASE_DATE),
                            obj.getString(Film.POSTER_PATH),
                            obj.getString(Film.BACKDROP_PATH));
                    filmList.add(film);
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

    /** Fetch the JSON of a URL */
    private String requestJSON(String url) {
        String response = "";
        HttpURLConnection urlConnection = null;

        try {
            URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();

            InputStream content = urlConnection.getInputStream();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

        } catch (Exception e) {
            response = "Unable to download the list of films, Reason: "
                    + e.getMessage();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    /** Download the list specified by an API URL. API Key is added in here*/
    public FilmList getList(String url) throws TMDBException {
        url = url + mContext.getString(R.string.tmdb_api_key);
        FilmList list = new FilmList();

        //download data
        String result = requestJSON(url);
        if(result.startsWith("Unable to")) {
            //something broke with network or URL
            throw new TMDBException(result);
        }

        //parse JSON
        result = parseFilmListJSON(result, list);
        if(result != null) {
            //something went wrong with parsing
            throw new TMDBException(result);
        }

        //add images
        try {
            fetchPosters(list);
        } catch (TMDBException e) {
            throw new TMDBException(e);
        }

        return list;
    }

    /** A custom exception for handling errors in downloading or parsing data*/
    public class TMDBException extends Exception {
        public TMDBException(String message) {
            super(message);
        }

        public TMDBException(String message, Throwable cause) {
            super(message, cause);
        }

        public TMDBException(Throwable cause) {
            super(cause);
        }
    }
}
