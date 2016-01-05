package com.app.project.blooddonorfinder.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.app.project.blooddonorfinder.Modules.Login.LoginActivity;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RegisterDonorResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Pranavi on 9/26/2015.
 */
public class ATRegisterDonor extends AsyncTask<Void, Void, RegisterDonorResponse> {

    Context mContext;
    Map<String, Object> params;

    public ATRegisterDonor(Context context, Map<String, Object> params) {
        this.mContext = context;
        this.params = params;
    }

    @Override
    protected RegisterDonorResponse doInBackground(Void... params) {
        RegisterDonorResponse registerDonorResponse = null;
        ServiceConnector serviceConnector = new ServiceConnector(mContext);
        try {

            registerDonorResponse = serviceConnector.registerDonor(this.params);
        } catch (IOException ioex) {
            registerDonorResponse = new RegisterDonorResponse();
            registerDonorResponse.setIsRegistered(0);
            registerDonorResponse.setMessage(ioex.getMessage() + "");
        }
        return registerDonorResponse;
    }

    @Override
    protected void onPostExecute(RegisterDonorResponse registerDonorResponse) {
        super.onPostExecute(registerDonorResponse);
        if (registerDonorResponse != null) {
            if (registerDonorResponse.getIsRegistered() == 1) {
                // Redirect user to the login page to start using the app
                Intent loginIntent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(loginIntent);
                ((AppCompatActivity) mContext).finish();
            } else {
                Toast.makeText(mContext, registerDonorResponse.getMessage() + "", Toast.LENGTH_LONG).show();
            }
        }
    }
}
