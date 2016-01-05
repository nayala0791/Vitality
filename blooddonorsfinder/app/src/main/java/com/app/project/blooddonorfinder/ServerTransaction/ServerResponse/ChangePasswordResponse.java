package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

/**
 * Created by XEC42356 on 9/27/2015.
 */
public class ChangePasswordResponse {

    private int isPwdChanged;
    private String message;

    public int getIsPwdChanged() {
        return isPwdChanged;
    }

    public void setIsPwdChanged(int isPwdChanged) {
        this.isPwdChanged = isPwdChanged;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
