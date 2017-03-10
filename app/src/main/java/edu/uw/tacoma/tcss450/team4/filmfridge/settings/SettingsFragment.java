package edu.uw.tacoma.tcss450.team4.filmfridge.settings;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.tcss450.team4.filmfridge.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnSettingsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    public static final String URL_SET_THRESHOLD =
            "http://cssgate.insttech.washington.edu/~_450bteam4/setThreshold.php?";

    private Context mContext;
    private TextView mAtHomeTV, mInTheatersTV, mUserEmail;
    private SeekBar mAtHomeSB, mInTheatersSB;
    private ProgressBar mProgressSpinner;
    private LocalSettings mLocalSettings;
    private OnSettingsInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create the layout for the settings fragment
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle(getString(R.string.settings));
        mLocalSettings = new LocalSettings(v.getContext());

        //hide spinner
        mProgressSpinner = (ProgressBar) getActivity().findViewById(R.id.progress_spinner);
        mProgressSpinner.setVisibility(View.GONE);

        //update user email
        mUserEmail = (TextView) v.findViewById(R.id.useremail) ;
        mUserEmail.setText(mLocalSettings.getEmail());

        //create seek bars for recommendation thresholds
        mAtHomeSB = (SeekBar) v.findViewById(R.id.ahSeekBar);
        mAtHomeTV = (TextView) v.findViewById(R.id.ahtextview);
        mInTheatersSB = (SeekBar) v.findViewById(R.id.itSeekBar);
        mInTheatersTV = (TextView) v.findViewById(R.id.ittextview);
        mAtHomeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int theProgress, boolean fromUser) {
                //limit max progress to in theaters recommendation
                if(theProgress > mInTheatersSB.getProgress()) {
                    mAtHomeSB.setProgress(mInTheatersSB.getProgress());
                }
                mAtHomeTV.setText(mAtHomeSB.getProgress() + "/" + mAtHomeSB.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAtHomeTV.setText(mAtHomeSB.getProgress() + "/" + mAtHomeSB.getMax());
                mLocalSettings.setAtHomeThreshold(mAtHomeSB.getProgress());
                mListener.onSettingsChange();
            }
        });
        mInTheatersSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int theProgress, boolean fromUser) {
                //limit progress min to at home threshold
                if(theProgress < mAtHomeSB.getProgress()) {
                    mInTheatersSB.setProgress(mAtHomeSB.getProgress());
                }
                mInTheatersTV.setText(mInTheatersSB.getProgress() + "/" + mInTheatersSB.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do nothing out of the ordinary
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mInTheatersTV.setText(mInTheatersSB.getProgress() + "/" + mInTheatersSB.getMax());
                mLocalSettings.setInTheatersThreshold(mInTheatersSB.getProgress());
                mListener.onSettingsChange();
            }
        });
        mInTheatersSB.setProgress(mLocalSettings.getInTheatersThreshold());
        mAtHomeSB.setProgress(mLocalSettings.getAtHomeThreshold());

        //create save prefs button to sync to server
        Button savePrefsButton = (Button) v.findViewById(R.id.saveprefs_button);
        savePrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocalSettings.setInTheatersThreshold(mInTheatersSB.getProgress());
                new ThresholdTask().execute(buildUserUrl(URL_SET_THRESHOLD,
                        mUserEmail.getText().toString(),
                        mAtHomeSB.getProgress(),
                        mInTheatersSB.getProgress()));
            }
        });

        return v;
    }

    /**
     * Lifecycle method: Attach this fragment to an activity, and get the listener and create
     * local settings
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsInteractionListener) {
            mListener = (OnSettingsInteractionListener) context;
            mContext = context;
            mLocalSettings = new LocalSettings(getContext());
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsInteractionListener");
        }
    }

    /**
     * Lifecycle method: Remove the listener after detaching from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Create a url to submit the at home thresholds and in theaters thresholds for a user.
     * @param url the base url
     * @param email the users email
     * @param atHome threshold
     * @param inTheaters threshold
     * @return a String url
     */
    private String buildUserUrl(String url, String email, int atHome, int inTheaters) {

        StringBuilder sb = new StringBuilder(url);

        try {
            sb.append("email=");
            sb.append(email);

            sb.append("&athome=");
            sb.append(atHome);

            sb.append("&intheaters=");
            sb.append(inTheaters);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * AsyncTask for updating thresholds.
     */
    private class ThresholdTask extends AsyncTask<String, Void, String> {

        /**
         * Connect and submit the updated thresholds
         * @param urls
         * @return
         */
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
                    response = "Unable to set Threshold, Reason: "
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
                    Toast.makeText(getActivity().getApplicationContext(), "Updated Thresholds Successfully."
                            , Toast.LENGTH_LONG)
                            .show();

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to update Thresholds: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSettingsInteractionListener {
        void onSettingsChange();
    }
}
