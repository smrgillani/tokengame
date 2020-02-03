package com.example.gstokengame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class TokenActivity extends AppCompatActivity {
    SharedPreferences pref;
    String UserId,TokId;
    TextView Content , Contenth , Createdh, Created , Consumed , Consumedh , ConsumedAt , ConsumedAth , Message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("_Token_Game_", MODE_PRIVATE);
        String serializedDataFromPreference = pref.getString("_Token_Game_UD", null);
        User restoredMyData = User.create(serializedDataFromPreference);
        UserId = restoredMyData.getId();

        Contenth = (TextView) findViewById(R.id.contenth);
        Content = (TextView) findViewById(R.id.contentt);
        Createdh = (TextView) findViewById(R.id.createdh);
        Created = (TextView) findViewById(R.id.createdt);
        Consumed = (TextView) findViewById(R.id.consumedt);
        Consumedh = (TextView) findViewById(R.id.consumedh);
        ConsumedAt = (TextView) findViewById(R.id.consumedatt);
        ConsumedAth = (TextView) findViewById(R.id.consumedAt);
        Message = (TextView) findViewById(R.id.Message);

        Bundle bundle = getIntent().getExtras();
        TokId= bundle.getString("TokId");

        Token t = new Token();
        t.execute(UserId);

        boolean loggedInt = AccessToken.getCurrentAccessToken() == null;

        if(loggedInt && serializedDataFromPreference.isEmpty()) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
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

    class Token extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/token/consumebyid/"+TokId;
            String JSONStr = rh.makeServiceCall(URL,"GET");

            if(JSONStr != null){
                return JSONStr;
            }else {
                return "";
            }
        }

        protected void onPostExecute(String s) {
            if(s.isEmpty() == false) {
                    try {

                        JSONObject root = new JSONObject(s);
                        Consumed.setText(root.getString("consumed"));
                        ConsumedAt.setText(root.getString("consumedAt"));
                        Created.setText(root.getString("created"));
                        Content.setText(root.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error : " +  e.getMessage(), Toast.LENGTH_LONG).show();
                    }
            }else  {
                Contenth.setVisibility(View.GONE);
                Content.setVisibility(View.GONE);
                Createdh.setVisibility(View.GONE);
                Created.setVisibility(View.GONE);
                Consumedh.setVisibility(View.GONE);
                Consumed.setVisibility(View.GONE);
                ConsumedAt.setVisibility(View.GONE);
                ConsumedAth.setVisibility(View.GONE);
                Message.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Data Not Founded.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
