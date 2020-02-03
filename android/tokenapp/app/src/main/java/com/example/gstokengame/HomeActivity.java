package com.example.gstokengame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    SharedPreferences pref;
    String UserId;
    private ListView lv;
    private List<TokenCategory> TCList;
    private ArrayAdapter<TokenCategory> TCLadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getApplicationContext().getSharedPreferences("_Token_Game_", MODE_PRIVATE);
        String serializedDataFromPreference = pref.getString("_Token_Game_UD", null);
        User restoredMyData = User.create(serializedDataFromPreference);
        //UserId = restoredMyData.getId();
        lv = findViewById(R.id.token_cat);
        CatList cl = new CatList();
        cl.execute(UserId);

        boolean loggedInt = AccessToken.getCurrentAccessToken() == null;

        if(loggedInt && pref.contains("_Token_Game_UD") == false) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView itemid = (TextView) view.findViewById(R.id.c_id);
                String id_ = itemid.getText().toString();
                Intent i = new Intent(getApplicationContext(), TokensActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CatId", id_);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean loggedInt = AccessToken.getCurrentAccessToken() != null;
        if(loggedInt) {
            MenuItem menuOpen = menu.findItem(R.id.change_user_password);
            menuOpen.setVisible(false);
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void TCAdapter(List<TokenCategory> tc){
        TCLadapter = new TokenCategoryadapter(this,R.layout.category_row,tc);
        lv.setAdapter(TCLadapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                LoginManager.getInstance().logOut();
                pref.edit().remove("_Token_Game_UD").commit();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.user_settings:
                startActivity(new Intent(this,UserSettingsActivity.class));
                break;
            case R.id.change_user_password:
                startActivity(new Intent(this,ChangepassActivity.class));
                break;
        }
        return true;
    }

    class CatList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpReqHandler rh = new HttpReqHandler();
            String URL = "http://netdokan.herokuapp.com/category/all";
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
                    TCList = new ArrayList<>();

                    int i = 0;

                    while (i < jsonArray.length()){
                        TokenCategory tc = new TokenCategory();
                        JSONObject data = jsonArray.getJSONObject(i);
                        tc.setId(data.getString("id"));
                        tc.setName(data.getString("name"));
                        tc.setGreetingCount(data.getString("greetingCount"));
                        tc.setModified(data.getString("modified"));
                        TCList.add(tc);
                        i++;
                    }
                    TCAdapter(TCList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else  {
                Toast.makeText(getApplicationContext(), "Data Not Founded.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
