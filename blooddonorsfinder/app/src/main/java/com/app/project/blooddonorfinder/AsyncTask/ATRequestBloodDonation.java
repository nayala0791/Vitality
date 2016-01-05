package com.app.project.blooddonorfinder.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.project.blooddonorfinder.Adapters.DonorsListAdapter;
import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RequestDonationResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

/**
 * Created by xyz on 24-10-2015.
 */
public class ATRequestBloodDonation extends AsyncTask<Void, Void, RequestDonationResponse> {

    Context mContext;
    DonorsListAdapter.OnRequestDonationListener onRequestDonationListener;
    long hospitalId, userId;
    ProgressDialog mProgressDialog;

    public ATRequestBloodDonation(Context context, DonorsListAdapter.OnRequestDonationListener onRequestDonationListener, long hospitalId, long userId) {
        mContext = context;
        this.onRequestDonationListener = onRequestDonationListener;
        this.hospitalId = hospitalId;
        this.userId = userId;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, "Blood Donor", "Sending the Request.Please Wait");
    }


    @Override
    protected RequestDonationResponse doInBackground(Void... params) {
        RequestDonationResponse requestDonationResponse = null;
        ServiceConnector serviceConnector = new ServiceConnector(mContext);
        try {
            requestDonationResponse = serviceConnector.requestBloodDonation(hospitalId, userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestDonationResponse;
    }


    @Override
    protected void onPostExecute(RequestDonationResponse requestDonationResponse) {
        super.onPostExecute(requestDonationResponse);
        if (mProgressDialog != null)
            mProgressDialog.dismiss();

        if (requestDonationResponse != null) {
            if (requestDonationResponse.isRequestSent == 1) {
                Transaction[] transaction = requestDonationResponse.transaction;
                for (int index = 0; index < transaction.length; index++) {
                    transaction[index].setStatus(BloodDonationStatus.REQUEST_SENT);
                    TransactionRepository.insertOrUpdateTransaction(mContext, transaction[index]);
                }
                onRequestDonationListener.onRequestSuccessful();
            } else {
                onRequestDonationListener.onRequestFailure();
            }
        }

    }
}
