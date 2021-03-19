package com.example.vehicle_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CurrentStatus extends AppCompatActivity {
    public String UrlFetchData = IpAddressGet.getIp() + "CurrentStatus.php";
    String id;
    String servicecentername, success, Status;
    String phoneno;
    String date;
    String toastMsg;
    ListView listView;
    RequestQueue queue;
    Context context;
    JsonArray array;
    TextView name, c_date, phone, c_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_status);

        context = this;
        name = (TextView) findViewById(R.id.Current_Name);
        name.setText("hello there");
        c_date = findViewById(R.id.C_DateofBooking);
        phone = findViewById(R.id.C_Phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone.getText()));
                context.startActivity(intent);
            }
        });
        c_status = findViewById(R.id.C_Status);
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
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.setText(object.getString("servicecentername").trim());
                                    phone.setText(object.getString("phoneno").trim());
                                    c_date.setText(object.getString("Date").trim());
                                    c_status.setText(object.getString("Status").trim());
                                    Status = object.getString("Status").trim();
                                }
                                if (Status.equals("0")) {
                                    c_status.setText("Not Accepted");
                                } else {
                                    c_status.setText("Accepted");
                                }

                            }

                        } catch (Exception e) {

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