package com.hasanin.hossam.photosorganiser.ImagesFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasanin.hossam.photosorganiser.IndexingDB;
import com.hasanin.hossam.photosorganiser.MainFoldersFragments.FoldersFragmentsListener;
import com.hasanin.hossam.photosorganiser.R;
import com.hasanin.hossam.photosorganiser.ShowImages.ImageRecAdapter;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesFragmentsListener;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;

import java.util.ArrayList;

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
    ImagesFragmentsListener imagesFragmentsListener;

    public void setData(String folder_title , int folder_id){
        this.folder_title = folder_title;
        this.folder_id = folder_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_images , container , false);
        this.setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        show_images = (RecyclerView) view.findViewById(R.id.show_images);
        indexingDB = new IndexingDB(getActivity());
        all_images = indexingDB.GetAllImages(Integer.toString(folder_id));
        imageRecAdapter = new ImageRecAdapter(all_images , getActivity() , imagesFragmentsListener , "Main" , -1);
        show_images.setAdapter(imageRecAdapter);
        show_images.setLayoutManager(new GridLayoutManager(getActivity() , 1));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            imagesFragmentsListener = (ImagesFragmentsListener) context;
        }catch (Exception e){}
    }

}
