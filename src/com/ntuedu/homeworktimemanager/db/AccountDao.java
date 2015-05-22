package com.ntuedu.homeworktimemanager.db;

import com.ntuedu.homeworktimemanager.model.Student;


public interface AccountDao {

	public void addStudent(Student student);

	public void clearStudent();

	public Student lookupStudent();

}
