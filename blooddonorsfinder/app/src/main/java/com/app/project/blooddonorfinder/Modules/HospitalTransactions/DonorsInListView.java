package com.app.project.blooddonorfinder.Modules.HospitalTransactions;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.project.blooddonorfinder.Adapters.DonorsListAdapter;
import com.app.project.blooddonorfinder.AsyncTask.ATGetAllActiveDonors;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.GetDonorsResponse;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DonorsInListView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonorsInListView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonorsInListView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DonorsListAdapter donorsListAdapter;

    public interface OnGetAllDonors{
        public void onGetAllDonorsCompleted(GetDonorsResponse getDonorsResponse);
        public void onGetAllDonorsFailed();

    }

    RecyclerView recyclerView;


    OnGetAllDonors onGetAllDonorsCallBack=new OnGetAllDonors() {
        @Override
        public void onGetAllDonorsCompleted(GetDonorsResponse getDonorsResponse) {
            if(mSwipeRefreshLayout!=null)
                mSwipeRefreshLayout.setRefreshing(false);
            Donor[] donors=getDonorsResponse.donors;

            for(int donorIndex=0;donorIndex<donors.length;donorIndex++)
            {
                DonorTransactionRepository.insertOrUpdateDonor(getActivity(),donors[donorIndex]);
            }

            donorsListAdapter=new DonorsListAdapter(getActivity());
            recyclerView.setAdapter(donorsListAdapter);


        }

        @Override
        public void onGetAllDonorsFailed() {
            if(mSwipeRefreshLayout!=null)
                mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonorsInListView.
     */
    // TODO: Rename and change types and number of parameters
    public static DonorsInListView newInstance(String param1, String param2) {
        DonorsInListView fragment = new DonorsInListView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DonorsInListView() {
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
        View rootView=inflater.inflate(R.layout.fragment_donors_as_list, container, false);
        mSwipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ATGetAllActiveDonors(getActivity(),onGetAllDonorsCallBack).execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ATGetAllActiveDonors(getActivity(),onGetAllDonorsCallBack).execute();
            }
        });



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
        if (activity instanceof OnFragmentInteractionListener)
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