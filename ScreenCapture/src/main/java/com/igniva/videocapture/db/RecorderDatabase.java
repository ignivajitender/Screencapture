package com.igniva.videocapture.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class RecorderDatabase
		extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "RecorderDatabase.db";
	private SQLiteDatabase sqliteDatabase;

	private boolean upgrade = false;

	private RecorderDatabase(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public RecorderDatabase(Context context) {

		//this(context, DATABASE_NAME, null, DATABASE_VERSION);
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase androidDatabase) {
		androidDatabase.execSQL(RecorderDatabaseUtility.CREATE_TABLE_LOGIN);
	}

	@Override
	public void onUpgrade(SQLiteDatabase androidDatabase, int oldVersion, int newVersion) {
	
		dropTables(androidDatabase);
		onCreate(androidDatabase);
	}

	private void dropTables(SQLiteDatabase androidDatabase) {
		androidDatabase.execSQL(RecorderDatabaseUtility.DROP_TABLE_IF_EXISTS + RecorderDatabaseUtility.TABLE_LOGIN);
	}

	public void openDatabase() {
		sqliteDatabase = this.getWritableDatabase();
	}

	public synchronized void closeDatabase() {
		if (sqliteDatabase != null) {
			sqliteDatabase.close();
			sqliteDatabase = null;
		}
	}

	public List<Long> insertData(List<ContentValues> contentValues, String tableName) {
		sqliteDatabase.beginTransaction();
		List<Long> idList = new ArrayList<Long>();
		try {
			for (ContentValues contentValue : contentValues) {
				idList.add(insertData(contentValue, tableName));
			}
			sqliteDatabase.setTransactionSuccessful();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} finally {
			sqliteDatabase.endTransaction();
		}
		return idList;
	}

	public long insertData(ContentValues contentValues, String tableName) {
		long insert = sqliteDatabase.insert(tableName, null, contentValues);
		return insert;
	}

	public Cursor getData(String query) {
		Cursor cursor = sqliteDatabase.rawQuery(query, null);
		return cursor;

	}

	public Cursor selectData(String query, String[] value) {
		Cursor cursor = sqliteDatabase.rawQuery(query, value);
		return cursor;

	}

	public Cursor getData(String tableName, String[] columns) {
		Cursor cursor = sqliteDatabase.query(tableName, columns, null, null, null, null, null);
		return cursor;
	}

	public Cursor getColumnFromTable(String table, String[] columns, String selection, String selectionArgs[], String groupBy, String having, String orderBy) {
		Cursor cursor = sqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;

	}

	public long updateData(ContentValues contentValues, String tableName) {
		long updateData = updateData(contentValues, tableName, null, null);
		return updateData;
	}

	public long updateData(ContentValues contentValues, String tableName, String whereClause, String[] whereArgs) {
		sqliteDatabase.beginTransaction();
		long update = sqliteDatabase.update(tableName, contentValues, whereClause, whereArgs);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();

		return update;
	}

	public void updateData(List<ContentValues> contentValues, String tableName, String whereClause, String[] whereArgs) {
		sqliteDatabase.beginTransaction();

		try {
			for (ContentValues contentValue : contentValues) {
				updateData(contentValue, tableName, whereClause, whereArgs);
			}
			sqliteDatabase.setTransactionSuccessful();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} finally {
			sqliteDatabase.endTransaction();
		}

	}

	public long deleteData(String tableName) {
		sqliteDatabase.beginTransaction();
		int delete = sqliteDatabase.delete(tableName, null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return delete;
	}

	public long deleteData(String tableName, String whereClause, String[] whereArgs) {
		sqliteDatabase.beginTransaction();
		int delete = sqliteDatabase.delete(tableName, whereClause, whereArgs);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return delete;
	}

	public long deleteData(String tableName, String where) {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return deleteData(tableName, where, null);
	}

	public void deleteDataFromAllTables() {
		
		//String ALL_TABLES_QUERY = "SELECT name FROM sqlite_master WHERE type='table'";
		Cursor cursor = getData(RecorderDatabaseUtility.ALL_TABLES_QUERY);

		cursor.moveToFirst();

		do {
			String tableName = cursor.getString(cursor.getColumnIndex(RecorderDatabaseUtility.NAME));
				deleteData(tableName);
		
		} while (cursor.moveToNext());
		cursor.close();
	}

}
