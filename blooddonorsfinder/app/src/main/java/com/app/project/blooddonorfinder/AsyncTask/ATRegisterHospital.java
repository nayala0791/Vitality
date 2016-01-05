package com.app.project.blooddonorfinder.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.app.project.blooddonorfinder.Modules.Login.LoginActivity;
import com.app.project.blooddonorfinder.Modules.Register.Register;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RegisterDonorResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RegisterHospitalResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by XEC42356 on 10/2/2015.
 */
public class ATRegisterHospital  extends AsyncTask<Void,Void,RegisterHospitalResponse>{

    Context mContext;
    Map<String, Object> params;

    public ATRegisterHospital(Context context, Map<String, Object> params) {
        mContext = context;
        this.params = params;
    }


    @Override
    protected RegisterHospitalResponse doInBackground(Void... params) {
        RegisterHospitalResponse registerHospitalResponse = null;
        ServiceConnector serviceConnector = new ServiceConnector(mContext);
        try {

            registerHospitalResponse = serviceConnector.registerHospital(this.params);
        } catch (IOException ioex) {
            registerHospitalResponse = new RegisterHospitalResponse();
            registerHospitalResponse.setIsRegistered(0);
            registerHospitalResponse.setMessage(ioex.getMessage() + "");
        }
        return registerHospitalResponse;
    }

    @Override
    protected void onPostExecute(RegisterHospitalResponse registerHospitalResponse) {
        super.onPostExecute(registerHospitalResponse);
        if (registerHospitalResponse != null) {
            if (registerHospitalResponse.getIsRegistered() == 1) {
                // Redirect user to the login page to start using the app
                Intent loginIntent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(loginIntent);
                ((AppCompatActivity) mContext).finish();
            } else {
                Toast.makeText(mContext, registerHospitalResponse.getMessage() + "", Toast.LENGTH_LONG).show();
            }
        }
    }
}
