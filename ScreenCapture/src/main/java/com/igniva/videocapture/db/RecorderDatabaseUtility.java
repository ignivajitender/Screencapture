package com.igniva.videocapture.db;

/**
 * Created by Rohit on 28/11/16.
 */

public class RecorderDatabaseUtility {
    public static String CREATE_TABLE_LOGIN = "CREATE TABLE " + "Table_HISTORY" +"(" + "video_id" +" INTEGER PRIMARY KEY,"+
            "Name" +" TEXT,"+ "Description" +" TEXT," + "Duration" +" TEXT," + "Time" +" TEXT," + "Status" +" TEXT,"  +"Video_Path" +" TEXT"+")";
    public static final String NAME = "";
    public static final String TABLE_HISTORY = "Table_HISTORY";

    public static final String DROP_TABLE_IF_EXISTS = "";
    public static String ALL_TABLES_QUERY="";
    public static String WHOLE_TABLE="SELECT * FROM Table_HISTORY";

    public static String TABLE_LOGIN="";

    public static String KEY_ID= "video_id";
    public static String KEY_NAME = "Name";
    public static String KEY_DESC = "Description";
    public static String KEY_TIME = "Time";
    public static String KEY_STATUS = "Status";
    public static String KEY_VIDEO_PATH = "Video_Path";
    public static String KEY_DURATION= "Duration";




}
