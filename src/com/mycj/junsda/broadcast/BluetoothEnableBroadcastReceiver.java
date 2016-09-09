package com.mycj.junsda.broadcast;

import com.laput.service.XBlueBroadcastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class BluetoothEnableBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
	/*	if (intent.getAction().equals(XBlueBroadcastUtils.ACTION_BLUETOOTH_ADAPTER_DISABLE)) {
			doBluetoothEnable();
		}*/
	}
	public abstract void doBluetoothEnable() ;
}
