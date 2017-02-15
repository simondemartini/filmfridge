package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LogInActivity extends AppCompatActivity {

    public static final String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam4/login.php?";
    public static final String ADD_USER =
            "http://cssgate.insttech.washington.edu/~_450bteam4/adduser.php?";

    private String[] user_urls = {
            LOGIN_URL, ADD_USER
    };

    private EditText mEmail;
    private EditText mPassword;
    private boolean mUserAuthStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }


    public void addUser (View v) {
        mEmail = (EditText)findViewById(R.id.login_email);
        mPassword = (EditText)findViewById(R.id.login_password);

        UserTask usertask = new UserTask();
        usertask.execute(buildUserUrl(ADD_USER));
        Log.i("adduserurl: ", buildUserUrl(ADD_USER));
//        if(mUserAuthStatus) {
//            Intent intent = new Intent(this, FilmActivity.class);
//            startActivity(intent);
//        }
    }

    public void login (View view) {

        mEmail = (EditText)findViewById(R.id.login_email);
        mPassword = (EditText)findViewById(R.id.login_password);

        UserTask usertask = new UserTask();
        usertask.execute(buildUserUrl(LOGIN_URL));

    }


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
                    mUserAuthStatus = true;
                    Intent i = new Intent(LogInActivity.this, FilmActivity.class);
                    startActivity(i);


                } else {
                    Toast.makeText(getApplicationContext(), "Failed to login: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                    mUserAuthStatus = false;
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

            String email = mEmail.getText().toString();
            sb.append("email=");
            sb.append(email);


            String password = mPassword.getText().toString();
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



