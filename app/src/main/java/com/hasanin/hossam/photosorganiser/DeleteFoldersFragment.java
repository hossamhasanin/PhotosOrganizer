package com.hasanin.hossam.photosorganiser;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.SpinnerModel;

import java.util.ArrayList;

/**
 * Created by mohamed on 03/12/2017.
 */

public class DeleteFoldersFragment extends Fragment {

    public RecyclerView show_files;
    ArrayList<FilesRec> filesRec;
    GridLayoutManager gridLayoutManager;
    FragmentsListener fragmentsListener;
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
        toolbar.setNavigationIcon(android.R.drawable.ic_delete);
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
            filesRec.add(new FilesRec(all_folders.get(i).icon , all_folders.get(i).icon_name));
        }
        fileRecAdapter = new FileRecAdapter(filesRec , getActivity() , future_positions , "Delete" , fragmentsListener );
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
        switch (item_id) {
            case R.id.delete_folder_dmenu:
                fileRecAdapter.DeleteFolder();
        }

        return super.onOptionsItemSelected(item);
    }


}
