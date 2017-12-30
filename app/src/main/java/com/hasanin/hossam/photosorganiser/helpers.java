package com.hasanin.hossam.photosorganiser;

import android.app.Activity;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hasanin.hossam.photosorganiser.PopUpSpinner.PopUpSpinnerArrayAdapter;
import com.hasanin.hossam.photosorganiser.PopUpSpinner.SpinnerModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mohamed on 09/12/2017.
 */

public class helpers {
    public AlertDialog.Builder popupmess;
    public EditText folder_name;
    public Spinner folder_icons;
    public AlertDialog ad;
    public IndexingDB indexingDB;
    int selected_icon;


    public void CreateNewFolder (final Activity context , final ArrayList filesRec , final FileRecAdapter fileRecAdapter , final boolean bottombar , final BottomNavigationView bottomNavigationView){
        popupmess = new AlertDialog.Builder(context);
        View poplayout = LayoutInflater.from(context).inflate(R.layout.create_folder_mess , null);
        popupmess.setView(poplayout);
        ad = popupmess.show();
        folder_icons = (Spinner) poplayout.findViewById(R.id.popup_spinner);

        final ArrayList<SpinnerModel> FolderIcons = new ArrayList<SpinnerModel>();
        FolderIcons.add(new SpinnerModel("random" , R.drawable.if_help_mark_query_question_support_talk));
        FolderIcons.add(new SpinnerModel("acorn" , R.drawable.if_acorn));
        FolderIcons.add(new SpinnerModel("adim" , R.drawable.if_adium));
        FolderIcons.add(new SpinnerModel("coda" , R.drawable.if_coda));
        FolderIcons.add(new SpinnerModel("deviant" , R.drawable.if_deviant));
        FolderIcons.add(new SpinnerModel("indesign" , R.drawable.if_indesign));
        FolderIcons.add(new SpinnerModel("front row" , R.drawable.if_front_row));
        FolderIcons.add(new SpinnerModel("inkscape" , R.drawable.if_inkscape));
        FolderIcons.add(new SpinnerModel("the pirate" , R.drawable.if_thepirate));

        PopUpSpinnerArrayAdapter popUpSpinnerArrayAdapter = new PopUpSpinnerArrayAdapter(context , R.layout.popup_spinner_layout , R.id
                .chosen_folder_icon_name, FolderIcons);
        folder_icons.setAdapter(popUpSpinnerArrayAdapter);
        selected_icon = 0;
        folder_icons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_icon = FolderIcons.get(position).icon;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        folder_name = (EditText) poplayout.findViewById(R.id.pop_folder_name);
        Button create_folder = (Button) poplayout.findViewById(R.id.pop_create_folder);
        indexingDB = new IndexingDB(context);
        final int[] ImageFolders = {R.drawable.if_acorn , R.drawable.if_adium , R.drawable.if_coda , R.drawable.if_deviant , R.drawable.if_indesign , R.drawable.if_front_row , R.drawable.if_inkscape , R.drawable.if_thepirate};
        create_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!folder_name.getText().toString().isEmpty()){
                    String fn = folder_name.getText().toString();
                    Integer fp = filesRec.size();
                    Toast.makeText(context , fn , Toast.LENGTH_LONG).show();
                    if (selected_icon == 0 || selected_icon == R.drawable.if_help_mark_query_question_support_talk)
                        selected_icon = GetRandomChoice(ImageFolders);
                    indexingDB.InsertNewFolder(fn , selected_icon);
                    filesRec.add(fp ,new FilesRec(selected_icon , fn));
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

    public static int GetRandomChoice(int[] array){
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
