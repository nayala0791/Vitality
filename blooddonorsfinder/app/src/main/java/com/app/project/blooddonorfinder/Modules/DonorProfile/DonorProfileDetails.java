package com.app.project.blooddonorfinder.Modules.DonorProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.app.project.blooddonorfinder.Utils.RightDrawableOnTouchListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DonorProfileDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonorProfileDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonorProfileDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView userNameTextView,mobileNumberTextView,emailTextView,donationCount;

    SwitchCompat readyToDonateSwitch;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonorProfileDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static DonorProfileDetails newInstance(String param1, String param2) {
        DonorProfileDetails fragment = new DonorProfileDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DonorProfileDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_donor_profile_details, container, false);
        userNameTextView=(TextView)rootView.findViewById(R.id.username);
        mobileNumberTextView=(TextView)rootView.findViewById(R.id.mobile_number);

        mobileNumberTextView.setOnTouchListener(new RightDrawableOnTouchListener(mobileNumberTextView) {
            @Override
            public boolean onDrawableTouch(MotionEvent event) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.edit_phonumber_layout,null);

                AlertDialog.Builder editPhoneNumber=new AlertDialog.Builder(getActivity())
                        .setTitle("Update Phone Number")
                        .setView(view)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                editPhoneNumber.show();

                return false;
            }
        });

        emailTextView=(TextView)rootView.findViewById(R.id.email);
        donationCount=(TextView)rootView.findViewById(R.id.donation_count);
        readyToDonateSwitch=(SwitchCompat)rootView.findViewById(R.id.switch_compat);
        SharedPreferences appPreferences = getActivity().getSharedPreferences(getString(R.string.app_preference), getActivity().MODE_PRIVATE);
        long userId=appPreferences.getLong(getString(R.string.userid), -1);


        Donor donor=DonorTransactionRepository.getDonorByUserId(getActivity(), userId);

        if(donor!=null)
        {
            userNameTextView.setText(String.valueOf(donor.getUserName()));
            mobileNumberTextView.setText(String.valueOf(donor.getPhoneNumber()));
            emailTextView.setText(String.valueOf(donor.getEmail()));
            donationCount.setText(String.valueOf(donor.getNumberOfDonation()+" time(s) donated blood"));
            if(donor.getReadyToDonate()==1)
                readyToDonateSwitch.setChecked(true);
            else
                readyToDonateSwitch.setChecked(false);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnFragmentInteractionListener)
        mListener = (OnFragmentInteractionListener) activity;

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
