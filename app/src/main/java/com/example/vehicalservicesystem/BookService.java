package com.example.vehicle_try;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookService extends AppCompatActivity {
    double latitude, longitude;
    String s_name, u_name, id, mobile, userName, U_id;
    boolean flag = true;
    public static final String URL_FETCH = IpAddressGet.getIp() + "Book.php";
    public int locationStatus, gpsStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        s_name = sharedPreferences.getString("value", "");
        u_name = sharedPreferences.getString("userId", "");
        id = sharedPreferences.getString("service_id", "");
        Toast.makeText(this, id + "     ********************************     ", Toast.LENGTH_SHORT).show();
        mobile = sharedPreferences.getString("Mob_No", "");
        U_id = sharedPreferences.getString("U_id", "");
        userName = sharedPreferences.getString("U_Name", "");
        System.out.println("******************************" + " " + s_name);
        System.out.println("******************************" + " " + u_name);
        System.out.println("******************************" + " " + id);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("To Continue you must turn on your Gps")
                    .setCancelable(false)
                    .setPositiveButton("Turn on ", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            Intent intent = new Intent(BookService.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        } else {
            LocationManager locationManager;
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if (flag == true) {
                            flag = false;
                            SendData(latitude, longitude);
                        }

                        //getWeaterData(longitude, latitude);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.i("LOCATION", "disable");
                        Toast.makeText(getApplicationContext(), "Please enable your Location Service.", Toast.LENGTH_LONG).show();
                        locationStatus = -1;
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.i("LOCATION", "enable");
                        locationStatus = 1;
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.i("LOCATION", "status" + status);
                    }

                });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void SendData(final Double lat, Double lng) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FETCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("JSON", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");//"1"
                            String responseDescription = jsonObject.getString("responseDescription");//"Registration succsfull"
                            // JSONArray jsonArray=jsonObject.getJSONArray("login");
                            //   String otp = jsonObject.getString("otp");

                            if (success.equals("1")) {
                                Toast.makeText(BookService.this, responseDescription, Toast.LENGTH_SHORT).show();
                                Intent io = new Intent(getApplicationContext(), MainActivity.class);
                                io.putExtra("Response Description", responseDescription);
                                startActivity(io);
                            } else if (success.equals(("2"))) {
                                Toast.makeText(BookService.this, responseDescription, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("Response Description", responseDescription);
                                startActivity(intent);
                            } else {
                                Toast.makeText(BookService.this, responseDescription, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("Response Description", responseDescription);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BookService.this, "Register fail" + e.toString(), Toast.LENGTH_SHORT).show();
                            // reg.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(BookService.this, "Register failed" + error.toString(), Toast.LENGTH_SHORT).show();
                // reg.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("S_name", s_name);
                params.put("UserId", u_name);
                params.put("Mob_no", mobile);
                params.put("UserName", userName);
                params.put("U_id", U_id);

                params.put("Latitude", Double.toString(latitude));
                params.put("Longitude", Double.toString(longitude));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}