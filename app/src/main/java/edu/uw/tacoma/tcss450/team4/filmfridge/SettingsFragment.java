package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mAtHomeTV;
    private SeekBar mAtHomeSB;
    private TextView mUserEmail;
    private TextView mInTheatersTV;
    private SeekBar mInTheatersSB;

    private LocalSettings mLocalSettings;

    private OnSettingsInteractionListener mListener;

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
        mAtHomeSB = (SeekBar) v.findViewById(R.id.seekBar);
        mAtHomeTV = (TextView) v.findViewById(R.id.ahtextview);
        mInTheatersSB = (SeekBar) v.findViewById(R.id.itSeekBar);
        mInTheatersTV = (TextView) v.findViewById(R.id.ittextview);
        mUserEmail = (TextView) v.findViewById(R.id.useremail) ;
        mAtHomeTV.setText(mAtHomeSB.getProgress() + "/" + mAtHomeSB.getMax());
        mAtHomeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int theProgress, boolean fromUser) {
                //limit max progress to in theaters recomenndation
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
                mLocalSettings.setAtHomeThreshold(progress);
            }
        });
        mAtHomeSB.setProgress(mLocalSettings.getAtHomeThreshold());

        mInTheatersTV.setText(mAtHomeSB.getProgress() + "/" + mAtHomeSB.getMax());
        mInTheatersSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
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

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mInTheatersTV.setText(mInTheatersSB.getProgress() + "/" + mInTheatersSB.getMax());
            }
        });
        mInTheatersSB.setProgress(mLocalSettings.getInTheatersThreshold());

        mUserEmail.setText(mLocalSettings.getEmail());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsInteractionListener) {
            mListener = (OnSettingsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        // TODO: Update argument type and name
        void settings(Uri uri);
    }
}
