package com.example.shopunlimited;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<ProductData>list;
    Context context;
    private String img_address="https://oakspro.com/projects/project39/rutuja/StroeUn/products_pics/";

    public ProductAdapter(ArrayList<ProductData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ProductAdapter.ViewHolder holder, int position) {
         ProductData productData=list.get(position);
        holder.prod_price.setText(productData.getProdPrice());
        holder.prod_name.setText(productData.getProdName());
        holder.prod_price.setText("Price: Rs. "+productData.getProdPrice());
        Picasso.get().load(img_address+productData.getProdImage()).into(holder.prod_img);

        holder.prodItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, pDetailsActivity.class);
                intent.putExtra("pid", productData.getProdID());
                intent.putExtra("cid", productData.getCat_id());
                intent.putExtra("pname", productData.getProdName());
                intent.putExtra("pprice", productData.getProdPrice());
                intent.putExtra("pdetails", productData.getProdDetails());
                intent.putExtra("pstock", productData.getProdStock());
                intent.putExtra("pseller", productData.getProdSeller());
                intent.putExtra("pimage", productData.getProdImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView prod_img;
        TextView prod_name,prod_price;
        LinearLayout prodItemLL;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            prod_img=itemView.findViewById(R.id.prod_img);
            prod_name=itemView.findViewById(R.id.prod_name);
            prod_price=itemView.findViewById(R.id.product_price);
            prodItemLL=itemView.findViewById(R.id.prod_itemll);
        }
    }
}
