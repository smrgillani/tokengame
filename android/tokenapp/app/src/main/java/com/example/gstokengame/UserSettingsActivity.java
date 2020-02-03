package com.example.gstokengame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class UserSettingsActivity extends AppCompatActivity {
    EditText Firstname , Lastname , Email , Phone , Dob , Username;
    String Firstname_ , Lastname_ , Gender_ , Email_ , Phone_ , Dob_ , City_ , Currency_ , Password_;
    String UserId ;
    TextView R_Message , gspinnert , cspinnert , cspinnert_;
    User user ;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MaterialSpinner gspinner , cspinner , cspinner_;
    private ArrayAdapter gAdapter, cAdapter , cAdapter_;
    List<String> glist, clist , clist_;
    private int mSelectedIndex = 0 , cSelectedIndex = 0 , cSelectedIndex_ = 0;
    Button Update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean loggedInf = AccessToken.getCurrentAccessToken() == null;



        pref = getApplicationContext().getSharedPreferences("_Token_Game_", MODE_PRIVATE);
        String serializedDataFromPreference = pref.getString("_Token_Game_UD", null);

        User restoredMyData = User.create(serializedDataFromPreference);
        UserId = restoredMyData.getId();
        Password_ = restoredMyData.getPassword();

        if(loggedInf && pref.contains("_Token_Game_UD") == false) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        Firstname = (EditText) findViewById(R.id.first_name);
        Lastname = (EditText) findViewById(R.id.last_name);
        Email = (EditText) findViewById(R.id.email);
        Phone = (EditText) findViewById(R.id.phone);
        Dob = (EditText) findViewById(R.id.dob);
        Username = (EditText) findViewById(R.id.username);
        R_Message = (TextView) findViewById(R.id.r_message);
        Update = (Button) findViewById(R.id.update_user);
        gspinner = (MaterialSpinner) findViewById(R.id.gender);
        cspinner = (MaterialSpinner) findViewById(R.id.city);
        cspinner_ = (MaterialSpinner) findViewById(R.id.currency);

        UserInfo ui = new UserInfo();
        ui.execute();

        glist = new ArrayList<String>();
        clist = new ArrayList<String>();
        clist_ = new ArrayList<String>();

        glist.add("Male");
        glist.add("Female");

        // Initialize an array adapter
        gAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,glist){
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(19);
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);
                tv.setTextColor(Color.parseColor("#000000"));
                if(position == mSelectedIndex){
                    tv.setBackgroundColor(Color.parseColor("#f8f6f6"));
                }
                return tv;
            }
        };

        cAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,clist){
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(19);
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);
                tv.setTextColor(Color.parseColor("#000000"));
                if(position == mSelectedIndex){
                    tv.setBackgroundColor(Color.parseColor("#f8f6f6"));
                }
                return tv;
            }
        };

        cAdapter_ = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,clist_){
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(19);
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);
                tv.setTextColor(Color.parseColor("#000000"));
                if(position == mSelectedIndex){
                    tv.setBackgroundColor(Color.parseColor("#f8f6f6"));
                }
                return tv;
            }
        };

        gspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedIndex = i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedIndex = i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cspinner_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedIndex = i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gAdapter.setDropDownViewResource(R.layout.spinner_item);
        gspinner.setAdapter(gAdapter);

        GetCityList gcl = new GetCityList();
        gcl.execute();

        GetCurrencyList gcl_ = new GetCurrencyList();
        gcl_.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void user_update(View v){

        gspinnert = (TextView)gspinner.getSelectedView();
        cspinnert = (TextView)cspinner.getSelectedView();
        cspinnert_ = (TextView)cspinner_.getSelectedView();

        Firstname_ = Firstname.getText().toString();;
        Lastname_ = Lastname.getText().toString();
        Gender_ = gspinnert.getText().toString();
        Email_ = Email.getText().toString();
        Phone_ = Phone.getText().toString();
        Dob_ = Dob.getText().toString();
        City_ = cspinnert.getText().toString();
        Currency_ = cspinnert_.getText().toString();

        if(Firstname_.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter The First Name.",Toast.LENGTH_LONG).show();
        }else if (Lastname_.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Enter The Last Name.",Toast.LENGTH_LONG).show();
        }else if (Gender_.equals("Select Your Gender")) {
            Toast.makeText(getApplicationContext(),"Please Enter The Gender Type.",Toast.LENGTH_LONG).show();
        }else if (Email_.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Enter The Email Address.",Toast.LENGTH_LONG).show();
        }else if (Phone_.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Enter The Phone Number.",Toast.LENGTH_LONG).show();
        }else if (Dob_.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Enter The Date Of Birth.",Toast.LENGTH_LONG).show();
        }else if (City_.equals("Select Your City")) {
            Toast.makeText(getApplicationContext(),"Please Enter The City Name.",Toast.LENGTH_LONG).show();
        }else if (Currency_.equals("Select Your Currency")) {
            Toast.makeText(getApplicationContext(),"Please Enter The Currency Type.",Toast.LENGTH_LONG).show();
        } else {
            UserUpdate uu = new UserUpdate();
            uu.execute(Firstname_,Lastname_,Gender_,Email_,Phone_,Dob_,City_, Currency_);
        }
    }

    class GetCityList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/city/list";
            String JSONStr = rh.makeServiceCall(URL,"GET");

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
                    JSONArray jsonArray = new JSONArray(s);
                    int i = 0;
                    while (i < jsonArray.length()){
                        JSONObject data = jsonArray.getJSONObject(i);
                        i++;
                        clist.add(data.getString("name"));
                        if(data.getString("name").equals(City_)){
                            cSelectedIndex = i;
                        }
                    }
                    cAdapter.setDropDownViewResource(R.layout.spinner_item);
                    cspinner.setAdapter(cAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Unable To Get Cities List , Service Not Working.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class GetCurrencyList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/currency/list";
            String JSONStr = rh.makeServiceCall(URL,"GET");

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
                    JSONArray jsonArray = new JSONArray(s);
                    int i = 0;
                    while (i < jsonArray.length()){
                        JSONObject data = jsonArray.getJSONObject(i);
                        i++;
                        clist_.add(data.getString("name"));
                        if(data.getString("name").equals(Currency_)){
                            cSelectedIndex_ = i;
                        }
                    }

                    cAdapter_.setDropDownViewResource(R.layout.spinner_item);
                    cspinner_.setAdapter(cAdapter_);
                    R_Message.setVisibility(View.GONE);
                    Firstname.setVisibility(View.VISIBLE);
                    Lastname.setVisibility(View.VISIBLE);
                    Email.setVisibility(View.VISIBLE);
                    Phone.setVisibility(View.VISIBLE);
                    Dob.setVisibility(View.VISIBLE);
                    Update.setVisibility(View.VISIBLE);

                    gspinner.setVisibility(View.VISIBLE);
                    cspinner.setVisibility(View.VISIBLE);
                    cspinner_.setVisibility(View.VISIBLE);

                    cspinner.setSelection(cSelectedIndex);
                    cspinner_.setSelection(cSelectedIndex_);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Unable To Get Currency List , Service Not Working.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class UserInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/user/show/" + UserId;
            String JSONStr = rh.makeServiceCall(URL,"GET");

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
                    Firstname.setText(root.getString("firstName") , TextView.BufferType.EDITABLE);
                    Lastname.setText(root.getString("lastName"));
                    String getgs = root.getString("genderString");

                    if(getgs.equals("Male")){
                        gspinner.setSelection(1);
                    }else if(getgs.equals("Female")){
                        gspinner.setSelection(2);
                    }

                    Gender_ = root.getString("genderString");
                    Email.setText(root.getString("email"));
                    Phone.setText(root.getString("phone"));
                    Dob.setText(root.getString("dateOfBirth"));
                    City_ = root.getString("cityString");
                    Currency_ = root.getString("currencyString");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error : " +  e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Data not Founder.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class UserUpdate extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/user/update/"+ UserId +"?firstName=" + Uri.encode(params[0]) + "&lastName=" + Uri.encode(params[1]) + "&gender="  + Uri.encode(params[2]) +  "&email="  + Uri.encode(params[3]) +  "&phone="  + Uri.encode(params[4]) +  "&dateOfBirth="  + Uri.encode(params[5]) +  "&city="  + Uri.encode(params[6]) +  "&currency="  + Uri.encode(params[7]);
            String JSONStr = rh.makeServiceCall(URL,"PUT");
            Log.v("LoginActivity ", "short response :" + URL);
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
                    user = new User( root.getString("id") , Password_ , root.getString("firstName") , root.getString("lastName"),root.getString("email"),root.getString("phone"),root.getString("dateOfBirth"),root.getString("membershipDate"),root.getString("oauthtoken"),root.getString("modified"),root.getString("cityString") , root.getString("currencyString") ,root.getString("genderString") , "");

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
