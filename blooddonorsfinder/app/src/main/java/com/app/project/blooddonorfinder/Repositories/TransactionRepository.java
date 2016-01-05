package com.app.project.blooddonorfinder.Repositories;

import android.content.Context;

import com.app.project.blooddonorfinder.Application.BloodDonorApp;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.DonorDao;
import com.app.project.blooddonorfinder.DataModels.Transaction;
import com.app.project.blooddonorfinder.DataModels.TransactionDao;

import java.util.List;

/**
 * Created by xyz on 22-10-2015.
 */
public class TransactionRepository {

    public static void insertOrUpdateTransaction(Context context, Transaction transaction) {
        //Check if the user Id Exist

        Transaction checkTransaction = getTransactionByTransactionId(context, transaction.getTransactionId());
        if (checkTransaction != null)
            transaction.setId(checkTransaction.getId());

         getTransactionDao(context).insertOrReplace(transaction);
    }

    public static List<Transaction> getAllTransaction(Context context) {
        return getTransactionDao(context).loadAll();
    }

    public static Transaction getTransactionByTransactionId(Context context, long Id) {
        Transaction transaction = null;
        List<Transaction> transactions = getTransactionDao(context).queryRaw(" Where TRANSACTION_ID=?", new String[]{String.valueOf(Id)});
        if (transactions != null && transactions.size() > 0)
            transaction = transactions.get(0);
        return transaction;
    }

    public static List<Transaction> getTransactionByStatus(Context context, long status) {
        List<Transaction> transactions = getTransactionDao(context).queryRaw(" Where STATUS=?", new String[]{String.valueOf(status)});
        return transactions;
    }

    public static Transaction getTransactionById(Context context, long id) {
        return getTransactionDao(context).load(id);
    }

    public static void deleteTransactionById(Context context, long id) {
        getTransactionDao(context).deleteByKey(id);
    }

    public static void deleteAllTransaction(Context context) {
        getTransactionDao(context).deleteAll();
    }

    public static TransactionDao getTransactionDao(Context context) {
        return ((BloodDonorApp) context.getApplicationContext()).getDaoSession().getTransactionDao();
    }


}
