package com.ntuedu.homeworktimemanager.db;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.SliceValue;

import com.ntuedu.homeworktimemanager.model.HomeWorkTime;

public interface HomeWorkTimeDao {

	public ArrayList<HomeWorkTime> getTimeByMonth(String subject);

	public void addTime(HomeWorkTime homeWorkTime);

	/** 判断今日subject是否已经提交 */
	public boolean isNull();

	/** 判断今日subject是否已经提交 */
	public boolean todayHavePush(Date date, String subject);

	/** 获取今日subject的时间 */
	public int getTodayTime(Date date, String subject);

	public List<SliceValue> getPieList();

}
