package com.mycj.junsda.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mycj.junsda.R;
import com.mycj.junsda.bean.HistorySleep;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistroySleepAdapter extends BaseAdapter{
	private List<HistorySleep> datas = new ArrayList<HistorySleep>(); 
	public HistroySleepAdapter(List<HistorySleep> datas){
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView==null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_sleep, parent,false);
			holder = new ViewHolder();
			holder.tvSleepDate = (TextView) convertView.findViewById(R.id.tv_sleep_date);
			holder.tvSleepTotal = (TextView) convertView.findViewById(R.id.tv_sleep_total);
			holder.tvSleepTime = (TextView) convertView.findViewById(R.id.tv_sleep_time);
			holder.tvSleepDeep = (TextView) convertView.findViewById(R.id.tv_sleep_deep);
			holder.tvSleepLight = (TextView) convertView.findViewById(R.id.tv_sleep_light);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//设置数据
		HistorySleep sleep = datas.get(position);
		String date = sleep.getDate();
		int deep = sleep.getDeep();
		int light = sleep.getLight();
		String startTime = sleep.getStartTime();
		String endTime = sleep.getEndTime();
		
		String minUnit = parent.getContext().getString(R.string.minute);
		holder.tvSleepDate.setText(formateDate(date, parent.getContext()));
//		holder.tvSleepTotal.setText(String.valueOf((int)((light +deep)/60)) + minUnit);
//		holder.tvSleepDeep.setText(String.valueOf((int)(deep/60)) + minUnit);
//		holder.tvSleepLight.setText(String.valueOf((int)(light/60)) + minUnit);
		holder.tvSleepTotal.setText(DataUtil.format(((light +deep)*1.0f)) + minUnit);
		holder.tvSleepDeep.setText(DataUtil.format(((deep)*1.0f)) + minUnit);
		holder.tvSleepLight.setText(DataUtil.format(((light)*1.0f)) + minUnit);
		
		String sleepTime = startTime;
		String awakTime = endTime;
		
		//未确定 
		if (TextUtils.isEmpty(sleepTime)) {
			 sleepTime = (String) SharedPreferenceUtil.get(parent.getContext(), StaticValue.SHARE_SLEEP_TIME,  StaticValue.DEFAULT_SLEEP_TIME);
//			 sleepTime = "未知";
		}else{
			String hour = sleepTime.substring(0,2);
			String min = sleepTime.substring(2,4);
			sleepTime = hour + ":" + min;
		
		}
		if (TextUtils.isEmpty(awakTime)) {
			 awakTime = (String) SharedPreferenceUtil.get(parent.getContext(), StaticValue.SHARE_AWAK_TIME,  StaticValue.DEFAULT_AWAK_TIME);
//			 awakTime = "未知";
		}else{
			String hour = awakTime.substring(0,2);
			String min = awakTime.substring(2,4);
			awakTime = hour + ":" + min;
		}
		
		holder.tvSleepTime.setText(sleepTime + "-" + awakTime);
		
		return convertView;
	}
	
	
	private String formateDate(String date, Context context) {

		StringBuffer sb = new StringBuffer();
		try {

			String month = date.substring(4, 6);
			String day = date.substring(6, 8);
			Resources resources = context.getResources();
			sb.append(month).append(resources.getString(R.string.month)).append(day).append(resources.getString(R.string.day));
		} catch (Exception e) {
			return "未知";
		}
		return sb.toString();
	}
	
	public String getString(int value){
		String valueOf = String.valueOf(value);
		if (valueOf.length()==1) {
			return "0"+valueOf;
		}
		return valueOf;
	}
	public class ViewHolder {
		TextView tvSleepDate;
		TextView tvSleepTime;
		TextView tvSleepTotal;
		TextView tvSleepDeep;
		TextView tvSleepLight;
	}
	
	

}
