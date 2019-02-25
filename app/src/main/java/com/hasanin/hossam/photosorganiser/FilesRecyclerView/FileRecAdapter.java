package com.hasanin.hossam.photosorganiser.FilesRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hasanin.hossam.photosorganiser.MainFoldersFragments.DeleteFoldersFragment;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.EditFoldersFragment;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.FoldersFragmentsListener;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.Helper.helpers;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;
import com.hasanin.hossam.photosorganiser.ShowImages.ShowImages;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohamed on 09/11/2017.
 */

public class FileRecAdapter extends RecyclerView.Adapter<FileRecAdapter.ViewHolder> {

    public static ArrayList<FilesRec> filesRec;
    public static Activity context;
    // this var to take the position of the checkbox that should be checked in the DeleteFolderActivity
    public static ArrayList future_positions;
    public static String frag;
    public static FoldersFragmentsListener foldersFragmentsListener;

    public FileRecAdapter(ArrayList filesRec , Activity context , ArrayList future_positions , String frag , FoldersFragmentsListener foldersFragmentsListener){
        this.filesRec = filesRec;
        this.context = context;
        this.future_positions = future_positions;
        this.frag = frag;
        this.foldersFragmentsListener = foldersFragmentsListener;
    }

    public static FileRecAdapter fileRecAdapter;

    public static synchronized FileRecAdapter getInstance(){
        if (fileRecAdapter == null)
            fileRecAdapter = new FileRecAdapter(filesRec , context , future_positions , frag , foldersFragmentsListener);
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
    public int g;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Animation folder_jump = AnimationUtils.loadAnimation(context , R.anim.folder_jumpup);
        final FileRecAdapter fileRecAdapter = this;
        if (position == 0 && frag == "Main") {
            holder.file_im.setImageResource(R.drawable.if_new_folder_red);
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
                }
                if (frag == "Delete" && future_positions.size() == 2){
                    if (Integer.toString(position) == future_positions.get(0)) {
                        holder.delete_folder.setChecked(true);
                        ch.add(future_positions.get(0));
                    } else if (Integer.toString(position) == future_positions.get(1)){
                        holder.delete_folder.setChecked(true);
                        ch.add(future_positions.get(1));
                    }
                }
                holder.delete_folder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true){
                            ch.add(Integer.toString(position));
                            if (frag == "Edit" && ch.size() == 2){
                                if ((int) Build.VERSION.SDK_INT >= 23){
                                    foldersFragmentsListener.OnMoveToListener(1 , ch);
                                } else {
                                    DeleteFoldersFragment deleteFoldersFragment = new DeleteFoldersFragment();
                                    deleteFoldersFragment.setFuture_positions(ch);
                                    context.getFragmentManager().beginTransaction().replace(R.id.lists_container , deleteFoldersFragment).addToBackStack(null).commit();

                                }
                            }
                        } else if (isChecked == false) {
                            ch.remove(Integer.toString(position));
                            if (frag == "Delete" && ch.size() == 1){
                                new helpers().MoveTo(context , "Edit" , ch.get(0).toString());
                            }
                            if (ch.size() == 0){
                                new helpers().MoveTo(context , "Main" , null);
                            }
                        }
                    }
                });

                holder.folder_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.delete_folder.isChecked()){
                            holder.delete_folder.setChecked(false);
                        } else {
                            holder.delete_folder.setChecked(true);
                        }
                    }
                });
            }
            if (frag == "Main") {
                holder.folder_card.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // i sent the position-1 because there is a place had been taken for (create new folder)
                        //Toast.makeText(context , String.valueOf(position) , Toast.LENGTH_LONG).show();
                        if ((int) Build.VERSION.SDK_INT >= 23){
                            foldersFragmentsListener.OnPositionListener(position-1);
                        } else {
                            holder.delete_folder.setChecked(true);
                        }

                        return false;
                    }
                });
                if ((int) Build.VERSION.SDK_INT < 23){
                    holder.delete_folder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                EditFoldersFragment editFoldersFragment = new EditFoldersFragment();
                                ArrayList positions = new ArrayList();
                                positions.add(Integer.toString(position-1));
                                editFoldersFragment.setFuturePositions(positions);
                                context.getFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragments_fade_in , R.animator.fragments_fade_out).replace(R.id.lists_container , editFoldersFragment).addToBackStack(null).commit();
                            }
                        }
                    });
                }

                holder.folder_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.file_im.startAnimation(folder_jump);
//                        try {
//                            Thread.sleep(350);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        if((int) Build.VERSION.SDK_INT >= 23){
                            if (ActivityCompat.checkSelfPermission(context , android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                g = position;
                                ActivityCompat.requestPermissions(context ,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE} , 400);
                            } else {
                                openTheFolder(position);
                            }
                        } else {
                            openTheFolder(position);
                        }
                    }
                });
            }
            holder.file_im.setImageResource(filesRec.get(position).file_im);
            if (!filesRec.get(position).pass.isEmpty()){
                holder.file_name.setText(filesRec.get(position).file_name + " \uD83D\uDD12");
            } else {
                holder.file_name.setText(filesRec.get(position).file_name);
            }
        }
    }

    EditText password;
    public void openTheFolder(final int position){
        indexingDB = new IndexingDB(context);
        final ArrayList<ImagesRecModel> folder_is_empty = indexingDB.GetAllImages(Integer.toString(filesRec.get(position).id));
        if (!filesRec.get(position).pass.isEmpty()) {
            popupmess = new AlertDialog.Builder(context);
            View poplayout = LayoutInflater.from(context).inflate(R.layout.get_password, null);
            popupmess.setView(poplayout);
            ad = popupmess.show();
            password = (EditText) poplayout.findViewById(R.id.pop_folder_pass);
            //access = false;
            Button accept = (Button) poplayout.findViewById(R.id.pop_accept);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pass = password.getText().toString();
                    if (folder_is_empty.size() > 0 && new helpers().checkPassword(context, pass, filesRec.get(position).id)) {
                        ad.dismiss();
                        Intent intent = new Intent(context, ShowImages.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("folder_id", filesRec.get(position).id);
                        bundle.putString("folder_name", filesRec.get(position).file_name);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        context.finish();
                    } else {
                        Toast.makeText(context, "There is no images inside!", Toast.LENGTH_LONG).show();
                        ad.dismiss();
                    }
                }
            });
        } else {
            if (folder_is_empty.size() > 0) {
                Intent intent = new Intent(context, ShowImages.class);
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", filesRec.get(position).id);
                bundle.putString("folder_name", filesRec.get(position).file_name);
                intent.putExtras(bundle);
                context.startActivity(intent);
                context.finish();
            } else {
                Toast.makeText(context , "There is no images inside!" , Toast.LENGTH_LONG).show();
            }
        }
    }

    public void DeleteFolder() {
        indexingDB = new IndexingDB(context);
        for (int i = 0; i < ch.size(); i++) {
            Integer p = Integer.parseInt(ch.get(i).toString());
            String go = filesRec.get(p).file_name;
            try {
                indexingDB.DeleteFolders(go , filesRec.get(p).id);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
        TastyToast.makeText(context, "Deleted successfully , don't regret now !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
        public LinearLayout folder_card;
        public CheckBox delete_folder;
        public ViewHolder(View itemView) {
            super(itemView);
            folder_card = (LinearLayout) itemView.findViewById(R.id.folder_card);
            delete_folder = (CheckBox) itemView.findViewById(R.id.delete_folder);
            file_im = (ImageView) itemView.findViewById(R.id.files);
            file_name = (TextView) itemView.findViewById(R.id.file_name);
        }
    }


}
