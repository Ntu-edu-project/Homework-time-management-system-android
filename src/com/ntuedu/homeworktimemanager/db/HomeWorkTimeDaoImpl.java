package com.ntuedu.homeworktimemanager.db;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.SliceValue;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

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
				new Object[]{homeWorkTime.getDate(), homeWorkTime.getSubject(),
						homeWorkTime.getTime()});
		db.close();
	}

	@Override
	public boolean todayHavePush(Date date, String subject) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("homeworktime", null, "date=? and subject=?",
				new String[]{date.toString(), subject}, null, null, null);
		boolean tmp = cursor.moveToFirst();
		db.close();
		return tmp;
	}

	@Override
	public int getTodayTime(Date date, String subject) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("homeworktime", null, "date=? and subject=?",
				new String[]{date.toString(), subject}, null, null, null);

		if (cursor.moveToFirst()) {
			int tmp = cursor.getInt(3);
			db.close();
			return tmp;
		}
		db.close();
		return -1;

	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("homeworktime", null, null, null, null, null,
				null);

		boolean tmp = !cursor.moveToFirst();

		db.close();

		return tmp;

	}

	@Override
	public ArrayList<HomeWorkTime> getTimeByMonth(String subject) {
		// TODO Auto-generated method stub

		Calendar cal = Calendar.getInstance();

		String thisMonth = Date.valueOf(
				cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1)
						+ "-01").toString();
		String nextMonth = Date.valueOf(
				cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 2)
						+ "-01").toString();

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		Cursor cursor = db.query("homeworktime", null,
				"subject = ? AND date BETWEEN  ? AND ?", new String[]{subject,
						thisMonth, nextMonth}, null, null, "date");

		if (!cursor.moveToFirst()) {
			db.close();
			return null;
		}

		ArrayList<HomeWorkTime> arrayList = new ArrayList<HomeWorkTime>();

		for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {

			arrayList.add(new HomeWorkTime(Date.valueOf(cursor.getString(1)),
					cursor.getInt(3), cursor.getString(2)));

		}
		db.close();
		return arrayList;
	}

	@Override
	public List<SliceValue> getPieList() {
		// TODO Auto-generated method stub
		List<SliceValue> values = new ArrayList<SliceValue>();

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("homeworktime", new String[]{"AVG(time)"},
				"subject='ÓïÎÄ'", null, null, null, null);

		if (cursor.moveToFirst()) {
			float tmp = cursor.getFloat(cursor.getColumnIndex("AVG(time)"));
			values.add(new SliceValue(tmp, Color.parseColor("#FF7F24")));

		}

		cursor = db.query("homeworktime", new String[]{"AVG(time)"},
				"subject='ÊýÑ§'", null, null, null, null);

		if (cursor.moveToFirst()) {
			float tmp = cursor.getFloat(cursor.getColumnIndex("AVG(time)"));
			values.add(new SliceValue(tmp, Color.parseColor("#6495ED")));

		}

		cursor = db.query("homeworktime", new String[]{"AVG(time)"},
				"subject='Ó¢Óï'", null, null, null, null);

		if (cursor.moveToFirst()) {
			float tmp = cursor.getFloat(cursor.getColumnIndex("AVG(time)"));
			values.add(new SliceValue(tmp, Color.parseColor("#5F9EA0")));

		}

		db.close();

		return values;
	}
}
