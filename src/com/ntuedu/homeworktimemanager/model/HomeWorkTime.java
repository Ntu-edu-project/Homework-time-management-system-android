package com.ntuedu.homeworktimemanager.model;

import java.sql.Date;

public class HomeWorkTime {

	public HomeWorkTime() {
		// TODO Auto-generated constructor stub
	}

	private Date date;
	private int time;
	private String subject;

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public HomeWorkTime(Date date, int time, String subject) {
		super();
		this.date = date;
		this.time = time;
		this.subject = subject;
	}

}
