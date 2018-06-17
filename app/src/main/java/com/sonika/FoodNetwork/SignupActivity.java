package com.sonika.FoodNetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sonika.FoodNetwork.JsonHelper.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupActivity  extends AppCompatActivity{
    EditText mName, mPassword, mPhone, mAddress, mEmail;
    String sName, sPassword, sPhone, sAddress, sEmail;
    Button signup;
    int flag=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mName = (EditText) findViewById(R.id.activity_signup_name);
        mAddress = (EditText) findViewById(R.id.activity_signup_address);
        mEmail = (EditText) findViewById(R.id.activity_signup_email);
        mPassword = (EditText) findViewById(R.id.activity_signup_password);
        mPhone = (EditText) findViewById(R.id.activity_signup_phone);
        signup = (Button) findViewById(R.id.activity_signup_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = mName.getText().toString();
                sAddress = mAddress.getText().toString();
                sEmail = mEmail.getText().toString();
                sPassword = mPassword.getText().toString();
                sPhone = mPhone.getText().toString();

                if    (mName.length()<=0 || mAddress.length()<=0 || mPhone.length()<=0 || mPassword.length()<=0 || mEmail.length()<=0 )
                {
                    Toast.makeText(SignupActivity.this, "Please, fill all the fields! ", Toast.LENGTH_SHORT).show();
                }

                else if (!isValidEmail(sEmail))
                {
                    mEmail.setError("Enter valid email address");
                }
                else if (!isValidPassword(sPassword))
                {
                    mPassword.setError("Passwords should at least be 6 charecters long");
                }
                else if (!isValidContact(sPhone))
                {
                    mPhone.setError("Enter a valid phone number!!");
                }

                else {

                    new registerAsyncTask().execute();
                }
            }
        });
    }
    private boolean isValidPassword(String spassword) {
        if (spassword != null && spassword.length() > 6) {
            return true;
        }
        return false;
    }

    private boolean isValidEmail(String sEmail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(sEmail);
        return matcher.matches();
    }
    private boolean isValidContact(String sPhone) {
        if (sPhone != null && sPhone.length() == 10){
            return true;
        }
        return false;
    }


    class registerAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog mprogressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogressDialog = new ProgressDialog(SignupActivity.this);
            mprogressDialog.setMessage("please wait");
            mprogressDialog.setCancelable(false);
            mprogressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> registerActivityHashMap = new HashMap<>();
            registerActivityHashMap.put("email", sEmail);
            registerActivityHashMap.put("password", sPassword);
            registerActivityHashMap.put("name", sName);
            registerActivityHashMap.put("phone", sPhone);
            registerActivityHashMap.put("address", sAddress);

            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.performPostCI("http://kinbech.6te.net/ResturantFoods/api/register", registerActivityHashMap);

            try {
                if (jsonObject == null) {
                    flag = 1;
                } else if (jsonObject.getString("status").equals("success")) {
                    flag = 2;
                } else {
                    flag = 3;

                }
            } catch (JSONException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mprogressDialog.dismiss();
            if (flag == 1) {
                Toast.makeText(SignupActivity.this, "Network issue", Toast.LENGTH_SHORT).show();

            } else if (flag == 2) {
                Toast.makeText(SignupActivity.this, "Registration success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(SignupActivity.this, "Invalid user Try again ", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
