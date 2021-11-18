package com.example.shopunlimited;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SigninActivity extends AppCompatActivity {
    EditText mobile_ed,password_ed;
    Button btn_signin;
    TextView signup_txt;

    private ProgressDialog progressDialog;

    private String signin_api="https://oakspro.com/projects/project39/rutuja/StroeUn/signin_api.php";

    //sharedpreference:

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mobile_ed=findViewById(R.id.phon_sin);
        password_ed=findViewById(R.id.password_sin);
        signup_txt=findViewById(R.id.txt_signup);
        btn_signin=findViewById(R.id.signin_btn);


        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //sharedpreference

        sharedPreferences =getSharedPreferences("MyLogin",0);
        editor=sharedPreferences.edit();



        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String user_mobile=mobile_ed.getText().toString();
                String user_password=password_ed.getText().toString();

                if(!TextUtils.isEmpty(user_mobile) && !TextUtils.isEmpty(user_password)){
                    if(user_mobile.length()==10){
                        loginAuth(user_mobile, user_password);
                    }else{
                        Toast.makeText(SigninActivity.this, "Please Enter Valid 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SigninActivity.this, "Please Enter Valid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loginAuth(String user_mobile, String user_password) {
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, signin_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String st = jsonObject.getString("status");
                    if (st.equals("Success"))
                    {

                        editor.putBoolean("isLogged", true);
                        editor.putString("fname", jsonObject.getString("fname"));
                        editor.putString("lname", jsonObject.getString("lname"));
                        editor.putString("email", jsonObject.getString("Email"));
                        editor.putString("mobile", jsonObject.getString("mobile"));
                        editor.putString("address", jsonObject.getString("address"));
                        editor.commit();

                        Toast.makeText(SigninActivity.this, "WelCome to a Shop Unlimited", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SigninActivity.this,ShopActivity.class);
                        startActivity(intent);
                        finish();

                    } else if(st.equals("Failed")){
                        Toast.makeText(SigninActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                    }else if(st.equals("Invalid")){
                        Toast.makeText(SigninActivity.this, "Invalid  Password", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SigninActivity.this, "Please Check Internet Connection...", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> data=new HashMap<>();
                data.put("Mobile",user_mobile);
                data.put("Pass",user_password);
                data.put("package",getPackageName());
                return data;

            }


        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public void signup(View view) {
        Intent intent=new Intent(SigninActivity.this,SignupActivity.class);
        startActivity(intent);
    }
}