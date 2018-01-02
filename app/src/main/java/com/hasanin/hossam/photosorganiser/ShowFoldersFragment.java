package com.hasanin.hossam.photosorganiser;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.SpinnerModel;

import java.util.ArrayList;

/**
 * Created by mohamed on 16/11/2017.
 */

public class ShowFoldersFragment extends Fragment {

    public RecyclerView show_files;
    ArrayList<FilesRec> filesRec;
    GridLayoutManager gridLayoutManager;
    FragmentsListener fragmentsListener;
    IndexingDB indexingDB;
    FileRecAdapter fileRecAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_folders , container , false);
        this.setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(null);
        show_files = (RecyclerView) view.findViewById(R.id.show_files);
        indexingDB = new IndexingDB(getActivity());
        ArrayList<FoldersModel> all_folders = indexingDB.GetAllFolders();
        filesRec = new ArrayList();
        filesRec.add(new FilesRec(0 , ""));
        for (Integer i=0;i<all_folders.size();i++){
            filesRec.add(new FilesRec(all_folders.get(i).icon , all_folders.get(i).icon_name));
        }
        ArrayList positions = new ArrayList();
        positions.add(Integer.toString(0));
        fileRecAdapter = new FileRecAdapter(filesRec , getActivity() , positions , "Main" , fragmentsListener);
        show_files.setAdapter(fileRecAdapter);
        if(getActivity().getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity() , 4);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity() , 5);
        }
        show_files.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.cam){
            Toast.makeText(getActivity() , "Will take a photo" , Toast.LENGTH_LONG).show();
        }
        if (item_id == R.id.new_folder){
            Toast.makeText(getActivity() , "Will make new folder" , Toast.LENGTH_LONG).show();
            helpers helpers = new helpers();
            helpers.CreateNewFolder(getActivity() , filesRec , fileRecAdapter , false , (BottomNavigationView) null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentsListener = (FragmentsListener) context;
        }catch (Exception e){}
    }
}
