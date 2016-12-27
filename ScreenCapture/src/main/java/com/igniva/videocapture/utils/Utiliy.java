package com.igniva.videocapture.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.igniva.videocapture.db.RecorderDatabase;
import com.igniva.videocapture.db.RecorderDatabaseUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by igniva-android-21 on 22/12/16.
 */

public class Utiliy {



    public static void setUpDataBase(Context context,List<ContentValues> contentValuesList) {

        RecorderDatabase recorderDatabase = new RecorderDatabase(context);
        recorderDatabase.openDatabase();


        recorderDatabase.insertData(contentValuesList, RecorderDatabaseUtility.TABLE_HISTORY);
        recorderDatabase.closeDatabase();

    }
    public static ContentValues loadContentValues(int id, String name, String test, String time, String path, String status,String duration) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecorderDatabaseUtility.KEY_ID,id);

        contentValues.put(RecorderDatabaseUtility.KEY_NAME,name);
        contentValues.put(RecorderDatabaseUtility.KEY_DESC,test);
        contentValues.put(RecorderDatabaseUtility.KEY_DURATION,duration);
        contentValues.put(RecorderDatabaseUtility.KEY_TIME,time);
        contentValues.put(RecorderDatabaseUtility.KEY_VIDEO_PATH,path);

        contentValues.put(RecorderDatabaseUtility.KEY_STATUS,status);

        return contentValues;

    }

    public static String getName(String path) {

        String video_name = path.substring(path.lastIndexOf("/")+1,path.length());

        return video_name;

    }

    public static String curentDateTime(){
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
           SimpleDateFormat writeFormat = new SimpleDateFormat("dd MMM,yyyy HH:mm");

            String strDate = sdf.format(calendar.getTime());

            Date date = sdf.parse(strDate);

            return writeFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();

            Log.e("date Error",e.getMessage());
            return "date";
        }

    }

}
