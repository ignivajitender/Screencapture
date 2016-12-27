package com.igniva.videocapture.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igniva.videocapture.R;
import com.igniva.videocapture.db.RecorderDatabase;
import com.igniva.videocapture.db.RecorderDatabaseUtility;
import com.igniva.videocapture.model.HistoryData;
import com.igniva.videocapture.ui.adapters.HistoryListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class HistoryFragment extends Fragment {
    View mView;
    public static RecyclerView mRvCategories;
    public static ArrayList<HistoryData> historyList;
    RecorderDatabase mRecorderDatabase;
    HistoryListAdapter mHistoryAdapter;
    TextView message;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_history, container, false);

        setUpLayouts();

        //setUpDataBase();

        syncData();

        return mView;
    }

    private void setUpDataBase() {

        mRecorderDatabase = new RecorderDatabase(getActivity());
        mRecorderDatabase.openDatabase();


        ContentValues contentValues = loadContentValues(001,"VID_20161214_120720633.mp4","test","19 dec ,2016","/storage/emulated/0/DCIM/Camera/VID_20161214_120720633.mp4","Available","5:00");


        List<ContentValues> contentValuesList = new ArrayList<>();
        contentValuesList.add(contentValues);

        mRecorderDatabase.insertData(contentValuesList, RecorderDatabaseUtility.TABLE_HISTORY);

    }

    public void setUpLayouts() {

        historyList = new ArrayList<>();

        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);

        message = (TextView)mView.findViewById(R.id.iv_message);

        try {

            mHistoryAdapter = new HistoryListAdapter(getActivity(), historyList,HistoryFragment.this);
            mRvCategories.setAdapter(mHistoryAdapter);
            mRvCategories.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRvCategories.setLayoutManager(mLayoutManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
}


    public void syncData(){

        mRecorderDatabase = new RecorderDatabase(getActivity());
        mRecorderDatabase.openDatabase();

        Cursor cursor = mRecorderDatabase.getData(RecorderDatabaseUtility.WHOLE_TABLE);

        if (cursor.moveToFirst()) {

            do {
                String data =  cursor.getString(0);

                String video_path = cursor.getString(6);

                File  file = new File(video_path);

                if(!file.exists()) {

                    updateRow(cursor);

                    loadDataInView(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(4),"DELETED",cursor.getString(6),cursor.getString(3));

                }else {

                    loadDataInView(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(4),"Available",cursor.getString(6),cursor.getString(3));

                }

        }while (cursor.moveToNext());

       }else {

            message.setVisibility(View.VISIBLE);

        }

      //  Collections.reverse(historyList);

         sort();
        mHistoryAdapter.notifyDataSetChanged();

        mRecorderDatabase.closeDatabase();
}

    private void loadDataInView(String id, String name, String desc, String time, String status, String path,String duration) {

        HistoryData historyData = new HistoryData();

        historyData.setmName(name);
        historyData.setmDesc(desc);
        historyData.setmTime(time);
        historyData.setmStatus(status);
        historyData.setVideo_path(path);
        historyData.setVideo_id(id);
        historyData.setDuration(duration);

        historyData.setVideo_name(getName(path));
        historyList.add(historyData);

    }

    private String getName(String path) {

        String video_name = path.substring(path.lastIndexOf("/")+1,path.length());

        return video_name;
    }

    private void loadDataInView(Cursor cursor) {

        HistoryData historyData = new HistoryData();

        historyData.setVideo_id(cursor.getString(0));
        historyData.setmName(cursor.getString(1));
        historyData.setmDesc(cursor.getString(2));
        historyData.setDuration(cursor.getString(3));
        historyData.setmTime(cursor.getString(5));

        historyList.add(historyData);
        mHistoryAdapter.notifyDataSetChanged();
    }

    private void updateRow(Cursor cursor) {

        ContentValues contentData = loadContentValues(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(4),cursor.getString(6),"Deleted",cursor.getString(3));

        mRecorderDatabase.updateData(contentData,RecorderDatabaseUtility.TABLE_HISTORY,"video_id=" +cursor.getString(0),null);

    }


    private ContentValues loadContentValues(int id, String name, String test, String time, String path, String status,String duration) {
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

    public void sort(){
        Collections.sort(historyList, new Comparator<HistoryData>(){
            public int compare(HistoryData date1, HistoryData date2){
                return new Date(date2.getmTime()).compareTo(new Date(date1.getmTime()));
            }
        });
        //Collections.reverse(historyList);
    }


    public void showHideView(){
        message.setVisibility(View.VISIBLE);
    }

}