package com.hasanin.hossam.photosorganiser.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FileRecAdapter;
import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FilesRec;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersSpinnerArrayAdapter;
import com.hasanin.hossam.photosorganiser.ImportImage;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.EditFoldersFragment;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.ShowFoldersFragment;
import com.hasanin.hossam.photosorganiser.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.content.ContentValues.TAG;

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
    public Uri imageUri;


    public void CreateNewFolder (final Activity context , final ArrayList filesRec , final FileRecAdapter fileRecAdapter , final boolean bottombar , final BottomNavigationView bottomNavigationView){
        popupmess = new AlertDialog.Builder(context);
        View poplayout = LayoutInflater.from(context).inflate(R.layout.create_folder_mess , null);
        popupmess.setView(poplayout);
        ad = popupmess.show();
        folder_icons = (Spinner) poplayout.findViewById(R.id.popup_spinner);

        final ArrayList<FoldersModel> FolderIcons = new ArrayList<FoldersModel>();
        FolderIcons.add(new FoldersModel("random" , R.drawable.if_help_mark_query_question_support_talk , 0));
        FolderIcons.add(new FoldersModel("acorn" , R.drawable.if_acorn , 0));
        FolderIcons.add(new FoldersModel("adim" , R.drawable.if_adium , 0));
        FolderIcons.add(new FoldersModel("coda" , R.drawable.if_coda , 0));
        FolderIcons.add(new FoldersModel("deviant" , R.drawable.if_deviant , 0));
        FolderIcons.add(new FoldersModel("indesign" , R.drawable.if_indesign , 0));
        FolderIcons.add(new FoldersModel("front row" , R.drawable.if_front_row , 0));
        FolderIcons.add(new FoldersModel("inkscape" , R.drawable.if_inkscape , 0));
        FolderIcons.add(new FoldersModel("the pirate" , R.drawable.if_thepirate , 0));

        FoldersSpinnerArrayAdapter foldersSpinnerArrayAdapter = new FoldersSpinnerArrayAdapter(context , R.layout.popup_spinner_layout , R.id
                .chosen_folder_icon_name, FolderIcons);
        folder_icons.setAdapter(foldersSpinnerArrayAdapter);
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

                if (!CreateFolderInstructions(context)){
                    return;
                }

                String fn = folder_name.getText().toString();
                Integer fp = filesRec.size();
                if (selected_icon == 0 || selected_icon == R.drawable.if_help_mark_query_question_support_talk)
                    selected_icon = GetRandomChoice(ImageFolders);
                indexingDB.InsertNewFolder(fn , selected_icon);
                int id = indexingDB.GetLastRecordId();
                filesRec.add(fp ,new FilesRec(selected_icon , fn , id));
                TastyToast.makeText(context, "Created successfully !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                // To add the new item to the list to make it show
                fileRecAdapter.notifyItemInserted(fp);
                ad.dismiss();

                if (bottombar) {
                    bottomNavigationView.setSelectedItemId(R.id.main_bar_item);
                }
            }
        });
    }

    public void EditFolder (final Activity context , final ArrayList<FilesRec> filesRec , final FileRecAdapter fileRecAdapter , String f_name , int f_icon){
        popupmess = new AlertDialog.Builder(context);
        View poplayout = LayoutInflater.from(context).inflate(R.layout.create_folder_mess , null);
        popupmess.setView(poplayout);
        ad = popupmess.show();
        folder_icons = (Spinner) poplayout.findViewById(R.id.popup_spinner);

        final ArrayList<FoldersModel> FolderIcons = new ArrayList<FoldersModel>();
        FolderIcons.add(new FoldersModel("random" , R.drawable.if_help_mark_query_question_support_talk , 0));
        FolderIcons.add(new FoldersModel("acorn" , R.drawable.if_acorn , 0));
        FolderIcons.add(new FoldersModel("adim" , R.drawable.if_adium , 0));
        FolderIcons.add(new FoldersModel("coda" , R.drawable.if_coda , 0));
        FolderIcons.add(new FoldersModel("deviant" , R.drawable.if_deviant , 0));
        FolderIcons.add(new FoldersModel("indesign" , R.drawable.if_indesign , 0));
        FolderIcons.add(new FoldersModel("front row" , R.drawable.if_front_row , 0));
        FolderIcons.add(new FoldersModel("inkscape" , R.drawable.if_inkscape , 0));
        FolderIcons.add(new FoldersModel("the pirate" , R.drawable.if_thepirate , 0));

        FoldersSpinnerArrayAdapter foldersSpinnerArrayAdapter = new FoldersSpinnerArrayAdapter(context , R.layout.popup_spinner_layout , R.id
                .chosen_folder_icon_name, FolderIcons);
        folder_icons.setAdapter(foldersSpinnerArrayAdapter);
        for (int i=0;i<FolderIcons.size();i++){
            if (f_icon == FolderIcons.get(i).icon){
                folder_icons.setSelection(i);
                selected_icon = f_icon;
                break;
            }
        }

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
        folder_name.setText(f_name);
        Button create_folder = (Button) poplayout.findViewById(R.id.pop_create_folder);
        indexingDB = new IndexingDB(context);
        final int[] ImageFolders = {R.drawable.if_acorn , R.drawable.if_adium , R.drawable.if_coda , R.drawable.if_deviant , R.drawable.if_indesign , R.drawable.if_front_row , R.drawable.if_inkscape , R.drawable.if_thepirate};
        create_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!folder_name.getText().toString().isEmpty()){
                    String fn = folder_name.getText().toString();
                    Integer fp = filesRec.size();
                    if (selected_icon == 0 || selected_icon == R.drawable.if_help_mark_query_question_support_talk)
                        selected_icon = GetRandomChoice(ImageFolders);
                    int pos = Integer.parseInt(fileRecAdapter.ch.get(0).toString());
                    int id = filesRec.get(pos).id;
                    indexingDB.UpdateFolder(fn , Integer.toString(selected_icon) , Integer.toString(id));
                    filesRec.set(pos , new FilesRec(selected_icon , fn , id));
                    fileRecAdapter.notifyItemChanged(pos);
                    TastyToast.makeText(context, "The edit has saved !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    MoveTo(context , "Edit" , fileRecAdapter.ch.get(0).toString());
                }
                ad.dismiss();
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

    public boolean CreateFolderInstructions(Activity context){
        IndexingDB indexingDB = new IndexingDB(context);
        if (folder_name.getText().toString().isEmpty()){
            TastyToast.makeText(context , "Write name to the folder !" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            return false;
        }
        if (indexingDB.FolderNameExist(folder_name.getText().toString())){
            TastyToast.makeText(context , "This name is exist !" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            return false;
        }
        if (folder_name.getText().toString().length() > 20){
            TastyToast.makeText(context , "The name is too long !" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            return false;
        }
        if (folder_name.getText().toString().length() < 3){
            TastyToast.makeText(context , "The name is too short !" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            return false;
        }
        return true;
    }

    public static boolean storingImageRestrictions(Activity context , String finalImageName){
        IndexingDB indexingDB = new IndexingDB(context);
        if (finalImageName.isEmpty()){
            TastyToast.makeText(context , "Write name to the folder !" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            return false;
        }
        if (indexingDB.imageNameExists(finalImageName)){
            TastyToast.makeText(context , "This image name exists !" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            return false;
        }
        if (finalImageName.length() > 30){
            TastyToast.makeText(context , "Write name is less than 30 chatacters !" , TastyToast.LENGTH_LONG , TastyToast.ERROR);
            return false;
        }
        return true;
    }

    public static int GetRandomChoice(int[] array){
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public AlertDialog.Builder AlertMessage(Activity context , String message , String title , int icon){
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setMessage(message).setIcon(icon).setTitle(title);
        return al;
    }

    public void takePhoto(Activity context){
        int CAM_REQUEST_CODE = 600;
        int WRITE_STORAGE_REQUEST_CODE = 700;
        int TAKE_PICTURE_REQUEST_CODE = 800;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_REQUEST_CODE);
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, CAM_REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uriSavedImage = createImageUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            context.startActivityForResult(intent , TAKE_PICTURE_REQUEST_CODE);
        }
    }

    public Uri createImageUri(){
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "jpeg_" + timestamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Create the image file
        File file = new File(storageDir , fileName);
        Uri uriSavedImage = Uri.fromFile(file);
        imageUri = uriSavedImage;
        return  uriSavedImage;
    }

    public static void saveImageToGallery(Activity context , Uri imageUri) {
        // We call media scanner to inform the gallery that there is a new imgae has just taken and it need to be shown
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        context.sendBroadcast(intent);
    }

    // use it just in the mainActivity
    public void moaveToImportImageActivity(Activity context , int SAVE_IMAGE_IN_DATATBASE_CODE , Intent data , Uri imageUri){
        Intent intent = new Intent(context , ImportImage.class);
        Bundle b = new Bundle();
        b.putString("image" , data != null ? String.valueOf(data.getData()) : imageUri.toString());
        b.putInt("folder_id" , 0);
        b.putString("folder_title" , "");
        intent.putExtras(b);
        context.startActivityForResult(intent , SAVE_IMAGE_IN_DATATBASE_CODE);
    }

    // To get the content uri which is the uri that saved in the gallery , i should get it to save it into the database
    public Uri getImageContentUri(Activity context, String absPath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[] { MediaStore.Images.Media._ID }
                , MediaStore.Images.Media.DATA + "=? "
                , new String[] { absPath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

}
