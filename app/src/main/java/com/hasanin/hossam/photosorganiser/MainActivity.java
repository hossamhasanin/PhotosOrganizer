package com.hasanin.hossam.photosorganiser;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentsListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().add(R.id.lists_container , new ShowFoldersFragment()).commit();

        final Activity context = this;
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottombar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bar_create_folder:
                        Toast.makeText(getApplicationContext() , "Create folder" , Toast.LENGTH_SHORT).show();
                        new helpers().CreateNewFolder(context , FileRecAdapter.filesRec , FileRecAdapter.getInstance() , true , bottomNavigationView);
                    case R.id.take_photo :
                        Toast.makeText(getApplicationContext() , "take photo" , Toast.LENGTH_SHORT).show();
                    case R.id.import_image:
                        Toast.makeText(getApplicationContext() , "Import" , Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });



    }

    @Override
    public void OnPositionListener(int position) {
        Toast.makeText(this , "Chosen position is "+position , Toast.LENGTH_LONG).show();
        EditFoldersFragment editFoldersFragment = new EditFoldersFragment();
        ArrayList positions = new ArrayList();
        positions.add(Integer.toString(position));
        editFoldersFragment.setFuturePositions(positions);
        getFragmentManager().beginTransaction().replace(R.id.lists_container , editFoldersFragment).addToBackStack(null).commit();
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
