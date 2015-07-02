package com.ntuedu.homeworktimemanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntuedu.homeworktimemanager.model.Student;

public class AccountDaoImpl implements AccountDao {

	private DBOpenHelper dbOpenHelper;

	public AccountDaoImpl(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	@Override
	public void addStudent(Student student) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into student(Sno, Sname, Tel, GradeNo, ClassNo) values(?,?,?,?,?)",
				new Object[]{student.getsNo(), student.getsName(),
						student.getTel(), student.getGradeNo(),
						student.getClassNo()});
		db.close();
	}

	@Override
	public void clearStudent() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("DELETE FROM student");
		db.execSQL("DELETE FROM homeworktime");
		db.execSQL("DELETE FROM selftestgrades");
		db.close();
	}

	@Override
	public Student lookupStudent() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("student", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			String sNo = cursor.getString(cursor.getColumnIndex("Sno"));
			String sName = cursor.getString(cursor.getColumnIndex("Sname"));
			String tel = cursor.getString(cursor.getColumnIndex("Tel"));
			int gradeNo = cursor.getInt(cursor.getColumnIndex("GradeNo"));
			int classNo = cursor.getInt(cursor.getColumnIndex("ClassNo"));
			Student student = new Student(sNo, sName, tel, gradeNo, classNo);
			db.close();
			return student;
		}
		db.close();
		return null;
	}
}
