package com.hasanin.hossam.photosorganiser.ShowImages;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    String frag;
    ImagesFragmentsListener imagesFragmentsListener;
    int future_pos;
    ArrayList<String> checked = new ArrayList<String>();;

    public ImageRecAdapter(ArrayList<ImagesRecModel> ImageRec , Activity context , ImagesFragmentsListener imagesFragmentsListener , String frag , int future_pos){
        this.context = context;
        this.imageRec = ImageRec;
        this.frag = frag;
        this.imagesFragmentsListener = imagesFragmentsListener;
        this.future_pos = future_pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View the_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card  , null);
        ViewHolder view = new ViewHolder(the_layout);
        return view;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (frag == "Main") {
            holder.stored_image_card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    imagesFragmentsListener.MoveToFragment("Delete" , position);
                    return false;
                }
            });
        } else if (frag == "Delete"){
            holder.stored_checked_image.setVisibility(View.VISIBLE);
            if (position == future_pos){
                holder.stored_checked_image.setChecked(true);
                holder.stored_image_card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.checked));
                checked.add(Integer.toString(future_pos));
                Toast.makeText(context , "s "+ checked.size() , Toast.LENGTH_SHORT).show();
                Toast.makeText(context , "f "+ future_pos , Toast.LENGTH_SHORT).show();
            }
            holder.stored_checked_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        checked.add(Integer.toString(position));
                        Toast.makeText(context , "p "+ position , Toast.LENGTH_SHORT).show();
                        Toast.makeText(context , "s "+ checked.size() , Toast.LENGTH_SHORT).show();
                        holder.stored_image_card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.checked));
                    } else{
                        Toast.makeText(context , "pbf "+ position , Toast.LENGTH_SHORT).show();
                        checked.remove(Integer.toString(position));
                        Toast.makeText(context , "paf "+ position , Toast.LENGTH_SHORT).show();
                        holder.stored_image_card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.checked));
                        if (checked.size() == 0){
                            imagesFragmentsListener.MoveToFragment("Main" , 0);
                        }
                    }
                }
            });
            holder.stored_image_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.stored_checked_image.isChecked()) {
                        holder.stored_checked_image.setChecked(false);
                    }else {
                        holder.stored_checked_image.setChecked(true);
                    }
                }
            });
        }
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
