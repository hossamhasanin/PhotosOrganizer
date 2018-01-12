package com.hasanin.hossam.photosorganiser.ShowImages;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hasanin.hossam.photosorganiser.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mohamed on 04/01/2018.
 */

public class ImageRecAdapter extends RecyclerView.Adapter<ImageRecAdapter.ViewHolder> {
    Activity context;
    ArrayList<ImagesRecModel> imageRec;

    public ImageRecAdapter(ArrayList<ImagesRecModel> ImageRec , Activity context){
        this.context = context;
        this.imageRec = ImageRec;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View the_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card  , null);
        ViewHolder view = new ViewHolder(the_layout);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(imageRec.get(position).image).into(holder.stored_image);
        //holder.stored_image.setImageURI(imageRec.get(position).image);
        holder.stored_image_name.setText(imageRec.get(position).image_name);
    }

    @Override
    public int getItemCount() {
        return imageRec.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView stored_image_card;
        ImageView stored_image;
        TextView stored_image_name;
        FloatingActionButton image_options;
        CheckBox stored_checked_image;
        public ViewHolder(View itemView) {
            super(itemView);
            stored_image_card = (CardView) itemView.findViewById(R.id.stored_image_card);
            stored_image = (ImageView) itemView.findViewById(R.id.stored_image);
            stored_image_name = (TextView) itemView.findViewById(R.id.stored_image_name);
            image_options = (FloatingActionButton) itemView.findViewById(R.id.image_options);
            stored_checked_image = (CheckBox) itemView.findViewById(R.id.stored_checked_image);
        }
    }

}
