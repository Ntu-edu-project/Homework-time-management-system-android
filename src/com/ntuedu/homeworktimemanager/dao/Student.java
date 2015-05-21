package com.ntuedu.homeworktimemanager.dao;

public class Student {
	private String sNo;
	private String sName;

	public Student() {
		super();

	}

	public Student(String sNo, String sName) {
		super();
		this.sNo = sNo;
		this.sName = sName;
	}

	public String getsNo() {
		return sNo;
	}

	public void setsNo(String sNo) {
		this.sNo = sNo;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

}
