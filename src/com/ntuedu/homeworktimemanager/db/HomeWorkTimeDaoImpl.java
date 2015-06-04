package com.ntuedu.homeworktimemanager.db;

import java.sql.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntuedu.homeworktimemanager.model.HomeWorkTime;

public class HomeWorkTimeDaoImpl implements HomeWorkTimeDao {

	private DBOpenHelper dbOpenHelper;

	public HomeWorkTimeDaoImpl(Context context) {
		// TODO Auto-generated constructor stub
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	@Override
	public void addTime(HomeWorkTime homeWorkTime) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into homeworktime(date, subject, time) values(?,?,?)",
				new Object[] { homeWorkTime.getDate(),
						homeWorkTime.getSubject(), homeWorkTime.getTime() });
		db.close();
	}

	@Override
	public boolean todayHavePush(Date date, String subject) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("homeworktime", null, "date=? and subject=?",
				new String[] { date.toString(), subject }, null, null, null);
		return cursor.moveToFirst();
	}

	@Override
	public int getTodayTime(Date date, String subject) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("homeworktime", null, "date=? and subject=?",
				new String[] { date.toString(), subject }, null, null, null);

		if (cursor.moveToFirst()) {
			return cursor.getInt(3);
		}

		return -1;

	}

}
