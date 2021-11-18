package com.example.shopunlimited.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.shopunlimited.R;
import com.example.shopunlimited.SigninActivity;
import com.example.shopunlimited.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

private FragmentProfileBinding binding;

EditText first_Ed,last_Ed,mobileEd,emailEd,addrEd;
Button update_btn;

    String mobile_s="";
    private Boolean isEdit=false;
    private String api_link="https://oakspro.com/projects/project39/rutuja/StroeUn/profile_update_api.php";
    private ProgressDialog progressDialog;

SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        View root=binding.getRoot();



        sharedPreferences=getActivity().getSharedPreferences("MyLogin",0);
        editor=sharedPreferences.edit();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //set ids
          // final  TextView textView=binding.Textvi;
        first_Ed=root.findViewById(R.id.firstname_ed);
        last_Ed=root.findViewById(R.id.lastname_ed);
        mobileEd=root.findViewById(R.id.mobile_ed);
        emailEd=root.findViewById(R.id.email_ed);
        addrEd=root.findViewById(R.id.address_ed);
        update_btn=root.findViewById(R.id.update_btn);


        first_Ed.setEnabled(false);
        last_Ed.setEnabled(false);
        mobileEd.setEnabled(false);
        emailEd.setEnabled(false);
        addrEd.setEnabled(false);


        first_Ed.setText(sharedPreferences.getString("fname",null));
        last_Ed.setText(sharedPreferences.getString("lname",null));
        mobile_s=sharedPreferences.getString("mobile", "null");
        mobileEd.setText(sharedPreferences.getString("mobile",null));
        emailEd.setText(sharedPreferences.getString("email",null));
        addrEd.setText(sharedPreferences.getString("address",null));

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit==false){
                    first_Ed.setEnabled(true);
                    last_Ed.setEnabled(true);
                    mobileEd.setEnabled(true);
                    emailEd.setEnabled(true);
                    addrEd.setEnabled(true);

                    isEdit=true;
                    update_btn.setText("Update");
                    update_btn.setBackgroundResource(R.drawable.btn2_bg);
                }else {
                    first_Ed.setEnabled(false);
                    last_Ed.setEnabled(false);
                    mobileEd.setEnabled(false);
                    emailEd.setEnabled(false);
                    addrEd.setEnabled(false);

                    isEdit=false;
                    update_btn.setText("Edit");
                    update_btn.setBackgroundResource(R.drawable.btn_bg);

                    String first_N=first_Ed.getText().toString();
                    String last_N=last_Ed.getText().toString();
                    String email_N=emailEd.getText().toString();
                    String mobile_N=mobileEd.getText().toString();
                    String address_N=addrEd.getText().toString();


                    updateprofile(first_N,last_N,email_N,mobile_N,address_N,mobile_s);

                }
            }
        });



        return root;



    }

    private void updateprofile(String first_n, String last_n, String email_n, String mobile_n, String address_n, String mobile_s) {
        progressDialog.show();

        StringRequest request_signup = new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    progressDialog.dismiss();
                    if(status.equals("Success")){
                        showMessage(status, message);
                    }else{
                        Toast.makeText(getContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Please Check Internet...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data=new HashMap<>();
                data.put("fname", first_n);
                data.put("lname", last_n);
                data.put("email", email_n);
                data.put("mobile", mobile_n);
                data.put("address", address_n);
                data.put("userid", mobile_s);
                return data;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request_signup);

    }

    private void showMessage(String status, String message) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Account Update");
        builder.setMessage("\n"+status+"\n"+message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                editor.clear().commit();
                Intent intent=new Intent(getActivity().getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}