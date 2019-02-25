package com.hasanin.hossam.photosorganiser.ImageViewer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hasanin.hossam.photosorganiser.R
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel
import kotlinx.android.synthetic.main.show_full_image.*

class FragmentPage : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.show_full_image , container , false)
        var data:Bundle? = getArguments()
        var imageUri: Uri = Uri.parse(data!!.getString("imageUri"))
        var theImage = view!!.findViewById(R.id.full_image) as ImageView
        Glide.with(getActivity()).load(imageUri).into(theImage)
        Log.i("ImageViewer" , imageUri.toString())
        return view!!
    }

}
