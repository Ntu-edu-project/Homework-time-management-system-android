package com.ntuedu.homeworktimemanager.ui;

import java.util.ArrayList;
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntuedu.homeworktimemanager.Constant;
import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.dao.AccountDao;
import com.ntuedu.homeworktimemanager.dao.AccountDaoImpl;
import com.ntuedu.homeworktimemanager.dao.Student;
import com.ntuedu.homeworktimemanager.service.MyHttpClient;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity {

	private EditText etSno;
	private EditText etPw;
	private Button btLogin;

	private String sno;
	private String pw;

	private ProgressDialog progressDialog;

	AccountDao accountDao = new AccountDaoImpl(this);

	String FLAG = "login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		progressDialog = new ProgressDialog(this);

		etSno = (EditText) findViewById(R.id.etSno);
		etPw = (EditText) findViewById(R.id.etPw);
		btLogin = (Button) findViewById(R.id.btlogin);

		btLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sno = etSno.getText().toString();
				pw = etPw.getText().toString();

				Login login = new Login();
				login.execute();

			}
		});

	}

	private class Login extends AsyncTask<Object, Integer, Integer> {
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
			case 0:
				Toast.makeText(LoginActivity.this,
						getResources().getString(R.string.login_error),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				returnMain();
				break;
			case 2:
				Toast.makeText(LoginActivity.this,
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
			HttpResponse httpResponse = new MyHttpClient().getHttpClient(2000,
					2000).execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());

			}

			if (!result.equals(0)) {
				doSaveLocal(result);
			}

			return result.equals("0") ? 0 : 1;
		} catch (Exception e) {
			return 2;
		}

	}

	public void doSaveLocal(String result) throws JSONException {
		JSONArray array = new JSONArray(result);
		JSONObject jsonObject = array.getJSONObject(0);
		String sNo = jsonObject.get("sNo").toString();
		String sName = jsonObject.get("sName").toString();
		Student student = new Student(sNo, sName);
		//存入本地数据
		accountDao.addStudent(student);
	}

	private void returnMain() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			returnMain();
		}
		return super.onOptionsItemSelected(item);
	}
}
