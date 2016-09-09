package com.mycj.junsda.activity;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mycj.junsda.R;
import com.mycj.junsda.R.layout;
import com.mycj.junsda.adapter.HistroySleepAdapter;
import com.mycj.junsda.adapter.HistroySportAdapter;
import com.mycj.junsda.adapter.HistroySportAdapterNew;
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
import com.mycj.junsda.view.DateUtil;
import com.mycj.junsda.view.FangTextView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class HistorySportActivity extends BaseActivity implements
		OnClickListener {
	protected static final int MSG_SPORT_DATA = 0xb1;
	protected static final int MSG_SPORT_DATA_NEW = 0xc1;
	protected static final int MSG_SPORT_HISTROY = 0xb2;
	protected static final int MSG_SPORT_HISTROY_NEW = 0xc2;
	protected static final String KEY_SPORT_DATA = "KEY_SPORT_DATA";
	private ImageView imgBack, imgShare;
	private FangTextView tvUp, tvDown, tvSportDate, tvSportSteps,
			tvSportDistance, tvSportCount, tvSportTime, tvSportConsume;
	private LinearLayout llSportTime, llSportDistance, llSportConsume;
	private ListView lvHistorySport;
	private List<HistorySport> datas;
	private List<HistorySportNew> datasNew;
	private HistroySportAdapter adapter;
	private HistroySportAdapterNew adapterNew;
	private Date toMonth;
	private LitePalManager litePalManager;
	private LinearLayout llSportSteps;
	private boolean isLoading1 = false, isLoading2 = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case StaticValue.MSG_SHARE:
				String path = (String) msg.obj;
				ShareUtil.shareImage(path, HistorySportActivity.this,
						getString(R.string.share));
				break;
			case MSG_SPORT_DATA:
				Bundle data = msg.getData();
				int[] sportData = data.getIntArray(KEY_SPORT_DATA);
				int count = sportData[0];
				int sportSteps = sportData[1];
				int time = sportData[2];// 单位：秒
				String sportDistance = DataUtil.getDistance(sportSteps);
				String sportKal = DataUtil.getKal(sportSteps);
				String sportTime = DataUtil.format(time / 60f);
				tvSportCount.setText(String.valueOf(count)
						+ getString(R.string.times));
				tvSportSteps.setText(String.valueOf(sportSteps)
						+ getString(R.string.step));
				tvSportTime.setText(sportTime + getString(R.string.hour));
				tvSportDistance.setText(sportDistance + getString(R.string.km));
				tvSportConsume.setText(sportKal + getString(R.string.kcal));
				isLoading1 = false;
				break;
			case MSG_SPORT_DATA_NEW:
				Bundle dataNew = msg.getData();
				int countNew = dataNew.getInt("count");
				int sportStepsNew = dataNew.getInt("sportSteps");
				int sportTimesNew = dataNew.getInt("sportTimes");
				float sportDistancesNew = dataNew.getFloat("sportDistances");
				float sportCaloriesNew = dataNew.getFloat("sportCalories");
				tvSportCount.setText(String.valueOf(countNew)
						+ getString(R.string.times));
				tvSportSteps.setText(String.valueOf(sportStepsNew)
						+ getString(R.string.step));
				tvSportTime.setText(DataUtil.format(sportTimesNew*1.0f/60) + getString(R.string.hour));
				String unit = (String) SharedPreferenceUtil.get(getApplicationContext(), StaticValue.SHARE_UNIT,StaticValue.DEFAULT_UNIT );
				if (TextUtils.equals(unit, StaticValue.UNIT_ZH)) {
					tvSportDistance.setText(sportDistancesNew
							+ getString(R.string.km));
				}else{
					String distanceStr = ConversionUtil.metricToLnch(getApplicationContext(), sportDistancesNew, ConversionUtil.Unit.Distance);
					tvSportDistance.setText(distanceStr);
				}
				
				tvSportConsume.setText(sportCaloriesNew
						+ getString(R.string.kcal));
				isLoading1 = false;
				break;
			case MSG_SPORT_HISTROY:
				ArrayList<HistorySport> sports = (ArrayList<HistorySport>) msg.obj;
				if (sports == null || sports.size() == 0) {
					datas.clear();
					// getTestData();
				} else {
					datas.clear();
					datas.addAll(sports);
				}
				adapter.notifyDataSetChanged();
				isLoading2 = false;
				break;
			case MSG_SPORT_HISTROY_NEW:
				ArrayList<HistorySportNew> sportsNew = (ArrayList<HistorySportNew>) msg.obj;
				if (sportsNew == null || sportsNew.size() == 0) {
					datasNew.clear();
					// getTestData();
				} else {
					datasNew.clear();
					datasNew.addAll(sportsNew);
				}
				adapterNew.notifyDataSetChanged();
				isLoading2 = false;
				   mPtrFrame.refreshComplete();
				break;
			default:
				break;
			}

		};
	};
	private PtrClassicFrameLayout mPtrFrame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_sport);
		litePalManager = LitePalManager.instance();
		initViews();
		registerReceiver(br, new IntentFilter(StaticValue.ACTION_DELETE_STARTED));
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(br);
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.img_share:
			// toast("分享");
			share(mHandler);
			break;
		case R.id.up:
			if (!isLoading1 && !isLoading2) {
				toMonth = DateUtil.getDateOfDiffMonth(toMonth, -1);
//				loadSportDataByDate(toMonth);
				loadSportDataByDateNew(toMonth);
			}
			break;
		case R.id.down:
			if (!isLoading1 && !isLoading2) {
				toMonth = DateUtil.getDateOfDiffMonth(toMonth, 1);
//				loadSportDataByDate(toMonth);
				loadSportDataByDateNew(toMonth);
			}
			break;
		case R.id.ll_sport_steps:
			intent = new Intent(HistorySportActivity.this, CountActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_sport_time:
			intent = new Intent(HistorySportActivity.this, CountActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_sport_distance:
			intent = new Intent(HistorySportActivity.this, CountActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_sport_consume:
			intent = new Intent(HistorySportActivity.this, CountActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void initViews() {
		imgBack = (ImageView) findViewById(R.id.img_back);
		imgShare = (ImageView) findViewById(R.id.img_share);

		tvUp = (FangTextView) findViewById(R.id.up);
		tvDown = (FangTextView) findViewById(R.id.down);
		tvSportDate = (FangTextView) findViewById(R.id.tv_date);
		tvSportCount = (FangTextView) findViewById(R.id.tv_sport_count);
		tvSportSteps = (FangTextView) findViewById(R.id.tv_sport_steps);
		tvSportTime = (FangTextView) findViewById(R.id.tv_sport_time);
		tvSportDistance = (FangTextView) findViewById(R.id.tv_sport_distance);
		tvSportConsume = (FangTextView) findViewById(R.id.tv_sport_consume);

		llSportSteps = (LinearLayout) findViewById(R.id.ll_sport_steps);
		llSportTime = (LinearLayout) findViewById(R.id.ll_sport_time);
		llSportDistance = (LinearLayout) findViewById(R.id.ll_sport_distance);
		llSportConsume = (LinearLayout) findViewById(R.id.ll_sport_consume);
		// listView
		lvHistorySport = (ListView) findViewById(R.id.lv_history_sport);
		// 加载数据
		toMonth = new Date();
		

	/*	datas = new ArrayList<HistorySport>();
		adapter = new HistroySportAdapter(datas);
		lvHistorySport.setAdapter(adapter);
		loadSportDataByDate(toMonth);*/

		
		 datasNew = new ArrayList<HistorySportNew>(); adapterNew = new
		 HistroySportAdapterNew(datasNew);
		  lvHistorySport.setAdapter(adapterNew);
		  
			initpullToRefreshUi();
			
//		 loadSportDataByDateNew(toMonth);

		// listener
		imgBack.setOnClickListener(this);
		imgShare.setOnClickListener(this);
		tvUp.setOnClickListener(this);
		tvDown.setOnClickListener(this);
		llSportSteps.setOnClickListener(this);
		llSportTime.setOnClickListener(this);
		llSportDistance.setOnClickListener(this);
		llSportConsume.setOnClickListener(this);
		lvHistorySport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
		/*		HistorySport historySport = datas.get(position);
				Intent intent = new Intent(HistorySportActivity.this,
						SportInfoActivity.class);
				Bundle data = new Bundle();
				data.putParcelable("historySport", historySport);
				intent.putExtras(data);
				startActivity(intent);*/
				
				
				
				HistorySportNew historySportNew = datasNew.get(position);
				Intent intent = new Intent(HistorySportActivity.this,
						SportInfoActivity.class);
				Bundle data = new Bundle();
				data.putParcelable("historySportNew", historySportNew);
				intent.putExtras(data);
				startActivity(intent);
			}
		});
	}

	/**
	 * 根据今天的月份加载所有的天数历史数据
	 * 
	 * @param date
	 */
	private void loadSportDataByDate(final Date date) {
		tvSportDate.setText(DateUtil.dateToString(date, "yyyy/MM"));
		// 数据库查询<放在一个Thread里>查询头部信息
		Thread sportThread = new Thread(new Runnable() {
			@Override
			public void run() {
				isLoading1 = true;
				int count = litePalManager.findHistroySportCountByDate(date);
				int sportSteps = litePalManager
						.findHistroySportStepsByDate(date);
				int time = litePalManager.findHistroySportTimeByDate(date);// 单位秒
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SPORT_DATA;
				Bundle b = new Bundle();
				b.putIntArray(KEY_SPORT_DATA, new int[] { count, sportSteps,
						time });
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		});
		sportThread.start();
		// 查询listview
		Thread sportHistroyThead = new Thread(new Runnable() {

			@Override
			public void run() {
				isLoading2 = true;
				List<HistorySport> result = litePalManager
						.findHistroySportByDate(date);
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SPORT_HISTROY;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		});
		sportHistroyThead.start();
	}

	/**
	 * 根据今天的月份加载所有的天数历史数据
	 * 
	 * @param date
	 */
	private void loadSportDataByDateNew(final Date date) {
		tvSportDate.setText(DateUtil.dateToString(date, "yyyy/MM"));
		// 数据库查询<放在一个Thread里>查询头部信息
		Thread sportThread = new Thread(new Runnable() {
			@Override
			public void run() {
				isLoading1 = true;
				int count = litePalManager.findHistroySportCountByDateNew(date);
				int sportSteps = litePalManager
						.findHistroySportStepsByDateNew(date);
				int sportTimes = litePalManager
						.findHistroySportTimeByDateNew(date);// 单位秒
				float sportDistances = litePalManager
						.findHistroySportDistanceByDate(date); // 单位千米
				float sportCalories = litePalManager
						.findHistroySportCalorieByDate(date); // 单位千卡

				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SPORT_DATA_NEW;
				Bundle b = new Bundle();
				b.putInt("count", count);
				b.putInt("sportSteps", sportSteps);
				b.putInt("sportTimes", sportTimes);
				b.putFloat("sportDistances", sportDistances);
				b.putFloat("sportCalories", sportCalories);
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		});
		sportThread.start();
		// 查询listview
		Thread sportHistroyThead = new Thread(new Runnable() {

			@Override
			public void run() {
				isLoading2 = true;
				List<HistorySportNew> result = litePalManager
						.findHistroySportNewByDate(date);
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SPORT_HISTROY_NEW;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		});
		sportHistroyThead.start();
		
	}

	public void openMoreInfo(View v) {
		Intent intent = new Intent(HistorySportActivity.this,
				CountActivity.class);
		startActivity(intent);
	}
	
	private void initpullToRefreshUi(){
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);
        mPtrFrame.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				   mPtrFrame.autoRefresh();
			}
		}, 100);
    

      /*  mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    final String url = mAdapter.getItem(position).optString("pic");
                    if (!TextUtils.isEmpty(url)) {
                        getContext().pushFragmentToBackStack(MaterialStyleFragment.class, url);
                    }
                }
            }
        });

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);

        mAdapter = new ListViewDataAdapter<JsonData>();
        mAdapter.setViewHolderClass(this, ViewHolder.class);
        mListView.setAdapter(mAdapter);*/

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvHistorySport, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadSportDataByDateNew(toMonth);
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
}
	
	// Test
	private void getTestData() {
		for (int i = 0; i < Math.random() * 100; i++) {
			datas.add(new HistorySport());
		}
	}
	
	// 删除动作的intent
	private BroadcastReceiver br = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					Log.i("HistorySportActivity", "\\- -- --- ---- 删除  ---- --- --- -\\");
					loadSportDataByDateNew(toMonth);
				}
			});
		}
	};
	
	// 
}
