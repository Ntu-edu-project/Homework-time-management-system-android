package com.ntuedu.homeworktimemanager.db;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntuedu.homeworktimemanager.model.Grades;

public class SelfTestGradesDaoImpl implements SelfTestGradesDao {

	private DBOpenHelper dbOpenHelper;

	public SelfTestGradesDaoImpl(Context context) {
		// TODO Auto-generated constructor stub
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	@Override
	public ArrayList<Grades> getGradesByMonth(String subject) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();

		String thisMonth = Date.valueOf(
				cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1)
						+ "-01").toString();
		String nextMonth = Date.valueOf(
				cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 2)
						+ "-01").toString();

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		Cursor cursor = db.query("selftestgrades", null,
				"subject = ? AND date BETWEEN  ? AND ?", new String[]{subject,
						thisMonth, nextMonth}, null, null, "date");

		if (!cursor.moveToFirst()) {
			db.close();
			return null;
		}

		ArrayList<Grades> arrayList = new ArrayList<Grades>();

		for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {

			arrayList.add(new Grades(Date.valueOf(cursor.getString(1)), cursor
					.getInt(3), cursor.getString(2)));

		}
		db.close();
		return arrayList;

	}

	@Override
	public void addGrades(Grades grades) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into selftestgrades(date, subject, scores) values(?,?,?)",
				new Object[]{grades.getDate(), grades.getSubject(),
						grades.getScore()});
		db.close();

	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("selftestgrades", null, null, null, null,
				null, null);

		boolean tmp = !cursor.moveToFirst();

		db.close();

		return tmp;
	}

	@Override
	public boolean todayHavePush(Date date, String subject) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("selftestgrades", null,
				"date=? and subject=?", new String[]{date.toString(), subject},
				null, null, null);
		boolean tmp = cursor.moveToFirst();
		db.close();
		return tmp;
	}

	@Override
	public int getTodayGrades(Date date, String subject) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("selftestgrades", null,
				"date=? and subject=?", new String[]{date.toString(), subject},
				null, null, null);

		if (cursor.moveToFirst()) {
			int tmp = cursor.getInt(3);
			db.close();
			return tmp;
		}
		db.close();
		return -1;
	}

}
