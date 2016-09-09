package com.mycj.junsda.activity;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.laput.service.XBlueBroadcastReceiver;
import com.laput.service.XBlueBroadcastUtils;
import com.laputa.blue.broadcast.LaputaBroadcast;
import com.laputa.blue.core.SimpleLaputaBlue;
import com.laputa.blue.util.BondedDeviceUtil;
import com.mycj.junsda.R;
import com.mycj.junsda.adapter.DeviceAdapter;
import com.mycj.junsda.adapter.DeviceAdapterNew;
import com.mycj.junsda.base.BaseActivity;
import com.mycj.junsda.bean.HistorySleep;
import com.mycj.junsda.bean.HistorySport;
import com.mycj.junsda.service.BlueService;
import com.mycj.junsda.view.AlertDialog;
import com.mycj.junsda.view.FangTextView;
import com.mycj.junsda.view.LaputaAlertDialog;
import com.mycj.junsda.view.RadarView;
import com.mycj.junsda.view.XplAlertDialog;

public class DeviceAcitivy extends BaseActivity {
	private List<BluetoothDevice> deviceList;
	private ListView lvDeivce;
	private DeviceAdapterNew adapter;
	// private XplBluetoothService xplBluetoothService;
	private XplAlertDialog showProgressDialog;
	private RadarView radarView;
	private ImageView imgBack;
	private ImageView imgConnect;
	private FangTextView tvBindedName;
	private FangTextView tvBindedAddress;
	private LinearLayout llBindedInfo;
	private boolean isStopScan = true;
	// private RelativeLayout rlBindedInfo;
	private Handler mHandler = new Handler() {
	};

	/*private XBlueBroadcastReceiver xReceiver = new XBlueBroadcastReceiver() {

		@Override
		public void doSportSyncStateChanged(int state) {

		}

		@Override
		public void doSportChanged(HistorySport sport) {

		}

		@Override
		public void doSleepSyncStateChanged(int state) {

		}

		@Override
		public void doSleepChanged(HistorySleep sleep) {

		}

		@Override
		public void doServiceDiscovered(final BluetoothDevice device) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (showProgressDialog != null) {
						showProgressDialog.cancel();
					}
					if (device != null) {
						// initBindedDeviceInfo(device.getAddress(),
						// device.getName());
					}
					Toast.makeText(getApplicationContext(), getString(R.string.connected), 0).show();
					// deviceList.remove(device);

					adapter.setConnectPos(deviceList.indexOf(device));
					adapter.setIsConecting(false);
				}
			});
		}

		@Override
		public void doHeartRateChanged(int hr) {
			// TODO Auto-generated method stub

		}

		@Override
		public void doDeviceFound(final ArrayList<BluetoothDevice> devices) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (!isStopScan) {
						Log.e("", "=================================devices" + devices);
						deviceList.clear();
						if (devices != null) {
							deviceList.addAll(devices);
						}
						adapter.notifyDataSetChanged();
					}
				}
			});
		}

		@Override
		public void doConnectStateChange(BluetoothDevice device, final int state) {
			mHandler.post(new Runnable() {
				public void run() {
					if (state == BluetoothGatt.STATE_DISCONNECTED) {
						tvBindedAddress.setTextColor(Color.RED);
					} else {

					}
					// updateBlueState();
				}
			});
		}

		@Override
		public void doBluetoothEnable() {
			// TODO Auto-generated method stub

		}
	};*/
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(LaputaBroadcast.ACTION_LAPUTA_DEVICE_FOUND)) {
				final BluetoothDevice device = intent.getExtras().getParcelable(LaputaBroadcast.EXTRA_LAPUTA_DEVICE);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (!isStopScan) {
							if (!deviceList.contains(device)) {
								deviceList.add(device);
							}
							/*
							  deviceList.clear();
								if (devices != null) {
								deviceList.addAll(devices);
							}*/
							adapter.notifyDataSetChanged();
						}
					}
				});
			}else if (action.equals(LaputaBroadcast.ACTION_LAPUTA_STATE)){
				int state = intent.getExtras().getInt(LaputaBroadcast.EXTRA_LAPUTA_STATE);
				final String address = intent.getExtras().getString(LaputaBroadcast.EXTRA_LAPUTA_ADDRESS);
				if (state == SimpleLaputaBlue.STATE_SERVICE_DISCOVERED) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (showProgressDialog != null) {
								showProgressDialog.cancel();
							}
							
						
							
							BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
							if (device != null) {
								// initBindedDeviceInfo(device.getAddress(),
								// device.getName());
								
								updateLaputaBindInfo(true);
								deviceList.remove(device);
								adapter.setConnectPos(-1);
								
							}
							Toast.makeText(getApplicationContext(), getString(R.string.connected), 0).show();
							// deviceList.remove(device);

						
						}
					});
				}else if(state != SimpleLaputaBlue.STATE_SERVICE_DISCOVERED){
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							updateLaputaBindInfo(false);
						}
					});
				}
			}
		}
	};
	
//	private BlueService xBlueService;
	private AlertDialog isDisBindedDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settting_device);
//		xBlueService = getBlueService();
		registerReceiver(receiver, LaputaBroadcast.getIntentFilter());
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		radarView.stop();
		unregisterReceiver(receiver);
		if (showProgressDialog != null) {
			showProgressDialog.dismiss();
		}
	}
	
	private Runnable changeConnectingStateRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (showProgressDialog != null) {
				showProgressDialog.cancel(getString(R.string.connect_fail));
			}
			if (getBlueService() != null && getBlueService().isConnect()) {
				adapter.setIsConecting(false);
			} else {
				adapter.setConnectPos(-1);
				adapter.setIsConecting(false);
			}
		}
	};
	private RelativeLayout rlLaputaBind;
	private FangTextView tvLaputaBindedName;
	private FangTextView tvLaputaBindedInfo;
	
	/**
	 * 连接成功时，显示；
	 * 
	 * @param isconnect
	 */
	private void updateLaputaBindInfo(boolean isconnect){
	
		if (isconnect) {
			String address = BondedDeviceUtil.get(1, this);
			BluetoothDevice device = getBlueService().getSimpleLaputaBlue().getRemoteDevice(address);
			String name ="";
			if (device != null) {
				name = (device.getName()==null?getString(R.string.nuknow):device.getName())+" ";
			}
			String info = "";
			if (!TextUtils.isEmpty(address)) {
				 info = name + address.substring(address.length()-5, address.length());
			}else{
			}
		
			tvLaputaBindedName.setText(info);
			rlLaputaBind.setVisibility(View.VISIBLE);
			tvLaputaBindedInfo.setText(getString(R.string.connected));
		}else{
			rlLaputaBind.setVisibility(View.GONE);
			tvLaputaBindedInfo.setText(getString(R.string.disconnected));
		}
	}
	
	private void initViews() {
	
		imgBack = (ImageView) findViewById(R.id.img_back);
		imgConnect = (ImageView) findViewById(R.id.img_connect);
		tvBindedName = (FangTextView) findViewById(R.id.tv_binded_device_name);
		tvBindedAddress = (FangTextView) findViewById(R.id.tv_binded_device_address);
		llBindedInfo = (LinearLayout) findViewById(R.id.ll_binded_info);
		
		rlLaputaBind = (RelativeLayout) findViewById(R.id.rl_bind);
		tvLaputaBindedName = (FangTextView) findViewById(R.id.tv_bind_device_name);
		tvLaputaBindedInfo = (FangTextView) findViewById(R.id.tv_bind_connecting_info);
		updateLaputaBindInfo(getBlueService() != null && getBlueService().isConnect());
		
		// rlBindedInfo = (RelativeLayout) findViewById(R.id.rl_binded_info);
		// String bindedAddress = AddressSaved.getBindedAddress(getApplicationContext());
		// String bindedName = AddressSaved.getBindedName(getApplicationContext());
//		String bindedAddress = XBlueUtils.getBlueAddress(this);
//		String bindedName = XBlueUtils.getBlueName(this);
		// initBindedDeviceInfo(bindedAddress, bindedName);
		lvDeivce = (ListView) findViewById(R.id.lv_device);
		deviceList = new ArrayList<BluetoothDevice>();
		adapter = new DeviceAdapterNew(this, deviceList);
	
		lvDeivce.setAdapter(adapter);
		lvDeivce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
				// showProgressDialog = new
				// XplAlertDialog(DeviceAcitivy.this).builder2(getString(R.string.connect_success));
				// showProgressDialog.show();
				// showProgressDialog = showProgressDialog("正在连接设备...");
				// BluetoothDevice device = devices.get(i);
				// if (xBlueService!=null ) {
				// xBlueService.connect(device);
				// }
	
				// adapter.clear();
				mHandler.removeCallbacks(changeConnectingStateRunnable);
				
				mHandler.post(new Runnable() {
					@Override
					public void run() {
	
						if (getBlueService() != null) {
							getBlueService().stopScan();
							radarView.stop();
							isStopScan = true;
							getBlueService().closeAll();
	
							adapter.setConnectPos(i);
							adapter.setIsConecting(true);
	
							BluetoothDevice device = deviceList.get(i);
							BondedDeviceUtil.save(1, device.getAddress(), getApplicationContext());
							// setCurrentDevice();
							// initBindedDeviceInfo(device.getAddress(),
							// device.getName());
							// device.createBond();
							getBlueService().connect(device);
						}
					}
				});
				mHandler.postDelayed(changeConnectingStateRunnable, 10*1000);
			}
		});
	
		radarView = (RadarView) findViewById(R.id.scan_radar);
		radarView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
	
				if (getBlueService() != null && !getBlueService().isEnable()) {
					LaputaAlertDialog dialog = new LaputaAlertDialog(DeviceAcitivy.this).builder(getString(R.string.pls_enable_ble));
					dialog.show();
					return;
				}
				
				adapter.setConnectPos(-1);
				adapter.setIsConecting(false);
	
				boolean rotating = radarView.isRotating();
				if (rotating) {
					radarView.stop();
					isStopScan = true;
	
				} else {
					isStopScan = false;
					deviceList.clear();
					radarView.start();
					if (getBlueService() != null) {
						getBlueService().startScan();
					}
				}
			}
		});
	
		imgBack.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imgConnect.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "重练",
				// Toast.LENGTH_SHORT).show();
			}
		});
		
/*		rlBindedInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogForDisBindedDevice();
			}

		});*/
	}
	
	
	/**
	 * 初始化绑定设备信息
	 * @param bindedAddress
	 * @param bindedName
	 */
	private void initBindedDeviceInfo(String bindedAddress, String bindedName) {
		if (bindedAddress.equals("")) {
			llBindedInfo.setVisibility(View.GONE);
			tvBindedName.setText("");
			tvBindedAddress.setText("");
		} else {
			llBindedInfo.setVisibility(View.VISIBLE);
			if (bindedName == null || bindedName.equals("")) {
				tvBindedName.setText(R.string.nuknow);
			} else {
				tvBindedName.setText(bindedName);
			}
			tvBindedAddress.setText(bindedAddress);
			// if (!xplBluetoothService.isBluetoothConnected()) {
			if (getBlueService() != null && getBlueService().isConnect()) {
				tvBindedAddress.setTextColor(Color.WHITE);
				tvBindedName.setTextColor(Color.WHITE);
			} else {
				tvBindedAddress.setTextColor(Color.RED);
				tvBindedName.setTextColor(Color.RED);
			}
		}
	}
	
	/**
	 * 提示框：是否移除绑定设备
	 */
	private void showDialogForDisBindedDevice() {
		if (isDisBindedDialog == null) {
			isDisBindedDialog = new AlertDialog(this).builder().setMsg("删除绑定的手环？").setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {
	
				}
			}).setPositiveButton("确定", new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					String address = BondedDeviceUtil.get(1, getApplicationContext());
					BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
					try {
						if (remoteDevice != null) {
							BondedDeviceUtil.removeBond(remoteDevice);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					BondedDeviceUtil.save(1, "", getApplicationContext());
					// 2.关闭连接（当时连接状态时）
					// if (mSimpleBlueService.getConnectState() ==
					// BluetoothProfile.STATE_CONNECTED) {
					if (getBlueService() != null) {
						getBlueService().closeAll();
					}
					// initBindedDeviceInfo("", "");
				}
			});
		}
		isDisBindedDialog.show();
	
	}
	
	
	

}
