package com.app.project.blooddonorfinder.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.Hospital;
import com.app.project.blooddonorfinder.Modules.ChangePassword.ChangePassword;
import com.app.project.blooddonorfinder.Modules.DonorProfile.DonorProfile;
import com.app.project.blooddonorfinder.Modules.HospitalTransactions.HospitalProfile;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Repositories.DonorTransactionRepository;
import com.app.project.blooddonorfinder.Repositories.HospitalTransactionRepository;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.HospitalResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.LoginResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServiceConnector;

import java.io.IOException;

/**
 *  * Created by Pranavi on 9/26/2015.
 */
public class ATLogin extends AsyncTask<Void, Void, LoginResponse> {
    private static final int USER_IS_DONOR = 0;
    private static final int USER_IS_HOSPITAL = 1;

    String mUsername;
    String mPassword;
    Context mContext;
    ProgressDialog mProgresDailog;

    public ATLogin(Context context, String username, String password) {
        this.mUsername = username;
        this.mPassword = password;
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgresDailog = ProgressDialog.show(mContext,mContext.getString(R.string.login_progress_title),mContext.getString(R.string.login_progress_message));
    }

    @Override
    protected LoginResponse doInBackground(Void... params) {
        ServiceConnector serviceConnector = new ServiceConnector(mContext);
        LoginResponse loginResponse = null;
        try {
            loginResponse = serviceConnector.authenticateUser(mUsername, mPassword);
        } catch (IOException e) {
            e.printStackTrace();
            loginResponse = new LoginResponse();
            loginResponse.setIsLoggedIn(0);
            loginResponse.setChangePassWord(0);
            loginResponse.setMessage(e.getMessage());
        }
        return loginResponse;
    }

    @Override
    protected void onPostExecute(LoginResponse loginResponse) {
        super.onPostExecute(loginResponse);
        if(mProgresDailog!=null)
            mProgresDailog.dismiss();
        if (loginResponse != null) {
            //Need to check the Login Response Code


            if (loginResponse.getChangePassWord() == 1) {
                Intent changePasswordIntent = new Intent(mContext, ChangePassword.class);
                changePasswordIntent.putExtra("username", mUsername);
                changePasswordIntent.putExtra("uid", loginResponse.getUid());
                mContext.startActivity(changePasswordIntent);
                ((AppCompatActivity) mContext).finish();
            } else {
                if (loginResponse.getIsLoggedIn() == 1) {
                    Intent donorProfilePage = new Intent(mContext, DonorProfile.class);
                    Intent hospitalProfilePage = new Intent(mContext, HospitalProfile.class);
                    SharedPreferences appPreferences = mContext.getSharedPreferences(mContext.getString(R.string.app_preference), mContext.MODE_PRIVATE);
                    appPreferences.edit().putLong(mContext.getString(R.string.userid), loginResponse.getUid()).commit();
                    appPreferences.edit().putLong(mContext.getString(R.string.usertype), loginResponse.getUserType()).commit();
                    appPreferences.edit().putString(mContext.getString(R.string.username), mUsername).commit();
                    appPreferences.edit().putBoolean(mContext.getString(R.string.isLoggedIn), true).commit();
                    donorProfilePage.putExtra("username", mUsername);
                    hospitalProfilePage.putExtra("username", mUsername);

                    Donor[] donors=loginResponse.getDonorResponse();
                    Hospital[] hospitals=loginResponse.getHospitalResponses();

                    DonorTransactionRepository.deleteAllDonor(mContext);
                    HospitalTransactionRepository.deleteAllHospital(mContext);

                    if(donors!=null)
                    for(int donorIndex=0;donorIndex<donors.length;donorIndex++)
                    {
                        DonorTransactionRepository.insertOrUpdateDonor(mContext,donors[donorIndex]);
                    }

                    if(hospitals!=null)
                    for(int hospitalIndex=0;hospitalIndex<hospitals.length;hospitalIndex++)
                    {
                        HospitalTransactionRepository.insertOrUpdateHospital(mContext,hospitals[hospitalIndex]);
                    }

                    if (loginResponse.getUserType() == USER_IS_DONOR)
                    {
                        mContext.startActivity(donorProfilePage);

                    }
                    else if(loginResponse.getUserType()==USER_IS_HOSPITAL)
                        mContext.startActivity(hospitalProfilePage);

                    ((AppCompatActivity) mContext).finish();
                } else {
                    Toast.makeText(mContext, loginResponse.getMessage() + "", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
