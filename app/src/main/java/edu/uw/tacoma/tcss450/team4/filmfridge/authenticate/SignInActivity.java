package edu.uw.tacoma.tcss450.team4.filmfridge.authenticate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import edu.uw.tacoma.tcss450.team4.filmfridge.FilmActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.settings.LocalSettings;
import edu.uw.tacoma.tcss450.team4.filmfridge.R;


/**
 * The Signin Activity for signing in to the app
 *
 * Created by Samantha Ong
 */
public class SignInActivity extends AppCompatActivity implements
        LoginFragment.LoginInteractionListener,
        RegisterFragment.OnRegisterInteractionListener
{
    /**
     * Login Php url.
     */
    public static final String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam4/login.php?";

    /**
     * Register php url.
     */
    public static final String ADD_USER =
            "http://cssgate.insttech.washington.edu/~_450bteam4/adduser.php?";

    /**
     * get threshold url.
     */
    public static final String THRESHOLD_GET_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam4/thresholds.php?cmd=thresholds&";

    /**
     * set initial thresholds when registering url.
     */
    public static final String THRESHOLD_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam4/addThreshold.php?";

    /**
     * text typed in email edittxt field.
     */
    private String mEmail;
    /**
     * text typed in password edittxt field.
     */
    private String mPassword;

    /**
     * Shared preferences to remember that the user successfully logged in.
     */
    private LocalSettings mLocalSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mLocalSettings = new LocalSettings(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_sign_in, new LoginFragment() )
                .commit();

        Log.i("LOGGEDIN STAT", "SingInActivity onCreate " + mLocalSettings.getLoggedIn());
        if (mLocalSettings.getLoggedIn()) {
            Intent i = new Intent(SignInActivity.this, FilmActivity.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * A listener method to login a user
     * @param userId the userid
     * @param pwd the password
     */
    @Override
    public void login(String userId, String pwd) {
        mEmail = userId;
        mPassword = pwd;

        SignInActivity.UserTask usertask = new SignInActivity.UserTask();
        usertask.execute(buildUserUrl(LOGIN_URL));

        GetThresholdTask createThreshTask = new GetThresholdTask();
        createThreshTask.execute(buildThreshGetURL(THRESHOLD_GET_URL));

    }

    /**
     * A listener method to register a user
     * @param userId the userid
     * @param pwd the password
     */
    @Override
    public void register(String userId, String pwd) {
        mEmail = userId;
        mPassword = pwd;

        SignInActivity.RegisterTask usertask = new SignInActivity.RegisterTask();
        usertask.execute(buildUserUrl(ADD_USER));

        CreateThresholdTask createThreshTask = new CreateThresholdTask();
        createThreshTask.execute(buildThreshAddURL(THRESHOLD_ADD_URL));
    }


    /**
     * AsyncTask for Users registration and login.
     */
    private class UserTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
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
                    response = "Unable to login, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("response: ", response);
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Welcome to FilmFridge!"
                            , Toast.LENGTH_LONG)
                            .show();

                    mLocalSettings.setLoggedIn(true);
                    mLocalSettings.setEmail(mEmail);

                    Log.i("LOGGEDIN STAT:", "ONPOSTEXECUTE TRUE");

                    Intent i = new Intent(SignInActivity.this, FilmActivity.class);
                    startActivity(i);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), "Failed to login: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }


    /**
     * Build a url for fetching the threshold for a user.
     * @param string the base url
     * @return a complete url
     */
    private String buildThreshGetURL(String string) {

        StringBuilder sb = new StringBuilder(string);

        try {
            String email = mEmail;
            sb.append("email=");
            sb.append(email);
        } catch (Exception e) {
            Toast.makeText(this, "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * Build a url for adding the initial thresholds for a user.
     * @param string the base url
     * @return a complete url
     */
    private String buildThreshAddURL(String string) {

        StringBuilder sb = new StringBuilder(string);

        try {
            String email = mEmail;
            sb.append("email=");
            sb.append(email);

            int ah = LocalSettings.DEFAULT_AT_HOME_VALUE;
            sb.append("&athome=");
            sb.append(ah);

            int it = LocalSettings.DEFAULT_IN_THEATERS_VALUE;
            sb.append("&intheaters=");
            sb.append(it);

        } catch (Exception e) {
            Toast.makeText(this, "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * Build a url for logging in a user.
     * @param string the base url
     * @return a complete url
     */
    private String buildUserUrl(String string) {

        StringBuilder sb = new StringBuilder(string);

        try {

            String email = mEmail;
            sb.append("email=");
            sb.append(email);


            String password = mPassword;
            sb.append("&password=");
            sb.append(password);


        }
        catch(Exception e) {
            Toast.makeText(this, "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * AsyncTask for Users registration and login.
     */
    private class RegisterTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
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
                    response = "Unable to register, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("response: ", response);
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Registered Successfully."
                            , Toast.LENGTH_LONG)
                            .show();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.activity_sign_in, new LoginFragment())
                            .commit();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to register: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * AsyncTask for creating thresholds.
     */
    private class GetThresholdTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
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
                    response = "Unable to create Threshold, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("response: ", response);
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int atHome = jsonObject.getInt("athome");
                int intheaters = jsonObject.getInt("intheaters");
                mLocalSettings.setAtHomeThreshold(atHome);
                mLocalSettings.setInTheatersThreshold(intheaters);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * AsyncTask for creating thresholds.
     */
    private class CreateThresholdTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
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
                    response = "Unable to create Threshold, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("response: ", response);
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {


                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update Thresholds: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
