package com.app.project.blooddonorfinder.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.project.blooddonorfinder.Application.BloodDonorApp;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

/**
 * Created by techjini1 on 21/10/15.
 */
public class SendLatLong extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = SendLatLong.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastKnownLocation;

    private static int mUpdateFrequency = 2 * 60 * 1000; // 15 min
    private static int mFastestInterval = 5000; // 5 sec
    private static int mDisplacement = 10; // 10 meters


    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient != null)
            startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        new Thread() {
            @Override
            public void run() {
                super.run();

                if (mGoogleApiClient != null)
                    mGoogleApiClient.connect();
            }
        }.start();

        return START_STICKY_COMPATIBILITY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null)
            stopLocationUpdates();

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                                /*GooglePlayServicesUtil.getErrorDialog(resultCode, getBaseContext(),
                                        PLAY_SERVICES_RESOLUTION_REQUEST).show();*/
            } else {
                Log.d(TAG, "Play Service Not Supported");

            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(mUpdateFrequency);
        mLocationRequest.setFastestInterval(mFastestInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(mDisplacement); // 10 meters
    }

    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
        BloodDonorApp.currentLocation=mLastKnownLocation;
        final ServiceConnector serviceConnector = new ServiceConnector(getBaseContext());
        if (mLastKnownLocation != null)
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        SharedPreferences appPreferences = getSharedPreferences(getString(R.string.app_preference),MODE_PRIVATE);
                        long userId=appPreferences.getLong(getString(R.string.userid), -1);

                        if(userId!=-1)
                        serviceConnector.updateUserLatLong(userId, mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

    }
}

