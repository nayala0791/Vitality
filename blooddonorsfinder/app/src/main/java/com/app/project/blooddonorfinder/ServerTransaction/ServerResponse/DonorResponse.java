package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

/**
 * Created by nitin on 9/25/2015.
 */
public class DonorResponse {
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberOfDonation() {
        return numberOfDonation;
    }

    public void setNumberOfDonation(String numberOfDonation) {
        this.numberOfDonation = numberOfDonation;
    }

    public String getReadyToDonate() {
        return readyToDonate;
    }

    public void setReadyToDonate(String readyToDonate) {
        this.readyToDonate = readyToDonate;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private int userId;
    private String bloodGroup;
    private String gender;
    private String email;
    private String numberOfDonation;
    private String readyToDonate;
    private String phonenumber;
    private String address;


}
