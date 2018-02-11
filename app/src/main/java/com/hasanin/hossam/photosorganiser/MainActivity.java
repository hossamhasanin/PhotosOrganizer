package com.hasanin.hossam.photosorganiser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.hasanin.hossam.photosorganiser.FilesRecyclerView.FileRecAdapter;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.Helper.BottomNavigationViewHelper;
import com.hasanin.hossam.photosorganiser.Helper.helpers;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.DeleteFoldersFragment;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.EditFoldersFragment;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.FoldersFragmentsListener;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.ShowFoldersFragment;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FoldersFragmentsListener {

    int GET_IMAGE_CODE = 100;
    int SAVE_IMAGE_IN_DATATBASE_CODE = 200;
    private static final int REQUEST_STORAGE_PERM_FROM_BAR = 300;
    private static final int REQUEST_STORAGE_PERM_FROM_RECYC = 400;
    private static final int CAM_REQUEST_CODE = 600;
    private static final int WRITE_STORAGE_REQUEST_CODE = 700;
    private static final int TAKE_PICTURE_REQUEST_CODE = 800;

    BottomNavigationView bottomNavigationView;
    ArrayList<FoldersModel> folders;
    IndexingDB indexingDB;
    helpers helpers;
    //ShowFoldersFragment showFoldersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().add(R.id.lists_container , new ShowFoldersFragment()).commit();
        indexingDB = new IndexingDB(this);
        helpers = new helpers();

        try {
            if (!getIntent().getStringExtra("show_no_folders_error").isEmpty()){
                TastyToast.makeText(getApplicationContext() , "There is no folders!" , TastyToast.LENGTH_SHORT , TastyToast.ERROR);
            }
        }catch (NullPointerException e){}


        final Activity context = this;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottombar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bar_create_folder:
                        new helpers().CreateNewFolder(context , FileRecAdapter.filesRec , FileRecAdapter.getInstance() , true , bottomNavigationView);
                        break;
                    case R.id.take_photo :
                        Toast.makeText(getApplicationContext() , "take photo" , Toast.LENGTH_SHORT).show();
                        helpers.takePhoto(context);
                        break;
                    case R.id.import_image:
                        folders = indexingDB.GetAllFolders();
                        if((int) Build.VERSION.SDK_INT >= 23){
                            if (folders.size() != 0) {
                                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERM_FROM_BAR);
                                } else {
                                    getImageFromGallery();
                                }
                            } else {
                                TastyToast.makeText(getApplicationContext(), "There is no folders create one first !", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        } else {
                            if (folders.size() != 0) {
                                getImageFromGallery();
                            } else {
                                TastyToast.makeText(getApplicationContext(), "There is no folders create one first !", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        }

                        break;
                }

                return true;
            }
        });



    }

    public void getImageFromGallery(){
        //Toast.makeText(getApplicationContext() , "Import" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent , GET_IMAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_STORAGE_PERM_FROM_BAR:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    bottomNavigationView.setSelectedItemId(R.id.main_bar_item);
                } else {
                    TastyToast.makeText(getApplicationContext(), "Permission denied !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            case REQUEST_STORAGE_PERM_FROM_RECYC:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                } else {
                    TastyToast.makeText(getApplicationContext(), "Permission denied !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            case CAM_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                } else {
                    TastyToast.makeText(getApplicationContext(), "Permission denied !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            case WRITE_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                } else {
                    TastyToast.makeText(getApplicationContext(), "Permission denied !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_CODE && resultCode == RESULT_OK){
            helpers.moaveToImportImageActivity(this, SAVE_IMAGE_IN_DATATBASE_CODE ,data , null);
        } else if (requestCode == SAVE_IMAGE_IN_DATATBASE_CODE){
            if (data.getExtras().getInt("success") == 1)
                TastyToast.makeText(getApplicationContext(), "Saved successfully !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            else
                TastyToast.makeText(getApplicationContext(), "You escaped !", TastyToast.LENGTH_SHORT , TastyToast.CONFUSING);

            bottomNavigationView.setSelectedItemId(R.id.main_bar_item);
        } else if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK){
            //Bitmap image = (Bitmap) data.getExtras().get("data");
            helpers.saveImageToGallery(this , helpers.imageUri);
            TastyToast.makeText(getApplicationContext(), "Take successfully ! , choose it now", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            //helpers.moaveToImportImageActivity(this, SAVE_IMAGE_IN_DATATBASE_CODE ,null , helpers.imageUri);
            getImageFromGallery();
        }

    }

    @Override
    public void OnPositionListener(int position) {
        Toast.makeText(this , "Chosen position is "+position , Toast.LENGTH_LONG).show();
        EditFoldersFragment editFoldersFragment = new EditFoldersFragment();
        ArrayList positions = new ArrayList();
        positions.add(Integer.toString(position));
        editFoldersFragment.setFuturePositions(positions);
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragments_fade_in , R.animator.fragments_fade_out).replace(R.id.lists_container , editFoldersFragment).addToBackStack(null).commit();
    }

    @Override
    public void OnMoveToListener(int move_to , ArrayList positions) {
        if (move_to == 1){
            DeleteFoldersFragment deleteFoldersFragment = new DeleteFoldersFragment();
            deleteFoldersFragment.setFuture_positions(positions);
            getFragmentManager().beginTransaction().replace(R.id.lists_container , deleteFoldersFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu , menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    ArrayList mTitles=new ArrayList();
//    ArrayList mUrls=new ArrayList();



    /*public void getBrowserHist()  {
        Cursor mCur = getContentResolver().query(Browser,
                Browser.HISTORY_PROJECTION, null, null, null);
        mCur.moveToFirst();
        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            while (mCur.isAfterLast() == false) {
                Log.v("titleIdx", mCur
                        .getString(Browser.HISTORY_PROJECTION_TITLE_INDEX));
                Log.v("urlIdx", mCur
                        .getString(Browser.HISTORY_PROJECTION_URL_INDEX));
                mCur.moveToNext();
            }
        }
    }*/

}
