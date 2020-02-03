package com.example.gstokengame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    EditText Username , Password;
    String Username_ , Password_;
    User user ;
    Context ctx=this;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    LoginButton loginButton;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    private static final String EMAIL = "email";
    String userEmail , userHomeTown ,userBirthday, userLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("_Token_Game_", MODE_PRIVATE);
        //String serializedDataFromPreference = pref.getString("_Token_Game_UD", null);
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        //Login_Button = (LoginButton) findViewById(R.id.main_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email" , "user_birthday" , "user_hometown" , "user_location" ));

        boolean loggedInf = AccessToken.getCurrentAccessToken() != null;

        if(loggedInf || pref.contains("_Token_Game_UD")) {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }else {
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken accessToken = loginResult.getAccessToken();

                    if (accessToken != null) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        try {
                                            userEmail = object.getString("email");
                                        } catch (Exception e) {
                                            userEmail = "";
                                        }

                                        try {
                                            userBirthday = object.getString("birthday");
                                        } catch (Exception e) {
                                            userBirthday = "";
                                        }

                                        try {
                                            JSONObject hometown =  object.getJSONObject("hometown");
                                            userHomeTown = hometown.getString("name");
                                        } catch (Exception e) {
                                            userHomeTown = "";
                                        }


                                        try {
                                            JSONObject location =  object.getJSONObject("location");
                                            userLocation = location.getString("name");
                                        } catch (Exception e) {
                                            userLocation = "";
                                        }

                                        if(userHomeTown.isEmpty()){
                                            if(userLocation.length() > 0) {
                                                userHomeTown = userLocation.substring( 0, userLocation.indexOf(","));
                                            }else {
                                                userHomeTown = userLocation;
                                            }
                                        }else {
                                            userHomeTown = userHomeTown.substring( 0, userHomeTown.indexOf(","));
                                        }


                                        try {
                                            SignIn si = new SignIn();
                                            si.execute("fbSignIn",object.getString("first_name"),object.getString("last_name") ,object.getString("gender"), userEmail,"0",userBirthday.replace("\\/", "-") , userHomeTown ,"","","", object.getString("id"));
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error : While Processing User Information." + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }

                                        //Log.v("LoginActivity ", "short response :" + response.toString());

                                    }

                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,hometown,location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Error : User Not Authorized.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void main_register(View v){
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void main_login(View v){

        Username_ = Username.getText().toString();
        Password_ = Password.getText().toString();
        if(Username_.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter The Username.",Toast.LENGTH_LONG).show();
        }else  if(Password_.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter The Password.",Toast.LENGTH_LONG).show();
        }
        else {
            SignIn b = new SignIn();
            b.execute("SignIn" , Username_,Password_);
        }
    }

    class SignIn extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpReqHandler rh = new HttpReqHandler();
            String URL ="";
            String JSONStr = "";
            if (params[0].equals("fbSignIn")) {
                URL = "http://netdokan.herokuapp.com/user/fb/" + params[11];
                JSONStr = rh.makeServiceCall(URL, "GET");
            } else{
                URL = "http://netdokan.herokuapp.com/user/login?username=" + params[1] + "&userpass=" + params[2];
                JSONStr = rh.makeServiceCall(URL, "POST");
            }

            boolean checknull = JSONStr == null;

            if(checknull == false) {
                if (JSONStr.isEmpty() == false) {
                    return JSONStr;
                } else if (params[0].equals("fbSignIn") && JSONStr.isEmpty()) {
                    SignUp su = new SignUp();
                    su.execute(params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11]);
                    return "registerFbUser";
                } else {
                    return "";
                }
            }else {
                return "";
            }
        }

        //@Override
        protected void onPostExecute(String s) {

            if(s.isEmpty() == false && s.equals("registerFbUser") == false) {
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

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if(s.equals("registerFbUser")) {
                Toast.makeText(getApplicationContext(), "Registering Facebook User", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "Username / Password Invalid", Toast.LENGTH_LONG).show();
            }
        }
    }

    class SignUp extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/user/add?firstName=" + Uri.encode(params[0]) + "&lastName=" + Uri.encode(params[1]) + "&gender="  + Uri.encode(params[2]) +  "&email="  + Uri.encode(params[3]) +  "&phone="  + Uri.encode(params[4]) +  "&dateOfBirth="  + Uri.encode(params[5]) +  "&city="  + Uri.encode(params[6]) +  "&currency="  + Uri.encode(params[7]) +  "&username="  + Uri.encode(params[8]) +  "&userpass="  + Uri.encode(params[9]) + "&facebookid="+ Uri.encode(params[10]);
            String JSONStr = rh.makeServiceCall(URL,"POST");

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

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error : " +  e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Service Not Working.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
