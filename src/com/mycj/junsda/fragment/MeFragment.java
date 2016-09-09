package com.mycj.junsda.fragment;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import com.mycj.junsda.R;
import com.mycj.junsda.bean.LitePalManager;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.util.ConversionUtil;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;
import com.mycj.junsda.view.DateUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

	private RelativeLayout rlHistrorySleep;
	private RelativeLayout rlHistrorySprot;
	private TextView tvHistoryDistance;
	private TextView tvHistoryTime;
	private TextView tvHistoryStep;
	private TextView tvHistoryCal;
	private PtrClassicFrameLayout mPtrFrame;
	private ScrollView mScrollView;

	public MeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View meView = inflater.inflate(R.layout.fragment_me, container, false);
		// LinearLayout llTop = (LinearLayout) meView.findViewById(R.id.ll_top);
		// ImageView imgTop = new ImageView(getContext());
		// imgTop.setImageResource(R.drawable.ic_me_top);
		// // imgTop.setScaleType(ImageView.ScaleType.FIT_XY);
		// llTop.addView(imgTop);

		// // ImageView imgTop = (ImageView) meView.findViewById(R.id.img_top);
		// imgTop.setLayoutParams(new
		// LinearLayout.LayoutParams(DisplayUtil.getScreenMetrics(getContext()).x,LinearLayout.LayoutParams.WRAP_CONTENT));
		rlHistrorySleep = (RelativeLayout) meView
				.findViewById(R.id.rl_history_sleep);
		rlHistrorySprot = (RelativeLayout) meView
				.findViewById(R.id.rl_history_sport);
		tvHistoryDistance = (TextView) meView
				.findViewById(R.id.tv_history_distance);
		tvHistoryTime = (TextView) meView.findViewById(R.id.tv_history_time);
		tvHistoryStep = (TextView) meView.findViewById(R.id.tv_history_step);
		tvHistoryCal = (TextView) meView.findViewById(R.id.tv_history_cal);

		rlHistrorySleep.setOnClickListener(this);
		rlHistrorySprot.setOnClickListener(this);
		initRefreshView(meView);
		return meView;
	}

	private void initRefreshView(View v){
	        mScrollView = (ScrollView) v.findViewById(R.id.rotate_header_scroll_view);
	        mPtrFrame = (PtrClassicFrameLayout) v.findViewById(R.id.rotate_header_web_view_frame);
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
	                   
//	                        refresh();
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
	            	refreshMeFragmentUi();
	                mPtrFrame.autoRefresh();
	            }
	        }, 100);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		refreshMeFragmentUi();
	}

	public void refreshMeFragmentUi() {
		// new MyAsyncTask().execute();

		//
		new MyAsyncTaskNew().execute();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.rl_history_sleep:
			if (mOnMeFragmentClickListener != null) {
				mOnMeFragmentClickListener.doLookSleepHistory();
			}
			break;
		case R.id.rl_history_sport:
			if (mOnMeFragmentClickListener != null) {
				mOnMeFragmentClickListener.doLookSportHistory();
			}
			break;
		}

	}

	public interface OnMeFragmentClickListener {
		void doLookSleepHistory();

		void doLookSportHistory();
	}

	private OnMeFragmentClickListener mOnMeFragmentClickListener;
	private Object loadingDialog;
	private long old;

	public void setOnMeFragmentClickListener(
			OnMeFragmentClickListener mOnMeFragmentClickListener) {
		this.mOnMeFragmentClickListener = mOnMeFragmentClickListener;
	}

	private class MyAsyncTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {

			int totalStep = LitePalManager.instance().getTotalStep();
			int time = LitePalManager.instance().getTotalSportTime();
			if (getActivity() != null) {
				// String distanceStr =
				// DataUtil.format(DataUtil.getDistanceValue(totalStep))+getActivity().getString(R.string.km);
				String distanceStr = DataUtil.getDistanceWithUnit(totalStep,
						getActivity());
				String sportTimeStr = DateUtil
						.formateTime2(time, getActivity());
				String kalStr = DataUtil
						.getKalWithUnit(totalStep, getContext());
				String stepStr = String.valueOf(totalStep)
						+ getString(R.string.step);
				return new String[] { distanceStr, sportTimeStr, kalStr,
						stepStr };
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			rt = result;
			if (result != null) {
				tvHistoryDistance.setText(result[0]);
				tvHistoryTime.setText(result[1]);
				tvHistoryCal.setText(result[2]);
				tvHistoryStep.setText(result[3]);
			}
			
			
			
			
		}
		
	}
	
	private String[] rt;
	
	

	/******************************
	 * 运动新数据 ------带距离和卡路里
	 ******************************/
	private class MyAsyncTaskNew extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {

			int totalStep = LitePalManager.instance().getTotalStepNew();
			int time = LitePalManager.instance().getTotalSportTimeNew();
			float distance = LitePalManager.instance()
					.getTotalSportDistanceNew();
			float alorie = LitePalManager.instance().getTotalSportCalorieNew();

			// if (getActivity()!=null) {
			// String distanceStr =
			// DataUtil.format(DataUtil.getDistanceValue(totalStep))+getActivity().getString(R.string.km);
			if (isAdded()) {
				String unit = (String) SharedPreferenceUtil.get(getContext(), StaticValue.SHARE_UNIT,StaticValue.DEFAULT_UNIT );
				String stepStr = String.valueOf(totalStep)
						+ getString(R.string.step);
				String sportTimeStr = DateUtil.formateTime3(time, getContext());
				
				
				String distanceStr = DataUtil.format(distance) + ""
						+ getString(R.string.km);
				String kalStr = DataUtil.format(alorie) + ""
						+ getString(R.string.kcal);
				
				if (TextUtils.equals(unit, StaticValue.UNIT_ZH)) {
					
				}else if (TextUtils.equals(unit, StaticValue.UNIT_EN)){
					 distanceStr = ConversionUtil.metricToLnch(getContext(), distance, ConversionUtil.Unit.Distance);
				}
				return new String[] { distanceStr, sportTimeStr, kalStr,
						stepStr };
				
				
			} else {
				return null;
			}

			// }else{
			// return null;
			// }
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
		     mPtrFrame.refreshComplete();
			rt = result;
			if (result != null) {
				Log.e("", "bu bei detached ");
				tvHistoryDistance.setText(result[0]);
				tvHistoryTime.setText(result[1]);
				tvHistoryCal.setText(result[2]);
				tvHistoryStep.setText(result[3]);

			}
		}

	}
	
	
	private void refresh(){
		if (rt != null) {
			tvHistoryDistance.setText(rt[0]);
			tvHistoryTime.setText(rt[1]);
			tvHistoryCal.setText(rt[2]);
			tvHistoryStep.setText(rt[3]);
		}
	}
}
