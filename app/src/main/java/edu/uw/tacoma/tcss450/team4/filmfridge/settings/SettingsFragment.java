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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String SET_THRESHOLD=
            "http://cssgate.insttech.washington.edu/~_450bteam4/setThreshold.php?";

    private String mParam1;
    private String mParam2;
    private TextView mAtHomeTV;
    private SeekBar mAtHomeSB;
    private int mAtHomeProgress;
    private TextView mUserEmail;
    private TextView mInTheatersTV;
    private SeekBar mInTheatersSB;
    private int mInTheatersProgress;
    private ProgressBar mProgressSpinner;
    private Context mContext;
    private LocalSettings mLocalSettings;
    private OnSettingsInteractionListener mListener;

    //for Adding thresholds
    private String mEmail;
    private int mAtHome;
    private int mInTheaters;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mLocalSettings = new LocalSettings(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle(getString(R.string.settings));

        //hide spinner
        mProgressSpinner = (ProgressBar) getActivity().findViewById(R.id.progress_spinner);
        mProgressSpinner.setVisibility(View.GONE);

        mAtHomeSB = (SeekBar) v.findViewById(R.id.ahSeekBar);
        mAtHomeTV = (TextView) v.findViewById(R.id.ahtextview);
        mLocalSettings = new LocalSettings(v.getContext());
        mAtHomeTV.setText(mAtHomeSB.getProgress() + "/" + mAtHomeSB.getMax());
        mInTheatersSB = (SeekBar) v.findViewById(R.id.itSeekBar);
        mInTheatersTV = (TextView) v.findViewById(R.id.ittextview);
        mAtHomeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int theProgress, boolean fromUser) {
                mAtHomeProgress = theProgress;
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
                mInTheatersProgress = theProgress;
                //limit progress min to at home threshold
                if(mInTheatersProgress < mAtHomeSB.getProgress()) {
                    mInTheatersSB.setProgress(mAtHomeSB.getProgress());
                }
                mInTheatersTV.setText(mInTheatersSB.getProgress() + "/" + mInTheatersSB.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mInTheatersTV.setText(mInTheatersSB.getProgress() + "/" + mInTheatersSB.getMax());
//                mLocalSettings.setInTheatersThreshold(mInTheatersSB.getProgress());
                mListener.onSettingsChange();
            }
        });

        mUserEmail = (TextView) v.findViewById(R.id.useremail) ;
        mUserEmail.setText(mLocalSettings.getEmail());

        Button savePrefsButton = (Button) v.findViewById(R.id.saveprefs_button);
        savePrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocalSettings.setInTheatersThreshold(mInTheatersSB.getProgress());
                setThreshold(mUserEmail.getText().toString(),
                        mAtHomeSB.getProgress(),
                        mInTheatersSB.getProgress());
            }
        });

        mInTheatersSB.setProgress(mLocalSettings.getInTheatersThreshold());
        mAtHomeSB.setProgress(mLocalSettings.getAtHomeThreshold());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsInteractionListener) {
            mListener = (OnSettingsInteractionListener) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void setThreshold(String theEmail, int theAtHome, int theInTheaters) {
        mEmail = theEmail;
        mAtHome = theAtHome;
        mInTheaters = theInTheaters;

        ThresholdTask setthreshtask = new ThresholdTask();
        setthreshtask.execute(buildUserUrl(SET_THRESHOLD));
    }

    /**
     * AsyncTask for updating thresholds.
     */
    private class ThresholdTask extends AsyncTask<String, Void, String> {

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

    private String buildUserUrl(String string) {

        StringBuilder sb = new StringBuilder(string);

        try {

            String email = mEmail;
            sb.append("email=");
            sb.append(email);

            int ah = mAtHome;
            sb.append("&athome=");
            sb.append(ah);

            int it = mInTheaters;
            sb.append("&intheaters=");
            sb.append(it);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }
}
