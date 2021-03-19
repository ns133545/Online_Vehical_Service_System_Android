package com.example.vehicle_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static String URL_REGIST = IpAddressGet.getIp() + "Register.php";
    EditText name_e, mobile_number_e, email_e, password_e, c_password_e;
    TextView loginLink;
    String name,email,password,mobile_number,c_password;
    Button btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        name_e = (EditText) findViewById(R.id.userName);
        mobile_number_e = (EditText) findViewById(R.id.Phone);
        email_e = (EditText) findViewById(R.id.Email);
        password_e = (EditText) findViewById(R.id.password);
        c_password_e = (EditText) findViewById(R.id.c_password);
        btnReg = (Button) findViewById(R.id.submit);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist();
            }
        });
        loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public void Regist(){
        name = this.name_e.getText().toString();
        email = this.email_e.getText().toString();
        password = this.password_e.getText().toString();
        mobile_number = this.mobile_number_e.getText().toString();
        c_password = this.c_password_e.getText().toString();
        if (name.equals("")) {
            this.name_e.setError("Name required");

        } else if (mobile_number.equals("")) {
            this.mobile_number_e.setError("Mobile number required");
        } else if (email.equals("")) {
            this.email_e.setError("E-mail required ");
        } else if (password.equals("")) {
            this.password_e.setError("Password required");
        } else if (c_password.equals("")) {
            this.c_password_e.setError("Confirm Password required");
        } else if (!password.equals(c_password)) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        }
        else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("JSON", response);
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");//"1"
                                String  responseDescription=jsonObject.getString("responseDescription");//"Registration succsfull"
                                // JSONArray jsonArray=jsonObject.getJSONArray("login");
                                //   String otp = jsonObject.getString("otp");

                                if (success.equals("1")) {
                                    Toast.makeText(Register.this, responseDescription, Toast.LENGTH_SHORT).show();
                                    Intent io = new Intent(getApplicationContext(), Login.class);
                                    startActivity(io);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(Register.this, responseDescription, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Register.this, "Register fail" + e.toString(), Toast.LENGTH_SHORT).show();
                                // reg.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(Register.this, "Register failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    // reg.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("mobile_number", mobile_number);
                    params.put("email", email);
                    params.put("password", password);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}