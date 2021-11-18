package com.example.shopunlimited;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class ProductActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ProductData> list=new ArrayList<>();
    ProductAdapter adapter;
    private String prod_api="https://oakspro.com/projects/project39/rutuja/StroeUn/products_api.php";
    private String categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        recyclerView=findViewById(R.id.recyclerView_products);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        categoryID=getIntent().getStringExtra("catID");
        getProductsServer(categoryID);
    }

    private void getProductsServer(String categoryID) {

        StringRequest request=new StringRequest(Request.Method.POST, prod_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        list.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            ProductData productdata = new ProductData();
                            productdata.setProdID(object.getString("prod_id"));
                            productdata.setProdPrice(object.getString("prod_price"));
                            productdata.setProdDetails(object.getString("prod_details"));
                            productdata.setProdName(object.getString("prod_name"));
                            productdata.setProdSeller(object.getString("prod_seller"));
                            productdata.setProdStock(object.getString("prod_stock"));
                            productdata.setCat_id(object.getString("cat_id"));
                            productdata.setProdImage(object.getString("prod_image"));
                            list.add(productdata);
                        }
                        adapter = new ProductAdapter(list, ProductActivity.this);
                        recyclerView.setAdapter(adapter);


                    } else {
                        Toast.makeText(ProductActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> cat=new HashMap<>();
                cat.put("catID", categoryID);
                return cat;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void backhome(View view) {
        Intent intent=new Intent(ProductActivity.this,ShopActivity.class);
        startActivity(intent);
    }
}
