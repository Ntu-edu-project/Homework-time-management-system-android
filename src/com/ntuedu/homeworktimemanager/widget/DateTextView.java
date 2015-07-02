package com.ntuedu.homeworktimemanager.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.widget.TextView;

public class DateTextView extends TextView {

	public DateTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_TIME_TICK.equals(action)
					|| Intent.ACTION_TIME_CHANGED.equals(action)
					|| Intent.ACTION_TIMEZONE_CHANGED.equals(action)
					|| Intent.ACTION_LOCALE_CHANGED.equals(action)) {

				updateDate();

			}
		}
	};

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_LOCALE_CHANGED);
		getContext().registerReceiver(mIntentReceiver, filter, null, null);

		updateDate();
	}
	
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getContext().unregisterReceiver(mIntentReceiver);
	}

	void updateDate() {
		setText("≥…º®Ã·Ωª\t" + getFormatDate());
	}

	@SuppressLint("SimpleDateFormat")
	String getFormatDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
	
	
}
