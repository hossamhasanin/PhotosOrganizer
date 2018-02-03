package com.hasanin.hossam.photosorganiser.ImagesFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.MainActivity;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.ShowImages.ImageRecAdapter;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesFragmentsListener;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

/**
 * Created by mohamed on 14/01/2018.
 */

public class DeleteImagesFragment extends Fragment {

    RecyclerView show_images;
    IndexingDB indexingDB;
    ArrayList<ImagesRecModel> all_images;
    ImageRecAdapter imageRecAdapter;
    Activity context;
    String folder_title;
    int folder_id;
    ImagesFragmentsListener imagesFragmentsListener;
    int future_pos;
    public void setData(int future_pos , int folder_id){
        this.future_pos = future_pos;
        this.folder_id = folder_id;
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
        imageRecAdapter = new ImageRecAdapter(all_images , getActivity() , imagesFragmentsListener , "Delete" , future_pos);
        show_images.setAdapter(imageRecAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity() , 1);
        show_images.setLayoutManager(gridLayoutManager);
        gridLayoutManager.scrollToPosition(future_pos);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete_images_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.delete_image_dmenu){
            ArrayList checked_images = imageRecAdapter.checked;
            for (int i=0;i<checked_images.size();i++){
                int p = Integer.parseInt(checked_images.get(i).toString());
                int id = all_images.get(p).id;
                indexingDB.DeleteImage(Integer.toString(id));
            }

            if (checked_images.size() > 1)
                TastyToast.makeText(getActivity() , "The images is deleted successfully !" , TastyToast.LENGTH_LONG , TastyToast.SUCCESS);
            else
                TastyToast.makeText(getActivity() , "The image is deleted successfully !" , TastyToast.LENGTH_LONG , TastyToast.SUCCESS);

            if (checked_images.size() == all_images.size()) {
                startActivity(new Intent(getActivity() , MainActivity.class));
            }else{
                ShowImagesFragment showImagesFragment = new ShowImagesFragment();
                showImagesFragment.setData(folder_title, folder_id, future_pos);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.images_container, showImagesFragment).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            imagesFragmentsListener = (ImagesFragmentsListener) context;
        }catch (Exception e){}
    }
}
