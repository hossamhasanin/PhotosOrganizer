package com.hasanin.hossam.photosorganiser.ShowImages;

import android.net.Uri;

/**
 * Created by mohamed on 06/01/2018.
 */

public class ImagesRecModel {
    Uri image;
    String image_name;
    int id;
    public ImagesRecModel(Uri image , String image_name , int id){
        this.image = image;
        this.image_name = image_name;
        this.id = id;
    }
}
