package com.app.project.blooddonorfinder.Application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.app.project.blooddonorfinder.DataModels.DaoMaster;
import com.app.project.blooddonorfinder.DataModels.DaoSession;

/**
 * Created by xyz on 22-10-2015.
 */
public class BloodDonorApp extends Application {

    DaoSession mDaoSession;
    public static Location currentLocation=null;

    @Override
    public void onCreate() {
        super.onCreate();
        currentLocation=new Location("");
        currentLocation.setLatitude(36.0725);
        currentLocation.setLongitude(-79.8194);


        DaoMaster.DevOpenHelper devOpenHelper= new DaoMaster.DevOpenHelper(this,"blooddonor.db",null);
        SQLiteDatabase sqLiteDatabase=devOpenHelper.getWritableDatabase();
        DaoMaster master=new DaoMaster(sqLiteDatabase);
        mDaoSession=master.newSession();

    }

    public DaoSession getDaoSession()
    {
    return mDaoSession;
    }

}
