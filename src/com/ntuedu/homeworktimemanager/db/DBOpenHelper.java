package com.ntuedu.homeworktimemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "homeworktimemanager.db";

	private String sqlStudent = "CREATE TABLE IF NOT EXISTS student (id integer primary key autoincrement, Sno varchar(20), Sname varchar(45), Tel varchar(20), GradeNo int, ClassNo int)";
	private String sqlHomeWorkTime = "CREATE TABLE IF NOT EXISTS homeworktime (id integer primary key autoincrement, date Date, subject varchar(20), time int)";
	private String sqlGrades = "CREATE TABLE IF NOT EXISTS selftestgrades (id integer primary key autoincrement, date Date, subject varchar(20), scores int)";

	private static final int VERSION = 3;

	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlStudent);
		db.execSQL(sqlHomeWorkTime);
		db.execSQL(sqlGrades);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);

	}

}
