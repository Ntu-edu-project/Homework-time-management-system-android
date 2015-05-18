package com.ntuedu.homeworktimemanager.ui;

import com.ntuedu.homeworktimemanager.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class Fragement1 extends Fragment {

	
	private int position;
	public Fragement1() {
		// TODO Auto-generated constructor stub
	}

	public static Fragement1 newInstance(int position) {
		
 
		Fragement1 f = new Fragement1();
		Bundle b = new Bundle();
		b.putInt("sds", position);
		f.setArguments(b);
		return f;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt("sds");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fm1,container,false);
		
		return view;

	}

}
