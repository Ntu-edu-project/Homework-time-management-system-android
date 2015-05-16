package com.ntuedu.homeworktimemanager.service;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class UIUtil {

	// 设置全屏
	public static void setFullScreen(Context context) {
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window window = ((Activity) context).getWindow();
		window.setFlags(flag, flag);
	}

	// 设置无标题栏
	public static void setNoTitle(Context context) {
		((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	// 背光常亮
	public static void keepScreenOn(Context context) {
		((Activity) context).getWindow().addFlags(
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

}