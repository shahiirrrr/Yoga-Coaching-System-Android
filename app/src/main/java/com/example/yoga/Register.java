package com.example.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
    Button b1;
    RadioButton r1,r2,r3;
    SharedPreferences sh;
    String gender,hlth,wgh,het,DOB,uname,pswrd,email,fname,lname,ph;
    final Calendar myCalendar= Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e1=findViewById(R.id.editTextTextPersonName5);
        e2=findViewById(R.id.editTextTextPersonName6);
        e3=findViewById(R.id.editTextTextPersonName7);
        e4=findViewById(R.id.editTextTextPersonName8);
        e5=findViewById(R.id.editTextTextPersonName9);
        e6=findViewById(R.id.editTextTextPersonName10);
        e7=findViewById(R.id.editTextTextPersonName11);
        e8=findViewById(R.id.editTextTextPersonName100);
        e9=findViewById(R.id.editTextTextPersonName101);
        e10=findViewById(R.id.editTextTextPersonName102);
        b1=findViewById(R.id.button2);
        r1=findViewById(R.id.radioButton3);
        r2=findViewById(R.id.radioButton2);
        r3=findViewById(R.id.radioButton);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Register.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (r1.isChecked())
                    gender = r1.getText().toString();
                else if (r2.isChecked()) {
                    gender = r2.getText().toString();
                }
                else {
                    gender = r3.getText().toString();
                }
                fname = e1.getText().toString();
                lname = e2.getText().toString();


                ph = e3.getText().toString();

                email = e4.getText().toString();

                DOB = e5.getText().toString();

                het = e8.getText().toString();


                wgh = e9.getText().toString();

                hlth = e10.getText().toString();

                uname = e6.getText().toString();
                pswrd = e7.getText().toString();


                if (fname.equalsIgnoreCase("")) {
                    e1.setError("Enter your firstname");
                    e1.requestFocus();
                } else if (fname.matches("[a-zA-Z]")) {
                    e1.setError("characters allowed");
                }

                else if(lname.equalsIgnoreCase("")) {
                    e2.setError("Enter your laststname");
                    e2.requestFocus();
                } else if (lname.matches("[a-zA-Z]")) {
                    e2.setError("characters allowed");
                }
                else if (ph.equalsIgnoreCase("")) {
                    e3.setError("Enter Your Phone No");
                    e3.requestFocus();
                } else if (ph.length() != 10) {
                    e3.setError("Minimum 10 nos required");
                    e3.requestFocus();
               }
               else if (email.equalsIgnoreCase("")) {
                   e4.setError("Enter Your Email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    e4.setError("Enter Valid Email");
                    e4.requestFocus();
                }
                else if (DOB.equalsIgnoreCase("")) {
                    e5.setError("Enter Your  DATE OF BIRTH");
                    e5.requestFocus();
                }
                else if (het.equalsIgnoreCase("")) {
                    e8.setError("Enter your Height");
                    e8.requestFocus();
                }
                else if (wgh.equalsIgnoreCase("")) {
                    e9.setError("Enter your weight");
                    e9.requestFocus();
                }
                else if (hlth.equalsIgnoreCase("")) {
                    e10.setError("Enter your Health description");
                    e10.requestFocus();
                }
                else if (uname.equalsIgnoreCase("")) {
                    e6.setError("Enter your username");
                    e6.requestFocus();
                }

                else if (pswrd.equalsIgnoreCase("")) {
                    e7.setError("Enter your password");
                    e7.requestFocus();
                }
                   else{

                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/Register";
                    //Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("valid")) {

                                    Toast.makeText(Register.this, "Successful Registered", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Login.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(Register.this, "error", Toast.LENGTH_SHORT).show();

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
                            params.put("fname", fname);
                            params.put("lname", lname);
                            params.put("gender", gender);
                            params.put("phn", ph);
                            params.put("email", email);
                            params.put("DOB",DOB);
                            params.put("heigth", het);
                            params.put("weigth", wgh);
                            params.put("health", hlth);
                            params.put("uname", uname);
                            params.put("pass", pswrd);
                       params.put("lid",sh.getString("lid",""));


                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });









    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e5.setText(dateFormat.format(myCalendar.getTime()));
    }
}