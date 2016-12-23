package com.igniva.videocapture.ui.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.AlphabeticIndex;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.igniva.videocapture.R;
import com.igniva.videocapture.db.RecorderDatabase;
import com.igniva.videocapture.db.RecorderDatabaseUtility;
import com.igniva.videocapture.model.HistoryData;
import com.igniva.videocapture.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by igniva-php-08 on 20/5/16.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    public static ArrayList<HistoryData> mListCategories = new ArrayList<>();

    ArrayList<HistoryData> mListNames = new ArrayList<>();



    Context mContext;
    Activity activity;
    SQLiteDatabase db;
    static List<HistoryData> contacts;
    int mData;
    SharedPreferences sharedPreferences;
    EditText message_editText = null;
    RecorderDatabase mRecorderDataBase;

    public HistoryListAdapter(Context context, ArrayList<HistoryData> listCategories) throws Exception {
        this.mListCategories = listCategories;

        this.mContext = context;

        mRecorderDataBase = new RecorderDatabase(mContext);

        mRecorderDataBase.openDatabase();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCategoryName, mTvCategoryDuration,mTvDesc,mTvDelete,mTvDeleteItem,mTvShare;
        CardView mCvMain;
        ImageView mIvVideoThumbImg, mIvEditName, mIvEditDesc;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvCategoryName = (TextView) itemView.findViewById(R.id.iv_name);
            mTvDeleteItem = (TextView) itemView.findViewById(R.id.delete);
            mTvShare = (TextView) itemView.findViewById(R.id.share);

            mTvDesc = (TextView) itemView.findViewById(R.id.iv_desc);
            mIvVideoThumbImg = (ImageView) itemView.findViewById(R.id.iv_thumb);
            mTvCategoryDuration = (TextView) itemView.findViewById(R.id.iv_time);
            mTvDelete = (TextView) itemView.findViewById(R.id.iv_delete);
            mIvEditName = (ImageView)itemView.findViewById(R.id.iv_edit_name);
            mIvEditDesc = (ImageView)itemView.findViewById(R.id.iv_edit_desc);
            mCvMain = (CardView)itemView.findViewById(R.id.mainView);


        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mListCategories.get(position).getVideo_path(),
                    MediaStore.Images.Thumbnails.MINI_KIND);

            if(thumb == null){

                holder.mIvVideoThumbImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));

            }else {

                holder.mIvVideoThumbImg.setImageBitmap(thumb);

            }

            holder.mTvCategoryName.setText(mListCategories.get(position).getVideo_name());

            holder.mTvDesc.setText(mListCategories.get(position).getmDesc());

            holder.mTvCategoryDuration.setText(mListCategories.get(position).getmTime());

            if(!mListCategories.get(position).getmStatus().equalsIgnoreCase("Available")){

                holder.mTvDelete.setVisibility(View.VISIBLE);
            }

            holder.mIvEditName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showCustomDialog(mContext.getResources().getString(R.string.rename),mListCategories.get(position).getVideo_name(),mContext.getResources().getString(R.string.ok),mContext.getResources().getString(R.string.cancel), Constants.RENAME_FILE,position);

                }
            });

            holder.mIvEditDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showCustomDialog(mContext.getResources().getString(R.string.edit),mListCategories.get(position).getmDesc(),mContext.getResources().getString(R.string.ok),mContext.getResources().getString(R.string.cancel), Constants.RENAME_DATA,position);

                }
            });
            holder.mTvDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPopUp(mContext.getResources().getString(R.string.Delete_Item),"Are U sure want to delete an item ,this will also remove data from your storage","ok","cancel",position);

                }
            });

            holder.mTvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showCustomDialog(mContext.getResources().getString(R.string.share)+" " +getAppName(mContext),"Hey check out my app at: https://play.google.com/store/apps/details?id=" + mContext.getPackageName(),mContext.getResources().getString(R.string.share),mContext.getResources().getString(R.string.Later),Constants.SHARE_APP,position);

                  //  showShareDialog();
                }
            });

            holder.mCvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File playFile = new File(mListCategories.get(position).getVideo_path());
                    intent.setDataAndType(Uri.fromFile(playFile), "video/mp4");
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);

        }
    }

    private void showShareDialog() {


        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View v = layoutInflater.inflate(R.layout.custum_share_dailog, null);

        MaterialDialog.Builder  materialDialog = new MaterialDialog.Builder((Activity)mContext)
                .title(mContext.getResources().getString(R.string.share)+" " +getAppName(mContext))
                .customView(v,true)


                .negativeText(mContext.getResources().getString(R.string.Later))
                .positiveText(mContext.getResources().getString(R.string.share))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Intent shareIntent = new Intent();

                        shareIntent.setAction(Intent.ACTION_SEND);

                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                message_editText.getText().toString());

                        shareIntent.setType("text/plain");

                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        try {

                            mContext.startActivity(shareIntent);

                        }catch (Exception e){

                            e.printStackTrace();

                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        dialog.dismiss();

                    }
                });

        message_editText = (EditText)v.findViewById(R.id.iv_share_message);

        message_editText.setText("Hey check out my app at: https://play.google.com/store/apps/details?id=" + mContext.getPackageName());

        message_editText.setSelection(message_editText.getText().length());

        materialDialog.show();

    }

    private void showCustomDialog(String title, final String content, final String postBtn, String  negBtn, final String action, final int position) {


        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View v = layoutInflater.inflate(R.layout.custum_share_dailog, null);

        MaterialDialog.Builder  materialDialog = new MaterialDialog.Builder((Activity)mContext)
                .title(title)
                .customView(v,true)


                .negativeText(negBtn)
                .positiveText(postBtn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        switch (action){

                            case Constants.RENAME_FILE:


                                renameFile(content,message_editText.getText().toString());

                                ContentValues contentData = loadContentValues(Integer.parseInt(mListCategories.get(position).getVideo_id()),message_editText.getText().toString()
                                        ,mListCategories.get(position).getmDesc(),mListCategories.get(position).getmTime(),Environment.getExternalStorageDirectory().toString()+"/Telecine/" +message_editText.getText().toString(),mListCategories.get(position).getmStatus());

                                updateData(contentData);

                                mListCategories.get(position).setVideo_name(message_editText.getText().toString());

                                mListCategories.get(position).setVideo_path(Environment.getExternalStorageDirectory().toString()+"/Telecine/" +message_editText.getText().toString());

                                notifyDataSetChanged();

                                break;

                            case Constants.RENAME_DATA:

                                ContentValues descriptionData = loadContentValues(Integer.parseInt(mListCategories.get(position).getVideo_id()),mListCategories.get(position).getmName()
                                        ,message_editText.getText().toString(),mListCategories.get(position).getmTime(),mListCategories.get(position).getVideo_name(),mListCategories.get(position).getmStatus());

                                updateData(descriptionData);

                                mListCategories.get(position).setmDesc(message_editText.getText().toString());

                                notifyDataSetChanged();
                                
                                break;

                            case Constants.SHARE_APP:

                                Intent shareIntent = new Intent();

                                shareIntent.setAction(Intent.ACTION_SEND);

                                shareIntent.putExtra(Intent.EXTRA_TEXT,
                                        message_editText.getText().toString());

                                shareIntent.setType("text/plain");

                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                try {

                                    mContext.startActivity(shareIntent);

                                }catch (Exception e){

                                    e.printStackTrace();



                                }
                                break;

                            default:
                                break;




                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        dialog.dismiss();

                    }
                });

        message_editText = (EditText)v.findViewById(R.id.iv_share_message);

        message_editText.setText(content);

        message_editText.setSelection(message_editText.getText().length());

        materialDialog.show();

    }

    private void updateData(ContentValues contentValues) {

        mRecorderDataBase.updateData(contentValues, RecorderDatabaseUtility.TABLE_HISTORY,RecorderDatabaseUtility.KEY_ID+"=" +contentValues.get(RecorderDatabaseUtility.KEY_ID),null);

    }

    @Override
    public int getItemCount() {//return array.size
        return mListCategories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public static String getAppName(Context context){
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        return applicationName;
    }


    public void showPopUp(String title, String content, String positiveButton, String negativeButton, final int position){

        MaterialDialog.Builder  materialDialog = new MaterialDialog.Builder((Activity)mContext)
                .title(title)
                .content(content)
                .positiveText(positiveButton)


                .negativeText(negativeButton)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                       deleteItem(position);

                        mListCategories.remove(position);

                        notifyDataSetChanged();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        dialog.dismiss();

                    }
                });

        materialDialog.show();

    }

    public void renameFile(String oldName,String newName){

        String sdcard_path  = Environment.getExternalStorageDirectory().toString()+"/Telecine/";



        File oldFile = new File(sdcard_path,oldName);

        if(oldFile.exists()) {
            File newFile = new File(sdcard_path, newName);
          Boolean success =   oldFile.renameTo(newFile);
            if(success){
                Log.i("Note","true");
            }
        }
    }


    public void deleteItem(int position){

        mRecorderDataBase.deleteData(RecorderDatabaseUtility.TABLE_HISTORY,RecorderDatabaseUtility.KEY_ID+"="+mListCategories.get(position).getVideo_id(),null);

    }

    private ContentValues loadContentValues(int id, String name, String test, String time, String path, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecorderDatabaseUtility.KEY_ID,id);

        contentValues.put(RecorderDatabaseUtility.KEY_NAME,name);
        contentValues.put(RecorderDatabaseUtility.KEY_DESC,test);
        contentValues.put(RecorderDatabaseUtility.KEY_TIME,time);
        contentValues.put(RecorderDatabaseUtility.KEY_VIDEO_PATH,path);

        contentValues.put(RecorderDatabaseUtility.KEY_STATUS,status);


        return contentValues;
    }


}

