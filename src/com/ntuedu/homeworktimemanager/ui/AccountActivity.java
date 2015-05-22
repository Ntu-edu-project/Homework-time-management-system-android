package com.ntuedu.homeworktimemanager.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.db.AccountDao;
import com.ntuedu.homeworktimemanager.db.AccountDaoImpl;

@SuppressWarnings("deprecation")
public class AccountActivity extends ActionBarActivity {

	private TextView tv_account;
	private Button bt_logout;
	AccountDao accountDao = new AccountDaoImpl(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		bt_logout = (Button) findViewById(R.id.bt_logout);
		tv_account = (TextView) findViewById(R.id.account);

		if (accountDao.lookupStudent() != null) {
			tv_account.setText(accountDao.lookupStudent().getsName());
		}

		bt_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				accountDao.clearStudent();
				onBackPressed();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
