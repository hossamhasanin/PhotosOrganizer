package com.hasanin.hossam.photosorganiser.MainFoldersFragments;

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

import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FileRecAdapter;
import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FilesRec;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.Helper.helpers;

import java.util.ArrayList;

/**
 * Created by mohamed on 17/11/2017.
 */

public class EditFoldersFragment extends Fragment {

    public RecyclerView show_files;
    ArrayList<FilesRec> filesRec;
    GridLayoutManager gridLayoutManager;
    FragmentsListener fragmentsListener;
    ArrayList future_positions;
    IndexingDB indexingDB;
    ArrayList<FoldersModel> all_folders;
    FileRecAdapter fileRecAdapter;

    public void setFuturePositions(ArrayList future_position){
        this.future_positions = future_position;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_folders , container , false);
        this.setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new helpers().MoveTo(getActivity() , "Main" , null);
            }
        });

        show_files = (RecyclerView) view.findViewById(R.id.show_files);
        indexingDB = new IndexingDB(getActivity());
        all_folders = indexingDB.GetAllFolders();
        filesRec = new ArrayList();
        //filesRec.add(new FilesRec(0 , ""));
        for (Integer i=0;i<all_folders.size();i++){
            filesRec.add(new FilesRec(all_folders.get(i).icon , all_folders.get(i).icon_name , all_folders.get(i).id));
        }
        fileRecAdapter = new FileRecAdapter(filesRec , getActivity() , future_positions , "Edit" , fragmentsListener);
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
        getActivity().setTitle("Edit");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_folder_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.delete_folder_emenu:
                int p = Integer.parseInt(fileRecAdapter.ch.get(0).toString());
                String g = filesRec.get(p).file_name;
                indexingDB.DeleteFolders(g , filesRec.get(p).id);
                fileRecAdapter.ch.remove(0);
                filesRec.remove(p);
                fileRecAdapter.notifyItemRemoved(p);
                Toast.makeText(getActivity(), "Deleted successfully !", Toast.LENGTH_SHORT).show();
            case R.id.edit_folder_name:
                int pos = Integer.parseInt(fileRecAdapter.ch.get(0).toString());
                new helpers().EditFolder(getActivity() ,filesRec, fileRecAdapter , filesRec.get(pos).file_name , filesRec.get(pos).file_im);
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
