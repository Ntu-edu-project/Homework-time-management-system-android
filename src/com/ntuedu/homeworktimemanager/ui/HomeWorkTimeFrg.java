package com.ntuedu.homeworktimemanager.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anjuke.library.uicomponent.chart.bessel.BesselChart;
import com.anjuke.library.uicomponent.chart.bessel.ChartData.LabelTransform;
import com.anjuke.library.uicomponent.chart.bessel.Point;
import com.anjuke.library.uicomponent.chart.bessel.Series;
import com.ntuedu.homeworktimemanager.R;

@SuppressLint({ "ShowToast", "ClickableViewAccessibility" })
public class HomeWorkTimeFrg extends Fragment {

	private EditText etHourChinese;
	private EditText etMinuteChinese;
	private CheckBox cbChinese;
	private Button btChinese;

	private EditText etHourMath;
	private EditText etMinuteMath;
	private CheckBox cbMath;
	private Button btMath;

	private EditText etHourEnglish;
	private EditText etMinuteEnglish;
	private CheckBox cbEnglish;
	private Button btEnglish;

	private BesselChart chart;

	private ViewPager mViewPager;

	// 推送科目标记
	private String pushFlag;

	public HomeWorkTimeFrg(ViewPager mViewPager) {
		// TODO Auto-generated constructor stub
		// 获取mViewPager以拦截
		this.mViewPager = mViewPager;
	}

	public HomeWorkTimeFrg() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.homework_time_frg, container,
				false);

		initView(view);
		return view;

	}

	/**
	 * @param v
	 */
	private void initView(View v) {

		etHourChinese = (EditText) v.findViewById(R.id.et_hour_chinese);
		etMinuteChinese = (EditText) v.findViewById(R.id.et_minute_chinese);
		cbChinese = (CheckBox) v.findViewById(R.id.cb_chinese);
		btChinese = (Button) v.findViewById(R.id.bt_chinese);

		etHourMath = (EditText) v.findViewById(R.id.et_hour_math);
		etMinuteMath = (EditText) v.findViewById(R.id.et_minute_math);
		cbMath = (CheckBox) v.findViewById(R.id.cb_math);
		btMath = (Button) v.findViewById(R.id.bt_math);

		etHourEnglish = (EditText) v.findViewById(R.id.et_hour_english);
		etMinuteEnglish = (EditText) v.findViewById(R.id.et_minute_english);
		cbEnglish = (CheckBox) v.findViewById(R.id.cb_english);
		btEnglish = (Button) v.findViewById(R.id.bt_english);

		btChinese.setOnClickListener(listener);
		btMath.setOnClickListener(listener);
		btEnglish.setOnClickListener(listener);

		chart = (BesselChart) v.findViewById(R.id.chart);
		chart.setSmoothness(0);

		// 拦截mViewPager的滑动事件
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.front_layout);
		layout.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_CANCEL:
					mViewPager.requestDisallowInterceptTouchEvent(false);
					break;
				default:
					mViewPager.requestDisallowInterceptTouchEvent(true);
				}

				return false;
			}
		});
		getSeriesList();
	}

	private void getSeriesList() {
		List<Series> seriess = new ArrayList<Series>();
		seriess.add(getRandomSeries("语文", Color.LTGRAY));
		seriess.add(getRandomSeries("数学", Color.GRAY));
		seriess.add(getRandomSeries("英语", Color.RED));

		chart.getData().setLabelTransform(new LabelTransform() {

			@Override
			public String verticalTransform(int valueY) {
				return String.format("%.1f时", (valueY *1.0f  / 60));
			}

			@Override
			public String horizontalTransform(int valueX) {
				return String.format("%s日", valueX);
			}

			@Override
			public boolean labelDrawing(int valueX) {
				return true;
			}

 
		});

		chart.getData().setSeriesList(seriess);
		chart.getData().setYLabels(0, 240, 60);
		chart.refresh(true);

	}

	private Series getRandomSeries(String title, int color) {
		List<Point> points = new ArrayList<Point>();
		Random random = new Random();

		for (int i = 0; i < 12; i++) {

			points.add(new Point(i + 1, random.nextInt(240), true));

		}

		for (Point point : points) {
			com.anjuke.library.uicomponent.chart.bessel.Log
					.d("getRandomSeries valueY=" + point.valueY);
		}
		return new Series(title, color, points);
	}

	public int getHomeWorkTime(EditText h, EditText m) {

		int hour = Integer.parseInt(h.getText().toString().isEmpty() ? "0" : h
				.getText().toString());
		int minute = Integer.parseInt(m.getText().toString().isEmpty() ? "0"
				: m.getText().toString());

		return hour * 60 + minute;

	}

	View.OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			int time = 0;
			int showName = 0;
			if (v == btChinese) {
				pushFlag = "chinese";
				time = getHomeWorkTime(etHourChinese, etMinuteChinese);
				showName = cbChinese.isChecked() ? 0 : 1;
			}
			if (v == btMath) {
				pushFlag = "math";
				time = getHomeWorkTime(etHourMath, etMinuteMath);
				showName = cbMath.isChecked() ? 0 : 1;
			}
			if (v == btEnglish) {
				pushFlag = "english";
				time = getHomeWorkTime(etHourEnglish, etMinuteEnglish);
				showName = cbEnglish.isChecked() ? 0 : 1;
			}

			Toast.makeText(getActivity(),
					pushFlag + ":" + time + ":" + showName, 1000).show();

		}
	};

}
