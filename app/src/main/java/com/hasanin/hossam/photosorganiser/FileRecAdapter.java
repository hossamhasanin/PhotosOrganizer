package com.hasanin.hossam.photosorganiser;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mohamed on 09/11/2017.
 */

public class FileRecAdapter extends RecyclerView.Adapter<FileRecAdapter.ViewHolder> {

    public static ArrayList<FilesRec> filesRec;
    public static Activity context;
    // this var to take the position of the checkbox that should be checked in the DeleteFolderActivity
    public static ArrayList future_positions;
    public static String frag;
    public static FragmentsListener fragmentsListener;

    public FileRecAdapter(ArrayList filesRec , Activity context , ArrayList future_positions , String frag , FragmentsListener fragmentsListener){
        this.filesRec = filesRec;
        this.context = context;
        this.future_positions = future_positions;
        this.frag = frag;
        this.fragmentsListener = fragmentsListener;
    }

    public static FileRecAdapter fileRecAdapter;

    public static synchronized FileRecAdapter getInstance(){
        if (fileRecAdapter == null)
            fileRecAdapter = new FileRecAdapter(filesRec , context , future_positions , frag , fragmentsListener);
        return fileRecAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View the_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_card , null);
        ViewHolder view = new ViewHolder(the_layout);
        return view;
    }

    public AlertDialog.Builder popupmess;
    public EditText folder_name;
    public AlertDialog ad;
    public IndexingDB indexingDB;
    public ArrayList ch = new ArrayList();
    // this var is used as flag to ensure if the user has used long click so that make checkboxes visible
    public String g;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Animation folder_jump = AnimationUtils.loadAnimation(context , R.anim.folder_jumpup);
        final FileRecAdapter fileRecAdapter = this;
        if (position == 0 && frag == "Main") {
            holder.file_im.setImageResource(R.drawable.new_folder_light_blue);
            holder.file_name.setText("New");
            holder.file_im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context , "Create new directory" , Toast.LENGTH_SHORT).show();
                    holder.file_im.startAnimation(folder_jump);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            new helpers().CreateNewFolder(context , filesRec , fileRecAdapter , false , (BottomNavigationView) null);
                        }
                    }, 300);
                }
            });
        }else {
            if (frag == "Edit" || frag == "Delete"){
                holder.delete_folder.setVisibility(View.VISIBLE);
                if (Integer.toString(position) == future_positions.get(0) && future_positions.get(0) != null && frag == "Edit" && future_positions.size() == 1){
                    holder.delete_folder.setChecked(true);
                    ch.add(future_positions.get(0));
                    Toast.makeText(context, "Size "+ch.size(), Toast.LENGTH_SHORT).show();
                }
                if (frag == "Delete" && future_positions.size() == 2){
                    if (Integer.toString(position) == future_positions.get(0)) {
                        holder.delete_folder.setChecked(true);
                        ch.add(future_positions.get(0));
                    } else if (Integer.toString(position) == future_positions.get(1)){
                        holder.delete_folder.setChecked(true);
                        ch.add(future_positions.get(1));
                    }
                    Toast.makeText(context, "Size "+ch.size(), Toast.LENGTH_SHORT).show();
                }
                holder.delete_folder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true){
                            ch.add(Integer.toString(position));
                            Toast.makeText(context, "Element " + position + " is checked "+ ch.size(), Toast.LENGTH_SHORT).show();
                            if (frag == "Edit" && ch.size() == 2){
                                fragmentsListener.OnMoveToListener(1 , ch);
                            }
                        } else if (isChecked == false) {
                            ch.remove(Integer.toString(position));
                            //Toast.makeText(context , "Element "+position+" is unchecked" , Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context , "s = "+ch.size() , Toast.LENGTH_SHORT).show();
                            if (frag == "Delete" && ch.size() == 1){
                                //context.onBackPressed();
                                new helpers().MoveTo(context , "Edit" , ch.get(0).toString());
                            }
                            if (ch.size() == 0){
                                //context.onBackPressed();
                                new helpers().MoveTo(context , "Main" , null);
                            }
                        }
                    }
                });
            }
            if (frag == "Main") {
                holder.folder_card.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //g = 1;
                        //holder.delete_folder.setChecked(true);
                        //notifyDataSetChanged();
                        fragmentsListener.OnPositionListener(position-1);
                        return false;
                    }
                });

//                if (g == 1) {
//                    holder.delete_folder.setVisibility(View.VISIBLE);
//                }
                holder.folder_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.file_im.startAnimation(folder_jump);
                    }
                });
            }
            holder.file_im.setImageResource(filesRec.get(position).file_im);
            holder.file_name.setText(filesRec.get(position).file_name);
        }
    }

    public void DeleteFolder() {
        indexingDB = new IndexingDB(context);
        for (int i = 0; i < ch.size(); i++) {
            Integer p = Integer.parseInt(ch.get(i).toString());
            String go = filesRec.get(p).file_name;
            try {
                indexingDB.DeleteFolders(go);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            Toast.makeText(context, go, Toast.LENGTH_SHORT).show();
        }
        if (ch.size() != filesRec.size()) {
            new helpers().MoveTo(context, "Edit", null);
        } else {
            new helpers().MoveTo(context , "Main" , null);
        }
    }
    @Override
    public int getItemCount() {
        return filesRec.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView file_im;
        public TextView file_name;
        public RelativeLayout folder_card;
        public CheckBox delete_folder;
        public ViewHolder(View itemView) {
            super(itemView);
            folder_card = (RelativeLayout) itemView.findViewById(R.id.folder_card);
            delete_folder = (CheckBox) itemView.findViewById(R.id.delete_folder);
            file_im = (ImageView) itemView.findViewById(R.id.files);
            file_name = (TextView) itemView.findViewById(R.id.file_name);
        }
    }


}
