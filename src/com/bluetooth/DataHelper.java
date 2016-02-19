package com.bluetooth;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DataHelper {
	private SQLiteDatabase database;
	private MSQLiteHelper mdatabase;
	Context context;
	
	public DataHelper(Context context){
		this.context = context;
		mdatabase = new MSQLiteHelper(context);
		this.database = mdatabase.getWritableDatabase();
	}
	
	public void insert(double latitude, double longitude){
		ContentValues values = new ContentValues();
		values.put("longitude", longitude);
		values.put("latitude", latitude);
		
		
		database.insert(MSQLiteHelper.TABLE_NAME, null, values);
	}
	

	public Object[] fetchLocationInfo() throws SQLException {
		Object result [] = new Object[2];
		Cursor mCursor = database.query(MSQLiteHelper.TABLE_NAME, new String[] {"longitude", "latitude"},null, null, null, null, null, null);
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			do{
				result [0] = mCursor.getDouble(0);
				result [1] = mCursor.getDouble(1);
				//Toast.makeText(context, "Location: "+result[0]+": "+result[1] , 200).show();
			}while(mCursor.moveToNext());
		}
		
		if (mCursor != null ){
			mCursor.close();
		}
		return result;
	}

	public void delete() {
		database.execSQL("delete from "+MSQLiteHelper.TABLE_NAME);
	}
	public void update(double latitude, double longitude){
		ContentValues values = new ContentValues();
		values.put("longitude", longitude);
		values.put("latitude", latitude);
		
		database.update(MSQLiteHelper.TABLE_NAME, values, null, null);
	}
}
