package com.ntuedu.homeworktimemanager.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.db.AccountDao;
import com.ntuedu.homeworktimemanager.db.AccountDaoImpl;
import com.ntuedu.homeworktimemanager.fragment.GradeFrg;
import com.ntuedu.homeworktimemanager.fragment.HomeWorkTimeFrg;
import com.ntuedu.homeworktimemanager.fragment.NoAccountFrg;
import com.ntuedu.homeworktimemanager.fragment.TimeAndGradeFrg;
import com.ntuedu.homeworktimemanager.util.UIUtil;
import com.ntuedu.homeworktimemanager.widget.DoubleClickExitHelper;
import com.ntuedu.homeworktimemanager.widget.PagerSlidingTabStrip;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private Toolbar mToolbar;
	private ListView lvLeftMenu;
	private TextView tvAccount;

	Boolean openOrClose = false;

	private ArrayList<HashMap<String, Object>> listItems;
	private SimpleAdapter listItemAdapter;

	DoubleClickExitHelper clickExitHelper;

	AccountDao accountDao = new AccountDaoImpl(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homework_time_main);
		UIUtil.setStatusBarTextColor(this, 0);
		initViews();
		refreshAccount();
		clickExitHelper = new DoubleClickExitHelper(MainActivity.this);
	}
	@SuppressLint("ShowToast")
	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// 标题的文字需在setSupportActionBar之前，不然会无效
		mToolbar.setTitle(getResources().getString(R.string.app_name));
		mToolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(mToolbar);

		/** 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_settings :
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("image/*");
						intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
						intent.putExtra(Intent.EXTRA_TEXT, "作业时间管理系统很棒哦!!!");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(Intent.createChooser(intent, getTitle()));
						break;
					default :
						break;
				}
				return true;
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// 设置菜单列表
		lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
		View headerContainer = LayoutInflater.from(this).inflate(
				R.layout.siderbar_header, lvLeftMenu, false);

		tvAccount = (TextView) headerContainer.findViewById(R.id.stu_name);

		lvLeftMenu.addHeaderView(headerContainer);
		listItems = new ArrayList<HashMap<String, Object>>();

		addLeftMenu(getResources().getString(R.string.settings),
				R.drawable.ic_drawer_settings);
		addLeftMenu(getResources().getString(R.string.help),
				R.drawable.ic_drawer_guide);
		addLeftMenu(getResources().getString(R.string.exit),
				R.drawable.ic_drawer_exit);

		listItemAdapter = new SimpleAdapter(this, listItems,
				R.layout.siderbar_lisetview_item, new String[]{"ItemTitle",
						"ItemImage"}, new int[]{R.id.ItemTitle, R.id.ItemImage});

		lvLeftMenu.setAdapter(listItemAdapter);
		lvLeftMenu.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = null;
				switch (position) {
					case 0 :

						if (accountDao.lookupStudent() != null) {
							intent = new Intent(MainActivity.this,
									AccountActivity.class);
							// startActivityForResult(intent, 1);

						} else {
							intent = new Intent(MainActivity.this,
									LoginActivity.class);
							// startActivityForResult(intent, 3);
						}
						startActivity(intent);
						MainActivity.this.finish();
						return;
					case 1 :
						intent = new Intent(MainActivity.this,
								SettingActivity.class);

						break;
					case 2 :
						intent = new Intent(MainActivity.this,
								HelpActivity.class);
						break;
					case 3 :
						MainActivity.this.finish();
						return;
					default :
						break;
				}
				startActivity(intent);
			}

		});

		// 设置首页
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				mToolbar, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
				openOrClose = false;
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
				openOrClose = true;
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(3);

		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// colorChange(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
		initTabsValue();
	}

	private void refreshAccount() {
		if (accountDao.lookupStudent() != null) {
			tvAccount.setText(accountDao.lookupStudent().getsName());
		} else {
			tvAccount.setText(getResources().getString(R.string.ple_login));
		}

	}

	private void initTabsValue() {
		// 底部游标颜色
		mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);

		// tab的分割线颜色
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
		// tab背景
		mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#6699FF"));

		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
						.getDisplayMetrics()));

		// 均分每个标题
		mPagerSlidingTabStrip.setShouldExpand(true);

		// 游标高度
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
						.getDisplayMetrics()));
		// 选中的文字颜色
		mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
		// 正常文字颜色
		mPagerSlidingTabStrip.setTextColor(Color.BLACK);
	}

	private void addLeftMenu(String title, int res) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemTitle", title);
		map.put("ItemImage", res);
		listItems.add(map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (openOrClose == false) {
				return clickExitHelper.onKeyDown(keyCode, event);
			} else {
				mDrawerLayout.closeDrawers();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**************** FragmentPagerAdapter***************** */
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = getResources().getStringArray(
				R.array.tab_title);

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {

			if (accountDao.lookupStudent() == null) {
				return new NoAccountFrg();
			}
			switch (position) {
				case 0 :
					return new HomeWorkTimeFrg(mViewPager, accountDao
							.lookupStudent().getsNo());
				case 1 :
					return new GradeFrg(mViewPager, accountDao.lookupStudent()
							.getsNo());
				case 2 :
					return new TimeAndGradeFrg();
				default :
					return null;
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}
}
