package com.hasanin.hossam.photosorganiser.ImagesFragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.Helper.helpers;
import com.hasanin.hossam.photosorganiser.ImportImage;
import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.ShowImages.ImageRecAdapter;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesFragmentsListener;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Locale;

import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

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
    private static final int TAKE_PICTURE_REQUEST_CODE = 800;
    FancyShowCaseView mFancyShowCaseView;
    helpers helpers;
    GridLayoutManager gridLayoutManager;
    SharedPreferences sharedPreferences;
    int spanCount;
    //CoordinatorLayout imagesRootView;

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
        indexingDB = new IndexingDB(getActivity());
        this.setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (Locale.getDefault().getDisplayLanguage().equals("English") || Locale.getDefault().getDisplayLanguage().equals(Locale.ENGLISH)){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        }else {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_right);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //imagesRootView = (CoordinatorLayout) view.findViewById(R.id.show_images_container);

        show_images = (RecyclerView) view.findViewById(R.id.show_images);
        all_images = indexingDB.GetAllImages(Integer.toString(folder_id));
        helpers = new helpers();
        imageRecAdapter = new ImageRecAdapter(all_images , getActivity() , imagesFragmentsListener , "Main" , -1 , folder_id , folder_title);
        show_images.setAdapter(imageRecAdapter);
        sharedPreferences = getActivity().getSharedPreferences("images_style" , Context.MODE_PRIVATE);
        spanCount = sharedPreferences.getInt("spanCount" , 1);
        gridLayoutManager = new GridLayoutManager(getActivity() , spanCount);
        show_images.setLayoutManager(gridLayoutManager);
        gridLayoutManager.scrollToPosition(previous_pos);


        fabAddFab = (FloatingActionButton) view.findViewById(R.id.fab_add_fab);

        fabAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFancyShowCaseView = new FancyShowCaseView.Builder(getActivity())
                        .focusOn(v)
                        .customView(R.layout.custom_overlab_fabs, onViewInflateListener)
                        .disableFocusAnimation()
                        .closeOnTouch(true)
                        .build();
                mFancyShowCaseView.show();
            }
        });

        return view;
    }


    // region ViewInflaterListener
    OnViewInflateListener onViewInflateListener = new OnViewInflateListener() {
        @Override
        public void onViewInflated(View view) {
            FloatingActionButton importImage = (FloatingActionButton) view.findViewById(R.id.cfab_import_image);
            FloatingActionButton takeImage = (FloatingActionButton) view.findViewById(R.id.cfab_take_image);
            final FloatingActionButton close = (FloatingActionButton) view.findViewById(R.id.cfab_add_fab);
            Animation fabRotateIn = AnimationUtils.loadAnimation(getActivity() , R.anim.fab_rotate_in);
            close.setAnimation(fabRotateIn);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFancyShowCaseView.hide();
                }
            });

            importImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    folders = indexingDB.GetAllFolders();
                    if((int) Build.VERSION.SDK_INT >= 23){
                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERM);
                        } else {
                            getImageFromGallery();
                        }
                    } else {
                        getImageFromGallery();
                    }
                    mFancyShowCaseView.hide();
                }
            });

            takeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int CAM_REQUEST_CODE = 600;
                    int WRITE_STORAGE_REQUEST_CODE = 700;
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_REQUEST_CODE);
                    } else if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAM_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri uriSavedImage = helpers.createImageUri();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                        startActivityForResult(intent , TAKE_PICTURE_REQUEST_CODE);
                    }
                }
            });

        }
    };
    //endregion

    public void getImageFromGallery(){
        //Toast.makeText(getApplicationContext() , "Import" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent , GET_IMAGE_CODE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.show_images_menu , menu);
        if (spanCount == 1) {
            menu.findItem(R.id.show_style).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_span_3));
        } else {
            menu.findItem(R.id.show_style).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_span_1));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.show_style:
                if (spanCount == 1){
                    spanCount = 2;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("spanCount" , spanCount);
                    editor.commit();
                    switchRecUi(spanCount);
                    switchIcon(1 , item);
                } else {
                    spanCount = 1;
                    switchRecUi(spanCount);
                    switchIcon(2 , item);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchIcon(int spanCount , MenuItem item) {
        if (spanCount == 1){
            item.setIcon(ContextCompat.getDrawable(getActivity() , R.drawable.ic_span_1));
        } else {
            item.setIcon(ContextCompat.getDrawable(getActivity() , R.drawable.ic_span_3));
        }
    }

    private void switchRecUi(int spanCount) {
        gridLayoutManager.setSpanCount(spanCount);
        imageRecAdapter.notifyItemRangeChanged(0 , imageRecAdapter.getItemCount());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_CODE && resultCode == RESULT_OK){
            Intent intent = new Intent(getActivity() , ImportImage.class);
            Bundle b = new Bundle();
            b.putString("image" , String.valueOf(data.getData()));
            b.putInt("folder_id" , folder_id);
            b.putString("folder_title" , folder_title);
            intent.putExtras(b);
            startActivityForResult(intent , SAVE_IMAGE_IN_DATATBASE_CODE);
        } else if (requestCode == SAVE_IMAGE_IN_DATATBASE_CODE){
            if (data.getExtras().getInt("success") == 1) {
                TastyToast.makeText(getActivity(), "Saved successfully !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                ArrayList<ImagesRecModel> allImages = indexingDB.GetAllImages(Integer.toString(folder_id));
                ImageRecAdapter imageRecAdapter = new ImageRecAdapter(allImages, getActivity() , imagesFragmentsListener , "Main" , -1 , folder_id , folder_title);
                show_images.setAdapter(imageRecAdapter);

            }else {
                TastyToast.makeText(getActivity(), "You escaped !", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            }
        } else if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK){
            //Bitmap image = (Bitmap) data.getExtras().get("data");
            TastyToast.makeText(getActivity(), "Take successfully ! , choose it now", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            helpers.saveImageToGallery(getActivity() , helpers.imageUri);
            Uri imageContentUri = helpers.getImageContentUri(getActivity() , helpers.imageUri.getPath());
            //getImageFromGallery();
            Intent intent = new Intent(getActivity() , ImportImage.class);
            Bundle b = new Bundle();
            b.putString("image" , data != null ? String.valueOf(data.getData()) : imageContentUri.toString());
            b.putInt("folder_id" , folder_id);
            b.putString("folder_title" , folder_title);
            intent.putExtras(b);
            startActivityForResult(intent , SAVE_IMAGE_IN_DATATBASE_CODE);
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
