package com.ntuedu.homeworktimemanager.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.service.NetTools;

@SuppressWarnings("deprecation")
public class SettingActivity extends ActionBarActivity {

	private String KEY_CHK_UPDATE = "chk_update";
	private Preference pre_chk_update;
	private String new_version;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Display the fragment as the main content.

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
						getResources().getString(R.string.net_error), Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				if (getCurrentVersion().equals(new_version)) {

					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.is_new), Toast.LENGTH_SHORT)
							.show();
				} else {
					if (!isFinishing()) {
						new AlertDialog.Builder(SettingActivity.this)
								.setTitle(
										getResources().getString(
												R.string.is_old))
								.setMessage(
										getResources().getString(
												R.string.cur_version)
												+ getCurrentVersion()
												+ "\n"
												+ getResources().getString(
														R.string.new_version)
												+ new_version + "\n")
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
										getResources().getString(R.string.ok),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Uri uri = Uri
														.parse("http://www.baidu.com");
												Intent intent = new Intent(
														Intent.ACTION_VIEW, uri);
												startActivity(intent);

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
			new_version = NetTools
					.getcontent("https://raw.githubusercontent.com/ghbhaha/Test/master/README.md");
			return 1;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
