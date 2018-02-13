package com.hasanin.hossam.photosorganiser.ShowImages;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hasanin.hossam.photosorganiser.Helper.helpers;
import com.hasanin.hossam.photosorganiser.ImagesFragments.ShowImagesFragment;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.MainActivity;
import com.hasanin.hossam.photosorganiser.R;
import com.sdsmdg.tastytoast.TastyToast;

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
    public ArrayList<String> checked = new ArrayList<String>();
    IndexingDB indexingDB;

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
            indexingDB = new IndexingDB(context);
            holder.stored_image_card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    imagesFragmentsListener.MoveToFragment("Delete" , position);
                    return false;
                }
            });
            holder.stored_image_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, imageRec.get(position).image));
                    }catch (Exception e){
                        //IndexingDB indexingDB = new IndexingDB(context);
                        indexingDB.DeleteImage(String.valueOf(imageRec.get(position).id));
                        imageRec.remove(position);
                        notifyItemRemoved(position);
                        TastyToast.makeText(context , "This image doesn't exist" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
                    }
                }
            });
            holder.image_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    View popup_options = LayoutInflater.from(context).inflate(R.layout.image_options , null);
                    ad.setView(popup_options);
                    final AlertDialog popup = ad.show();
                    TextView delete = (TextView) popup_options.findViewById(R.id.delete_image_option);
                    TextView edit = (TextView) popup_options.findViewById(R.id.edit_image_option);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //IndexingDB indexingDB = new IndexingDB(context);
                            AlertDialog.Builder al = new helpers().AlertMessage(context , "Are you sure you want to delete the image ?" , "Delete image" , R.drawable.ic_delete_basket_black);
                            al.setPositiveButton("Yes" , new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    indexingDB.DeleteImage(Integer.toString(imageRec.get(position).id));
                                    imageRec.remove(position);
                                    notifyItemRemoved(position);
                                    TastyToast.makeText(context , "Deleted successfully!" , TastyToast.LENGTH_SHORT , TastyToast.SUCCESS);
                                }
                            });
                            al.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TastyToast.makeText(context , "Nothing deleted !" , TastyToast.LENGTH_SHORT , TastyToast.CONFUSING);
                                }
                            });
                            al.show();
                            popup.dismiss();
                            if (imageRec.size() == 0){
                                context.startActivity(new Intent(context , MainActivity.class));
                                context.finish();
                            }
                        }
                    });
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TastyToast.makeText(context , "Edit the name" , TastyToast.LENGTH_SHORT , TastyToast.INFO);
                            popup.dismiss();
                            editImage(imageRec.get(position).image_name , String.valueOf(imageRec.get(position).id) , position , imageRec);

                        }
                    });
                }
            });
        } else if (frag == "Delete"){
            holder.stored_checked_image.setVisibility(View.VISIBLE);
            if (position == future_pos){
                holder.stored_checked_image.setChecked(true);
                holder.stored_image_card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.checked));
                checked.add(Integer.toString(future_pos));
            }
            holder.stored_checked_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        checked.add(Integer.toString(position));
                        holder.stored_image_card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.checked));
                    } else{
                        checked.remove(Integer.toString(position));
                        holder.stored_image_card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.checked));
                        if (checked.size() == 0){
                            imagesFragmentsListener.MoveToFragment("Main" , position);
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
        // Get the real uri from content uri
        ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(imageRec.get(position).image, projection, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String imagePath = cur.getString(0);
                System.out.println(imagePath);
                if (new File(imagePath).exists()) {
                    Glide.with(context).load(imageRec.get(position).image).into(holder.stored_image);
                    //holder.stored_image.setImageURI(imageRec.get(position).image);
                    holder.stored_image_name.setText(imageRec.get(position).image_name);
                }
            } else {
                Glide.with(context).load(R.drawable.image_not_found).into(holder.stored_image);
                holder.stored_image_name.setText("Image not found");
                //IndexingDB indexingDB = new IndexingDB(context);
                indexingDB.DeleteImage(String.valueOf(imageRec.get(position).id));
            }
            cur.close();
        }

    }

    String finalImageName;

    public void editImage(final String currentImageName , final String id , final int pos , final ArrayList<ImagesRecModel> imageRec){
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        View editImage = LayoutInflater.from(context).inflate(R.layout.edit_image , null);
        ad.setView(editImage);
        final AlertDialog popup = ad.show();
        final TextView currentName = (TextView) editImage.findViewById(R.id.currentImageNmae);
        final EditText newName = (EditText) editImage.findViewById(R.id.newImageName);
        Button saveChanges = (Button) editImage.findViewById(R.id.editImageName);
        currentName.setText(currentImageName);
        finalImageName = currentImageName;
        newName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //String newImageName = newName.getText().toString();
                currentName.setText(s.toString());
                finalImageName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpers.storingImageRestrictions(context , finalImageName)){
                    indexingDB.updateImageName(id , finalImageName);
                    imageRec.set(pos , new ImagesRecModel(imageRec.get(pos).image , finalImageName , Integer.parseInt(id)));
                    notifyItemChanged(pos);
                }
                popup.dismiss();
            }
        });
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
