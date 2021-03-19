package com.example.vehicle_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HistoryBook extends AppCompatActivity {

    public String UrlFetchData = IpAddressGet.getIp() + "DateGetSet.php";
    String id;
    String[] servicecentername;
    String[] phoneno;
    String[] address;
    String[] date;
    String toastMsg;
    ListView listView;
    RequestQueue queue;
    Context context;
    JsonArray array;
    public int locationStatus = 0;
    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_book);
        listView = (ListView) findViewById(R.id.lsthistory);
        context = this;
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        id = sharedPreferences.getString("U_id", "");
        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        intent.getExtras();


        if (intent.hasExtra("Response Description")) {
            toastMsg = intent.getStringExtra("Response Description");
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
        }


        StringRequest makeRequest = new StringRequest(Request.Method.POST, UrlFetchData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        System.out.println(res);
                        Gson gson = new Gson();
                        if (res.contains("servicecentername") && res.contains("Date")) {

                            array = gson.fromJson(res, JsonArray.class);
                            servicecentername = new String[array.size()];
                            phoneno = new String[array.size()];
                            date = new String[array.size()];
                            for (int i = 0; i < array.size(); i++) {
                                System.out.println("in loop");
                                JsonObject jobj = array.get(i).getAsJsonObject();

                                servicecentername[i] = jobj.get("servicecentername").getAsString();
                                phoneno[i] = jobj.get("phoneno").getAsString();
                                date[i] = jobj.get("Date").getAsString();

                            }
                            HistoryAdapter myAdapter = new HistoryAdapter(context, servicecentername, phoneno, date);
                            listView.setAdapter(myAdapter);

                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Once the request is performed, failed code over here is executed
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        queue.add(makeRequest);
    }
}
//package com.example.vehicle_try;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class HistoryBook extends AppCompatActivity {
//    //    Date date[];
//    public static String URL_FETCH = IpAddressGet.getIp() + "DateGetSet.php";
//    String date[];
//    String[] servicecentername;
//    String[] phoneno;
//    String[] address;
//    RequestQueue queue;
//    JsonArray array;
//    Context context;
//    ListView listView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history_book);
//        queue = Volley.newRequestQueue(this);
//        context=this;
//        listView=(ListView)findViewById(R.id.lsthistory);
//
//        StringRequest makeRequest = new StringRequest(Request.Method.POST, URL_FETCH,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String res) {
//                        System.out.println(res);
//                        Gson gson = new Gson();
//                        if (res.contains("servicecentername")) {
//
//                            array = gson.fromJson(res,JsonArray.class);
//                            servicecentername = new String[array.size()];
//                            phoneno = new String[array.size()];
//                            date=new String[array.size()];
//                            /*for (int i = 0; i < array.size(); i++) {
//                                System.out.println("in loop");
//                                JsonObject jobj = array.get(i).getAsJsonObject();
//
//                                servicecentername[i] = jobj.get("servicecentername").getAsString();
//                                phoneno[i] = jobj.get("phoneno").getAsString();
//                                date[i] = jobj.get("Date").getAsString();
//
//
//                            }*/
//                            HistoryAdapter myAdapter = new HistoryAdapter(context, servicecentername, phoneno,date);
//                            listView.setAdapter(myAdapter);
//
//                        }
//                    }
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Once the request is performed, failed code over here is executed
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                return params;
//            }
//        };
//        queue.add(makeRequest);
//    }
//}