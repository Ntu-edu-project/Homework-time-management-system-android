package com.ntuedu.homeworktimemanager.dao;


public interface AccountDao {

	public void addStudent(Student student);

	public void clearStudent();

	public Student lookupStudent();

}
