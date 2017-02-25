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

    //TODO: Static vars for URLS or Resources?
    private static final String URL_CONFIG
            = "https://api.themoviedb.org/3/configuration?api_key=";
    private static final String URL_NOWPLAYING
            = "https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1&api_key=";
    private static final String URL_IMAGE_BASE
            = "https://image.tmdb.org/t/p/";
    private static final String URL_IMAGE_SIZE
            = "w342";

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
     * @param baseUrl a base URL
     * @param imagePath the location of the image (appended to the base URL)
     * @return the downloaded Image, null if its all broken
     * @throws TMDBException when  there is some network error
     */
    private Bitmap fetchImage(String baseUrl, String imagePath) throws TMDBException {
        String response = "";
        HttpURLConnection urlConnection = null;
        Bitmap bitmap;
        File file = new File(mContext.getCacheDir().toString(),imagePath);

        //check if already cached
        if(file.exists()) {
            bitmap = readCachedImage(file);
            if (bitmap != null) {
                return bitmap;
            }
        }

        //fetch from interwebs
        try {
            URL urlObject = new URL(baseUrl + imagePath);
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

        //cache the image for future use
        cacheImage(bitmap, file);

        return bitmap;
    }

    /**
     * Save an image to a cache to save API requests
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
     * Retrieve and image from the cache
     */
    private Bitmap readCachedImage(File file) {
        FileOutputStream inputStream = null;
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeFile(file.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Download posters for each of the films in the list
     * @param list of films with poster URLS
     * @throws TMDBException when there is a network error
     */
    private void fetchPosters(List<Film> list) throws TMDBException{
        //TODO: Cache posters to limit API Requests
        String baseUrl = URL_IMAGE_BASE + URL_IMAGE_SIZE;
        try {
            for (Film f : list) {
                f.setPoster(fetchImage(baseUrl, f.getPosterPath()));
            }
        } catch (TMDBException e) {
            throw new TMDBException(e);
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
                reason =  "Unable to parse data, Reason: " + e.getMessage();
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
            response = "Unable to download the list of films, Reason: "
                    + e.getMessage();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    /**
     * Download the list specified by an API URL. API Key is added in here
     * @param url a URL without an API key but ending with "...&apikey="
     * @return A complete list of films with posters
     * @throws TMDBException when things break. The caller should handle it to let the user know.
     */
    public List<Film> getList(String url) throws TMDBException {
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
            fetchPosters(list);
        } catch (TMDBException e) {
            throw new TMDBException(e);
        }

        return list;
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
