package com.hasanin.hossam.photosorganiser.FoldersSpinner;

/**
 * Created by mohamed on 30/12/2017.
 */

public class FoldersModel {
    public String icon_name;
    public int icon;
    public int id;
    public String pass;
    public FoldersModel(String icon_name , int icon , int id , String pass){
        this.icon_name = icon_name;
        this.icon = icon;
        this.id = id;
        this.pass = pass;
    }
}
