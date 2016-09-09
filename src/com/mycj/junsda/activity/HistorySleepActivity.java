package com.mycj.junsda.activity;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mycj.junsda.R;
import com.mycj.junsda.R.layout;
import com.mycj.junsda.adapter.HistroySleepAdapter;
import com.mycj.junsda.base.BaseActivity;
import com.mycj.junsda.bean.HistorySleep;
import com.mycj.junsda.bean.LitePalManager;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.util.ShareUtil;
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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HistorySleepActivity extends BaseActivity implements OnClickListener {

	protected static final int MSG_SLEEP_DATA = 0x0a;
	protected static final String KEY_SLEEP_DATA = "KEY_SLEEP_DATA";
	protected static final int MSG_SLEEP_HISTROY = 0x0b;
	private ImageView imgBack;
	private ImageView imgShare;
	private FangTextView tvUp;
	private FangTextView tvDown;
	private FangTextView tvSleepDate;
	private FangTextView tvSleepSteps;
	private FangTextView tvSleepTime;
	private FangTextView tvSleepDeep;
	private FangTextView tvSleepLight;
	private LinearLayout llSleepTime;
	private LinearLayout llSleepDeep;
	private LinearLayout llSleepLight;
	private ListView lvHistorySleep;
	private List<HistorySleep> datas;
	private HistroySleepAdapter adapter;
	private Date toMonth;
	private LitePalManager litePalManager;
	private boolean isLoading1 = false;
	private boolean isLoading2 = false;
	
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case StaticValue.MSG_SHARE:
				String path = (String) msg.obj;
				ShareUtil.shareImage(path, HistorySleepActivity.this, getString(R.string.share));
				break;
			case MSG_SLEEP_DATA:
				// 设置头部数据<翻身次数、时间、深睡、浅睡>
				Bundle data = msg.getData();
				float[] sleepData = data.getFloatArray(KEY_SLEEP_DATA);
				String times = getString(R.string.times);
				String hour = getString(R.string.hour);
				tvSleepSteps.setText(String.valueOf((int)sleepData[0]) + times);
				tvSleepTime.setText(DataUtil.format(sleepData[1] / (60f )) + hour);
				tvSleepDeep.setText(DataUtil.format(sleepData[2] / (60f )) + hour);
				tvSleepLight.setText(DataUtil.format(sleepData[3] / (60f )) + hour);
				isLoading1 = false;
				break;
			case MSG_SLEEP_HISTROY:
				// 设置睡眠统计数据<listView>
				ArrayList<HistorySleep> findHistroySleepByDate = (ArrayList<HistorySleep>) msg.obj;
				if (findHistroySleepByDate == null || findHistroySleepByDate.size()==0) {
					datas.clear();
//					getTestData();// 测试
					
				} else {
					datas.clear();
					datas.addAll(findHistroySleepByDate);
				}
				adapter.notifyDataSetChanged();
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
		setContentView(R.layout.activity_history_sleep);
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
		Intent intent = null;
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
				loadSleepDataByDate(toMonth);
			}
			
			break;
		case R.id.down:
			if (!isLoading1 && !isLoading2) {
				toMonth = DateUtil.getDateOfDiffMonth(toMonth, 1);
				loadSleepDataByDate(toMonth);
			}
			break;
		case R.id.ll_sleep_time:
			intent = new Intent(HistorySleepActivity.this, CountActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_sleep_deep:
			intent = new Intent(HistorySleepActivity.this, CountActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_sleep_light:
			intent = new Intent(HistorySleepActivity.this, CountActivity.class);
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
		tvSleepDate = (FangTextView) findViewById(R.id.tv_date);
		tvSleepSteps = (FangTextView) findViewById(R.id.tv_sleep_steps);
		tvSleepTime = (FangTextView) findViewById(R.id.tv_sleep_time);
		tvSleepDeep = (FangTextView) findViewById(R.id.tv_sleep_deep);
		tvSleepLight = (FangTextView) findViewById(R.id.tv_sleep_light);
		llSleepTime = (LinearLayout) findViewById(R.id.ll_sleep_time);
		llSleepDeep = (LinearLayout) findViewById(R.id.ll_sleep_deep);
		llSleepLight = (LinearLayout) findViewById(R.id.ll_sleep_light);
		// listView
		lvHistorySleep = (ListView) findViewById(R.id.lv_history_sleep);
		datas = new ArrayList<HistorySleep>();
		adapter = new HistroySleepAdapter(datas);
		lvHistorySleep.setAdapter(adapter);
		// 日期
		toMonth = new Date();
//		loadSleepDataByDate(toMonth);
		initpullToRefreshUi();
		// listener
		imgBack.setOnClickListener(this);
		imgShare.setOnClickListener(this);
		tvUp.setOnClickListener(this);
		tvDown.setOnClickListener(this);
		llSleepTime.setOnClickListener(this);
		llSleepDeep.setOnClickListener(this);
		llSleepLight.setOnClickListener(this);

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
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvHistorySleep, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            	loadSleepDataByDate(toMonth);
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
	private void loadSleepDataByDate(final Date date) {
		tvSleepDate.setText(DateUtil.dateToString(date, "yyyy/MM"));
		// 数据库查询<放在一个Thread里> 头部信息
		Thread sleepThread = new Thread(new Runnable() {
			@Override
			public void run() {
				isLoading1 = true;
				float sleepSteps = litePalManager.findHistroySleepCount(date);// 睡眠次数
				float sleepDeep = litePalManager.findHistroySleepDeepByDate(date);
				float sleepLight = litePalManager.findHistroySleepLightByDate(date);
				float sleepTime = sleepDeep + sleepLight;
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SLEEP_DATA;
				Bundle b = new Bundle();
				b.putFloatArray(KEY_SLEEP_DATA, new float[] { sleepSteps, sleepTime, sleepDeep, sleepLight });
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		});
		sleepThread.start();
		// listView
		Thread sleepHistroyThead = new Thread(new Runnable() {

			@Override
			public void run() {
				isLoading2 = true;
				List<HistorySleep> findHistroySleepByDate = litePalManager.findHistroySleepByDate(date);
			
				
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_SLEEP_HISTROY;
				msg.obj = findHistroySleepByDate;
				mHandler.sendMessage(msg);
			}
		});
		sleepHistroyThead.start();
	}
	
	public void openMoreInfo(View v){
		Intent intent = new Intent(HistorySleepActivity.this, CountActivity.class);
		startActivity(intent);
	}
	
	// Test
	private void getTestData() {
		for (int i = 0; i < Math.random() * 100; i++) {
			datas.add(new HistorySleep());
		}
	}
	
	// 删除动作的intent
	private BroadcastReceiver br = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					Log.i("HistorySleepActivity", "\\- -- --- ---- 删除  ---- --- --- -\\");
					loadSleepDataByDate(toMonth);
				}
			});
		}
		
	};

}
