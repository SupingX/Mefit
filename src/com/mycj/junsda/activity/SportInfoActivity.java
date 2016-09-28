package com.mycj.junsda.activity;

import com.mycj.junsda.R;
import com.mycj.junsda.R.id;
import com.mycj.junsda.R.layout;
import com.mycj.junsda.base.BaseActivity;
import com.mycj.junsda.bean.HistorySport;
import com.mycj.junsda.bean.HistorySportNew;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.util.ConversionUtil;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.util.ShareUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;
import com.mycj.junsda.view.AlphaImageView;
import com.mycj.junsda.view.DateUtil;
import com.mycj.junsda.view.PopMoreView;
import com.mycj.junsda.view.PopMoreView.OnPopClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SportInfoActivity extends BaseActivity implements OnClickListener {

	private AlphaImageView imgBack;
	private AlphaImageView imgMore;
	private TextView tvSportInfoDate;
	private TextView tvSportInfoStep;
	private TextView tvSportInfoTime;
	private TextView tvSportInfoDistance;
	private TextView tvSportInfoAvgSpeed;
	private TextView tvSportInfoCal;
	private PopMoreView pop;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case StaticValue.MSG_SHARE:
				String path = (String) msg.obj;
				ShareUtil.shareImage(path, SportInfoActivity.this,
						getString(R.string.share));
				break;
			default:
				break;
			}

		};
	};
	private TextView tvSportInfoDistanceUnit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport_info);
		initViews();
		Intent intent = getIntent();
	/*	HistorySport historySport = null;
		if (intent != null) {
			historySport = intent.getParcelableExtra("historySport");
		}
		initValues(historySport);*/

		HistorySportNew historySportNew = null;
		if (intent != null) {
			historySportNew = intent.getParcelableExtra("historySportNew");
		}
		initValues(historySportNew);

	}

	// new
	private void initValues(HistorySportNew historySportNew) {
		String date = "--/--/--";
		int step = 0;
		int sportTime = 0;
		float distance = 0;
		float calorie = 0;
		if (historySportNew != null) {
			date = historySportNew.getSportDate();
			step = historySportNew.getSportStep();
			sportTime = historySportNew.getSportTime();
			distance = historySportNew.getDistance();
			calorie = historySportNew.getCalorie();

		}
		
		String unit = (String) SharedPreferenceUtil.get(getApplicationContext(), StaticValue.SHARE_UNIT,StaticValue.DEFAULT_UNIT );
		if (TextUtils.equals(unit, StaticValue.UNIT_ZH)) {
			tvSportInfoDistance.setText(DataUtil.format1(distance)
					+"");
			tvSportInfoDistanceUnit.setText(R.string.distance);
			
			if (sportTime==0) {
				tvSportInfoAvgSpeed.setText("0.00" +getString(R.string.km_per_hour));
			}else{
				String speed = DataUtil.format(distance * 60 * 60
						/ sportTime)
						+ getString(R.string.km_per_hour);
				tvSportInfoAvgSpeed.setText(speed);
			}
			
			
		}else{
			tvSportInfoDistanceUnit.setText(R.string.distance_inch);
			String distanceStr = ConversionUtil.metricToLnch(getApplicationContext(), distance, ConversionUtil.Unit.Distance);
			tvSportInfoDistance.setText(distanceStr);
			
			if (sportTime==0) {
				tvSportInfoAvgSpeed.setText("0.00" +getString(R.string.km_per_hour));
			}else{
				Log.i("zeej", "______________distance:" + distance)  ;
				String speed = ConversionUtil.metricToLnch(getApplicationContext(),distance * 60 * 60
						/ sportTime,ConversionUtil.Unit.Speed);
				tvSportInfoAvgSpeed.setText(speed);
			}
		}
		
		tvSportInfoDate.setText(formateString(date));
		tvSportInfoStep.setText(String.valueOf(step));
		tvSportInfoTime.setText(DateUtil.formateTime(sportTime));
		tvSportInfoCal.setText(DataUtil.format1(calorie)+getString(R.string.kcal));
	}

	private void initViews() {
		imgBack = (AlphaImageView) findViewById(R.id.img_back);
		imgMore = (AlphaImageView) findViewById(R.id.img_more);
		tvSportInfoDate = (TextView) findViewById(R.id.tv_info_date);
		tvSportInfoStep = (TextView) findViewById(R.id.tv_sport_info_step);
		tvSportInfoTime = (TextView) findViewById(R.id.tv_sport_info_time);
		tvSportInfoDistance = (TextView) findViewById(R.id.tv_sport_info_distance);
		tvSportInfoAvgSpeed = (TextView) findViewById(R.id.tv_sport_avg_speed);
		tvSportInfoCal = (TextView) findViewById(R.id.tv_sport_info_cal);
		tvSportInfoDistanceUnit = (TextView) findViewById(R.id.tv_unit_distance);

		imgBack.setOnClickListener(this);
		imgMore.setOnClickListener(this);

	}

	private void initValues(HistorySport historySport) {
		String date = "--/--/--";
		int step = 0;
		int sportTime = 0;
		if (historySport != null) {
			date = historySport.getDate();
			step = historySport.getStep();
			sportTime = historySport.getSportTime();

		}
		tvSportInfoDate.setText(formateString(date));
		tvSportInfoStep.setText(String.valueOf(step));
		tvSportInfoTime.setText(DateUtil.formateTime(sportTime));
		// tvSportInfoDistance.setText(DataUtil.getDistanceValue(step)+getString(R.string.km));
		tvSportInfoDistance.setText(DataUtil.getDistanceStrValue(step));
		tvSportInfoAvgSpeed.setText(getAvgSpeed(step, sportTime));
		tvSportInfoCal.setText(DataUtil.getKalWithUnit(step, this));
	}

	private String getAvgSpeed(int distance, int sportTime) {
		if (sportTime == 0) {
			return "0" + getString(R.string.km_per_hour);
		}
		return DataUtil.format(distance * 60 * 60
				/ sportTime)
				+ getString(R.string.km_per_hour);
	}

	private String formateString(String date) {
		String pad = "/";
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		return new StringBuffer().append(year).append(pad).append(month)
				.append(pad).append(day).toString();
	}

	private void showMorePop(View v) {
		if (pop == null) {
			pop = new PopMoreView().build(this, new OnPopClickListener() {
				@Override
				public void onShareClick(View v) {
					// toast("分享");
					share(mHandler);
				}

				@Override
				public void onDeleteClick(View v) {
					toast(getString(R.string.delete));
				}
			});

		}
		pop.showAsDropDown(v, 0, -18);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.img_more:
//			showMorePop(v);
			share(mHandler);
			break;

		default:
			break;
		}
	}
}
