package com.ntuedu.homeworktimemanager.activity;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ntuedu.homeworktimemanager.Constant;
import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.util.StringRequestUtil;

@SuppressWarnings("deprecation")
public class SettingActivity extends ActionBarActivity {

	private String KEY_CHK_UPDATE = "chk_update";
	private Preference pre_chk_update;
	private ProgressDialog progressDialog;

	private String new_version;
	private String url;
	private String info;

	//下载
	private DownloadManager dm;
	private Request request;
	private long enqueue;
	private String app_name;

	private DownloadCompleteReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Display the fragment as the main content.

		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		receiver = new DownloadCompleteReceiver();
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}

	public class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle paramBundle) {
			super.onCreate(paramBundle);
			addPreferencesFromResource(R.xml.settings);
			progressDialog = new ProgressDialog(getActivity());
			pre_chk_update = findPreference(KEY_CHK_UPDATE);

		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
				Preference preference) {
			// TODO Auto-generated method stub

			if (preference == pre_chk_update) {
				UpdateService romService = new UpdateService();
				romService.execute();
			}

			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
	}

	private class UpdateService extends AsyncTask<Object, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getResources()
					.getString(R.string.det_new));

			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Object... arg0) {

			return checkForUpdate();

		}

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.cancel();
			switch (result) {

			case 0:
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.net_error),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:

				if (getCurrentVersion().equals(new_version)) {

					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.is_new),
							Toast.LENGTH_SHORT).show();
				} else {
					if (!isFinishing()) {
						new AlertDialog.Builder(SettingActivity.this)
								.setTitle(
										getResources().getString(
												R.string.is_old)
												+ "V" + new_version)
								.setMessage(info)
								.setNegativeButton(
										getResources().getString(
												R.string.cancle),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

											}
										})
								.setPositiveButton(
										getResources().getString(R.string.down),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												String sdpath = Environment
														.getExternalStorageDirectory()
														+ "/";
												File file = new File(
														sdpath
																+ Environment.DIRECTORY_DOWNLOADS,
														app_name);
												if (file.exists()) {
													file.delete();
												}
												request = new Request(Uri
														.parse(url));
												request.setDestinationInExternalPublicDir(
														Environment.DIRECTORY_DOWNLOADS,
														app_name);
												request.setTitle(app_name);
												enqueue = dm.enqueue(request);

											}
										}).show();
					}
				}

				break;
			}
		}

	}

	public int checkForUpdate() {

		try {
			String json = StringRequestUtil.getcontent(Constant.UPDATE_URL);

			doParseJson(json);
			return 1;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	public void doParseJson(String json) {

		JSONArray array;
		try {
			array = new JSONArray(json);
			JSONObject jsonObject = array.getJSONObject(0);
			new_version = jsonObject.getString("version");
			app_name = "HomeWorkTimeManager";
			app_name += ("_v" + new_version + ".apk");
			url = jsonObject.getString("url");
			info = jsonObject.getString("info");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getCurrentVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			return info.versionName.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	// 接受下载完成后的intent
	class DownloadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long downId = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				if (downId != enqueue) {
					return;
				}

				Intent intentupdate = new Intent();
				intentupdate.setAction(Intent.ACTION_VIEW);
				String sdpath = Environment.getExternalStorageDirectory() + "/";
				File file = new File(sdpath + Environment.DIRECTORY_DOWNLOADS,
						app_name);
				if (file.exists()) {
					intentupdate.setDataAndType(Uri.fromFile(file),
							"application/vnd.android.package-archive");
					startActivity(intentupdate);
				}

			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (receiver != null)
			unregisterReceiver(receiver);
		super.onDestroy();
	}
}
