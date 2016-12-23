package com.igniva.videocapture.utils;

import android.content.ContentValues;
import android.content.Context;

import com.igniva.videocapture.db.RecorderDatabase;
import com.igniva.videocapture.db.RecorderDatabaseUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by igniva-android-21 on 22/12/16.
 */

public class Utiliy {



    public static void setUpDataBase(Context context,List<ContentValues> contentValuesList) {

        RecorderDatabase mRecorderDatabase = new RecorderDatabase(context);
        mRecorderDatabase.openDatabase();


        mRecorderDatabase.insertData(contentValuesList, RecorderDatabaseUtility.TABLE_HISTORY);

    }
    public static ContentValues loadContentValues(int id, String name, String test, String time, String path, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecorderDatabaseUtility.KEY_ID,id);

        contentValues.put(RecorderDatabaseUtility.KEY_NAME,name);
        contentValues.put(RecorderDatabaseUtility.KEY_DESC,test);
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
            String strDate = sdf.format(calendar.getTime());

            return strDate;
        }catch (Exception e){
            e.printStackTrace();
            return "date";
        }

    }

}
