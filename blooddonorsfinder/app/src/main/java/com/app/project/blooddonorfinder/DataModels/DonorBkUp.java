package com.app.project.blooddonorfinder.DataModels;

/**
 * Created by nitin on 9/27/2015.
 */
public class DonorBkUp {
    private String email;
    private String name;
    private String phoneNumber;
    private String altPhoneNumber;
    private String bloodGroup;
    private String gender;

    @Override
    public String toString() {
        return "email="+email+"&name="+name+"&phoneNumber="+phoneNumber+"&altPhoneNumber="+altPhoneNumber+"&bloodGroup="+bloodGroup+"&gender="+gender+"&address="+address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAltPhoneNumber() {
        return altPhoneNumber;
    }

    public void setAltPhoneNumber(String altPhoneNumber) {
        this.altPhoneNumber = altPhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String address;
}
