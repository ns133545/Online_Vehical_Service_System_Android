package com.example.vehicalservicesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initaialization

    Spinner spType;
    Button btnFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLatitude = 0, currentLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning
        spType = findViewById(R.id.sp_type);
        btnFind = findViewById(R.id.btn_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.google_map);

        //Initializing array of services type
        final String[] servicesList = {"car_service", "bike_service", "vehicle_service"};

        //Initializing array of services name
        String[] servicesName = {"Car Service", "Bike Service", "Vehicle Service"};

        //Set Adapter for Spinner
        spType.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, servicesName));

        //Initaializing Fused Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Check Permissions
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When Permission Granted
            //Initializing Task Location.
            getCurrentLocation();
        }
        else
        {
            //When Permission Denied
            //Request Permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 44);

        }
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Selected Position
                int i = spType.getSelectedItemPosition();
                //Initializing URL
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=" + currentLatitude + "," + currentLongitude +
                        "&radius=5000" +
                        "&types=" + servicesList[i] +
                        "&sensor=true" +
                        "&key=" + getResources().getString(R.string.google_map_key);
                Toast.makeText(getApplicationContext(),servicesList[i],Toast.LENGTH_SHORT).show();
                //Execute place task method to download Json data
                new PlaceTask().execute(url);
            }
        });
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //On Success
                if (location != null) {
                    //Get current Location
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //When Map is ready
                            map = googleMap;
                            //Zoom Current Location
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLatitude, currentLongitude), 10
                            ));
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When Permission Granted
                getCurrentLocation();
            }
        }
    }


    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                //Initialize Data
                Toast.makeText(getApplicationContext(),"test1",Toast.LENGTH_SHORT).show();
                 data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //Execute Parser task
            //new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //Initializing Url
        URL url = new URL(string);
        //Initializing Connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();;
        //Initializing Input
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while(line.equals(reader.readLine()))
        {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        Toast.makeText(getApplicationContext(),"hello1",Toast.LENGTH_SHORT).show();
        return data;
    }
/*
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //Create JSON Parser
            JsonParser jsonParser = new JsonParser();
            //Initialize Hash Map List
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject((strings[0]));
                //parse json object
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();
            for (int i=0;i<hashMaps.size();i++)
            {
                HashMap<String,String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                LatLng latLng = new LatLng(lat,lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                map.addMarker(options);
            }
        }
    }*/
}