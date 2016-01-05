package com.app.project.blooddonorfinder.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.app.project.blooddonorfinder.Modules.ChangePassword.ChangePassword;
import com.app.project.blooddonorfinder.Modules.HospitalTransactions.HospitalProfile;
import com.app.project.blooddonorfinder.Modules.Login.LoginActivity;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.ChangePasswordResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

/**
 * Created by XEC42356 on 9/27/2015.
 */
public class ATChangePassword extends AsyncTask<Void, Void, ChangePasswordResponse> {

    int uid;
    String oldPassword;
    String newPassword;
    Context mContext;
    public ATChangePassword(Context context,int uid, String oldPassword, String newPassword){
        this.uid = uid;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        mContext = context;
    }
    @Override
    protected ChangePasswordResponse doInBackground(Void... params) {
        ServiceConnector serviceConnector=new ServiceConnector(mContext);
        ChangePasswordResponse changePasswordResponse=null;
        try {
            changePasswordResponse=serviceConnector.changePassword(uid, oldPassword, newPassword);
        } catch (IOException e) {
            e.printStackTrace();
            changePasswordResponse=new ChangePasswordResponse();
            changePasswordResponse.setIsPwdChanged(0);
            changePasswordResponse.setMessage(e.getMessage());
        }
        /*LoginResponse loginResponse=new LoginResponse();
        loginResponse.setIsLoggedIn(1);
        loginResponse.setChangePassWord(0);
        loginResponse.setMessage("Is logged in Successfully!");*/
        return changePasswordResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ChangePasswordResponse changePasswordResponse) {

        super.onPostExecute(changePasswordResponse);

        if (changePasswordResponse != null) {
            if (changePasswordResponse.getIsPwdChanged() == 1) {
                Intent loginIntent = new Intent(mContext, LoginActivity.class);

                mContext.startActivity(loginIntent);
                ((AppCompatActivity) mContext).finish();
            } else {
                Toast.makeText(mContext, changePasswordResponse.getMessage() + "", Toast.LENGTH_LONG).show();
            }

        }
    }


}
