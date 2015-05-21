package com.ntuedu.homeworktimemanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "homeworktimemanager.db";

	// "CREATE TABLE IF NOT EXISTS Patient (id integer primary key autoincrement, Sno varchar(20), Sname varchar(45))"
	private String sqlStudent = "CREATE TABLE IF NOT EXISTS student (id integer primary key autoincrement, Sno varchar(20), Sname varchar(45))";
	private static final int VERSION = 1;

	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlStudent);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
