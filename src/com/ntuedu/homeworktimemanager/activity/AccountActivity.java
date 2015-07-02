package com.ntuedu.homeworktimemanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.db.AccountDao;
import com.ntuedu.homeworktimemanager.db.AccountDaoImpl;
import com.ntuedu.homeworktimemanager.model.Student;
import com.ntuedu.homeworktimemanager.widget.FloatingActionButton;

@SuppressWarnings("deprecation")
public class AccountActivity extends ActionBarActivity {

	private TextView tv_account;
	private TextView tv_tel;
	private TextView tv_grade_classNo;
	private ImageButton bt_logout;
	private FloatingActionButton edit_icon;
	private Student student;

	private SharedPreferences preferences;
	private Editor editor;
	public static int MODE = MODE_PRIVATE;
	public static final String PER_NAME = "time_no";

	AccountDao accountDao = new AccountDaoImpl(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// »•≥˝“ı”∞
		getSupportActionBar().setElevation(0);

		preferences = getSharedPreferences(PER_NAME, MODE);
		editor = preferences.edit();

		bt_logout = (ImageButton) findViewById(R.id.bt_logout);
		tv_account = (TextView) findViewById(R.id.account);
		tv_tel = (TextView) findViewById(R.id.tel);
		tv_grade_classNo = (TextView) findViewById(R.id.grade_classno);
		edit_icon = (FloatingActionButton) findViewById(R.id.edit_icon);

		if (accountDao.lookupStudent() != null) {
			student = accountDao.lookupStudent();

			tv_account.setText(student.getsName());
			tv_tel.setText(student.getTel());
			tv_grade_classNo.setText(student.getGradeNo()
					+ getString(R.string.grade_no) + student.getClassNo()
					+ getString(R.string.calss_no));
		}

		edit_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(AccountActivity.this,
						EditPassWordActivity.class);
				startActivity(intent);
				AccountActivity.this.finish();

			}
		});

		bt_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				accountDao.clearStudent();
				editor.clear();
				editor.commit();
				enterMian();
			}
		});

	}

	void enterMian() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.finish();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			enterMian();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			enterMian();
		}
		return super.onOptionsItemSelected(item);
	}
}
