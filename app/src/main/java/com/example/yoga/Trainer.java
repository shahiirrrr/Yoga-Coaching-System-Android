package com.example.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Trainer extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    ArrayList<String> Trainer,Qualification,Gender,tid;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);
        l1=findViewById(R.id.listv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/view_trainer";
        RequestQueue queue = Volley.newRequestQueue(Trainer.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    Trainer= new ArrayList<>();
                    Gender  = new ArrayList<>();
                    Qualification = new ArrayList<>();
                    tid= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        Trainer.add(jo.getString("FIRST_NAME")+jo.getString("LAST_NAME"));
                        Gender.add(jo.getString("GENDER"));
                        Qualification.add(jo.getString("QUALIFICATION"));
                        tid.add(jo.getString("LOGIN_ID"));



                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1.setAdapter(new customdob(Trainer.this, Trainer,Qualification,Gender));
                    l1.setOnItemClickListener(Trainer.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Trainer.this, "err"+error, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        AlertDialog.Builder ald=new AlertDialog.Builder(Trainer.this);
        ald.setTitle("Request")
                .setPositiveButton("send request ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                                            RequestQueue queue = Volley.newRequestQueue(Trainer.this);
                                            String url = "http://" + sh.getString("ip","")  + ":5000/send_request";
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
                                                            Toast.makeText(Trainer.this, "Successful", Toast.LENGTH_SHORT).show();

                                                            Intent ik = new Intent(getApplicationContext(), Userhome.class);
                                                            startActivity(ik);

                                                        } else {

                                                            Toast.makeText(Trainer.this, "invalid", Toast.LENGTH_SHORT).show();

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

                                                    params.put("lid",sh.getString("lid",""));
                                                    params.put("tid",tid.get(i));


                                                    return params;
                                                }
                                            };
                                            queue.add(stringRequest);





                    }
                })
                .setNegativeButton("cancel ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });

        AlertDialog al=ald.create();
        al.show();

    }
}