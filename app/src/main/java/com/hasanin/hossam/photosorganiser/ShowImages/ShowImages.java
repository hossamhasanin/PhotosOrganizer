package com.hasanin.hossam.photosorganiser.ShowImages;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hasanin.hossam.photosorganiser.Helper.helpers;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.R;

import java.util.ArrayList;

public class ShowImages extends AppCompatActivity {

    RecyclerView show_images;
    IndexingDB indexingDB;
    ArrayList<ImagesRecModel> all_images;
    ImageRecAdapter imageRecAdapter;
    Activity context = this;
    private static final int REQUEST_STORAGE_PERM = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        Bundle data = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data.getString("folder_name"));
       // toolbar.setTitle();
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        int folder_id = data.getInt("folder_id");
        show_images = (RecyclerView) findViewById(R.id.show_images);
        indexingDB = new IndexingDB(this);
        all_images = indexingDB.GetAllImages(Integer.toString(folder_id));
        imageRecAdapter = new ImageRecAdapter(all_images , this);
        show_images.setAdapter(imageRecAdapter);
        show_images.setLayoutManager(new GridLayoutManager(this , 1));

    }

}
