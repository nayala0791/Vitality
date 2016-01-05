package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

/**
 * Created by nitin on 9/27/2015.
 */
public class RegisterDonorResponse {
    private int isRegistered;
    private String message;

    public int getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
