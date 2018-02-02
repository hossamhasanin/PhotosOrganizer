package com.hasanin.hossam.photosorganiser.ImagesFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.ImportImage;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.MainActivity;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.FoldersFragmentsListener;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.ShowImages.ImageRecAdapter;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesFragmentsListener;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mohamed on 14/01/2018.
 */

public class ShowImagesFragment extends Fragment {
    RecyclerView show_images;
    IndexingDB indexingDB;
    ArrayList<ImagesRecModel> all_images;
    ImageRecAdapter imageRecAdapter;
    Activity context;
    String folder_title;
    int folder_id;
    int previous_pos;
    ImagesFragmentsListener imagesFragmentsListener;
    FloatingActionButton fabAddFab;
    FloatingActionButton fabImportImg;
    FloatingActionButton fabTakeImg;
    boolean isFabOben = false;
    int GET_IMAGE_CODE = 100;
    ArrayList<FoldersModel> folders;
    private static final int REQUEST_STORAGE_PERM = 300;
    int SAVE_IMAGE_IN_DATATBASE_CODE = 200;

    public void setData(String folder_title , int folder_id , int previous_pos){
        this.folder_title = folder_title;
        this.folder_id = folder_id;
        this.previous_pos = previous_pos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_images , container , false);
        this.setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        show_images = (RecyclerView) view.findViewById(R.id.show_images);
        indexingDB = new IndexingDB(getActivity());
        all_images = indexingDB.GetAllImages(Integer.toString(folder_id));
        imageRecAdapter = new ImageRecAdapter(all_images , getActivity() , imagesFragmentsListener , "Main" , -1);
        show_images.setAdapter(imageRecAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity() , 1);
        show_images.setLayoutManager(gridLayoutManager);
        gridLayoutManager.scrollToPosition(previous_pos);

        fabAddFab = (FloatingActionButton) view.findViewById(R.id.fab_add_fab);
        fabTakeImg = (FloatingActionButton) view.findViewById(R.id.fab_take_image);
        fabImportImg = (FloatingActionButton) view.findViewById(R.id.fab_import_image);

        fabAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fabGetIn = AnimationUtils.loadAnimation(getActivity() , R.anim.fab_get_in);
                Animation fabGetOut = AnimationUtils.loadAnimation(getActivity() , R.anim.fab_get_out);
                Animation fabRotateIn = AnimationUtils.loadAnimation(getActivity() , R.anim.fab_rotate_in);
                Animation fabRotateOut = AnimationUtils.loadAnimation(getActivity() , R.anim.fab_rotate_out);
                if (isFabOben){
                    fabAddFab.setAnimation(fabRotateOut);
                    fabImportImg.setAnimation(fabGetOut);
                    fabTakeImg.setAnimation(fabGetOut);
                    fabImportImg.setVisibility(View.GONE);
                    fabTakeImg.setVisibility(View.GONE);
                    isFabOben = false;
                } else {
                    fabAddFab.setAnimation(fabRotateIn);
                    fabImportImg.setAnimation(fabGetIn);
                    fabTakeImg.setAnimation(fabGetIn);
                    fabImportImg.setVisibility(View.VISIBLE);
                    fabTakeImg.setVisibility(View.VISIBLE);
                    isFabOben = true;
                }
            }
        });

        fabImportImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folders = indexingDB.GetAllFolders();
                if((int) Build.VERSION.SDK_INT >= 23){
                    if (folders.size() != 0) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERM);
                        } else {
                            MoveToShowImagesActivity();
                        }
                    } else {
                        TastyToast.makeText(getActivity(), "There is no folders create one first !", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }
                } else {
                    if (folders.size() != 0) {
                        MoveToShowImagesActivity();
                    } else {
                        TastyToast.makeText(getActivity(), "There is no folders create one first !", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }
                }
            }
        });

        return view;
    }

    public void MoveToShowImagesActivity(){
        //Toast.makeText(getApplicationContext() , "Import" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent , GET_IMAGE_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_CODE && resultCode == RESULT_OK){
            Intent intent = new Intent(getActivity() , ImportImage.class);
            Bundle b = new Bundle();
            b.putString("image" , String.valueOf(data.getData()));
            intent.putExtras(b);
            startActivityForResult(intent , SAVE_IMAGE_IN_DATATBASE_CODE);
        } else if (requestCode == SAVE_IMAGE_IN_DATATBASE_CODE){
            if (data.getExtras().getInt("success") == 1) {
                TastyToast.makeText(getActivity(), "Saved successfully !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                ArrayList<ImagesRecModel> allImages = indexingDB.GetAllImages(Integer.toString(folder_id));
                ImageRecAdapter imageRecAdapter = new ImageRecAdapter(allImages, getActivity() , imagesFragmentsListener , "Main" , -1);
                show_images.setAdapter(imageRecAdapter);

            }else {
                TastyToast.makeText(getActivity(), "You escaped !", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            imagesFragmentsListener = (ImagesFragmentsListener) context;
        }catch (Exception e){}
    }

}
