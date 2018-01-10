package com.hasanin.hossam.photosorganiser.FoldersSpinner;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasanin.hossam.photosorganiser.R;

import java.util.ArrayList;

/**
 * Created by mohamed on 30/12/2017.
 */

public class FoldersSpinnerArrayAdapter extends ArrayAdapter<SpinnerModel> {
    Activity context;
    ArrayList<FoldersModel> FolderIcons;
    LayoutInflater layoutInflater;

    public FoldersSpinnerArrayAdapter(@NonNull Activity context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull ArrayList FolderIcons) {
        super(context, resource, textViewResourceId, FolderIcons);
        this.FolderIcons = FolderIcons;
        layoutInflater = context.getLayoutInflater();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View spinner_layout = layoutInflater.inflate(R.layout.popup_spinner_layout , parent , false);
        ImageView folder_icon = (ImageView) spinner_layout.findViewById(R.id.chosen_folder_icon);
        TextView folder_icon_name = (TextView) spinner_layout.findViewById(R.id.chosen_folder_icon_name);

        folder_icon.setImageResource(FolderIcons.get(position).icon);
        folder_icon_name.setText(FolderIcons.get(position).icon_name);
        return spinner_layout;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.popup_spinner_layout,parent, false);
        }
        TextView folder_icon_name = (TextView) convertView.findViewById(R.id.chosen_folder_icon_name);
        folder_icon_name.setText(FolderIcons.get(position).icon_name);
        ImageView folder_icon = (ImageView) convertView.findViewById(R.id.chosen_folder_icon);
        folder_icon.setImageResource(FolderIcons.get(position).icon);
        return convertView;
    }
}
