package com.hasanin.hossam.photosorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.FoldersModel;
import com.hasanin.hossam.photosorganiser.ShowImages.ImagesRecModel;

import java.util.ArrayList;

/**
 * Created by mohamed on 12/11/2017.
 */

public class IndexingDB extends SQLiteOpenHelper {
    public static String dbname = "IndexingDB";
    public static int version = 1;

    public IndexingDB(Context context){
        super(context , dbname , null , version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `container` ( `id` INTEGER, `name` TEXT, `type` INTEGER DEFAULT 0, `place` INTEGER DEFAULT 0, `uri` TEXT,  PRIMARY KEY(`id`) ); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table IF EXISTS container");
        onCreate(db);
    }

    public boolean InsertNewFolder (String folder_name , int icon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",folder_name);
        contentValues.put("type" , 0);
        contentValues.put("place" , 0);
        contentValues.put("uri" , Integer.toString(icon));
        Long result = db.insert("container" , null , contentValues);
        if (result == 1){
            return true;
        }else {
            return false;
        }
    }

    public boolean InsertNewImage (String image_name , Uri image , int place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",image_name);
        contentValues.put("type" , 1);
        contentValues.put("place" , place);
        contentValues.put("uri" , String.valueOf(image));
        Long result = db.insert("container" , null , contentValues);
        if (result == 1){
            return true;
        }else {
            return false;
        }
    }


    public ArrayList<FoldersModel> GetAllFolders (){
        ArrayList<FoldersModel> all_folders = new ArrayList<FoldersModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        // type = 0 means folder
        Cursor data = db.rawQuery("SELECT * FROM container WHERE type = 0", null);
        data.moveToFirst();
        while (data.isAfterLast() == false){
            all_folders.add(new FoldersModel(data.getString(data.getColumnIndex("name")) , Integer.parseInt(data.getString(data.getColumnIndex("uri"))) , data.getInt(data.getColumnIndex("id")) ));
            data.moveToNext();
        }
        return all_folders;
    }

    public ArrayList<ImagesRecModel> GetAllImages (String place){
        ArrayList<ImagesRecModel> all_images = new ArrayList<ImagesRecModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        // type = 0 means folder
        Cursor data = db.rawQuery("SELECT * FROM container WHERE type = 1 AND place = " + "'" +place+ "'", null);
        data.moveToFirst();
        while (data.isAfterLast() == false){
            all_images.add(new ImagesRecModel(Uri.parse(data.getString(data.getColumnIndex("uri"))) , data.getString(data.getColumnIndex("name")) , data.getInt(data.getColumnIndex("id")) ));
            data.moveToNext();
        }
        return all_images;
    }

    public boolean FolderIsEmpty(String place){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT count(*) FROM container WHERE type = 1 AND place = " +place , null);
        data.moveToFirst();
        if (data.getInt(0) > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean FolderNameExist(String folder_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT count(*) FROM container WHERE name = '"+folder_name+"'" , null);
        data.moveToFirst();
        if (data.getInt(0) == 1){
            return true;
        } else {
            return false;
        }
    }

    public int GetLastRecordId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM container order by id desc limit 1", null);
        data.moveToFirst();
        return data.getInt(data.getColumnIndex("id"));
    }

    public void UpdateFolder(String name , String icon , String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update container set name = '"+ name +"' , uri = '"+ icon +"' where type = 0 AND id = " + id);
    }

    public void DeleteImagesByItsPlace(String place){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM container WHERE place = " + "'" +place+ "'");
        }catch (Exception e){
            System.out.println(e);
        }
    }


    public void DeleteFolders(String name , int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            DeleteImagesByItsPlace(Integer.toString(id));
            db.execSQL("DELETE FROM container WHERE name = " + "'" +name+ "'");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void DeleteImage(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM container WHERE type = 1 AND id = "+id);
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
