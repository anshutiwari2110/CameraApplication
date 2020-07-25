package com.example.cameraapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {
    Context context;
    private ArrayList<Bitmap> images;
    public GalleryAdapter (Context context){
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryHolder(LayoutInflater.from(context).inflate(R.layout.cell_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {

        Bitmap currentImage = images.get(position);

        holder.mIvCellPhoto.setImageBitmap(currentImage);
    }

    @Override
    public int getItemCount() {
        return images!=null ?images.size():0;
    }

    public class GalleryHolder extends RecyclerView.ViewHolder{
        private ImageView mIvCellPhoto;
        public GalleryHolder(@NonNull View itemView) {
            super(itemView);

            mIvCellPhoto = itemView.findViewById(R.id.iv_cell_image);
        }
    }
}
