package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

/**
 * Created by XEC42356 on 10/2/2015.
 */
public class PushNotificationRegisterResponse {

    int isRegistered;
    String message;

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
