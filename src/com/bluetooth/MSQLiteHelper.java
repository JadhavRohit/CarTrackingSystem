package com.bluetooth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MSQLiteHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "bappdqq.db";
	public static final String TABLE_NAME = "device_locations";
	public static final int DB_VERSION = 1;
	public static final String CREATE_TABLE = "create table "+TABLE_NAME+"(_id integer primary key autoincrement, latitude double not null , longitude double not null);";
	public MSQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
