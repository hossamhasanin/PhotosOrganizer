package com.hasanin.hossam.photosorganiser.ImageViewer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel

class SwipeManagerAdapter : FragmentStatePagerAdapter {
    val imagesUri:ArrayList<ImagesRecModel>
    var length:Int
    constructor(fm: FragmentManager, imagesUri: ArrayList<ImagesRecModel> , length:Int ) : super(fm){
        this.imagesUri = imagesUri
        this.length = length
    }
    override fun getItem(position: Int): Fragment {
        var fragmentPage:FragmentPage = FragmentPage()
        var bundle:Bundle = Bundle()
        bundle.putString("imageUri" , imagesUri.get(position).image.toString())
        fragmentPage.setArguments(bundle)
        return fragmentPage
    }

    override fun getCount(): Int {
        return imagesUri.size
    }
}