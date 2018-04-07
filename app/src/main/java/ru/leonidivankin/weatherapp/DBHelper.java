package ru.leonidivankin.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	//COLUMNS
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	//DB PROPERTIES
	public static final String DB_NAME = "cities.db";
	public static final String TABLE_NAME = "CITIES";
	public static final int DB_VERSION = '1';
	//default cities
	public static final String[] DEFAULT_CITIES = {"Saint Petersburg", "Moscow", "Minsk"};


	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE "
					+ TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COLUMN_NAME + " TEXT);");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new IllegalStateException("We do not support another version of db");
	}
}
