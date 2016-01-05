package com.app.project.blooddonorfinder.Helper;

/**
 * Created by ISSLT115-PC on 7/14/2015.
 */
public class EmailValidator {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
