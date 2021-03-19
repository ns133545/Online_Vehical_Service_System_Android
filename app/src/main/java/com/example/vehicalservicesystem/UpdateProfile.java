package com.example.vehicle_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    public static String URL_UPDATE = IpAddressGet.getIp() + "UpdateProfile.php";
    TextView UpdatedName, UpdateEmail, UpdatedMobile;
    Button btn;
    String updateName, updateMob, updateEmail, oldname, oldmobile, oldemail, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        UpdatedName = findViewById(R.id.updateName);
        UpdatedMobile = findViewById(R.id.updateMobile);
        UpdateEmail = findViewById(R.id.updateEmail);

        btn = findViewById(R.id.Update);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        oldname = sharedPreferences.getString("U_Name", "");
        oldemail = sharedPreferences.getString("userId", "");
        id = sharedPreferences.getString("U_id", "");
        oldmobile = sharedPreferences.getString("Mob_No", "");
        UpdatedName.setText(oldname);
        UpdatedMobile.setText(oldmobile);
        UpdateEmail.setText(oldemail);
    }

    public void UpdateData() {
        updateName = UpdatedName.getText().toString().trim();
        updateMob = UpdatedMobile.getText().toString().trim();
        updateEmail = UpdateEmail.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("JSON", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", updateEmail);
                                editor.putString("Mob_No", updateMob);
                                editor.putString("U_Name", updateName);
                                Toast.makeText(UpdateProfile.this, "User Updated Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateProfile.this, "Username or Password is Invalid", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateProfile.this, "login fail" + e.toString(), Toast.LENGTH_SHORT).show();
                            // reg.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(UpdateProfile.this, "login failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UpdatedName", updateName);
                params.put("UpdatedMob", updateMob);
                params.put("UpdatedEmail", updateEmail);
                params.put("id", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}