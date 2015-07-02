package com.ntuedu.homeworktimemanager.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ntuedu.homeworktimemanager.Constant;
import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.db.AccountDao;
import com.ntuedu.homeworktimemanager.db.AccountDaoImpl;
import com.ntuedu.homeworktimemanager.util.MyHttpClient;

@SuppressWarnings("deprecation")
public class EditPassWordActivity extends ActionBarActivity {

	private EditText et_password;
	private EditText et_password2;
	private Button btConfirm;

	private TextView tvPw;
	private TextView tvPw2;

	private String FLAG = "editPassWord";

	private String pw;
	private String pw2;

	private String sno;

	private Animation shakeAnim;

	private ProgressDialog progressDialog;

	AccountDao accountDao = new AccountDaoImpl(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_password);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// 去除阴影
		getSupportActionBar().setElevation(0);

		progressDialog = new ProgressDialog(this);

		et_password = (EditText) findViewById(R.id.etPw);
		et_password2 = (EditText) findViewById(R.id.etPw2);
		btConfirm = (Button) findViewById(R.id.btConfirm);

		tvPw = (TextView) findViewById(R.id.tvSno);
		tvPw2 = (TextView) findViewById(R.id.tvPw);

		sno = accountDao.lookupStudent().getsNo();

		shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);

		btConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pw = et_password.getText().toString();
				pw2 = et_password2.getText().toString();

				if (pw.isEmpty() && pw2.isEmpty()) {
					tvPw.setVisibility(View.VISIBLE);
					tvPw2.setVisibility(View.VISIBLE);
					tvPw.startAnimation(shakeAnim);
					tvPw2.startAnimation(shakeAnim);
					return;
				}

				if (pw.isEmpty()) {
					tvPw.setVisibility(View.VISIBLE);
					tvPw.startAnimation(shakeAnim);
					return;
				}

				if (pw2.isEmpty()) {
					tvPw2.setVisibility(View.VISIBLE);
					tvPw2.startAnimation(shakeAnim);
					return;
				}

				if (!pw.equals(pw2)) {
					Toast.makeText(EditPassWordActivity.this, "两次密码不一致", 1000)
							.show();
					return;
				}

				new EditPassWord().execute();

			}
		});

		et_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tvPw.setVisibility(View.GONE);
			}
		});

		et_password2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tvPw2.setVisibility(View.GONE);
			}
		});

	}

	private class EditPassWord extends AsyncTask<Object, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getResources().getString(
					R.string.logining));

			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			return postdata(sno, pw);

		}

		@Override
		protected void onPostExecute(Integer result) {

			progressDialog.cancel();
			switch (result) {
				case 0 :
					Toast.makeText(EditPassWordActivity.this,
							getResources().getString(R.string.login_error),
							Toast.LENGTH_SHORT).show();
					break;
				case 1 :
					enterLogin();
					accountDao.clearStudent();
					break;
				case 2 :
					Toast.makeText(EditPassWordActivity.this,
							getResources().getString(R.string.net_error),
							Toast.LENGTH_SHORT).show();
					break;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

		}

	}

	public int postdata(String name, String pwd) {
		String result = "0";
		try {
			HttpPost httpRequest = new HttpPost(Constant.SERVER_URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("flag", FLAG));
			params.add(new BasicNameValuePair("sNo", sno));
			params.add(new BasicNameValuePair("pPW", pw));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse httpResponse = new MyHttpClient().getHttpClient(4000,
					4000).execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());

			}

			if (!result.equals("0")) {

			}

			return result.equals("0") ? 0 : 1;
		} catch (Exception e) {
			return 2;
		}

	}

	void enterAccount() {
		Intent intent = new Intent(this, AccountActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.finish();

	}

	void enterLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.finish();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			enterAccount();
			this.finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			enterAccount();
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
