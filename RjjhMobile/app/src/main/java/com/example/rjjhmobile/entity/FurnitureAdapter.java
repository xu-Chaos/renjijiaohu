package com.example.rjjhmobile.entity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rjjhmobile.R;
import com.example.rjjhmobile.painter.RoomDesigner;

import java.util.List;

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.ViewHolder>{
    private List<Furniture> itemList;
    private RoomDesigner roomDesigner;
    private Context context;

    public FurnitureAdapter(List<Furniture> itemList) {
        this.itemList = itemList;
    }

    public void setAttribute(RoomDesigner roomDesigner, Context context){
        this.roomDesigner = roomDesigner;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemSize;
        ImageView itemImage;
        ImageButton itemButton;
        public ViewHolder(View view){
            super(view);
            itemName = view.findViewById(R.id.item_name);
            itemSize = view.findViewById(R.id.item_size);
            itemImage = view.findViewById(R.id.item_image);
            itemButton = view.findViewById(R.id.item_button);
        }
    }

    @NonNull
    @Override
    public FurnitureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FurnitureAdapter.ViewHolder holder, int position) {
        Furniture item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemSize.setText(item.getWidth()+"x"+item.getHeight());
        holder.itemImage.setImageBitmap(item.getImg());
        holder.itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomDesigner.createObj(item.getClassID(),context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
