package com.example.shopunlimited;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.security.AccessControlContext;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>

{
    Context context;
    ArrayList<CategoryModel>arrayList;
    private String img_address="https://oakspro.com/projects/project39/rutuja/StroeUn/category_pics/";


    public CategoryAdapter(Context context, ArrayList<CategoryModel> arrayList) {
        this.context= context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType)
    {
   View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CategoryAdapter.ViewHolder holder, int position)
    {
        CategoryModel model=arrayList.get(position);
        holder.catName.setText(model.getCat_name());

        Picasso.get().load(img_address+model.getCat_pic()).into(holder.catImg);

        holder.catCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cat_id=model.getCat_id();
                Intent intent=new Intent(context, ProductActivity.class);
                intent.putExtra("catID", cat_id);
                context.startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView catImg;
        TextView catName;

        CardView catCard;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            catImg=itemView.findViewById(R.id.cat_img);
            catName=itemView.findViewById(R.id.cat_name);
            catCard=itemView.findViewById(R.id.card_item);
        }
    }
}
