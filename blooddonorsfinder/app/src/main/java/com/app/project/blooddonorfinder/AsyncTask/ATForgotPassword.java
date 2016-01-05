package com.app.project.blooddonorfinder.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.ChangePasswordResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.ForgotPasswordResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

/**
 * Created by XEC42356 on 10/2/2015.
 */
public class ATForgotPassword extends AsyncTask<Void, Void,ForgotPasswordResponse> {
String mEmail;
    Context mContext;
public ATForgotPassword(Context context, String email){
    mEmail = email;
    mContext = context;
}
    @Override
    protected ForgotPasswordResponse doInBackground(Void... params) {

        ServiceConnector serviceConnector=new ServiceConnector(mContext);
        ForgotPasswordResponse forgotPasswordResponse=null;
        try {
            forgotPasswordResponse=serviceConnector.forgotPassword(mEmail);
        } catch (IOException e) {
            e.printStackTrace();
            forgotPasswordResponse=new ForgotPasswordResponse();
            forgotPasswordResponse.setIsEmailSent(0);
            forgotPasswordResponse.setMessage(e.getMessage());
        }
        return forgotPasswordResponse;
    }

    @Override
    protected void onPostExecute(ForgotPasswordResponse forgotPasswordResponse) {
        super.onPostExecute(forgotPasswordResponse);
        if(forgotPasswordResponse != null) {
            if (forgotPasswordResponse.getIsEmailSent() == 1) {
                Toast.makeText(mContext, forgotPasswordResponse.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
