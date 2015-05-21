package com.ntuedu.homeworktimemanager.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccountDaoImpl implements AccountDao {


	private DBOpenHelper dbOpenHelper;

	public AccountDaoImpl(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	@Override
	public void addStudent(Student student) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into student(Sno, Sname) values(?,?)", new Object[] {
				student.getsNo(), student.getsName() });
	}

	@Override
	public void clearStudent() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("DELETE FROM student");
	}

	@Override
	public Student lookupStudent() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("student", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			String sNo = cursor.getString(cursor.getColumnIndex("Sno"));
			String sName = cursor.getString(cursor.getColumnIndex("Sname"));
			Student student = new Student(sNo, sName);

			return student;
		}

		return null;
	}
}
