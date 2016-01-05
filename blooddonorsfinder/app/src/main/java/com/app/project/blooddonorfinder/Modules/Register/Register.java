package com.app.project.blooddonorfinder.Modules.Register;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.project.blooddonorfinder.AsyncTask.ATRegisterDonor;
import com.app.project.blooddonorfinder.AsyncTask.ATRegisterHospital;
import com.app.project.blooddonorfinder.R;

import java.util.LinkedHashMap;
import java.util.Map;


public class Register extends ActionBarActivity implements
        View.OnClickListener {
    LinearLayout bloodGroupLayout, specialityLayout;
    Button actionRegister;
    TextInputLayout licenseNumber;
    EditText emailET,nameET,phoneNumberET,altPhoneNumberET,addressET,hospitalLicenseNumber;
    Spinner bloodGroup, hospitalSpeciality;


    ProgressDialog progressBar;
    Toolbar mToolbar;
    RadioGroup userType,genderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hospitalSpeciality = (Spinner)findViewById(R.id.hospital_speciality);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        emailET=(EditText)findViewById(R.id.donor_email);
        nameET=(EditText)findViewById(R.id.donor_name);
        phoneNumberET=(EditText)findViewById(R.id.donor_phoneno);
        altPhoneNumberET=(EditText)findViewById(R.id.donor_alt_phone_number);
        addressET=(EditText)findViewById(R.id.address);
        bloodGroup=(Spinner)findViewById(R.id.donor_blood_group);
        hospitalLicenseNumber = (EditText)findViewById(R.id.license_number);
        setSupportActionBar(mToolbar);
        actionRegister=(Button)findViewById(R.id.register);
        actionRegister.setOnClickListener(this);

        userType = (RadioGroup) findViewById(R.id.user_type);
        genderType=(RadioGroup) findViewById(R.id.gender);

        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.donor) {
                    bloodGroupLayout.setVisibility(View.VISIBLE);
                    specialityLayout.setVisibility(View.GONE);
                    licenseNumber.setVisibility(View.GONE);
                } else if (checkedId == R.id.hospital) {
                    bloodGroupLayout.setVisibility(View.GONE);
                    specialityLayout.setVisibility(View.VISIBLE);
                    licenseNumber.setVisibility(View.VISIBLE);
                }
            }
        });
        mToolbar.setTitleTextColor(Color.WHITE);
        bloodGroupLayout = (LinearLayout) findViewById(R.id.donor_blood_group_layout);
        specialityLayout = (LinearLayout) findViewById(R.id.donor_speciality_layout);
        specialityLayout.setVisibility(View.GONE);
        licenseNumber=(TextInputLayout)findViewById(R.id.til_license_number);
        licenseNumber.setVisibility(View.GONE);

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:
                if(validateMandatoryField()) {
                    /*DonorBkUp donor = new DonorBkUp();
                    donor.setEmail(emailET.getText().toString().trim());
                    donor.setName(nameET.getText().toString().trim());
                    donor.setPhoneNumber(phoneNumberET.getText().toString().trim());
                    donor.setAltPhoneNumber(altPhoneNumberET.getText().toString().trim());
                    donor.setAddress(addressET.getText().toString().trim());

                    if(genderType.getCheckedRadioButtonId()==R.id.male){
                        donor.setGender("Male");
                    }else if(genderType.getCheckedRadioButtonId()==R.id.female)
                    {
                        donor.setGender("Female");
                    }

                    if(bloodGroup.getAdapter()!=null)
                    {
                        donor.setBloodGroup(bloodGroup.getSelectedItem().toString());
                    }*/


                    Map<String,Object> params = new LinkedHashMap<>();

                    params.put("email", emailET.getText().toString().trim());
                    params.put("name", nameET.getText().toString().trim());
                    params.put("phoneNumber", phoneNumberET.getText().toString().trim());
                    params.put("altPhoneNumber", altPhoneNumberET.getText().toString().trim());
                    params.put("address", addressET.getText().toString().trim());
                    if(userType.getCheckedRadioButtonId() == R.id.donor) {
                        if (genderType.getCheckedRadioButtonId() == R.id.male) {
                            params.put("gender", "Male");
                        } else if (genderType.getCheckedRadioButtonId() == R.id.female) {
                            params.put("gender", "FeMale");
                        }
                        params.put("bloodGroup", bloodGroup.getSelectedItem().toString());
                        new ATRegisterDonor(Register.this,params).execute();
                    }
                    else if(userType.getCheckedRadioButtonId() == R.id.hospital){
                        params.put("speciality", hospitalSpeciality.getSelectedItem().toString()+"");
                        params.put("licenseNumber", hospitalLicenseNumber.getText().toString()+"");
                        new ATRegisterHospital(Register.this,params).execute();
                    }




                }

                break;
        }
    }

    private boolean validateMandatoryField() {
        return true;
    }


}