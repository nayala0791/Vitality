package com.app.project.blooddonorfinder.Modules.ForgotPassWord;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.project.blooddonorfinder.AsyncTask.ATForgotPassword;
import com.app.project.blooddonorfinder.Modules.Login.LoginActivity;
import com.app.project.blooddonorfinder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class ForgotPassword extends ActionBarActivity implements View.OnClickListener{

    EditText registeredEmailET;
    FloatingActionButton resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        registeredEmailET=(EditText)findViewById(R.id.email);
        resetPassword=(FloatingActionButton)findViewById(R.id.reset);
        resetPassword.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reset:
                if(registeredEmailET.getText().toString().isEmpty())
                    registeredEmailET.setError("Please Enter the Email Id");
                else
                    new ATForgotPassword(ForgotPassword.this,registeredEmailET.getText().toString().trim() ).execute();
                break;
        }

    }

}
