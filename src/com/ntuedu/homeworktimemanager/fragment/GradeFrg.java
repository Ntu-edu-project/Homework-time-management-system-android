package com.ntuedu.homeworktimemanager.fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjuke.library.uicomponent.chart.bessel.BesselChart;
import com.anjuke.library.uicomponent.chart.bessel.ChartData.LabelTransform;
import com.anjuke.library.uicomponent.chart.bessel.Point;
import com.anjuke.library.uicomponent.chart.bessel.Series;
import com.ntuedu.homeworktimemanager.Constant;
import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.adapter.GradeRankingAdapter;
import com.ntuedu.homeworktimemanager.db.SelfTestGradesDao;
import com.ntuedu.homeworktimemanager.db.SelfTestGradesDaoImpl;
import com.ntuedu.homeworktimemanager.model.GradeRanking;
import com.ntuedu.homeworktimemanager.model.Grades;
import com.ntuedu.homeworktimemanager.util.MyHttpClient;

public class GradeFrg extends Fragment implements View.OnClickListener {

	private EditText et_grade_chinese;
	private EditText et_grade_math;
	private EditText et_grade_english;
	private Button bt_grade_chinese;
	private Button bt_grade_math;
	private Button bt_grade_english;

	String serviceFlag = "pushSelfTest";

	private SwipeRefreshLayout swipeRefreshLayout;

	private BesselChart chart;

	private String sno;
	private Context ct;

	private ViewPager mViewPager;

	private String subject;
	private int scores;

	private SharedPreferences preferences;
	private Editor editor;
	private static int MODE;
	private static final String PER_NAME = "grade";
	private GradeRankingAdapter adapter;
	private ListView gradeRankinglv;

	Handler handler = new Handler();
	ArrayList<GradeRanking> arrayList = new ArrayList<GradeRanking>();

	private SelfTestGradesDao selfTestGradesDao;

	public GradeFrg(ViewPager mViewPager, String sno) {
		// TODO Auto-generated constructor stub
		// 获取mViewPager以拦截
		this.mViewPager = mViewPager;
		this.sno = sno;
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

		View view = inflater.inflate(R.layout.grade_frg, container, false);

		ct = getActivity();

		selfTestGradesDao = new SelfTestGradesDaoImpl(ct);

		preferences = ct.getSharedPreferences(PER_NAME, MODE);
		editor = preferences.edit();

		initView(view);

		if (selfTestGradesDao.isNull()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getAllSelfTestGrades();
					getAllSchoolTestGradesRanking();
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							refreshPushUI();
							refreshChart();
							refreshGradeRankingView();
						}
					});

				}
			}).start();

		} else {
			refreshPushUI();
			refreshChart();
			refreshGradeRankingView();
		}
		return view;

	}
	private void initView(View v) {
		et_grade_chinese = (EditText) v.findViewById(R.id.et_grade_chinese);
		et_grade_math = (EditText) v.findViewById(R.id.et_grade_math);
		et_grade_english = (EditText) v.findViewById(R.id.et_grade_english);

		bt_grade_chinese = (Button) v.findViewById(R.id.bt_grade_chinese);
		bt_grade_math = (Button) v.findViewById(R.id.bt_grade_math);
		bt_grade_english = (Button) v.findViewById(R.id.bt_grade_english);

		// 成绩排名
		gradeRankinglv = (ListView) v.findViewById(R.id.grade_ranking_lv);
		doPraseRankingJson(preferences.getString("gradeRankingJson", ""));
		adapter = new GradeRankingAdapter(ct, arrayList);
		gradeRankinglv.setAdapter(adapter);

		bt_grade_chinese.setOnClickListener(this);
		bt_grade_math.setOnClickListener(this);
		bt_grade_english.setOnClickListener(this);

		// 拦截mViewPager左右滑动事件
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.front_layout);
		layout.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_CANCEL :
						mViewPager.requestDisallowInterceptTouchEvent(false);
						break;
					default :
						mViewPager.requestDisallowInterceptTouchEvent(true);
				}

				return false;
			}
		});

		// 拦截mViewPager上下滑动事件
		gradeRankinglv.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_CANCEL :
						gradeRankinglv.getParent().getParent().getParent()
								.getParent()
								.requestDisallowInterceptTouchEvent(false);
						break;
					default :
						gradeRankinglv.getParent().getParent().getParent()
								.getParent()
								.requestDisallowInterceptTouchEvent(true);
				}

				return false;
			}
		});
		chart = (BesselChart) v.findViewById(R.id.chart);
		chart.setSmoothness(0);
		chart.getData().setLabelTransform(new LabelTransform() {

			@Override
			public String verticalTransform(int valueY) {
				return String.format("%s分", valueY);
			}

			@Override
			public String horizontalTransform(int valueX) {
				return String.format("%s次", valueX);
			}

			@Override
			public boolean labelDrawing(int valueX) {
				return true;
			}

		});
		chart.getData().setYLabels(0, 100, 10);

		swipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.grade_RefreshLayout);

		swipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);

		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				// TODO Auto-generated method stub

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						getAllSchoolTestGradesRanking();
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								refreshGradeRankingView();
								swipeRefreshLayout.setRefreshing(false);
							}
						});

					}
				}).start();

			}
		});

	}
	private void refreshChart() {
		List<Series> seriess = new ArrayList<Series>();
		seriess.add(getSeries("语文", Color.parseColor("#FF7F24")));
		seriess.add(getSeries("数学", Color.parseColor("#6495ED")));
		seriess.add(getSeries("英语", Color.parseColor("#5F9EA0")));
		chart.getData().setSeriesList(seriess);
		chart.refresh(true);

	}

	private Series getSeries(String subject, int color) {
		List<Point> points = new ArrayList<Point>();
		ArrayList<Grades> arrayList = selfTestGradesDao
				.getGradesByMonth(subject);

		if (arrayList == null) {
			points.add(new Point(1, 0, true));
		} else {
			for (int i = 0; i < arrayList.size(); i++) {

				points.add(new Point(i + 1, arrayList.get(i).getScore(), true));

			}
		}

		return new Series(subject, color, points);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		serviceFlag = "pushSelfTest";
		if (v == bt_grade_chinese) {
			subject = "语文";
			scores = getScores(et_grade_chinese);

		} else if (v == bt_grade_math) {
			subject = "数学";
			scores = getScores(et_grade_math);

		} else if (v == bt_grade_english) {
			subject = "英语";
			scores = getScores(et_grade_english);
		}

		showConfirmDialog(subject, scores);

	}
	// dialog确认提示
	@SuppressLint("InflateParams")
	public void showConfirmDialog(String subject, int scores) {

		AlertDialog.Builder alert = new AlertDialog.Builder(ct);
		alert.setTitle(getString(R.string.push_confirm));
		LinearLayout parent = new LinearLayout(ct);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		parent.setLayoutParams(params);

		final TextView tv = new TextView(ct);

		tv.setTextSize(18.0f);
		params.setMargins(75, 40, 60, 0);
		tv.setLayoutParams(params);
		tv.setText(subject + "：" + scores + getString(R.string.scores));
		parent.addView(tv);
		alert.setView(parent);
		alert.setPositiveButton(getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						new GradesServices().execute();
					}
				});
		alert.setNegativeButton(getString(android.R.string.cancel), null);
		alert.show();

	}

	@SuppressWarnings("deprecation")
	public int pushTime() {
		String result = "0";
		try {
			HttpPost httpRequest = new HttpPost(Constant.SERVER_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("flag", serviceFlag));
			params.add(new BasicNameValuePair("sNo", sno));
			params.add(new BasicNameValuePair("subject", subject));
			params.add(new BasicNameValuePair("scores", String.valueOf(scores)));

			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse httpResponse = new MyHttpClient().getHttpClient(2000,
					4000).execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());

			}

			return result.equals("0") ? 0 : 1;
		} catch (Exception e) {
			return 2;
		}
	}

	private class GradesServices extends AsyncTask<Object, Integer, Integer> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub

			return pushTime();

		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
				case 0 :
					Toast.makeText(ct, getString(R.string.have_submit), 1000)
							.show();
					break;
				case 1 :

					Grades grades = new Grades(Date.valueOf(getFormatDate()),
							scores, subject);
					selfTestGradesDao.addGrades(grades);
					refreshChart();
					refreshPushUI();

					Toast.makeText(ct, getString(R.string.submit_success), 1000)
							.show();
					break;
				default :
					Toast.makeText(ct, getString(R.string.net_error), 1000)
							.show();
					break;
			}

		}
		@Override
		protected void onProgressUpdate(Integer... values) {

		}

	}

	// 如果今日已经提交设置控件不可用状态
	private void refreshPushUI() {

		boolean isChinesePush = selfTestGradesDao.todayHavePush(
				Date.valueOf(getFormatDate()), "语文");
		boolean isMathPush = selfTestGradesDao.todayHavePush(
				Date.valueOf(getFormatDate()), "数学");
		boolean isEnglishPush = selfTestGradesDao.todayHavePush(
				Date.valueOf(getFormatDate()), "英语");

		int chineseScore = isChinesePush ? selfTestGradesDao.getTodayGrades(
				Date.valueOf(getFormatDate()), "语文") : 0;
		int mathScore = isMathPush ? selfTestGradesDao.getTodayGrades(
				Date.valueOf(getFormatDate()), "数学") : 0;
		int englishScore = isEnglishPush ? selfTestGradesDao.getTodayGrades(
				Date.valueOf(getFormatDate()), "英语") : 0;

		bt_grade_chinese.setEnabled(!isChinesePush);
		bt_grade_math.setEnabled(!isMathPush);
		bt_grade_english.setEnabled(!isEnglishPush);

		et_grade_chinese.setText(chineseScore + "");
		et_grade_math.setText(mathScore + "");
		et_grade_english.setText(englishScore + "");

		et_grade_chinese.setEnabled(!isChinesePush);
		et_grade_math.setEnabled(!isMathPush);
		et_grade_english.setEnabled(!isEnglishPush);

	}

	public int getScores(EditText h) {

		return Integer.parseInt(h.getText().toString().isEmpty() ? "0" : h
				.getText().toString());

	}

	@SuppressWarnings("deprecation")
	public int getAllSelfTestGrades() {
		String result = "0";
		serviceFlag = "getAllSelfTestGrades";
		try {
			HttpPost httpRequest = new HttpPost(Constant.SERVER_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("flag", serviceFlag));
			params.add(new BasicNameValuePair("sNo", sno));

			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse httpResponse = new MyHttpClient().getHttpClient(2000,
					2000).execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			}

			if (!result.equals("]")) {
				doSaveJson(result);
			}

			return result.isEmpty() ? 0 : 1;
		} catch (Exception e) {
			return 2;
		}
	}

	@SuppressWarnings("deprecation")
	public int getAllSchoolTestGradesRanking() {
		String result = "0";
		serviceFlag = "getAllSchoolTestRanking";
		try {
			HttpPost httpRequest = new HttpPost(Constant.SERVER_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("flag", serviceFlag));
			params.add(new BasicNameValuePair("sNo", sno));

			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse httpResponse = new MyHttpClient().getHttpClient(2000,
					2000).execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			}

			if (!result.equals("]")) {
				editor.putString("gradeRankingJson", result);
				editor.commit();
			}

			return result.isEmpty() ? 0 : 1;
		} catch (Exception e) {
			return 2;
		}
	}
	private void doSaveJson(String result) {
		// TODO Auto-generated method stub
		try {
			JSONArray array = new JSONArray(result);

			for (int i = 0; i < array.length(); i++) {

				JSONObject jsonObject = array.getJSONObject(i);
				selfTestGradesDao.addGrades(new Grades(Date.valueOf(jsonObject
						.getString("TestNo")), Integer.parseInt(jsonObject
						.getString("Score")), jsonObject.getString("Subject")));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	private void doPraseRankingJson(String result) {
		// TODO Auto-generated method stub

		try {
			JSONArray array = new JSONArray(result);

			for (int i = 0; i < array.length(); i++) {

				JSONObject jsonObject = array.getJSONObject(i);
				JSONObject jsonObjectRanking = new JSONArray(
						jsonObject.getString("Ranking")).getJSONObject(0);

				arrayList.add(new GradeRanking(jsonObject.getString("TestNo"),
						jsonObjectRanking.getInt("语文"), jsonObjectRanking
								.getInt("数学"), jsonObjectRanking.getInt("英语")));

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}
	// 更新排名视图
	void refreshGradeRankingView() {

		arrayList.clear();
		doPraseRankingJson(preferences.getString("gradeRankingJson", ""));
		adapter.notifyDataSetChanged();

	}
	@SuppressLint("SimpleDateFormat")
	String getFormatDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
}
