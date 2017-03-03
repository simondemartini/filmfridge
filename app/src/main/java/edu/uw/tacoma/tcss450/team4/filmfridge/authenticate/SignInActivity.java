package edu.uw.tacoma.tcss450.team4.filmfridge.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.tcss450.team4.filmfridge.FilmActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.R;

public class SignInActivity extends AppCompatActivity implements
        LoginFragment.LoginInteractionListener {
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
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_sign_in, new LoginFragment() )
                .commit();

        Log.i("LOGGEDIN STAT", "SingInActivity onCreate " + mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false));
        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            Intent i = new Intent(SignInActivity.this, FilmActivity.class);
            startActivity(i);
            finish();
        }





    }

    @Override
    public void login(String userId, String pwd) {
        mEmail = userId;
        mPassword = pwd;

        SignInActivity.UserTask usertask = new SignInActivity.UserTask();
        usertask.execute(buildUserUrl(LOGIN_URL));

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

                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .commit();

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
}
