package com.mycj.junsda.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.mycj.junsda.bean.ProtolWrite;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.broadcast.I2WPhoneStateListener;
import com.mycj.junsda.broadcast.I2WPhoneStateListener.OnIncomingListener;
import com.mycj.junsda.service.BlueService;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;

public class BaseApp extends Application implements OnIncomingListener {

	// public JunsdaXplBluetoothService getXplBluetoothService(){
	// Log.e("", "==getXplBluetoothService() :" + xplBluetoothService);
	// return this.xplBluetoothService;
	// }
	// private JunsdaXplBluetoothService xplBluetoothService;
	// private ServiceConnection mXplServiceConnect = new ServiceConnection() {
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// xplBluetoothService=null;
	// }
	//
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// JunsdaXplBluetoothService.XplBinder xplBinder =
	// (JunsdaXplBluetoothService.XplBinder) service;
	// xplBluetoothService = (JunsdaXplBluetoothService)
	// xplBinder.getXplBluetoothService();
	// Log.e("", "==xplBluetoothService :" + xplBluetoothService);
	// if (xplBluetoothService.isBluetoothEnable()) {
	// // xplBluetoothService.scanDevice(true);
	// }else{
	// Toast.makeText(getApplicationContext(), "请开启蓝牙...",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// };
	private BlueService blueService;

	public BlueService getBlueService() {
		Log.e("", "Baseapp xBlueService:" + blueService);
		return this.blueService;
	}

	private ServiceConnection xBlueConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			blueService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			BlueService.XBlueBinder binder = (BlueService.XBlueBinder) service;
			blueService = binder.getXBlueService();
			Log.e("", "Baseapp blueService:" + blueService);
		}
	};

	private TelephonyManager telephonyManager;
	public static Typeface TYPEFACE_JIAN;
	public static Typeface TYPEFACE_NUM;
	public static Typeface TYPEFACE_FAN;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onCreate() {
		super.onCreate();
		Intent xplIntent = new Intent(this, BlueService.class);
		// bindService(xplIntent, mXplServiceConnect, Context.BIND_AUTO_CREATE);
		bindService(xplIntent, xBlueConnection, Context.BIND_AUTO_CREATE);
		// 系统字体
		TYPEFACE_JIAN = Typeface.createFromAsset(getAssets(),
				CustomTypeface.JIAN.getPath());
		TYPEFACE_NUM = Typeface.createFromAsset(getAssets(),
				CustomTypeface.NUM.getPath());
		TYPEFACE_FAN = Typeface.createFromAsset(getAssets(),
				CustomTypeface.FAN.getPath());

		// 电话
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		I2WPhoneStateListener i2wPhoneStateListener = new I2WPhoneStateListener();
		i2wPhoneStateListener.setOnIncomingListener(this);
		i2wPhoneStateListener.registerListenerForPhoneState(telephonyManager);
		// 短信
		IntentFilter filter = new IntentFilter();
		filter.addAction(SMS_RECEIVED_ACTION);
		registerReceiver(mPhoneReceiver, filter);
	}

	@Override
	public void onTerminate() {
		// xplBluetoothService.close();
		blueService.closeAll();
		unbindService(xBlueConnection);
		super.onTerminate();
	}

	@Override
	public void onIncoming(int state, String incomingNumber) {
		if (blueService != null && blueService.isConnect()) {
			boolean isIncoming = (Boolean) SharedPreferenceUtil.get(this,
					StaticValue.SHARE_REMIND_INCOMING, false);
			if (isIncoming) {
				byte[] writeForIncomingNumber = ProtolWrite.instance()
						.writeForIncomingNumber(incomingNumber, isIncoming);
				Log.i("BaseApp",
						"来电话了 "
								+ DataUtil
										.byteToHexString(writeForIncomingNumber));
				blueService.write(writeForIncomingNumber);
			}
		}
	}

	/**
	 * 新来短信 监听
	 */
	private BroadcastReceiver mPhoneReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
				// SmsMessage[] messages = getMessagesFromIntent(intent);
				/*
				 * if (null != mSimpleBlueService &&
				 * mSimpleBlueService.isBinded() &&
				 * mSimpleBlueService.getConnectState() ==
				 * BluetoothProfile.STATE_CONNECTED &&
				 * mSimpleBlueService.isWrieteServiceFound()) {
				 * mSimpleBlueService
				 * .writeCharacteristic(DataUtil.getBytesByString("83")); }
				 */
				// if (xplBluetoothService!=null &&
				// xplBluetoothService.isBluetoothConnected()) {
				// xplBluetoothService.writeCharacteristic(DataUtil.getBytesByString("83"));
				// }
				/*
				 * if (xBlueService!=null && xBlueService.isAllConnected()) {
				 * xBlueService.write(DataUtil.getBytesByString("83")); }
				 */
				// for (SmsMessage message : messages) {
				// Log.e("", "来信的信息了！！！！！！");
				// Log.i("", message.getOriginatingAddress() + " : " +
				// message.getDisplayOriginatingAddress() + " : " +
				// message.getDisplayMessageBody() + " : " +
				// message.getTimestampMillis());
				// else {
				// }
				// }

			}
		}

	};

	public final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];
		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}

		return msgs;
	}
}
