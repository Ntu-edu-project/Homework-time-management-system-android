package com.ntuedu.homeworktimemanager.db;

import java.sql.Date;
import java.util.ArrayList;

import com.ntuedu.homeworktimemanager.model.Grades;

public interface SelfTestGradesDao {

	public ArrayList<Grades> getGradesByMonth(String subject);

	public void addGrades(Grades grades);

	/** 判断今日subject分数是否已经提交 */
	public boolean isNull();

	/** 判断今日subject是否已经提交 */
	public boolean todayHavePush(Date date, String subject);

	/** 获取今日subject的分数*/
	public int getTodayGrades(Date date, String subject);
}
