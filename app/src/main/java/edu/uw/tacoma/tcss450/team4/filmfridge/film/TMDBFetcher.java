package edu.uw.tacoma.tcss450.team4.filmfridge.film;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.R;

/**
 * Download data from TMDb using their API to a kind of FilmList
 * Created by Simon DeMartini on 2/14/17.
 */

public final class TMDBFetcher {

    /** For tagging in the logger */
    private static final String TAG = "TMDBFetcher";

    private static final String URL_CONFIG
            = "https://api.themoviedb.org/3/configuration?api_key=";
    private static final String URL_NOWPLAYING
            = "https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1&api_key=";
    private static final String URL_IMAGE_BASE
            = "https://image.tmdb.org/t/p/";
    private static final String URL_IMAGE_SIZE
            = "w342";
    private static final String URL_DETAIL_BASE
            = "https://api.themoviedb.org/3/movie/";
    private static final String URL_DETAIL_PARAMS
            = "?language=en-US&append_to_response=credits,releases&api_key=";

    private static final int MAX_CAST = 10;

    private Context mContext;

    /**
     * Create a new TMDB Fetcher
     * @param context The context that is responsible for this instance.
     */
    public TMDBFetcher(Context context) {
        mContext = context;
        //TODO: Get API config?
    }

    /**
     * Download an image via a given URL
     * @param imagePath the location of the image (to be appended to the base URL)
     * @return the downloaded Image, null if its all broken
     * @throws TMDBException when  there is some network error
     */
    private void fetchImage(String imagePath) throws TMDBException {
        Bitmap bitmap;
        File file = new File(mContext.getCacheDir().toString(),imagePath);

        //not cached -- fetch from internet
        if (!file.exists()) {
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(URL_IMAGE_BASE + URL_IMAGE_SIZE + imagePath);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(content);
            } catch (Exception e) {
                throw new TMDBException(e);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            //cache the image for future use
            cacheImage(bitmap, file);
        }
    }

    /**
     * Save an image to a cache to save API requests
     * @param image the image to save
     * @param file the location to save
     */
    private void cacheImage(Bitmap image, File file) {
        FileOutputStream outputStream = null;
        try {
            outputStream =new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param filmJSON, courseList
     * @return reason or null if successful.
     */
    private static String parseFilmListJSON (String filmJSON, List<Film> filmList) {
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
                reason =  "Unable to parse data, Reason: " + e.toString();
            }
        }
        return reason;
    }

    /**
     * Get more details about a film
     * Parses the json string, returns an error message if unsuccessful.
     * @param filmJSON, film
     * @return reason or null if successful.
     */
    private static String parseFilmDetailsJSON (String filmJSON, Film film) {
        String reason = null;
        if (filmJSON != null) {
            try {
                JSONObject all = new JSONObject(filmJSON);

                //get content rating
                JSONObject releases = all.getJSONObject("releases");
                JSONArray countries = releases.getJSONArray("countries");
                for (int i = 0; i < countries.length(); i++) {
                    if(countries.getJSONObject(i).getString("iso_3166_1").equals("US")) {
                        film.setContentRating(countries.getJSONObject(i).getString("certification"));
                    }
                }

                //get cast
                JSONObject credits = all.getJSONObject("credits");
                JSONArray cast = credits.getJSONArray("cast");
                StringBuilder cast_str = new StringBuilder();
                for (int i = 0; i < cast.length() && i < MAX_CAST; i++) {
                    cast_str.append(cast.getJSONObject(i).getString("name")).append(", ");
                }
                cast_str.setLength(cast_str.length() - 2);
                film.setCast(cast_str.toString());

            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.toString();
            }
        }
        return reason;
    }

    /**
     * Request the Raw JSON String from a URL
     * @param url a complete URL
     * @return the web server response, or error message
     */
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
            response = "Unable to download the request JSON: " + e.toString();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    /**
     * Add details to a film
     */
    private void fetchDetails(Film film) {
        //TODO: Only fetch when looking at details to speed it up
        String url = URL_DETAIL_BASE + film.getId()
                + URL_DETAIL_PARAMS + mContext.getString(R.string.tmdb_api_key);
        String result = requestJSON(url);
        parseFilmDetailsJSON(result, film);
    }

    /**
     * Download the list specified by an API URL. API Key is added in here
     * @param url a URL without an API key but ending with "...&apikey="
     * @return A complete list of films with posters
     * @throws TMDBException when things break. The caller should handle it to let the user know.
     */
    private List<Film> getList(String url) throws TMDBException {
        url = url + mContext.getString(R.string.tmdb_api_key);
        List<Film> list = new ArrayList<>();

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
            for (Film f : list) {
                fetchImage(f.getPosterPath());
                fetchDetails(f);
            }
        } catch (TMDBException e) {
            throw new TMDBException(e);
        }

        return list;
    }

    public List<Film> getUpcoming() throws TMDBException {
        return getList(URL_NOWPLAYING);
    }

    public List<Film> getByIds(String... ids) throws TMDBException {
        return null;
    }

    /**
     * A simple custom exception for handling errors in downloading or parsing data
     */
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
