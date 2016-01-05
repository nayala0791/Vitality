package com.app.project.blooddonorfinder.ServerTransaction.ServerResponse;

import com.app.project.blooddonorfinder.DataModels.Transaction;

/**
 * Created by xyz on 24-10-2015.
 */
public class RequestDonationResponse {
    public int isRequestSent;
    public String message;
    public Transaction[] transaction;
}
