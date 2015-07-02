package com.ntuedu.homeworktimemanager.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntuedu.homeworktimemanager.R;
import com.ntuedu.homeworktimemanager.model.GradeRanking;

@SuppressLint("NewApi")
public class GradeRankingAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private ArrayList<GradeRanking> arrayList;

	public GradeRankingAdapter(Context context, ArrayList<GradeRanking> arrayList) {
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.arrayList = arrayList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({"NewApi", "InflateParams"})
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.gradeitem, null);

			holder.testNotv = (TextView) convertView
					.findViewById(R.id.testNotv);
			holder.chineseNotv = (TextView) convertView
					.findViewById(R.id.chineseNotv);
			holder.mathNotv = (TextView) convertView
					.findViewById(R.id.mathNotv);
			holder.englishNotv = (TextView) convertView
					.findViewById(R.id.englishNotv);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.gradeitemlayout);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.layout.setBackgroundColor(((position + 1) % 2) == 0 ? Color
				.parseColor("#D3D3D3") : Color.parseColor("#ffffff"));
		holder.testNotv.setText(arrayList.get(position).getTestNo());
		holder.chineseNotv.setText(arrayList.get(position).getChineseNo() == -1
				? "-"
				: arrayList.get(position).getChineseNo() + "");
		holder.mathNotv.setText(arrayList.get(position).getMathNo() == -1
				? "-"
				: arrayList.get(position).getMathNo() + "");
		holder.englishNotv.setText(arrayList.get(position).getEnglishNo() == -1
				? "-"
				: arrayList.get(position).getEnglishNo() + "");

		return convertView;
	}
	// ×Ô¶¨Òåadapter
	public class ViewHolder {
		public TextView testNotv;
		public TextView chineseNotv;
		public TextView mathNotv;
		public TextView englishNotv;
		public LinearLayout layout;

	}

}
