package com.ntuedu.homeworktimemanager.model;

import java.sql.Date;

public class Grades {

	public Grades() {
		// TODO Auto-generated constructor stub
	}

	private Date date;
	private int score;
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
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setScore(int score) {
		this.score = score;
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

	public Grades(Date date, int score, String subject) {
		super();
		this.date = date;
		this.score = score;
		this.subject = subject;
	}

}
