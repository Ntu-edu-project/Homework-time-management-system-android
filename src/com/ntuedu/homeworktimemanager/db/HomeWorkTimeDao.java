package com.ntuedu.homeworktimemanager.db;

import java.sql.Date;

import com.ntuedu.homeworktimemanager.model.HomeWorkTime;

public interface HomeWorkTimeDao {

	public void addTime(HomeWorkTime homeWorkTime);

	/** 判断今日subject是否已经提交 */
	public boolean todayHavePush(Date date, String subject);

	/** 获取今日subject的时间 */
	public int getTodayTime(Date date, String subject);
}
