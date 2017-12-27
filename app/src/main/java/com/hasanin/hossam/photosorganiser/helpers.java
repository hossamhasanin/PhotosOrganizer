package com.hasanin.hossam.photosorganiser;

import android.app.Activity;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mohamed on 09/12/2017.
 */

public class helpers {
    public AlertDialog.Builder popupmess;
    public EditText folder_name;
    public AlertDialog ad;
    public IndexingDB indexingDB;

    public void CreateNewFolder (final Activity context , final ArrayList filesRec , final FileRecAdapter fileRecAdapter , final boolean bottombar , final BottomNavigationView bottomNavigationView){
        popupmess = new AlertDialog.Builder(context);
        View poplayout = LayoutInflater.from(context).inflate(R.layout.create_folder_mess , null);
        popupmess.setView(poplayout);
        ad = popupmess.show();
        folder_name = (EditText) poplayout.findViewById(R.id.pop_folder_name);
        Button create_folder = (Button) poplayout.findViewById(R.id.pop_create_folder);
        indexingDB = new IndexingDB(context);
        create_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!folder_name.getText().toString().isEmpty()){
                    String fn = folder_name.getText().toString();
                    Integer fp = filesRec.size();
                    Toast.makeText(context , fn , Toast.LENGTH_LONG).show();
                    indexingDB.InsertNewFolder(fn);
                    filesRec.add(fp ,new FilesRec(R.drawable.if_folder_orange_54541 , fn));
                    // To add the new item to the list to make it show
                    fileRecAdapter.notifyItemInserted(fp);
                }
                ad.dismiss();
                if (bottombar) {
                    bottomNavigationView.setSelectedItemId(R.id.main_bar_item);
                }
            }
        });
    }

    public void MoveTo(Activity context , String frag , String data){
        if (frag == "Main"){
            ShowFoldersFragment showFoldersFragment = new ShowFoldersFragment();
            context.getFragmentManager().beginTransaction().replace(R.id.lists_container , showFoldersFragment).addToBackStack(null).commit();
        } else if (frag == "Edit"){
            EditFoldersFragment editFoldersFragment = new EditFoldersFragment();
            ArrayList ps = new ArrayList();
            ps.add(data);
            editFoldersFragment.setFuturePositions(ps);
            context.getFragmentManager().beginTransaction().replace(R.id.lists_container , editFoldersFragment).addToBackStack(null).commit();

        }
    }


}
