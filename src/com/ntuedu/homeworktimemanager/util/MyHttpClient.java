package com.ntuedu.homeworktimemanager.util;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

@SuppressWarnings("deprecation")
public class MyHttpClient extends DefaultHttpClient {
	/**
	 * 获取 HttpClient,主要是封装了超时设置
	 * 
	 * @param rTimeOut
	 *            请求超时
	 * @param sTimeOut
	 *            等待数据超时
	 * @return
	 */
	public DefaultHttpClient getHttpClient(int rTimeOut, int sTimeOut) {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, rTimeOut);
		HttpConnectionParams.setSoTimeout(httpParams, sTimeOut);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

}
