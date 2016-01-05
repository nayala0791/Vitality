package com.app.project.blooddonorfinder.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.project.blooddonorfinder.Adapters.AcceptedRequestAdapter;
import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.MarkCompleteResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

/**
 * Created by xyz on 24-10-2015.
 */
public class ATRequestMarkComplete extends AsyncTask<Void,Void,MarkCompleteResponse> {
    Context mContext;
    Transaction transaction;
    ProgressDialog progressDialog;
    AcceptedRequestAdapter.OnMarkCompletListener onMarkCompletListener;

    public ATRequestMarkComplete(Context context,Transaction transaction,AcceptedRequestAdapter.OnMarkCompletListener onMarkCompletListener)
    {
        mContext=context;
        this.transaction=transaction;
        this.onMarkCompletListener=onMarkCompletListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=ProgressDialog.show(mContext,mContext.getString(R.string.app_name),"Updating the status please wait");
    }

    @Override
    protected MarkCompleteResponse doInBackground(Void... params) {
        MarkCompleteResponse markCompleteResponse=null;
        ServiceConnector serviceConnector=new ServiceConnector(mContext);
        try {
            markCompleteResponse=serviceConnector.markRequestComplete(transaction.getTransactionId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return markCompleteResponse;
    }

    @Override
    protected void onPostExecute(MarkCompleteResponse markCompleteResponse) {
        super.onPostExecute(markCompleteResponse);
        if(progressDialog!=null)
            progressDialog.dismiss();

        if(markCompleteResponse!=null)
        {
            if(markCompleteResponse.isSuccess==1)
            {
                transaction.setStatus(BloodDonationStatus.REQUEST_COMPLETED);
                TransactionRepository.insertOrUpdateTransaction(mContext, transaction);
                Toast.makeText(mContext,String.valueOf(markCompleteResponse.message),Toast.LENGTH_LONG).show();
                onMarkCompletListener.onMarkCompleteSuccessful();
            }else {
                Toast.makeText(mContext,String.valueOf(markCompleteResponse.message),Toast.LENGTH_LONG).show();
            }
        }
    }
}
