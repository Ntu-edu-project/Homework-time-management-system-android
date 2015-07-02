package com.ntuedu.homeworktimemanager.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ntuedu.homeworktimemanager.R;

@SuppressLint("JavascriptInterface")
@SuppressWarnings("deprecation")
public class HelpActivity extends ActionBarActivity {

	private WebView mWebView;
	private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// »•≥˝“ı”∞
		getSupportActionBar().setElevation(0);

		mWebView = (WebView) findViewById(R.id.webhelp);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new Object() {
			public void clickOnAndroid() {
				mHandler.post(new Runnable() {
					public void run() {
						mWebView.loadUrl("javascript:wave()");
					}
				});
			}
		}, "demo");
		mWebView.loadUrl("file:///android_asset/help.html");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
