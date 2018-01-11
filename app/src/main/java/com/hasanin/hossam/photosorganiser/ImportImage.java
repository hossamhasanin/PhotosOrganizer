package com.hasanin.hossam.photosorganiser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersSpinnerArrayAdapter;

import java.util.ArrayList;

public class ImportImage extends AppCompatActivity {

    FloatingActionButton btn_import;
    FloatingActionButton btn_close;
    Spinner folders_spinner;
    ImageView imported_image;
    EditText image_name;
    IndexingDB indexingDB;
    int selected_folder;
    Activity context;
    int SAVE_IMAGE_IN_DATATBASE_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Import");
        indexingDB = new IndexingDB(this);
        context = this;

        final Uri image_uri = Uri.parse(getIntent().getExtras().getString("image"));

        btn_import = (FloatingActionButton) findViewById(R.id.btn_import);
        btn_close = (FloatingActionButton) findViewById(R.id.import_btn_close);
        folders_spinner = (Spinner) findViewById(R.id.import_folders_spinner);
        imported_image = (ImageView) findViewById(R.id.imported_image);
        image_name = (EditText) findViewById(R.id.image_name);

        imported_image.setImageURI(image_uri);

        final ArrayList<FoldersModel> all_folders = indexingDB.GetAllFolders();
        selected_folder  = all_folders.get(0).id;
        FoldersSpinnerArrayAdapter foldersSpinnerArrayAdapter = new FoldersSpinnerArrayAdapter(this , R.layout.popup_spinner_layout , R.id
                .chosen_folder_icon_name, all_folders);
        folders_spinner.setAdapter(foldersSpinnerArrayAdapter);
        folders_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_folder = all_folders.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Animation scale_up = AnimationUtils.loadAnimation(this , R.anim.scale_floatingbtn_import);

        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scale_up);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                indexingDB.InsertNewImage(image_name.getText().toString() , image_uri , selected_folder);
                Intent intent = new Intent(context , MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("success" , 1);
                intent.putExtras(b);
                setResult(SAVE_IMAGE_IN_DATATBASE_CODE , intent);
                finish();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scale_up);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(context , MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("success" , 0);
                intent.putExtras(b);
                setResult(SAVE_IMAGE_IN_DATATBASE_CODE , intent);
                finish();
            }
        });

    }
}
