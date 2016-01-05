package com.app.project.blooddonorfinder.PushNotification;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.PushNotificationRegisterResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by INUBE on 5/25/2015.
 */
public class GCMHelper {

    Context context;
    GoogleCloudMessaging gcm;
    static final String TAG = "BloodDonorGCM";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "790268021970";


    public GCMHelper(Context context) {
        this.context = context;
    }

    public void checkAndRegister() {
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            String regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i("", "No valid Google Play Services APK found.");
        }
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");

            }
            return false;
        }
        return true;
    }


    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        return registrationId;
    }


    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences("GCMPreference",
                Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, PushNotificationRegisterResponse>() {
            @Override
            protected PushNotificationRegisterResponse doInBackground(Void... params) {
                PushNotificationRegisterResponse pushNotificationRegisterResponse = null;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regid = gcm.register(SENDER_ID);

                    // You should send the registration ID to your server over HTTP, so it

                    // can use GCM/HTTP or CCS to send messages to your app.
                    storeRegistrationId(context, regid);

                    pushNotificationRegisterResponse=sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.

                } catch (Exception ex) {
                    pushNotificationRegisterResponse = null;
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return pushNotificationRegisterResponse;
            }

            @Override
            protected void onPostExecute(PushNotificationRegisterResponse pushNotificationRegisterResponse) {
                // mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private PushNotificationRegisterResponse sendRegistrationIdToBackend() throws Exception{
        ServiceConnector serviceConnector=new ServiceConnector(context);
        // get the userId from sharedpreference
        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getString(R.string.app_preference), Context.MODE_PRIVATE);
        long userId=sharedPreferences.getLong(context.getString(R.string.userid), -1);
        String regId=getRegistrationId(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // get IMEI
        String imei = tm.getDeviceId();
        if(imei==null)
        {
            imei= Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);;
        }
        String deviceName= Build.MANUFACTURER;
        PushNotificationRegisterResponse pushNotificationRegisterResponse=serviceConnector.registerDeviceForTheUser(userId, regId, imei, deviceName);
        return pushNotificationRegisterResponse;


    }


    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);

        editor.commit();
    }
}
