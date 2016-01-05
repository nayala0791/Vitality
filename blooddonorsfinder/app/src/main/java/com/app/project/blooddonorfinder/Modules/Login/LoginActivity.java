package com.app.project.blooddonorfinder.Modules.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.project.blooddonorfinder.AsyncTask.ATLogin;
import com.app.project.blooddonorfinder.Modules.DonorProfile.DonorProfile;
import com.app.project.blooddonorfinder.Modules.ForgotPassWord.ForgotPassword;
import com.app.project.blooddonorfinder.Modules.HospitalTransactions.HospitalProfile;
import com.app.project.blooddonorfinder.Modules.Register.Register;
import com.app.project.blooddonorfinder.R;



public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    TextView register, forgotPassWord;
    EditText userName, passWord;

    FloatingActionButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences appPreferences = getSharedPreferences(getString(R.string.app_preference), MODE_PRIVATE);
        if (appPreferences.getBoolean(getString(R.string.isLoggedIn), false)) {
            Intent intent = null;
            if(appPreferences.getLong(getString(R.string.usertype),0)==0) {
                intent=new Intent(LoginActivity.this, DonorProfile.class);

            }
            else {
                intent = new Intent(LoginActivity.this, HospitalProfile.class);
            }
            intent.putExtra("username",appPreferences.getString(getString(R.string.username),""));
            startActivity(intent);
            finish();
        } else {
            register = (TextView) findViewById(R.id.signup);
            forgotPassWord = (TextView) findViewById(R.id.forgotPassword);
            userName = (EditText) findViewById(R.id.username);
            passWord = (EditText) findViewById(R.id.password);
            login = (FloatingActionButton) findViewById(R.id.login);

            register.setOnClickListener(this);
            forgotPassWord.setOnClickListener(this);
            login.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        switch (v.getId()) {
            case R.id.signup:
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login:
                if (validateMandatoryFields())
                {
                    new ATLogin(LoginActivity.this,userName.getText().toString().trim(),passWord.getText().toString().trim()).execute();
                }

                else
                    Toast.makeText(LoginActivity.this, "Enter all the fields", Toast.LENGTH_LONG).show();

                break;
            case R.id.forgotPassword:
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(forgotPasswordIntent);
                finish();
                break;
        }
    }

    private boolean validateMandatoryFields() {

        if (userName.getText().toString().trim().length() == 0) {
            userName.setError("Enter the UserName");
            return false;
        }
        if (passWord.getText().toString().trim().length() == 0) {
            passWord.setError("Enter the Password");
            return false;
        }


        return true;
    }


}
