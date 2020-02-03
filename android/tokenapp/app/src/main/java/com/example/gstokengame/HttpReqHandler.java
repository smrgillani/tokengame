package com.example.gstokengame;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class HttpReqHandler {

    private static final String TAG = HttpReqHandler.class.getSimpleName();

    public String makeServiceCall(String reqUrl , String MethodType) {
        String response = null;
        try {
            URL url = new URL(reqUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(MethodType.equals("POST")) {
                //JSONObject postDataParams = new JSONObject(para);
                //String ids = "";
                //ListIterator<Integer> li = para.listIterator(para.size());
                //while(li.hasPrevious()) {
                //    int str=li.previous();
                //    ids = ids + str + ",";
                //}

                //postDataParams.put("id", ids);



                //postDataParams.put("Size",para.size());

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //writer.write(getPostDataString(postDataParams));
                //writer.flush();
                //writer.close();
                os.close();

            }else if(MethodType.equals("PUT")) {
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.close();

            }else {
                conn.setRequestMethod("GET");
                //conn.setDoInput(true);
                //conn.setDoOutput(true);
                //OutputStream os = conn.getOutputStream();
                //os.close();
            }

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
