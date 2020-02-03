package com.example.gstokengame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TokensActivity extends AppCompatActivity {
    SharedPreferences pref;
    String UserId,CatId;
    private ListView lv;
    private List<Token> TList;
    private ArrayAdapter<Token> TLadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokens);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("_Token_Game_", MODE_PRIVATE);
        String serializedDataFromPreference = pref.getString("_Token_Game_UD", null);
        User restoredMyData = User.create(serializedDataFromPreference);
        UserId = restoredMyData.getId();
        lv = findViewById(R.id.tokens);

        Bundle bundle = getIntent().getExtras();
        CatId= bundle.getString("CatId");

        TList tl = new TList();
        tl.execute(UserId);

        boolean loggedInt = AccessToken.getCurrentAccessToken() == null;

        if(loggedInt && serializedDataFromPreference.isEmpty()) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView itemid = (TextView) view.findViewById(R.id.t_id);
                String id_ = itemid.getText().toString();
                Intent i = new Intent(getApplicationContext(), TokenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TokId", id_);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public void TAdapter(List<Token> tc){
        TLadapter = new Tokenadapter(this,R.layout.token_row,tc);
        lv.setAdapter(TLadapter);
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

    class TList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/token/all?category_id="+CatId;
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
                    JSONArray jsonArray = new JSONArray(s);
                    TList = new ArrayList<>();

                    int i = 0;

                    while (i < jsonArray.length()){
                        Token t = new Token();
                        JSONObject data = jsonArray.getJSONObject(i);
                        t.setId(data.getString("id"));
                        if(data.getString("content").length() > 30) {
                            t.setContent(data.getString("content").substring(0, 30));
                        }else {
                            t.setContent(data.getString("content"));
                        }
                        t.setConsumed(data.getString("consumed"));
                        t.setConsumedA(data.getString("consumedAt"));
                        TList.add(t);
                        i++;
                    }
                    TAdapter(TList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Data Not Founded.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
