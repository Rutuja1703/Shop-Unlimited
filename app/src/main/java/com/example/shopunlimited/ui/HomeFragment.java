package com.example.shopunlimited.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopunlimited.CategoryAdapter;
import com.example.shopunlimited.CategoryModel;
import com.example.shopunlimited.R;
import com.example.shopunlimited.SliderImgAdapter;
import com.example.shopunlimited.SliderItem;
import com.example.shopunlimited.databinding.FragmentHomeBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    ArrayList<CategoryModel> arrayList=new ArrayList<>();
   // ArrayList<SliderItem> bannerList=new ArrayList<>();
    CategoryAdapter adapter;

    SliderImgAdapter imgAdapter;
    private String cat_api="https://oakspro.com/projects/project39/rutuja/StroeUn/category_api.php";


    ArrayList<SliderItem> adsArray=new ArrayList<>();
    SliderView adSlider;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView=root.findViewById(R.id.cat_recycler);

        getCategoriesFromServer();


        adSlider =root.findViewById(R.id.ads_slider);




        return root;
    }



    private void getCategoriesFromServer() {
        StringRequest request=new StringRequest(Request.Method.POST, cat_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){

                        arrayList.clear();
                        JSONArray jsonArray=jsonObject.getJSONArray("category");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object=jsonArray.getJSONObject(i);

                            CategoryModel categoryModel=new CategoryModel();
                            categoryModel.setCat_id(object.getString("cat_id"));
                            categoryModel.setCat_name(object.getString("cat_name"));
                            categoryModel.setCat_pic(object.getString("cat_pic"));
                            arrayList.add(categoryModel);
                        }
                        adapter=new CategoryAdapter(getContext(), arrayList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerView.setAdapter(adapter);

                        //ads

                        adsArray.clear();
                        JSONArray jsonArray2=jsonObject.getJSONArray("ads");
                        for (int i=0; i<jsonArray2.length(); i++){
                            JSONObject object=jsonArray2.getJSONObject(i);

                            SliderItem sliderItem=new SliderItem();
                            sliderItem.setAd_id(object.getString("ad_id"));
                            sliderItem.setAd_img(object.getString("ad_img"));
                            sliderItem.setAd_link(object.getString("ad_link"));

                            adsArray.add(sliderItem);
                        }
                       // SliderImgAdapter adapter = new SliderImgAdapter(getContext(), adsArray);
                         imgAdapter=new SliderImgAdapter(getContext(),adsArray);
                        adSlider.setSliderAdapter(imgAdapter);

                        adSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        adSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        adSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        adSlider.setIndicatorSelectedColor(Color.WHITE);
                        adSlider.setIndicatorUnselectedColor(Color.GRAY);
                        adSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                        adSlider.startAutoCycle();

                        //end of ads


                    }else {
                        Toast.makeText(getContext(), "Invalid Request", Toast.LENGTH_SHORT).show();
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
                cat.put("package", getActivity().getPackageName()); //com.example.shopunlimited
                return cat;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


