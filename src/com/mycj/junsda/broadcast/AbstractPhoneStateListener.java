package com.mycj.junsda.broadcast;

import java.util.HashMap;



import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public abstract class AbstractPhoneStateListener extends PhoneStateListener {

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:// 空闲
			Log.i("AbstractPhoneStateListener", "挂了" +incomingNumber);
			break;
		case TelephonyManager.CALL_STATE_RINGING:// 来电
			Log.i("AbstractPhoneStateListener", "来电话了" +incomingNumber);
			onIncoming(state, incomingNumber);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机（正在通话中）
			Log.i("AbstractPhoneStateListener", "正在通话中" +incomingNumber);
			break;

		default:
			break;
		}

	}

	public abstract void onIncoming(int state, String incomingNumber);

	/**
	 * 监听来电变化情况
	 */
	public void registerListenerForPhoneState(TelephonyManager telephonyManager) {
		telephonyManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
	}


}
