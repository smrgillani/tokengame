package com.example.gstokengame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangepassActivity extends AppCompatActivity {
    EditText OldPass , NewPass , RTNewPass;
    String OldPass_ , NewPass_ , RTNewPass_  , _Old_Pass_ , UserId;
    User user ;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("_Token_Game_", MODE_PRIVATE);
        String serializedDataFromPreference = pref.getString("_Token_Game_UD", null);
        User restoredMyData = User.create(serializedDataFromPreference);
        _Old_Pass_ = restoredMyData.getPassword();
        UserId = restoredMyData.getId();
        OldPass = (EditText) findViewById(R.id.old_pass);
        NewPass = (EditText) findViewById(R.id.new_pass);
        RTNewPass = (EditText) findViewById(R.id.rt_new_pass);
    }

    public void main_update(View v){
        OldPass_ = OldPass.getText().toString();
        NewPass_ = NewPass.getText().toString();
        RTNewPass_ = RTNewPass.getText().toString();

        if(OldPass_.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter The Old Password.",Toast.LENGTH_LONG).show();
        }else if (NewPass_.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Enter The New Password.",Toast.LENGTH_LONG).show();
        }else if (RTNewPass_.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Type Again The New Password.",Toast.LENGTH_LONG).show();
        }else if (OldPass_.equals(_Old_Pass_) == false) {
            Toast.makeText(getApplicationContext(),"Old Password Is Incorrect.",Toast.LENGTH_LONG).show();
        }else if (NewPass_.equals(RTNewPass_) == false) {
            Toast.makeText(getApplicationContext(),"New Password Not Matched.",Toast.LENGTH_LONG).show();
        } else {
            PassUpdate pu = new PassUpdate();
            pu.execute(NewPass_);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PassUpdate extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/user/update/"+ UserId +"?userpass=" + Uri.encode(params[0]) ;
            String JSONStr = rh.makeServiceCall(URL,"PUT");
            if(JSONStr != null){
                return JSONStr;
            }else {
                return "";
            }
        }

        //@Override
        protected void onPostExecute(String s) {
            if(s.isEmpty() == false) {
                try {
                    JSONObject root = new JSONObject(s);
                    user = new User( root.getString("id") , NewPass_ , root.getString("firstName") , root.getString("lastName"),root.getString("email"),root.getString("phone"),root.getString("dateOfBirth"),root.getString("membershipDate"),root.getString("oauthtoken"),root.getString("modified"),root.getString("cityString") , root.getString("currencyString") ,root.getString("genderString") , "");

                    String serializedData = user.serialize();
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);

                    editor = pref.edit();
                    editor.putString("_Token_Game_UD", serializedData);
                    editor.commit();

                    startActivity(i);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Service Not Working.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
