package com.mycj.junsda.activity;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.laput.service.XBlueBroadcastUtils;
import com.mycj.junsda.R;
import com.mycj.junsda.base.BaseActivity;
import com.mycj.junsda.bean.ProtolWrite;
import com.mycj.junsda.service.BlueService;
import com.mycj.junsda.util.CameraManager;
import com.mycj.junsda.util.CameraManager.CameraOpenReady;
import com.mycj.junsda.util.DisplayUtil;
import com.mycj.junsda.view.CameraSurfaceView;

public class CameraActivity extends BaseActivity  {
	private CameraSurfaceView surfaceView = null;
	/** ImageView ： 返回、拍照 **/
	private ImageView imgBack, imgCamera;
//	private ImpXplBroadcastReceiver xplReceiver = new ImpXplBroadcastReceiver(){
//		public void doCamera() {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					long currentTimeMillis = System.currentTimeMillis();
//					if (currentTimeMillis-lastTime<1500) {//当xiao于500ms
//						return;
//					}
//					mHandler.post(takePictureThread);
//					lastTime = currentTimeMillis;
//				}
//			});
//		};
//	};
//	
//	private XBlueBroadcastReceiver receiver = new XBlueBroadcastReceiver() {
//		public void doDeviceFound(final java.util.ArrayList<BluetoothDevice> devices) {
//		}
//
//		@Override
//		public void doServiceDiscovered(BluetoothDevice device) {
//		}
//
//		@Override
//		public void doStepAndCalReceiver(final long[] data) {
//			
//		}
//
//		@Override
//		public void doSyncStart() {
//		}
//
//		@Override
//		public void doSyncEnd() {
//			
//		}
//
//		@Override
//		public void doCamera() {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					long currentTimeMillis = System.currentTimeMillis();
//					if (currentTimeMillis-lastTime<1500) {//当xiao于500ms
//						return;
//					}
//					mHandler.post(takePictureThread);
//					lastTime = currentTimeMillis;
//				}
//			});
//		}
//
//		@Override
//		public void doConnectStateChange(BluetoothDevice device, int state) {
//			
//		};
//	};
//	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					long currentTimeMillis = System.currentTimeMillis();
					if (currentTimeMillis-lastTime<1500) {//当xiao于500ms
						return;
					}
					mHandler.post(takePictureThread);
					lastTime = currentTimeMillis;
				}
			});
		}
		
	};
	
	
	
	float previewRate = -1f;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
//	private BlueService xBlueService;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 拍照过程屏幕一直处于高亮
		// //设置手机屏幕朝向，一共有7种
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_camera);
		initUI();
		initViewParams();
		BtnListeners btnListeners = new BtnListeners();
		imgCamera.setOnClickListener(btnListeners);
		imgBack.setOnClickListener(btnListeners);
		imgSwitch.setOnClickListener(btnListeners);
		mCameraManager = CameraManager.instance(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		xplBluetoothService = getXplBluetoothService();
//		registerReceiver(xplReceiver, XplBluetoothService.getIntentFilter());
		
//		xBlueService = getBlueService();
		registerReceiver(receiver, new IntentFilter(XBlueBroadcastUtils.ACTION_TAKE_PICTURE));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		mHandler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
				imgCamera.setVisibility(View.VISIBLE);
//			}
//		}, 3000);
		
		if (getBlueService() != null && getBlueService().isConnect()) {
			getBlueService().write(ProtolWrite.instance().writeForCamera(1));
		}
		
		
		
		mHandler.postDelayed(openThread, 1500);
		
	}
	
	private SurfaceHolder mSurfaceHolder;
	private int cameraState = FRONT ;
	public final static int FRONT = 0;
	public final static int BACK = 1;
	private Runnable openThread = new Runnable() {
		
		@Override
		public void run() {
		mCameraManager.openCamera(new CameraOpenReady() {
				// 打开完成后，开启预览
				@Override
				public void openReady() {
					mSurfaceHolder = surfaceView.getSurfaceHolder();
					boolean isSurfaceCreate = mSurfaceHolder.isCreating();
					
					mCameraManager.startPreview(mSurfaceHolder, previewRate);
				}
			},cameraState);
		}
	};
	
	private Runnable takePictureThread = new Runnable() {
		
		@Override
		public void run() {
			mCameraManager.takePicture();
		}
	};
	private ImageView imgSwitch;
	
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(openThread);
		mHandler.removeCallbacks(takePictureThread);
		mCameraManager.stopPreviewCamera();
		
		if (getBlueService() != null && getBlueService().isConnect()) {
			getBlueService().write(ProtolWrite.instance().writeForCamera(0));
		}
	}
	
	
	@Override
	protected void onStop() {
//		if(isXplConnected()){
//			byte[] hexDataForUpdatePhotographState = I2WatchProtocolDataForWrite.hexDataForUpdatePhotographState(0);
////			mSimpleBlueService.writeCharacteristic(I2WatchProtocolDataForWrite.hexDataForUpdatePhotographState(0));
//			xplBluetoothService.writeCharacteristic(hexDataForUpdatePhotographState);
//		}
		
//		unregisterReceiver(xplReceiver);
		mCameraManager.stopPreviewCamera();
		unregisterReceiver(receiver);
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}

	private void initUI() {

		imgBack = (ImageView) findViewById(R.id.img_back);
		imgCamera = (ImageView) findViewById(R.id.img_camera);
		imgSwitch = (ImageView) findViewById(R.id.img_switch);
		surfaceView = (CameraSurfaceView) findViewById(R.id.surface);
	}

	private void initViewParams() {
		android.view.ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this); // 默认全屏的比例预览
		surfaceView.setLayoutParams(params);

		// 手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
		// LayoutParams p2 = shutterBtn.getLayoutParams();
		// p2.width = DisplayUtil.dip2px(this, 80);
		// p2.height = DisplayUtil.dip2px(this, 80);;
		// shutterBtn.setLayoutParams(p2);

	}


//	@Override
//	public void cameraHasOpened() {
//		// TODO Auto-generated method stub
//		SurfaceHolder holder = surfaceView.getSurfaceHolder();
//		//Log.d("CaneraActivity", " holder.isCreating() -->" + holder.isCreating());
////		CameraInterface.getInstance(getApplicationContext()).doStartPreview(holder, previewRate);
//	}
	private long 	lastTime=0;
	private CameraManager mCameraManager;
//	private XplBluetoothService xplBluetoothService;
	private class BtnListeners implements OnClickListener {
		@Override
		public void onClick(View v) {
			long currentTimeMillis = System.currentTimeMillis();
			switch (v.getId()) {
			case R.id.img_camera:
		
				if (currentTimeMillis-lastTime<1500) {//当xiao于500ms
					//Log.e("", "2次点击小于500ms ， 不能拍照");
//					lastTime = currentTimeMillis;
					return;
				}
				mHandler.post(takePictureThread);
				break;
			case R.id.img_back:
				
				
			/*	if (xBlueService != null && xBlueService.isAllConnected()) {
					byte[] hexDataForUpdatePhotographState = I2WatchProtocolDataForWrite.hexDataForUpdatePhotographState(0);
					xBlueService.write(hexDataForUpdatePhotographState);
				}*/
				
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						finish();
					}
				}, 500);
				break;
				
			case R.id.img_switch:
				if (cameraState==BACK) {
					cameraState = FRONT;
				}else if(cameraState == FRONT){
					cameraState = BACK;
				}
				
				mCameraManager.stopPreviewCamera();
				mHandler.postDelayed(openThread, 250);
				break;
			default:
				break;
			}
			lastTime = currentTimeMillis;
		}
		
		
	}
	
	@Override
	public void onBackPressed() {
		
		/*if (xBlueService != null && xBlueService.isAllConnected()) {
			byte[] hexDataForUpdatePhotographState = I2WatchProtocolDataForWrite.hexDataForUpdatePhotographState(0);
			xBlueService.write(hexDataForUpdatePhotographState);
		}*/
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		}, 500);
		
		
			
		
			}
		
}
