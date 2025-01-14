package com.example.tyminiproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView OrderItemName, OrderMessName, OrderItemPrice, OrderMobNo, OrderId, tv_userType, tv_Qty;
    public ImageView orderItemImg;
    public ItemClickListner itemClickListner;


    public OrderViewHolder(View itemView) {
        super(itemView);

        tv_userType = itemView.findViewById(R.id.tv_userType);
        OrderItemName = itemView.findViewById(R.id.OrderItemName);
        OrderMessName = itemView.findViewById(R.id.OrderMessName);
        OrderItemPrice = itemView.findViewById(R.id.OrderItemPrice);
        OrderMobNo = itemView.findViewById(R.id.orderMobNo);
        OrderId = itemView.findViewById(R.id.orderId);
        tv_Qty = itemView.findViewById(R.id.tv_Qty);
        orderItemImg = itemView.findViewById(R.id.orderFood_img);

        //  itemView.setOnClickListener(this);

    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
//        itemClickListner.onClick(view, getAdapterPosition(), true);
    }

}