package com.example.hawi.medmarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.hawi.medmarket.assest.isFav;
import com.example.hawi.medmarket.assest.isShipping;
import com.example.hawi.medmarket.medvolley.AppController;
import com.example.hawi.medmarket.medvolley.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawi on 31/01/18.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private List<items> items = new ArrayList<items>();
    private ImageLoader imageLoader = AppController.getmInstance().getmImageLoader();
    private int uID = SharedManager.getUserID();
    private Context mCnt ;

    public RecycleAdapter(Activity activity, List<items> item,Context context) {
        this.activity = activity;
        this.items = item;
        this.mCnt = context;
    }

    public RecycleAdapter(){}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,null);
        ViewHolder viewHolder =  new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(imageLoader == null){
            imageLoader = AppController.getmInstance().getmImageLoader();
        }
        holder.img.setImageUrl(Constant.URL_INV_IMG+items.get(position).getImg(),imageLoader);

        holder.details.setText(items.get(position).getDesc());

        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.valueOf(items.get(position).getPrice()));

        holder.qtyUnit.setText(items.get(position).getQtyUnit());

        if(items.get(position).getIsShop() == 0){
            holder.shop.setImageResource(R.drawable.ic_shopping);
        }else{
            holder.shop.setImageResource(R.drawable.ic_shop_remove);
            countShop += 1;
            getShopMenu();
        }

        if(items.get(position).getIsFav() == 0){
            holder.fav.setImageResource(R.drawable.ic_fav);
        }else{
            holder.fav.setImageResource(R.drawable.ic_fav_red);
            countFav = countFav + 1 ;
            getFavMenu();
        }



        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items.get(position).getIsFav() == 0){
                    holder.fav.setImageResource(R.drawable.ic_fav_red);
                    items.get(position).setIsFav(1);
                    countFav = countFav + 1 ;
                    getFavMenu();
                    new isFav(uID,items.get(position).getId(),1);
                }else if (items.get(position).getIsFav() == 1){
                    holder.fav.setImageResource(R.drawable.ic_fav);
                    items.get(position).setIsFav(0);
                    countFav = countFav - 1;
                    getFavMenu();
                    new isFav(uID,items.get(position).getId(),0);
                }
            }
        });

        holder.shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items.get(position).getIsShop() == 0){
                    holder.shop.setImageResource(R.drawable.ic_shop_remove);
                    items.get(position).setIsShop(1);
                    countShop += 1;
                    getShopMenu();
                    new isShipping(uID,items.get(position).getId(),1,mCnt);
                }else if (items.get(position).getIsShop() == 1){
                    holder.shop.setImageResource(R.drawable.ic_shopping);
                    items.get(position).setIsShop(0);
                    countShop -= 1;
                    getShopMenu();
                    new isShipping(uID,items.get(position).getId(),0,mCnt);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","items id: "+items.get(position).getId());
                Intent intent = new Intent(activity,ItemDetailsActivity.class);
                intent.putExtra("id",items.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    private int countFav = 0;

    private void getFavMenu(){
        if(countFav == 0){
            RecycleActivity.changeFavBlack();
        }else{
            RecycleActivity.changeFavRed();
        }
    }

    private int countShop = 0;

    private void getShopMenu(){
        if(countShop == 0){
            RecycleActivity.changeShopBlack();
        }else{
            RecycleActivity.changeShopRed();
        }
    }

    @Override
    public int getItemCount() {
        //return items.size();
        return (null != items ? items.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView img;
        private ImageView fav,shop ;
        private TextView name,price,details,qtyUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (NetworkImageView) itemView.findViewById(R.id.itemImg);
            fav = (ImageView) itemView.findViewById(R.id.itemFav);
            shop = (ImageView) itemView.findViewById(R.id.itemShopping);

            name = (TextView) itemView.findViewById(R.id.itemName);
            price =  (TextView) itemView.findViewById(R.id.itemPrice);

            qtyUnit = (TextView) itemView.findViewById(R.id.itemsQtyUnits);
            details =  (TextView) itemView.findViewById(R.id.itemDetail);
        }
    }
}
