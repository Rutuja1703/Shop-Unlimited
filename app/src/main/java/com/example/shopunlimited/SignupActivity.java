package com.example.shopunlimited;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SignupActivity extends AppCompatActivity {
    EditText first_name,last_name,phone_num,email,password,address;
    Button btn_signup;
    RadioGroup radioGroup;
    RadioButton gender_ratio;

    private String api_signup="https://oakspro.com/projects/project39/rutuja/StroeUn/signup_api.php";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        first_name=findViewById(R.id.fname_ed);
        last_name=findViewById(R.id.lname_ed);
        phone_num=findViewById(R.id.phone_ed);
        email=findViewById(R.id.email_ed);
        password=findViewById(R.id.pass_ed);
        address=findViewById(R.id.addr_ed);
        btn_signup=findViewById(R.id.signup_btn);
        radioGroup=findViewById(R.id.gender_radiogp);



        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);




        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String f_name=first_name.getText().toString();
                String l_name=last_name.getText().toString();
                String phone=phone_num.getText().toString();
                String Email_id=email.getText().toString();
                String pass=password.getText().toString();
                String addr=address.getText().toString();

                Integer gen=radioGroup.getCheckedRadioButtonId();
                gender_ratio=findViewById(gen);
                String sex=gender_ratio.getText().toString();


                uploadToServer(f_name,l_name,phone,Email_id,pass,addr,sex);




            }
        });
    }

    private void uploadToServer(String f_name, String l_name, String phone, String email_id, String pass, String addr, String sex) {
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, api_signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String str=jsonObject.getString("status");
                    if(str.equals("1"))
                    {
                        Toast.makeText(SignupActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    }
                    else if(str.equals("2"))
                    {
                        Toast.makeText(SignupActivity.this,"Account Already exist",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(SignupActivity.this,"Faild",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this,"Please check internet connection...",Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data=new HashMap<>();
                data.put("fname",f_name);
                data.put("lname",l_name);
                data.put("mobile",phone);
                data.put("Email",email_id);
                data.put("password",pass);
                data.put("address",addr);
                data.put("gender",sex);

                 return data;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}