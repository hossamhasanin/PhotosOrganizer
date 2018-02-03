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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersSpinnerArrayAdapter;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class ImportImage extends AppCompatActivity {

    FloatingActionButton btn_import;
    FloatingActionButton btn_close;
    Spinner folders_spinner;
    ImageView imported_image;
    ImageView existedFolderIcon;
    EditText image_name;
    TextView existedFolderTitle;
    TextView titleOfStatus;
    LinearLayout activityImportImage;
    LinearLayout existedFolder;
    IndexingDB indexingDB;
    int selected_folder;
    Activity context;
    int SAVE_IMAGE_IN_DATATBASE_CODE = 200;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_image);
        Bundle data = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (data.getInt("folder_id") == 0) {
            getSupportActionBar().setTitle("Import");
        } else {
            getSupportActionBar().setTitle("Import To " + data.getString("folder_title"));
        }
        indexingDB = new IndexingDB(this);
        context = this;

        String action = getIntent().getAction();
        String type = getIntent().getType();
        if (Intent.ACTION_SEND.equals(action) && type.startsWith("image/")){
            image_uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            if (indexingDB.GetAllFolders().size() == 0){
                Intent intent = new Intent(context , MainActivity.class);
                intent.putExtra("show_no_folders_error" , "error");
                startActivity(intent);
            }
        } else if(!data.getString("image").isEmpty()) {
            image_uri = Uri.parse(data.getString("image"));
        }

        activityImportImage = (LinearLayout) findViewById(R.id.activity_import_img);
        titleOfStatus = (TextView) findViewById(R.id.titleOfStatus);
        existedFolder = (LinearLayout) findViewById(R.id.existed_folder);
        existedFolderTitle = (TextView) findViewById(R.id.existed_folderTile);
        existedFolderIcon = (ImageView) findViewById(R.id.existed_folderIcon);
        btn_import = (FloatingActionButton) findViewById(R.id.btn_import);
        btn_close = (FloatingActionButton) findViewById(R.id.import_btn_close);
        folders_spinner = (Spinner) findViewById(R.id.import_folders_spinner);
        imported_image = (ImageView) findViewById(R.id.imported_image);
        image_name = (EditText) findViewById(R.id.image_name);

        imported_image.setImageURI(image_uri);

        final ArrayList<FoldersModel> all_folders = indexingDB.GetAllFolders();
        selected_folder  = data.getInt("folder_id") != 0 ? data.getInt("folder_id") : all_folders.get(0).id;
        if (data.getInt("folder_id") == 0){
            activityImportImage.removeView(existedFolder);
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
        } else {
            activityImportImage.removeView(folders_spinner);
            titleOfStatus.setText("Your folder is :");
            existedFolderTitle.setText(data.getString("folder_title"));
            int fIcon = 0;
            for (FoldersModel f : all_folders){
                fIcon = f.id == data.getInt("folder_id") ? f.icon : 0;
                break;
            }
            existedFolderIcon.setImageResource(fIcon);
        }

        final Animation scale_up = AnimationUtils.loadAnimation(this , R.anim.scale_floatingbtn_import);

        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scale_up);
                indexingDB.InsertNewImage(image_name.getText().toString() , image_uri , selected_folder);
                finishMoveBack(1);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scale_up);
                finishMoveBack(0);
            }
        });

    }

    public void finishMoveBack(int success){
        Intent intent = new Intent(context , MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("success" , success);
        intent.putExtras(b);
        setResult(SAVE_IMAGE_IN_DATATBASE_CODE , intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("success" , 0);
        intent.putExtras(b);
        setResult(SAVE_IMAGE_IN_DATATBASE_CODE , intent);
        finish();
        super.onBackPressed();
    }
}
