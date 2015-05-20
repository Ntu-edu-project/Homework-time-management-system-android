package com.ntuedu.homeworktimemanager.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetTools {

	public static String getcontent(String path) throws Exception {
		// TODO 自动生成的方法存根
		URL url = new URL(path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(5000);
		con.setRequestMethod("GET");
		if (con.getResponseCode() == 200) {
			InputStream inputStream = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				result = result + line;
			}
			reader.close();

			return result;
		}
		return null;
	}

}