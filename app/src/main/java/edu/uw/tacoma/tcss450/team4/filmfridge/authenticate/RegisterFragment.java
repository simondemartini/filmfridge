package edu.uw.tacoma.tcss450.team4.filmfridge.authenticate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uw.tacoma.tcss450.team4.filmfridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterInteractionListener} interface
 * to handle interaction events.
 *
 * Created by Samantha Ong
 */
public class RegisterFragment extends Fragment {
    private OnRegisterInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Lifecycle method: Create the layout and view for this fragment
     * @return the complete view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        final EditText userIdText = (EditText) v.findViewById(R.id.register_email);
        final EditText cUserIdText = (EditText) v.findViewById(R.id.confirm_register_email);
        final EditText pwdText = (EditText) v.findViewById(R.id.register_password);
        final EditText cPwdText = (EditText) v.findViewById(R.id.confirm_register_password);

        Button addUserButton = (Button) v.findViewById(R.id.addUser_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String cUserId = cUserIdText.getText().toString();
                String pwd = pwdText.getText().toString();
                String cPwd = cPwdText.getText().toString();

                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter email"
                            , Toast.LENGTH_SHORT)
                            .show();
                    userIdText.requestFocus();
                    return;
                }
                if (!userId.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT)
                            .show();
                    userIdText.requestFocus();
                    return;
                }

                if (!userId.equals(cUserId)) {
                    Toast.makeText(v.getContext(), "Emails do not match"
                            , Toast.LENGTH_SHORT)
                            .show();
                    userIdText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext()
                            , "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }
                if (!pwd.equals(cPwd)) {
                    Toast.makeText(v.getContext(), "Passwords do not match"
                            , Toast.LENGTH_SHORT)
                            .show();
                    userIdText.requestFocus();
                    return;
                }
                ((SignInActivity) getActivity()).register(userId, pwd);
            }


        });
        return v;
    }

    /**
     * Lifecycle method: Get the mandatory listeners when attaching to an activity
     * @param context the context that must implement OnFragmentInteractionListener
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterInteractionListener) {
            mListener = (OnRegisterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Lifecycle methodL Remove the listener
     */
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
    public interface OnRegisterInteractionListener {
        void register(String userId, String pwd);
    }
}
