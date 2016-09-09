package com.mycj.junsda.activity;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.laput.dialog.BaseDialog;
import com.laput.dialog.SimpleAlertDialog;
import com.mycj.junsda.R;
import com.mycj.junsda.base.BaseActivity;
import com.mycj.junsda.bean.HistorySleep;
import com.mycj.junsda.bean.HistorySport;
import com.mycj.junsda.bean.HistorySportNew;
import com.mycj.junsda.bean.LitePalManager;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.util.ConversionUtil;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.util.ShareUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;
import com.mycj.junsda.view.CountView;
import com.mycj.junsda.view.CountView.FormatType;
import com.mycj.junsda.view.DateUtil;
import com.mycj.junsda.view.FangRadioButton;
import com.mycj.junsda.view.FangTextView;
import com.mycj.junsda.view.PopMoreView;
import com.mycj.junsda.view.PopMoreView.OnPopClickListener;
import com.mycj.junsda.view.XplAlertDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class CountActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	protected static final int MSG_SLEEP_HISTORY = 0xC1;
//	protected static final int MSG_SPORT_HISTORY = 0xC2;
	protected static final int MSG_SPORT_HISTORY_NEW = 0xD2;
	private CountView countSport;
	private ImageView imgBack;
	private ImageView imgMore;
	private FangTextView tvUp;
	private FangTextView tvDown;
	private TextView tvSportValue;
	private FangTextView tvSportUnit;
	private RadioGroup rgCountSport;
	private FangRadioButton rbSportStep;
	private FangRadioButton rbSportTime;
	private FangRadioButton rbSportDistance;
	private FangRadioButton rbSportCal;
	private TextView tvSleepValue;
	private FangTextView tvSleepUnit;
	private RadioGroup rgCountSleep;
	private FangRadioButton rbSleepDeep;
	private FangRadioButton rbSleepLight;
	private CountView countSleep;
	private Date monthDate;
	private FangTextView tvDate;
	private PopMoreView pop;
	private int monthMaxDay;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case StaticValue.MSG_SHARE:
				String path = (String) msg.obj;
				ShareUtil.shareImage(path, CountActivity.this, getString(R.string.share));
				break;
			case MSG_SLEEP_HISTORY:

				ArrayList<HistorySleep> histroySleeps = (ArrayList<HistorySleep>) msg.obj;
				// 5.解析数据库的值，解析为float[]
				float[] sleepDatas = new float[monthMaxDay];
				if (histroySleeps != null && histroySleeps.size() > 0) {
					for (int i = 0; i < histroySleeps.size(); i++) {
						HistorySleep historySleep = histroySleeps.get(i);
						
						String date = historySleep.getDate();
						Log.e("xpl", "historySleep :" + historySleep.toString());
						String day = date.substring(6, 8);
						Log.e("xpl", "day :" + day);
						int index = Integer.valueOf(day);
						switch (rgCountSleep.getCheckedRadioButtonId()) {
						case R.id.rb_sleep_deep:
							sleepDatas[index-1] = historySleep.getDeep()*1.0f / 1L;
							break;
						case R.id.rb_sleep_light:
							sleepDatas[index-1] = historySleep.getLight()*1.0f / 1L;
							break;
						}
					}
				}
				// 6.设置图标
				countSleep.setDatas(sleepDatas,DateUtil.getToday()-1);
				// 7.设置平均统计
				String sleepAvgValue = getAvgValue(sleepDatas);
				tvSleepValue.setText(sleepAvgValue);
				isLoadSleepOver = true;
				closeLoadingDialog();

				break;
			case 0xff:

				ArrayList<HistorySport> historySports = (ArrayList<HistorySport>) msg.obj;
				float[] sportDatas = new float[monthMaxDay];
				// 5.解析数据库的值，解析为float[]
				if (historySports != null && historySports.size() > 0) {
					for (int i = 0; i < historySports.size(); i++) {
						HistorySport historySport = historySports.get(i);
						String date = historySport.getDate();
						Log.e("xpl", "date :" + date); //20160301
						String day = date.substring(6, 8);
						Log.e("xpl", "day :" + day);
						int index = Integer.valueOf(day);
						int step = historySport.getStep();
						int sportTime = historySport.getSportTime();
						switch (rgCountSport.getCheckedRadioButtonId()) {
						case R.id.rb_sport_step:
							sportDatas[index-1] = step;
							break;
						case R.id.rb_sport_time:
							sportDatas[index-1] = sportTime;
							break;
						case R.id.rb_sport_distance:
							sportDatas[index-1] = DataUtil.getDistanceValue(step);
							break;
						case R.id.rb_sport_cal:
							sportDatas[index-1] = DataUtil.getKalValue(step);
							break;
						}
					}
				}
				// 6.设置
				
				countSport.setDatas(sportDatas,DateUtil.getToday()-1);
				// 7.设置平均统计
				String sportAvgValue = getAvgValue(sportDatas);
				tvSportValue.setText(sportAvgValue);
				isLoadSportOver = true;
				closeLoadingDialog();
				break;
				
			case MSG_SPORT_HISTORY_NEW:

				ArrayList<HistorySportNew> historySportsNew = (ArrayList<HistorySportNew>) msg.obj;
				float[] sportDatasNew = new float[monthMaxDay];
				// 5.解析数据库的值，解析为float[]
				float maxValue = 1000;
				String unit = (String) SharedPreferenceUtil.get(getApplicationContext(), StaticValue.SHARE_UNIT,StaticValue.DEFAULT_UNIT );
				if (historySportsNew != null && historySportsNew.size() > 0) {
					for (int i = 0; i < historySportsNew.size(); i++) {
						HistorySportNew historySport = historySportsNew.get(i);
						String date = historySport.getSportDate();
						Log.e("xpl", "date :" + date); //20160301
						String day = date.substring(6, 8);
						Log.e("xpl", "day :" + day);
						int index = Integer.valueOf(day);
						int step = historySport.getSportStep();
						int sportTime = historySport.getSportTime();
						float distance = historySport.getDistance();
						float calorie = historySport.getCalorie();
						
						switch (rgCountSport.getCheckedRadioButtonId()) {
						case R.id.rb_sport_step:
							sportDatasNew[index-1] = step;
							maxValue = 10000;
							break;
							
						case R.id.rb_sport_time:
							sportDatasNew[index-1] = sportTime*1.0f/60;
							maxValue = 360;
							break;
							
						case R.id.rb_sport_distance:
							Log.e("xpl", "distance :" + distance);
							if (TextUtils.equals(unit, StaticValue.UNIT_ZH)) {
								sportDatasNew[index-1] = distance;
								maxValue = 1000;
							}else{
								sportDatasNew[index-1] = ConversionUtil.metricToLnchValue(distance,ConversionUtil.Unit.Distance);
								maxValue = 1000;
							}
					
						
							break;
							
						case R.id.rb_sport_cal:
							maxValue = 2000;
							sportDatasNew[index-1] =calorie;
							Log.e("xpl", "calorie :" + calorie);
							break;
						}
					}
				}
				// 6.设置
				countSport.setMaxValue(maxValue);
				countSport.setDatas(sportDatasNew,DateUtil.getToday()-1);
				// 7.设置平均统计
				String sportAvgValueNew = getAvgValue(sportDatasNew);
				tvSportValue.setText(sportAvgValueNew);
				isLoadSportOver = true;
				closeLoadingDialog();
				break;
			default:
				break;
			}

		};
	};

	private boolean isLoadSportOver = true;
	private boolean isLoadSleepOver = true;
	private XplAlertDialog loadingDialog;
	private long old;
	private ScrollView mScrollView;
	private PtrClassicFrameLayout mPtrFrame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		monthDate = new Date();
		initViews();
		loadHistoryData(monthDate);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeLoadingDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up:
			if (isLoadSportOver && isLoadSleepOver) {
				monthDate = DateUtil.getDateOfDiffMonth(monthDate, -1);
				loadHistoryData(monthDate);
			}
	
			break;
		case R.id.down:
			if (isLoadSportOver && isLoadSleepOver) {
				monthDate = DateUtil.getDateOfDiffMonth(monthDate, 1);
				loadHistoryData(monthDate);
			}
			break;
		case R.id.img_back:
			finish();
			break;
		case R.id.img_more:
			showMorePop(v);
			break;
	
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.rg_count_sport:
//			loadSportValues(monthDate);
			loadSportValuesNew(monthDate);
			break;
		case R.id.rg_count_sleep:
//			loadSleepValues(monthDate);
			loadSleepValues(monthDate);
			break;
		default:
			break;
		}
	}

	private void initViews() {

		imgBack = (ImageView) findViewById(R.id.img_back);
		imgMore = (ImageView) findViewById(R.id.img_more);
		tvUp = (FangTextView) findViewById(R.id.up);
		tvDown = (FangTextView) findViewById(R.id.down);
		tvDate = (FangTextView) findViewById(R.id.tv_date);
		tvSportValue = (TextView) findViewById(R.id.tv_sport_value);
		tvSportUnit = (FangTextView) findViewById(R.id.tv_sport_unit);

		rgCountSport = (RadioGroup) findViewById(R.id.rg_count_sport);
		rbSportStep = (FangRadioButton) findViewById(R.id.rb_sport_step);
		rbSportTime = (FangRadioButton) findViewById(R.id.rb_sport_time);
		rbSportDistance = (FangRadioButton) findViewById(R.id.rb_sport_distance);
		rbSportCal = (FangRadioButton) findViewById(R.id.rb_sport_cal);
		countSport = (CountView) findViewById(R.id.count_sport);
		tvSleepValue = (TextView) findViewById(R.id.tv_sleep_value);
		tvSleepUnit = (FangTextView) findViewById(R.id.tv_sleep_unit);
		rgCountSleep = (RadioGroup) findViewById(R.id.rg_count_sleep);
		rbSleepDeep = (FangRadioButton) findViewById(R.id.rb_sleep_deep);
		rbSleepLight = (FangRadioButton) findViewById(R.id.rb_sleep_light);
		countSleep = (CountView) findViewById(R.id.count_sleep);
			
		
//		init();
		
		tvUp.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		imgMore.setOnClickListener(this);
		tvDown.setOnClickListener(this);
		rgCountSport.setOnCheckedChangeListener(this);
		rgCountSleep.setOnCheckedChangeListener(this);

		rbSportStep.setChecked(true);
		rbSleepDeep.setChecked(true);

	}
	
	
	
	private void init(){
        mScrollView = (ScrollView) findViewById(R.id.rotate_header_scroll_view);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_web_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mScrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                   
//                        refresh();
                    	mPtrFrame.refreshComplete();
                    }
                }, 2000);
            }
        });

        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
            	loadHistoryData(monthDate);
                mPtrFrame.autoRefresh();
            }
        }, 100);
}
	private void loadHistoryData(Date date) {
	
		tvDate.setText(DateUtil.dateToString(date, "yyyy/MM"));
		monthMaxDay = DateUtil.getMonthMaxDay(date);
		// 运动数据更新
//		loadSportValues(date);
		loadSportValuesNew(date);
		// 睡眠数据更新
		loadSleepValues(date);
	}
	
	
	private void showLoadingDialog(){
		old = System.currentTimeMillis();
		if (loadingDialog==null) {
			loadingDialog = showXplDialog("正在加载...");
		}
		if (loadingDialog!=null && !loadingDialog.isShowing()) {
			loadingDialog.show();
		}
	}
	
	private void loadSleepValues(final Date date) {
		showLoadingDialog();
		isLoadSleepOver = false;
		float sleepMaxValue = StaticValue.MAX_SLEEP;// 最大值
		String sleeptUnit = getString(R.string.minute);// 单位
		FormatType f = FormatType.FORMAT_2;// 格式化数字
		// 设置一些初始参数
		countSleep.setMaxValue(sleepMaxValue / 1L);
		countSleep.setUnit(sleeptUnit);
		countSleep.setFormat(f);
		String sleepAvgUnit = sleeptUnit + getString(R.string.per_day);
		tvSleepUnit.setText(sleepAvgUnit);
		// 查询数据库
		Thread sleepThread = new Thread(new Runnable() {
			@Override
			public void run() {
				List<HistorySleep> histroySleeps = LitePalManager.instance().findHistroySleepByDate(date);
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SLEEP_HISTORY;
				msg.obj = histroySleeps;
				mHandler.sendMessage(msg);
			}
		});
		sleepThread.start();
	}

	private void loadSportValues(final Date date) {
		showLoadingDialog();
		isLoadSportOver = false;
		float sportMaxValue = 100;// 最大值
		String sportUnit = "";// 单位
		FormatType f = FormatType.FORMAT_0;// 格式化数字
		switch (rgCountSport.getCheckedRadioButtonId()) {
		case R.id.rb_sport_step:
			sportUnit = getString(R.string.step);
			sportMaxValue = StaticValue.MAX_STEP;
			break;
		case R.id.rb_sport_time:
			sportUnit = getString(R.string.minute);
			sportMaxValue = StaticValue.MAX_TIME;
			break;
		case R.id.rb_sport_distance:
			sportUnit = getString(R.string.km);
			f = FormatType.FORMAT_2;
			sportMaxValue = DataUtil.getDistanceValue(StaticValue.MAX_STEP);
			break;
		case R.id.rb_sport_cal:
			sportUnit = getString(R.string.kcal);
			sportMaxValue = (int) DataUtil.getKalValue(StaticValue.MAX_STEP);
			f = FormatType.FORMAT_2;

		}
		// 4.设置初始参数
		countSport.setMaxValue(sportMaxValue);
		countSport.setUnit(sportUnit);
		countSport.setFormat(f);
		String sportAvgUnit = sportUnit + getString(R.string.per_day);
		tvSportUnit.setText(sportAvgUnit);

		// 5.查询数据库
		Thread sleepThread = new Thread(new Runnable() {
			@Override
			public void run() {
				List<HistorySport> historySports = LitePalManager.instance().findHistroySportByDate(date);
				Message msg = mHandler.obtainMessage();
//				msg.what = MSG_SPORT_HISTORY;
				msg.obj = historySports;
				mHandler.sendMessage(msg);
			}
		});
		sleepThread.start();
	}
	
	private void loadSportValuesNew(final Date date) {
		String unit = (String) SharedPreferenceUtil.get(getApplicationContext(), StaticValue.SHARE_UNIT,StaticValue.DEFAULT_UNIT );
		showLoadingDialog();
		isLoadSportOver = false;
		float sportMaxValue = 100;// 最大值
		String sportUnit = "";// 单位
		FormatType f = FormatType.FORMAT_0;// 格式化数字
		switch (rgCountSport.getCheckedRadioButtonId()) {
		case R.id.rb_sport_step:
			sportUnit = getString(R.string.step);
			sportMaxValue = StaticValue.MAX_STEP;
			break;
		case R.id.rb_sport_time:
			sportUnit = getString(R.string.minute);
			sportMaxValue = StaticValue.MAX_TIME;
			break;
		case R.id.rb_sport_distance:
			
			if (TextUtils.equals(unit, StaticValue.UNIT_ZH)) {
				sportUnit = getString(R.string.km);
			}else{
				sportUnit = getString(R.string.unit_inch_distance);
			}
	
			f = FormatType.FORMAT_2;
			sportMaxValue = DataUtil.getDistanceValue(StaticValue.MAX_STEP);
			break;
		case R.id.rb_sport_cal:
			sportUnit = getString(R.string.kcal);
			sportMaxValue = (int) DataUtil.getKalValue(StaticValue.MAX_STEP);
			f = FormatType.FORMAT_2;

		}
		// 4.设置初始参数
		countSport.setMaxValue(sportMaxValue);
		countSport.setUnit(sportUnit);
		countSport.setFormat(f);
		String sportAvgUnit = sportUnit + getString(R.string.per_day);
		tvSportUnit.setText(sportAvgUnit);

		// 5.查询数据库
		Thread sleepThread = new Thread(new Runnable() {
			@Override
			public void run() {
				List<HistorySportNew> historySports = LitePalManager.instance().findHistroySportNewByDate(date);
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SPORT_HISTORY_NEW;
				msg.obj = historySports;
				mHandler.sendMessage(msg);
			}
		});
		sleepThread.start();
	}

	private String getAvgValue(float[] sportDatas) {
		if (sportDatas != null && sportDatas.length > 0) {
			float total = 0f;
			int size = sportDatas.length;
			for (int i = 0; i < size; i++) {
				total += sportDatas[i];
			}
			return DataUtil.format(total*1.0f / size);
		} else {
			return "0";
		}
	}
	private SimpleAlertDialog sad;
	
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
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							DataSupport.deleteAll(HistorySleep.class);
							DataSupport.deleteAll(HistorySportNew.class);
							countSleep.setDatas(new float[DateUtil.getMonthMaxDay(monthDate)]);
							countSport.setDatas(new float[DateUtil.getMonthMaxDay(monthDate)]);
							String sleepAvgValue = getAvgValue(null);
							tvSleepValue.setText(sleepAvgValue);
							String sportAvgValue = getAvgValue(null);
							tvSportValue.setText(sportAvgValue);
							sendBroadcast(new Intent(StaticValue.ACTION_DELETE_STARTED));
						}
					});
			
					
					
					// 删除 
					/*if (sad == null) {
						
					
				 	sad = (SimpleAlertDialog) new SimpleAlertDialog(CountActivity.this)
					.setTitle(getString(R.string.app_name))
					.setTitleIcon(R.drawable.ic_app_jun)
					.setMsg("确认清空历史数据？")
					.setLeftText("确认")
					.setLeftOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Log.i("xx", "==========确认========");
							sad.dismiss();
							DataSupport.deleteAll(HistorySleep.class);
							DataSupport.deleteAll(HistorySportNew.class);
							countSleep.setDatas(new float[DateUtil.getMonthMaxDay(monthDate)]);
							countSport.setDatas(new float[DateUtil.getMonthMaxDay(monthDate)]);
							String sleepAvgValue = getAvgValue(null);
							tvSleepValue.setText(sleepAvgValue);
							String sportAvgValue = getAvgValue(null);
							tvSportValue.setText(sportAvgValue);
						}
					})
					.setRightText("取消")
					.setRightOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Log.i("xx", "==========取消========");
								sad.dismiss();
						}
					});
					
					}
					sad.show();*/
				}
			});

		}
		pop.showAsDropDown(v, 0, -18);
	}

	private void closeLoadingDialog() {
		if (isLoadSportOver && isLoadSleepOver) {
			long now = System.currentTimeMillis();
			long deff = now - old;
			if (deff < 1000) {

				if (loadingDialog != null) {
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							loadingDialog.dismiss();
						}
					}, 1000);
				}
			} else {
				if (loadingDialog != null) {
					loadingDialog.dismiss();
				}
			}
		}
	}
}
