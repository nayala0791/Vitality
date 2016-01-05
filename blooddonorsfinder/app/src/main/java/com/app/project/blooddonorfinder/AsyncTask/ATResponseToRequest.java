package com.app.project.blooddonorfinder.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by xyz on 24-10-2015.
 */;import com.app.project.blooddonorfinder.Adapters.IncomingRequestAdapter;
import com.app.project.blooddonorfinder.Constants.BloodDonationStatus;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.TransactionRepository;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.UserActionResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

public class ATResponseToRequest extends AsyncTask<Void, Void, UserActionResponse> {

    Context mContext;
    Transaction transaction;
    int userAction;
    ProgressDialog progressDialog;
    IncomingRequestAdapter.OnRequestUpdateCompleteListener onRequestUpdateCompleteListener;

    public ATResponseToRequest(Context context, Transaction transaction, int userAction,IncomingRequestAdapter.OnRequestUpdateCompleteListener onRequestUpdateCompleteListener) {
        mContext = context;
        this.transaction = transaction;
        this.userAction = userAction;
        this.onRequestUpdateCompleteListener=onRequestUpdateCompleteListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=ProgressDialog.show(mContext,mContext.getString(R.string.app_name),"Updating the request Please wait");
    }

    @Override
    protected UserActionResponse doInBackground(Void... params) {
        UserActionResponse userActionResponse=null;
        ServiceConnector serviceConnector=new ServiceConnector(mContext);
        try {
            userActionResponse=serviceConnector.updateUserAction(transaction.getTransactionId(),userAction);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userActionResponse;
    }

    @Override
    protected void onPostExecute(UserActionResponse userActionResponse) {
        super.onPostExecute(userActionResponse);

        if(progressDialog!=null)
            progressDialog.dismiss();

        if(userActionResponse!=null)
        {
            if(userActionResponse.isSuccess==1)
            {
                if(userAction==1)
                transaction.setStatus(BloodDonationStatus.REQUEST_ACCEPTED);
                else
                transaction.setStatus(BloodDonationStatus.REQUEST_REJECTED);
                TransactionRepository.insertOrUpdateTransaction(mContext,transaction);
                Toast.makeText(mContext,String.valueOf(userActionResponse.message),Toast.LENGTH_LONG).show();
                onRequestUpdateCompleteListener.onRefresh();
            }else{
                Toast.makeText(mContext,String.valueOf(userActionResponse.message),Toast.LENGTH_LONG).show();
            }
        }
    }
}
