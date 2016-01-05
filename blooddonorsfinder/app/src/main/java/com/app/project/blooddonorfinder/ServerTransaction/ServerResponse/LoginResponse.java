package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.Hospital;

/**
 * Created by nitin on 9/25/2015.
 */
public class LoginResponse {
    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public int getChangePassWord() {
        return changePassWord;
    }

    public void setChangePassWord(int changePassWord) {
        this.changePassWord = changePassWord;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int isLoggedIn;
    private int changePassWord;
    private String message;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

   /* public DonorResponse getDonorResponse() {
        return donorResponse;
    }

    public void setDonorResponse(DonorResponse donorResponse) {
        this.donorResponse = donorResponse;
    }*/

    private int uid;
    private int userType;

    public Donor[] getDonorResponse() {
        return donorResponse;
    }

    public void setDonorResponse(Donor[] donorResponse) {
        this.donorResponse = donorResponse;
    }

    public Hospital[] getHospitalResponses() {
        return hospitalResponse;
    }

    public void setHospitalResponses(Hospital[] hospitalResponses) {
        this.hospitalResponse = hospitalResponses;
    }

    private Donor[] donorResponse;
    private Hospital[] hospitalResponse;


}
