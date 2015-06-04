package com.ntuedu.homeworktimemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "homeworktimemanager.db";

	private String sqlStudent = "CREATE TABLE IF NOT EXISTS student (id integer primary key autoincrement, Sno varchar(20), Sname varchar(45))";
	private String sqlHomeWorkTime = "CREATE TABLE IF NOT EXISTS homeworktime (id integer primary key autoincrement, date Date, subject varchar(20), time int)";

	private static final int VERSION = 2;

	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlStudent);
		db.execSQL(sqlHomeWorkTime);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
		db.execSQL(sqlHomeWorkTime);
	}

}
