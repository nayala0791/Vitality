package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DAOGenerator {
    public static void  main(String[] args)
    {
        Schema schema = new Schema(2, "com.app.project.blooddonorfinder.DataModels");
        Entity donor=schema.addEntity("Donor");
        donor.addIdProperty();
        donor.addIntProperty("userId");
        donor.addStringProperty("userName");
        donor.addStringProperty("bloodGroup");
        donor.addStringProperty("gender");
        donor.addStringProperty("email");
        donor.addIntProperty("numberOfDonation");
        donor.addIntProperty("readyToDonate");
        donor.addStringProperty("phoneNumber");
        donor.addStringProperty("altPhoneNumber");
        donor.addStringProperty("address");
        donor.addStringProperty("lastKnownLocation");
        donor.addStringProperty("lastKnownTime");


        Entity hospital=schema.addEntity("Hospital");
        hospital.addIdProperty();
        hospital.addLongProperty("userId");
        hospital.addStringProperty("hospitalName");
        hospital.addStringProperty("hospitalAddress");
        hospital.addStringProperty("email");
        hospital.addStringProperty("speciality");
        hospital.addStringProperty("phoneNumber");
        hospital.addStringProperty("hospitalLicense");
        hospital.addStringProperty("alternativePhonenumber");

        Entity transaction=schema.addEntity("Transaction");
        transaction.addIdProperty();
        transaction.addLongProperty("transactionId");
        transaction.addLongProperty("userId");
        transaction.addLongProperty("hospitalId");
        transaction.addStringProperty("hospitalName");
        transaction.addStringProperty("hospitalAddress");
        transaction.addStringProperty("speciality");
        transaction.addStringProperty("phoneNumber");
        transaction.addIntProperty("status");

        try {
            new DaoGenerator().generateAll(schema, "./daomodule/src-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

