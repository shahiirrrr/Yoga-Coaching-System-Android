package com.example.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sendcomplaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner s1;
    EditText e1;
    Button b1;
    ArrayList<String> Trainer,tid;
    SharedPreferences sh;
    String url,stid="";
    String complaint;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendcomplaint);
        s1=findViewById(R.id.spinner);
        e1=findViewById(R.id.editTextTextPersonName4);
        b1=findViewById(R.id.button6);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sh.getString("ip", "") + ":5000/view_trainercomp";
        RequestQueue queue = Volley.newRequestQueue(sendcomplaint.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    Trainer= new ArrayList<>();
                    tid= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        Trainer.add(jo.getString("FIRST_NAME")+jo.getString("LAST_NAME"));
                        tid.add(jo.getString("LOGIN_ID"));



                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<>(sendcomplaint.this,android.R.layout.simple_spinner_item,Trainer);
                    s1.setAdapter(ad);
                    s1.setOnItemSelectedListener(sendcomplaint.this);
//                    s1.setAdapter(new customcomp(Trainer.this,Trainer));
////                    l1.setOnItemClickListener(Complaint.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(sendcomplaint.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid",sh.getString("lid",""));
                return params;
            }
        };
        queue.add(stringRequest);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaint = e1.getText().toString();
                if (complaint.equalsIgnoreCase("")) {
                    e1.setError("Enter your complaint ");
                    e1.requestFocus();
                } else {
                    RequestQueue queue = Volley.newRequestQueue(sendcomplaint.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/send_complaints";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("valid")) {
                                    Toast.makeText(sendcomplaint.this, "Successful", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Complaint.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(sendcomplaint.this, "error", Toast.LENGTH_SHORT).show();

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
                            params.put("complaint", complaint);
                            params.put("tid", stid);
                            params.put("lid", sh.getString("lid", ""));


                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        stid=tid.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}