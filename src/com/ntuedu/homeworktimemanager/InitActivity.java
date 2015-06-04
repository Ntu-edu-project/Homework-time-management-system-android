package com.ntuedu.homeworktimemanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ntuedu.homeworktimemanager.activity.MainActivity;
import com.ntuedu.homeworktimemanager.util.UIUtil;

@SuppressWarnings("deprecation")
public class InitActivity extends Activity {

	// 总介绍页面数
	private int pageCount = 5;

	//
	private Button start_app_bt;
	private ViewPager pager;
	private ImageView[] mPage = new ImageView[pageCount];
	private String KEY_FIRST_IN = "isFirstIn";
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_activity);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		// 不是第一次启动则进入主界面
		if (!isFirstIn()) {
			enterMain();
		}

		UIUtil.setFullScreen(InitActivity.this);

		pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setOnPageChangeListener(new MyOnPageChangeListner());

		LayoutInflater mLi = LayoutInflater.from(this);
		final ArrayList<View> views = new ArrayList<View>();
		for (int i = 0; i < pageCount; i++) {
			String name = "introduce" + i;
			views.add(mLi.inflate(
					getResources().getIdentifier(name, "layout",
							getPackageName()), null));
		}

		// 获取最后一页的Button
		start_app_bt = (Button) views.get(pageCount - 1).findViewById(
				R.id.start_app_bt);

		start_app_bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sharedPreferences.edit();
				editor.putBoolean(KEY_FIRST_IN, false);
				editor.commit();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						enterMain();
					}
				}, 1000);

			}
		});

		// 初始化底部圆点
		mPage[0] = (ImageView) findViewById(R.id.page0);
		mPage[1] = (ImageView) findViewById(R.id.page1);
		mPage[2] = (ImageView) findViewById(R.id.page2);
		mPage[3] = (ImageView) findViewById(R.id.page3);
		mPage[4] = (ImageView) findViewById(R.id.page4);

		PagerAdapter adapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		pager.setAdapter(adapter);

	}

	class MyOnPageChangeListner implements OnPageChangeListener {
		@Override
		public void onPageSelected(int page) {
			mPage[page].setImageDrawable(getResources().getDrawable(
					R.drawable.btn_code_lock_touched_holo));
			if (page != 0) {
				mPage[page - 1].setImageDrawable(getResources().getDrawable(
						R.drawable.btn_code_lock_default_holo));
			}
			if (page != pageCount - 1) {
				mPage[page + 1].setImageDrawable(getResources().getDrawable(
						R.drawable.btn_code_lock_default_holo));
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void enterMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	// 判断是否首次启动
	private boolean isFirstIn() {
		return sharedPreferences.getBoolean(KEY_FIRST_IN, true);
	}

}
