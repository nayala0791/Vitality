/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.project.blooddonorfinder.PushNotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.Modules.DonorProfile.DonorProfile;
import com.app.project.blooddonorfinder.Modules.HospitalTransactions.HospitalProfile;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            /*if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else */
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                sendNotification(extras.getString("message"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        try {
            JSONObject jsonResponseObject = new JSONObject(msg);
            JSONArray transactionArray = jsonResponseObject.getJSONArray("transaction");

            if (transactionArray.length() > 0) {
                JSONObject jsonObject = transactionArray.getJSONObject(0);

                if (jsonObject.has("pushType")) {
                    int pushType = jsonObject.getInt("pushType");
                    switch (pushType) {
                        //Push Notification to the Donor For Incoming Request
                        case 1:
                            // Now check the local userId with once received in the push response
                            SharedPreferences appPreferences = getSharedPreferences(getString(R.string.app_preference), MODE_PRIVATE);
                            long userId = appPreferences.getLong(getString(R.string.userid), -1);
                            if (jsonObject.has("userId")) {
                                if (userId == jsonObject.getInt("userId")) {
                                    // No Create the Transaction Object save it in the data base and allow user to accept of reject it

                                    Transaction transaction = new Gson().fromJson(jsonObject.toString(), Transaction.class);
                                    transaction.setStatus(BloodDonationStatus.REQUEST_RECEIVED);
                                    TransactionRepository.insertOrUpdateTransaction(getApplicationContext(), transaction);


                                    //Construct the message here
                                    String pushNotificationMessage = "You have a new blood donation request from " + transaction.getHospitalName() + "Please confirm your action";

                                    Intent acceptIntent = new Intent(getApplicationContext(), DonorProfile.class);
                                    acceptIntent.putExtra("action", 1);
                                    /*Intent cancelIntent = new Intent(getApplicationContext(), DonorProfile.class);
                                    cancelIntent.putExtra("action", 0);
                                    cancelIntent.putExtra("transactionId", transaction.getTransactionId());*/

                                    PendingIntent pendingSuccessIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), acceptIntent, 0);
                                    /*PendingIntent pendingRejectIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), cancelIntent, 0);*/

                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(this)
                                                    .setSmallIcon(R.drawable.ic_launcher)
                                                    .setContentTitle(getString(R.string.app_name))
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(pushNotificationMessage))
                                                    .setContentText(pushNotificationMessage)
                                                    .setContentIntent(pendingSuccessIntent);
                                                    /*.addAction(R.drawable.ic_done_white_24dp, "Accept", pendingSuccessIntent)
                                                    .addAction(R.drawable.ic_error_red, "Reject", pendingRejectIntent)*/
                                    ;


                                    Notification notification = mBuilder.build();

                                    //  mBuilder.setContentIntent(contentIntent);
                                    mNotificationManager.notify(NOTIFICATION_ID, notification);

                                }
                            }
                            break;
                        //Push Notification for the hospital as the response to user action
                        case 2:

                            int transactionId = jsonObject.getInt("transactionId");

                            Transaction transaction = TransactionRepository.getTransactionByTransactionId(getApplicationContext(), transactionId);


                            if (transaction != null) {
                                //Construct the message here
                                String pushNotificationMessage = "";

                                Donor donor = DonorTransactionRepository.getDonorById(getApplicationContext(), transaction.getUserId());

                                if (donor != null) {
                                    if (jsonObject.getInt("userAction") == 1) {
                                        pushNotificationMessage = "Your Request has been Accepted By the " + donor.getUserName();
                                        transaction.setStatus(BloodDonationStatus.REQUEST_ACCEPTED);

                                    } else if (jsonObject.getInt("userAction") == 0) {
                                        pushNotificationMessage = "Your Request has been Rejected By the " + donor.getUserName();
                                        transaction.setStatus(BloodDonationStatus.REQUEST_REJECTED);
                                    }

                                    TransactionRepository.insertOrUpdateTransaction(getApplicationContext(),transaction);


                                    Intent acceptIntent = new Intent(getApplicationContext(), HospitalProfile.class);
                                    acceptIntent.putExtra("action", 1);

                                    PendingIntent pendingSuccessIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), acceptIntent, 0);

                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(this)
                                                    .setSmallIcon(R.drawable.ic_launcher)
                                                    .setContentTitle(getString(R.string.app_name))
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(pushNotificationMessage))
                                                    .setContentText(pushNotificationMessage)
                                                    .setContentIntent(pendingSuccessIntent);


                                    Notification notification = mBuilder.build();

                                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                                }
                            }

                            break;
                        //Push Notification for the Donor when Hospital mark the donation successful
                        case 3:
                            transactionId = jsonObject.getInt("transactionId");

                            transaction = TransactionRepository.getTransactionByTransactionId(getApplicationContext(), transactionId);


                            if (transaction != null) {
                                //Construct the message here
                                String pushNotificationMessage = "";


                                if (jsonObject.getInt("isComplete") == 1) {
                                    pushNotificationMessage = "Your Donation With Hospital " + transaction.getHospitalName() + " has been Completed Successfully ";
                                    transaction.setStatus(BloodDonationStatus.REQUEST_COMPLETED);

                                } else if (jsonObject.getInt("isComplete") == 0) {
                                    pushNotificationMessage = "Your Donation With Hospital " + transaction.getHospitalName() + " has not been Completed";
                                    transaction.setStatus(BloodDonationStatus.REQUEST_NOT_COMPLETED);
                                }


                                TransactionRepository.insertOrUpdateTransaction(getApplicationContext(),transaction);


                                Intent acceptIntent = new Intent(getApplicationContext(), DonorProfile.class);
                                acceptIntent.putExtra("action", 1);

                                PendingIntent pendingSuccessIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), acceptIntent, 0);

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(this)
                                                .setSmallIcon(R.drawable.ic_launcher)
                                                .setContentTitle(getString(R.string.app_name))
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText(pushNotificationMessage))
                                                .setContentText(pushNotificationMessage)
                                                .setContentIntent(pendingSuccessIntent);


                                Notification notification = mBuilder.build();

                                mNotificationManager.notify(NOTIFICATION_ID, notification);
                            }

                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
