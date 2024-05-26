package com.example.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Askdoubt extends AppCompatActivity {
    Button b1;
    EditText e1;
    SharedPreferences sh;
    String Doubt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askdoubt);
        e1=findViewById(R.id.editTextTextPersonName12);
        b1=findViewById(R.id.button14);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doubt = e1.getText().toString();
                if (Doubt.equalsIgnoreCase("")) {
                    e1.setError("Enter your Doubt");
                    e1.requestFocus();
                } else {
                    RequestQueue queue = Volley.newRequestQueue(Askdoubt.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/ask_doubt";
                    //Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("valid")) {
                                    Toast.makeText(Askdoubt.this, "Successful", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Doubt.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(Askdoubt.this, "you have no trainer", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("doubt", Doubt);
                            params.put("lid", sh.getString("lid", ""));


                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}