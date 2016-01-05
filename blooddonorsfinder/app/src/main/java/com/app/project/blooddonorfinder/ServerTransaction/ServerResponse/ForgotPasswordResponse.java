package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

/**
 * Created by XEC42356 on 10/2/2015.
 */
public class ForgotPasswordResponse {
    int isEmailSent;
    String message;

    public int getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(int isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
