package com.hasanin.hossam.photosorganiser.MainFoldersFragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FileRecAdapter;
import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FilesRec;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.Helper.helpers;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mohamed on 03/12/2017.
 */

public class DeleteFoldersFragment extends Fragment {

    public RecyclerView show_files;
    ArrayList<FilesRec> filesRec;
    GridLayoutManager gridLayoutManager;
    FoldersFragmentsListener foldersFragmentsListener;
    ArrayList future_positions;
    FileRecAdapter fileRecAdapter;
    IndexingDB indexingDB;

    public void setFuture_positions(ArrayList future_positions){
        this.future_positions = future_positions;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_folders , container , false);
        this.setHasOptionsMenu(true);
        //getActivity().getActionBar().setTitle("Delete");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (Locale.getDefault().getDisplayLanguage().equals("English") || Locale.getDefault().getDisplayLanguage().equals(Locale.ENGLISH)){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        }else {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_right);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity() , "kokokoo" , Toast.LENGTH_LONG).show();
                //getActivity().onBackPressed();
                new helpers().MoveTo(getActivity() , "Main" , null);
            }
        });

        show_files = (RecyclerView) view.findViewById(R.id.show_files);
        indexingDB = new IndexingDB(getActivity());
        ArrayList<FoldersModel> all_folders = indexingDB.GetAllFolders();

        filesRec = new ArrayList();
        //filesRec.add(new FilesRec(0 , ""));
        for (Integer i=0;i<all_folders.size();i++){
            filesRec.add(new FilesRec(all_folders.get(i).icon , all_folders.get(i).icon_name , all_folders.get(i).id , all_folders.get(i).pass));
        }
        fileRecAdapter = new FileRecAdapter(filesRec , getActivity() , future_positions , "Delete" , foldersFragmentsListener);
        show_files.setAdapter(fileRecAdapter);
        if(getActivity().getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity() , 4);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity() , 5);
        }
        show_files.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.scrollToPosition(Integer.parseInt(future_positions.get(0).toString()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Delete");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete_folder_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.delete_folder_dmenu) {
                int folders_are_full = 0;
                for (int i=0;i<fileRecAdapter.ch.size();i++){
                    int p = Integer.parseInt(fileRecAdapter.ch.get(i).toString());
                    ArrayList<ImagesRecModel> folder_is_empty = indexingDB.GetAllImages(Integer.toString(filesRec.get(p).id));
                    if (folder_is_empty.size() > 0){
                        folders_are_full = 1;
                        break;
                    }
                }
                if (folders_are_full == 1){
                    AlertDialog.Builder al = new helpers().AlertMessage(getActivity() , "Are you sure you want to delete ? there is folder contains images" , "Delete folders" , R.drawable.ic_delete_basket_black);
                    al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fileRecAdapter.DeleteFolder();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TastyToast.makeText(getActivity(), "Don't play with me i am confused !", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                        }
                    }).show();
                } else {
                    AlertDialog.Builder al = new helpers().AlertMessage(getActivity() , "Are you sure you want to delete ?" , "Delete folders" , R.drawable.ic_delete_basket_black);
                    al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fileRecAdapter.DeleteFolder();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TastyToast.makeText(getActivity(), "Don't play with me i am confused !", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                        }
                    }).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }


}
