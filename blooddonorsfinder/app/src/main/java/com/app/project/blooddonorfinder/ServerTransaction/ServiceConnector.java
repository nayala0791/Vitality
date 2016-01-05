package com.app.project.blooddonorfinder.ServerTransaction;

import android.content.Context;
import android.util.Log;

import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.ChangePasswordResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.ForgotPasswordResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.GetDonorsResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.LoginResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.MarkCompleteResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.PushNotificationRegisterResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RegisterDonorResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RegisterHospitalResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.RequestDonationResponse;
import com.app.project.blooddonorfinder.ServerTransaction.ServerResponse.UserActionResponse;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by  on 9/25/2015.
 */
public class ServiceConnector {

    private static final int METHOD_GET = 1;
    private static final int METHOD_POST = 2;

    private static final int TIMEOUT_CONNECT_MILLIS = 2 * 60 * 1000;
    private static final int TIMEOUT_READ_MILLIS = 2 * 60 * 1000;
    private String serviceBaseUrl = "http://www.vitalitydonors.us/Vitality/";
    Context mContext;

    public ServiceConnector(Context mContext) {
        this.mContext = mContext;
    }


    public String getServiceUrl() {
        return serviceBaseUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceBaseUrl = serviceUrl;
    }

    public LoginResponse authenticateUser(String username,String password) throws IOException {
        LoginResponse loginResponse=null;
        String stringResponseForLogin=consumeWebService(serviceBaseUrl+"Login.php?uname="+username+"&pword="+password,METHOD_GET,null);
        loginResponse=new Gson().fromJson(stringResponseForLogin, LoginResponse.class);
        return loginResponse;
    }

    public RegisterDonorResponse registerDonor(Map<String,Object> params) throws IOException{
        RegisterDonorResponse registerDonorResponse=null;
        String strResponseForDonorRegistration=consumeWebService(serviceBaseUrl+"RegisterDonor.php",METHOD_POST,params);
        registerDonorResponse=new Gson().fromJson(strResponseForDonorRegistration,RegisterDonorResponse.class);

        return registerDonorResponse;
    }

    public ChangePasswordResponse changePassword(int uid,String oldPassword, String newPassword) throws IOException {
        ChangePasswordResponse changePasswordResponse=null;

        String stringResponseForLogin=consumeWebService(serviceBaseUrl+"ChangePassword.php?uid="+uid+"&currentPwd="+oldPassword+"&newPwd="+newPassword,METHOD_GET,null);
        changePasswordResponse=new Gson().fromJson(stringResponseForLogin, ChangePasswordResponse.class);


        return changePasswordResponse;
    }

    private String consumeWebService(String urlString, int methodType,  Map<String,Object> params) throws MalformedURLException, IOException, NullPointerException {
        String response = "";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(methodType == METHOD_GET ? "GET" : "POST");

        if (methodType == METHOD_POST) {

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            outputStream.write(postDataBytes);
            outputStream.flush();
            outputStream.close();
        }

        connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
        connection.setReadTimeout(TIMEOUT_READ_MILLIS);
        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case 200:
                InputStream is = new BufferedInputStream(connection.getInputStream());
                response = getStringFromInputStream(is);

                break;
        }

        return response;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


    public PushNotificationRegisterResponse registerDeviceForTheUser(long userId, String regId,String imei,String deviceModel) throws IOException,Exception{
        PushNotificationRegisterResponse pushNotificationRegisterResponse=new PushNotificationRegisterResponse();
        String registrationResponse=consumeWebService(serviceBaseUrl+"/RegisterDeviceForUser.php?userId="+userId+"&regId="+regId+"&imei="+imei+"&deviceModel="+deviceModel,METHOD_GET,null);
        pushNotificationRegisterResponse=new Gson().fromJson(registrationResponse,PushNotificationRegisterResponse.class);
        return pushNotificationRegisterResponse;
    }

    public RegisterHospitalResponse registerHospital(Map<String, Object> params) throws IOException{
        RegisterHospitalResponse registerHospitalResponse=null;
        String strResponseForDonorRegistration=consumeWebService(serviceBaseUrl+"RegisterHospital.php",METHOD_POST,params);

        Log.d("Register Hospital",strResponseForDonorRegistration);
        registerHospitalResponse=new Gson().fromJson(strResponseForDonorRegistration,RegisterHospitalResponse.class);

        return registerHospitalResponse;

    }

    public ForgotPasswordResponse forgotPassword(String mEmail)throws IOException{
        ForgotPasswordResponse forgotPasswordResponse = null;
        String forgotResponse=consumeWebService(serviceBaseUrl+"ForgotPassWord.php?email="+mEmail,METHOD_GET,null);
        forgotPasswordResponse=new Gson().fromJson(forgotResponse,ForgotPasswordResponse.class);
        return null;
    }

    public GetDonorsResponse getAllActiveDonors()throws IOException{
        String getAllDonorResponse=consumeWebService(serviceBaseUrl+"GetAllActiveDonors.php",METHOD_GET,null);
        return new Gson().fromJson(getAllDonorResponse,GetDonorsResponse.class);
    }

    public void updateUserLatLong(long userId, double latitude, double longitude) throws IOException{
        consumeWebService(serviceBaseUrl+"UpdateLatLong.php?userId="+userId+"&lattitude="+latitude+"&longitude="+longitude,METHOD_GET,null);

    }

    public RequestDonationResponse requestBloodDonation(long hospitalId, long userId) throws IOException{
        RequestDonationResponse requestDonationResponse=null;
        String requestDonationResponseString=consumeWebService(serviceBaseUrl+"SendRequestToDonor.php?userId="+userId+"&hospitalId="+hospitalId,METHOD_GET,null);
        requestDonationResponse=new Gson().fromJson(requestDonationResponseString,RequestDonationResponse.class);
        return requestDonationResponse;
    }

    public UserActionResponse updateUserAction(long transactionId, int userAction) throws IOException{
        UserActionResponse userActionResponse=null;
        String userActionResponseString=consumeWebService(serviceBaseUrl+"UserAction.php?transactionId="+transactionId+"&userAction="+userAction,METHOD_GET,null);
        userActionResponse=new Gson().fromJson(userActionResponseString,UserActionResponse.class);
        return userActionResponse;
    }

    public MarkCompleteResponse markRequestComplete(Long transactionId) throws IOException{
        MarkCompleteResponse markCompleteResponse=null;
        String markCompleteResponseString=consumeWebService(serviceBaseUrl+"MarkDonationComplete.php?transactionId="+transactionId+"&userAction=1",METHOD_GET,null);
        markCompleteResponse=new Gson().fromJson(markCompleteResponseString,MarkCompleteResponse.class);
        return markCompleteResponse;
    }
}
