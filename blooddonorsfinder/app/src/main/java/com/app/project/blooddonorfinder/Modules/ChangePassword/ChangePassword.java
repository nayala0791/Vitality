package com.app.project.blooddonorfinder.Modules.ChangePassword;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.project.blooddonorfinder.AsyncTask.ATChangePassword;
import com.app.project.blooddonorfinder.R;


public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText old_psswd, new_psswd;
    ProgressDialog progressBar;
    Button changePassword;
    int uid = 0;
    String userName;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
       /* Drawable drawable = getResources().getDrawable(R.drawable.japalauncher);
        mToolbar.setLogo(BitmapHelper.scaleImage(this, drawable, 50));*/
        if (getIntent().getExtras() != null) {
            uid = getIntent().getExtras().getInt("uid", 0);
            userName = getIntent().getExtras().getString("username");
        }
        old_psswd = (EditText) findViewById(R.id.old_psswd);
        new_psswd = (EditText) findViewById(R.id.new_psswd);
        changePassword = (Button) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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
        if (v.getId() == R.id.changePassword) {

            if (validateMandatoryField())
            {
                //new sendRegistrationID().execute();
                new ATChangePassword(ChangePassword.this, uid, old_psswd.getText().toString(), new_psswd.getText().toString()).execute();
            }
        }
    }




    private boolean validateMandatoryField() {
        // TODO Auto-generated method stub

        if (old_psswd.length() == 0) {
            old_psswd.setError("Enter your Old Password");
            return false;
        }

        if (new_psswd.length() == 0) {
            new_psswd.setError("Enter your New Password");
            return false;
        }

        return true;
    }
}