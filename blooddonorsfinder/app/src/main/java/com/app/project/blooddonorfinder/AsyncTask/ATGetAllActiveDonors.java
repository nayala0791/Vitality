package com.app.project.blooddonorfinder.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.app.project.blooddonorfinder.Modules.HospitalTransactions.DonorsInListView;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.GetDonorsResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

/**
 * Created by xyz on 22-10-2015.
 */
public class ATGetAllActiveDonors extends AsyncTask<Void,Void,GetDonorsResponse> {
    Context mContext;
    DonorsInListView.OnGetAllDonors mOnGetAllDonorsCallBack;

    public ATGetAllActiveDonors(Context context,DonorsInListView.OnGetAllDonors onGetAllDonorsCallBack)
    {
        mContext=context;
        mOnGetAllDonorsCallBack=onGetAllDonorsCallBack;
    }
    @Override
    protected GetDonorsResponse doInBackground(Void... params) {
        ServiceConnector serviceConnector=new ServiceConnector(mContext);
        GetDonorsResponse getDonorsResponse=null;
        try {
            getDonorsResponse=serviceConnector.getAllActiveDonors();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getDonorsResponse;
    }

    @Override
    protected void onPostExecute(GetDonorsResponse getDonorsResponse) {
        super.onPostExecute(getDonorsResponse);

        if(getDonorsResponse!=null)
            mOnGetAllDonorsCallBack.onGetAllDonorsCompleted(getDonorsResponse);
        else
            mOnGetAllDonorsCallBack.onGetAllDonorsFailed();
    }
}
