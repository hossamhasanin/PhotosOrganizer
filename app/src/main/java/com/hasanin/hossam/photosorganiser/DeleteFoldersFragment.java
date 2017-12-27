package com.hasanin.hossam.photosorganiser;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import android.widget.Toast;

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
        ArrayList<String> all_folders = indexingDB.GetAllFolders();

        filesRec = new ArrayList();
        //filesRec.add(new FilesRec(0 , ""));
        for (Integer i=0;i<all_folders.size();i++){
            filesRec.add(new FilesRec(R.drawable.if_folder_orange_54541 , all_folders.get(i)));
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
                Toast.makeText(getActivity(), "Should delete "+fileRecAdapter.ch.size() , Toast.LENGTH_SHORT).show();
                //new DeleteFoldersTask().execute();
                //for (int i=1;i<3;i++){
                //int c = 0;
               // while (c < fileRecAdapter.ch.size()) {
                fileRecAdapter.DeleteFolder();
                 //   Toast.makeText(getActivity() , fileRecAdapter.ch.get(c).toString() , Toast.LENGTH_SHORT).show();
                   // c++;
                //}
//                int i = 0;
//                while (i < ps.size()){
//                    fileRecAdapter.filesRec.remove(ps.get(i));
//                    fileRecAdapter.notifyItemRemoved(ps.get(i));
//                    i++;
//                }
                //fileRecAdapter.ch.clear();
//                int f = 0;
//                while (f < ps.size()){
//                    fileRecAdapter.notifyItemRemoved(ps.get(f));
//                    f++;
//                }
                //}
        }

        return super.onOptionsItemSelected(item);
    }

    /*public class DeleteFoldersTask extends AsyncTask<String , String , String>{
        int p;
        String g;
        @Override
        protected String doInBackground(String... params) {

            for (int i=1;i<3;i++){
                p = Integer.parseInt(fileRecAdapter.ch.get(i).toString());
                g = filesRec.get(p).file_name;
                indexingDB.DeleteFolders(g);
                fileRecAdapter.ch.remove(i);
                filesRec.remove(p);
                fileRecAdapter.notifyItemRemoved(p);
            }
            return null;
        }
    }*/

}
