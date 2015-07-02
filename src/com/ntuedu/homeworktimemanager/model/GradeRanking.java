package com.ntuedu.homeworktimemanager.model;

public class GradeRanking {

	public GradeRanking() {
		// TODO Auto-generated constructor stub
	}

	public GradeRanking(String testNo, int chineseNo, int mathNo, int englishNo) {
		super();
		this.testNo = testNo;
		this.chineseNo = chineseNo;
		this.mathNo = mathNo;
		this.englishNo = englishNo;
	}

	private String testNo;
	private int chineseNo;
	private int mathNo;
	private int englishNo;
	/**
	 * @return the testNo
	 */
	public String getTestNo() {
		return testNo;
	}
	/**
	 * @param testNo
	 *            the testNo to set
	 */
	public void setTestNo(String testNo) {
		this.testNo = testNo;
	}
	/**
	 * @return the chineseNo
	 */
	public int getChineseNo() {
		return chineseNo;
	}
	/**
	 * @param chineseNo
	 *            the chineseNo to set
	 */
	public void setChineseNo(int chineseNo) {
		this.chineseNo = chineseNo;
	}
	/**
	 * @return the mathNo
	 */
	public int getMathNo() {
		return mathNo;
	}
	/**
	 * @param mathNo
	 *            the mathNo to set
	 */
	public void setMathNo(int mathNo) {
		this.mathNo = mathNo;
	}
	/**
	 * @return the englishNo
	 */
	public int getEnglishNo() {
		return englishNo;
	}
	/**
	 * @param englishNo
	 *            the englishNo to set
	 */
	public void setEnglishNo(int englishNo) {
		this.englishNo = englishNo;
	}

}
