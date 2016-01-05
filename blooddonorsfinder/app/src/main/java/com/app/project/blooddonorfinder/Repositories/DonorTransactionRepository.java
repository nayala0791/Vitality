package com.app.project.blooddonorfinder.Repositories;

import android.content.Context;

import com.app.project.blooddonorfinder.Application.BloodDonorApp;
import com.app.project.blooddonorfinder.DataModels.Donor;
import com.app.project.blooddonorfinder.DataModels.DonorDao;

import java.util.Comparator;
import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by xyz on 22-10-2015.
 */
public class DonorTransactionRepository {

    public static void insertOrUpdateDonor(Context context, Donor donor) {
        //Check if the user Id Exist
        Donor checkDonor = getDonorByUserId(context, donor.getUserId());
        if (checkDonor != null)
            donor.setId(checkDonor.getId());
        getDonorDao(context).insertOrReplace(donor);
    }

    public static List<Donor> getAllDonors(Context context) {
        return getDonorDao(context).loadAll();
    }

    public static Donor getDonorByUserId(Context context, long userId) {
        Donor donor = null;
        List<Donor> donors= getDonorDao(context).queryRaw(" Where USER_ID=?", new String[]{String.valueOf(userId)});
        if(donors!=null && donors.size()>0)
            donor=donors.get(0);
        return donor;
    }


    public static Donor getDonorById(Context context, long id) {
        return getDonorDao(context).load(id);
    }

    public static void deleteDonorById(Context context, long id) {
        getDonorDao(context).deleteByKey(id);
    }

    public static void deleteAllDonor(Context context) {
        getDonorDao(context).deleteAll();
    }

    public static DonorDao getDonorDao(Context context) {
        return ((BloodDonorApp) context.getApplicationContext()).getDaoSession().getDonorDao();
    }
}
