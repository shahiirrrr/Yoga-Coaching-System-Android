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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Doubt extends AppCompatActivity {
    ListView l1;
    Button b1;
    SharedPreferences sh;
    String url;
    ArrayList<String> doubt,date,reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt);
        b1=findViewById(R.id.button7);
        l1=findViewById(R.id.lv1);
        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/doubt_reply";
        RequestQueue queue = Volley.newRequestQueue(Doubt.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    doubt= new ArrayList<>();
                    date= new ArrayList<>();
                    reply=new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        doubt.add(jo.getString("DOUBT"));
                        date.add(jo.getString("DATE"));
                        reply.add(jo.getString("REPLY"));



                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1.setAdapter(new customdob(Doubt.this,doubt,date,reply));
//                    l1.setOnItemClickListener(Complaint.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Doubt.this, "err"+error, Toast.LENGTH_SHORT).show();
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
                Intent i=new Intent(getApplicationContext(),Askdoubt.class);
                startActivity(i);
            }
        });

    }
}