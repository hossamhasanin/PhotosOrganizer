package com.hasanin.hossam.photosorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.hasanin.hossam.photosorganiser.FoldersSpinner.SpinnerModel;

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


    public void DeleteFolders(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM container WHERE name = " + "'" +name+ "'");
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
