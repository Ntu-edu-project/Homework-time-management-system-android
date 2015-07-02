package com.ntuedu.homeworktimemanager.model;

public class Student {
	private String sNo;
	private String sName;
	private String tel;
	private int gradeNo;
	private int classNo;

	public Student() {
		super();

	}

	public Student(String sNo, String sName, String tel, int gradeNo, int classNo) {
		super();
		this.sNo = sNo;
		this.sName = sName;
		this.tel = tel;
		this.gradeNo = gradeNo;
		this.classNo = classNo;	
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

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel
	 *            the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the gradeNo
	 */
	public int getGradeNo() {
		return gradeNo;
	}

	/**
	 * @param gradeNo
	 *            the gradeNo to set
	 */
	public void setGradeNo(int gradeNo) {
		this.gradeNo = gradeNo;
	}

	/**
	 * @return the classNo
	 */
	public int getClassNo() {
		return classNo;
	}

	/**
	 * @param classNo
	 *            the classNo to set
	 */
	public void setClassNo(int classNo) {
		this.classNo = classNo;
	}

}
