package com.app.project.blooddonorfinder.Modules.HospitalTransactions;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.project.blooddonorfinder.Application.BloodDonorApp;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonorsInMapView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonorsInMapView extends Fragment implements GoogleMap.OnMarkerClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GoogleMap googleMap;
    SupportMapFragment mapView;
    GoogleMap map;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.Double.parseDouble(
     * @return A new instance of fragment DonorsInMapView.
     */
    // TODO: Rename and change types and number of parameters
    public static DonorsInMapView newInstance(String param1, String param2) {
        DonorsInMapView fragment = new DonorsInMapView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DonorsInMapView() {
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

    View rootView;
    Spinner filterSpinner;
    Circle mapCircle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_donors_in_map_view, container, false);
        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        filterSpinner=(Spinner)rootView.findViewById(R.id.radius_filter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mapCircle!=null)
                    mapCircle.remove();
                map.clear();
                switch (position)
                {
                    case 1:

                        mapCircle=map.addCircle(new CircleOptions()
                                .center(new LatLng(BloodDonorApp.currentLocation.getLatitude(), BloodDonorApp.currentLocation.getLongitude()))
                                .radius(16093.4)
                                .strokeWidth(0.5f)
                                .fillColor(0x550000FF));


                        break;
                    case 2:
                        mapCircle=map.addCircle(new CircleOptions()
                                .center(new LatLng(BloodDonorApp.currentLocation.getLatitude(), BloodDonorApp.currentLocation.getLongitude()))
                                .radius(24140.2)
                                .strokeWidth(0.5f)
                                .fillColor(0x550000FF));
                        break;
                    case 3:
                        mapCircle=map.addCircle(new CircleOptions()
                                .center(new LatLng(BloodDonorApp.currentLocation.getLatitude(), BloodDonorApp.currentLocation.getLongitude()))
                                .radius(32186.9)
                                .strokeWidth(0.5f)
                                .fillColor(0x550000FF));
                        break;
                    case 4:
                        if (mapCircle!=null)
                            mapCircle.remove();
                        break;
                }

                addUserLocatinToMap(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(BloodDonorApp.currentLocation.getLatitude(),BloodDonorApp.currentLocation.getLongitude()));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);

        map.setOnMarkerClickListener(this);

        if(BloodDonorApp.currentLocation!=null)
        mapCircle=map.addCircle(new CircleOptions()
                .center(new LatLng(BloodDonorApp.currentLocation.getLatitude(),BloodDonorApp.currentLocation.getLongitude()))
                .radius(16093.4)
                .strokeWidth(0.5f)
                .fillColor(0x550000FF));



        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        addUserLocatinToMap(4);
    }

    private void addUserLocatinToMap(int option) {
        MarkerOptions marker = new MarkerOptions();

        List<Donor> donorList = DonorTransactionRepository.getAllDonors(getActivity());
        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        for (int donorIndex = 0; donorIndex < donorList.size(); donorIndex++) {
            String[] latLong = donorList.get(donorIndex).getLastKnownLocation().split(";");

            if (latLong != null && latLong.length == 2) {
                LatLng latLng=new LatLng(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1]));
                Location donorLocation=new Location("");
                        donorLocation.setLongitude(latLng.latitude);
                        donorLocation.setLongitude(latLng.longitude);

                marker = new MarkerOptions().position(
                        latLng)
                        .title(donorList.get(donorIndex).getUserName())
                        .snippet("Blood Group:"+donorList.get(donorIndex).getBloodGroup())
                     ;
                float distanceInMeters=0.0f;
                if(BloodDonorApp.currentLocation!=null)
                {
                    distanceInMeters= BloodDonorApp.currentLocation.distanceTo(donorLocation);
                }

                switch (option) {
                    case 1:
                        if(distanceInMeters<16094)
                            map.addMarker(marker);
                        break;
                    case 2:
                        if(distanceInMeters<24141)
                            map.addMarker(marker);

                        break;
                    case 3:
                        if(distanceInMeters<32187)
                            map.addMarker(marker);

                        break;
                    case 4:
                        map.addMarker(marker);
                        break;

                }
            }
        }

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener)
            mListener = (OnFragmentInteractionListener) activity;

        /*initializeMap();*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
