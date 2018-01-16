package com.hasanin.hossam.photosorganiser.ShowImages;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hasanin.hossam.photosorganiser.ImagesFragments.DeleteImagesFragment;
import com.hasanin.hossam.photosorganiser.ImagesFragments.ShowImagesFragment;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.MainActivity;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.FoldersFragmentsListener;
import com.hasanin.hossam.photosorganiser.R;

import java.util.ArrayList;

public class ShowImages extends AppCompatActivity implements ImagesFragmentsListener {

    RecyclerView show_images;
    IndexingDB indexingDB;
    ArrayList<ImagesRecModel> all_images;
    ImageRecAdapter imageRecAdapter;
    Activity context = this;
    private static final int REQUEST_STORAGE_PERM = 300;
    ShowImagesFragment showImagesFragment;
    int folder_id;
    String folder_title;
    String frag;
    int previous_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        Bundle data = getIntent().getExtras();
        folder_title = data.getString("folder_name");
        folder_id = data.getInt("folder_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(folder_title);

        frag = "Main";
        showImagesFragment = new ShowImagesFragment();
        showImagesFragment.setData(data.getString("folder_name") , folder_id , 0);
        getFragmentManager().beginTransaction().add(R.id.images_container , showImagesFragment).commit();


    }

    @Override
    public void MoveToFragment(String frag , int future_pos) {
        if (frag == "Delete"){
            this.frag = "Delete";
            DeleteImagesFragment deleteImagesFragment = new DeleteImagesFragment();
            this.previous_pos = future_pos;
            deleteImagesFragment.setData(future_pos , folder_id);
            getFragmentManager().beginTransaction().replace(R.id.images_container , deleteImagesFragment).addToBackStack(null).commit();
        } else if (frag == "Main") {
            this.frag = "Main";
            ShowImagesFragment showImagesFragment = new ShowImagesFragment();
            int previous_pos = future_pos;
            showImagesFragment.setData(folder_title , folder_id , previous_pos);
            getFragmentManager().beginTransaction().replace(R.id.images_container , showImagesFragment).addToBackStack(null).commit();
        }
    }


    @Override
    public void onBackPressed() {
        if (frag == "Main"){
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
        } else if (frag == "Delete") {
            ShowImagesFragment showImagesFragment = new ShowImagesFragment();
            showImagesFragment.setData(folder_title , folder_id , this.previous_pos);
            getFragmentManager().beginTransaction().replace(R.id.images_container , showImagesFragment).addToBackStack(null).commit();
        }
    }
}
