package com.ntuedu.homeworktimemanager.fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.anjuke.library.uicomponent.chart.bessel.BesselChart;
import com.anjuke.library.uicomponent.chart.bessel.ChartData.LabelTransform;
import com.anjuke.library.uicomponent.chart.bessel.Point;
import com.anjuke.library.uicomponent.chart.bessel.Series;
import com.ntuedu.homeworktimemanager.Constant;
import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.db.AccountDao;
import com.ntuedu.homeworktimemanager.db.AccountDaoImpl;
import com.ntuedu.homeworktimemanager.db.HomeWorkTimeDao;
import com.ntuedu.homeworktimemanager.db.HomeWorkTimeDaoImpl;
import com.ntuedu.homeworktimemanager.model.HomeWorkTime;
import com.ntuedu.homeworktimemanager.util.MyHttpClient;
import com.ntuedu.homeworktimemanager.util.OtherUtils;

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

	private String sno;

	private BesselChart chart;

	private ViewPager mViewPager;

	private Context ct;

	private HomeWorkTimeDao homeWorkTimeDao;

	// 默认获取全部信息
	private String serviceFlag = "getAll";

	private String subject;
	private int time;
	private String showName;
	private String message;

	public HomeWorkTimeFrg(ViewPager mViewPager, String sno) {
		// TODO Auto-generated constructor stub
		// 获取mViewPager以拦截
		this.mViewPager = mViewPager;
		this.sno = sno;
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

		ct = getActivity();

		homeWorkTimeDao = new HomeWorkTimeDaoImpl(ct);

		initView(view);

		refreshPushUI();

		return view;

	}

	// 如果今日已经提交设置控件不可用状态
	private void refreshPushUI() {

		boolean isChinesePush = homeWorkTimeDao.todayHavePush(
				Date.valueOf(getFormatDate()), "chinese");
		boolean isMathPush = homeWorkTimeDao.todayHavePush(
				Date.valueOf(getFormatDate()), "math");
		boolean isEnglishPush = homeWorkTimeDao.todayHavePush(
				Date.valueOf(getFormatDate()), "english");

		int chineseTime = isChinesePush ? homeWorkTimeDao.getTodayTime(
				Date.valueOf(getFormatDate()), "chinese") : 0;
		int mathTime = isMathPush ? homeWorkTimeDao.getTodayTime(
				Date.valueOf(getFormatDate()), "math") : 0;
		int englishTime = isEnglishPush ? homeWorkTimeDao.getTodayTime(
				Date.valueOf(getFormatDate()), "english") : 0;

		btChinese.setEnabled(!isChinesePush);
		cbChinese.setEnabled(!isChinesePush);
		etHourChinese.setText(chineseTime / 60 + "");
		etMinuteChinese.setText(chineseTime % 60 + "");
		etHourChinese.setEnabled(!isChinesePush);
		etMinuteChinese.setEnabled(!isChinesePush);

		btMath.setEnabled(!isMathPush);
		cbMath.setEnabled(!isMathPush);
		etHourMath.setText(mathTime / 60 + "");
		etMinuteMath.setText(mathTime % 60 + "");
		etHourMath.setEnabled(!isChinesePush);
		etMinuteMath.setEnabled(!isChinesePush);

		btEnglish.setEnabled(!isEnglishPush);
		cbEnglish.setEnabled(!isEnglishPush);
		etHourEnglish.setText(englishTime / 60 + "");
		etMinuteEnglish.setText(englishTime % 60 + "");
		etHourEnglish.setEnabled(!isChinesePush);
		etMinuteEnglish.setEnabled(!isChinesePush);
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
				return String.format("%.1f时", (valueY * 1.0f / 60));
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

	String getValue(String key) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("english", OtherUtils.getResourcesString(ct, R.string.english));
		map.put("math", OtherUtils.getResourcesString(ct, R.string.math));
		map.put("chinese", OtherUtils.getResourcesString(ct, R.string.chinese));
		map.put("1", OtherUtils.getResourcesString(ct, R.string.show_name));
		map.put("0", OtherUtils.getResourcesString(ct, R.string.no_name));
		return map.get(key);
	}

	String formatTime(int time) {
		return (time / 60) + "时" + (time % 60) + "分";
	}

	// dialog确认提示
	@SuppressLint("InflateParams")
	public void showConfirmDialog(String subject, int time, String showName) {

		AlertDialog.Builder alert = new AlertDialog.Builder(ct);

		LayoutInflater layoutInflater = LayoutInflater.from(ct);
		View myDialog = layoutInflater.inflate(R.layout.mydialog, null);

		TextView tv = (TextView) myDialog.findViewById(R.id.mydialog_tv);
		final EditText et = (EditText) myDialog.findViewById(R.id.mydialog_et);

		tv.setText("课程:" + getValue(subject) + "\n时间:" + formatTime(time)
				+ "\n匿名:" + getValue(showName));

		alert.setView(myDialog);
		alert.setPositiveButton(getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						message = et.getText().toString();

						new TimeServices().execute();
					}
				});
		alert.setNegativeButton(getString(android.R.string.cancel), null);
		alert.show();

	}

	View.OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			serviceFlag = "pushTime";
			if (v == btChinese) {
				subject = "chinese";
				time = getHomeWorkTime(etHourChinese, etMinuteChinese);
				showName = cbChinese.isChecked() ? "0" : "1";
			}
			if (v == btMath) {
				subject = "math";
				time = getHomeWorkTime(etHourMath, etMinuteMath);
				showName = cbMath.isChecked() ? "0" : "1";
			}
			if (v == btEnglish) {
				subject = "english";
				time = getHomeWorkTime(etHourEnglish, etMinuteEnglish);
				showName = cbEnglish.isChecked() ? "0" : "1";
			}
			showConfirmDialog(subject, time, showName);

		}
	};

	@SuppressWarnings("deprecation")
	public int pushTime() {
		String result = "0";
		try {
			HttpPost httpRequest = new HttpPost(Constant.SERVER_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("flag", serviceFlag));
			params.add(new BasicNameValuePair("sNo", sno));
			params.add(new BasicNameValuePair("message", message));
			params.add(new BasicNameValuePair("subject", subject));
			params.add(new BasicNameValuePair("time", String.valueOf(time)));
			params.add(new BasicNameValuePair("showname", showName));

			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse httpResponse = new MyHttpClient().getHttpClient(2000,
					2000).execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());

			}

			return result.equals("0") ? 0 : 1;
		} catch (Exception e) {
			return 2;
		}

	}

	private class TimeServices extends AsyncTask<Object, Integer, Integer> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			if (serviceFlag.equals("pushTime")) {
				return pushTime();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Integer result) {
			if (serviceFlag.equals("pushTime")) {
				switch (result) {
				case 0:
					Toast.makeText(
							ct,
							OtherUtils.getResourcesString(ct,
									R.string.have_submit), 1000).show();
					break;
				case 1:
					HomeWorkTime homeWorkTime = new HomeWorkTime(
							Date.valueOf(getFormatDate()), time, subject);
					homeWorkTimeDao.addTime(homeWorkTime);
					refreshPushUI();
					Toast.makeText(
							ct,
							OtherUtils.getResourcesString(ct,
									R.string.submit_success), 1000).show();
					break;
				default:
					Toast.makeText(
							ct,
							OtherUtils.getResourcesString(ct,
									R.string.net_error), 1000).show();
					break;
				}
			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {

		}

	}

	@SuppressLint("SimpleDateFormat")
	String getFormatDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
}
