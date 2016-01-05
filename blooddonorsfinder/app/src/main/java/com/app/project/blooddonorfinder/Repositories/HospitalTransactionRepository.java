package com.app.project.blooddonorfinder.Repositories;

import android.content.Context;

import com.app.project.blooddonorfinder.Application.BloodDonorApp;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.DonorDao;
import com.app.project.blooddonorfinder.DataModels.Hospital;
import com.app.project.blooddonorfinder.DataModels.HospitalDao;

import java.util.List;

/**
 * Created by xyz on 23-10-2015.
 */
public class HospitalTransactionRepository {

    public static void insertOrUpdateHospital(Context context, Hospital hospital) {
        //Check if the user Id Exist
        getHospitalDao(context).insertOrReplace(hospital);
    }

    public static List<Hospital> getAllHospitals(Context context) {
        return getHospitalDao(context).loadAll();
    }

    public static Hospital getDonorByUserId(Context context, long userId) {
        Hospital hospital = null;
        List<Hospital> hospitals= getHospitalDao(context).queryRaw(" Where USER_ID=?", new String[]{String.valueOf(userId)});
        if(hospitals!=null && hospitals.size()>0)
            hospital=hospitals.get(0);
        return hospital;
    }


    public static Hospital getHospitalById(Context context, long id) {
        return getHospitalDao(context).load(id);
    }

    public static void deleteHospitalById(Context context, long id) {
        getHospitalDao(context).deleteByKey(id);
    }

    public static void deleteAllHospital(Context context) {
        getHospitalDao(context).deleteAll();
    }

    public static HospitalDao getHospitalDao(Context context) {
        return ((BloodDonorApp) context.getApplicationContext()).getDaoSession().getHospitalDao();
    }
}
