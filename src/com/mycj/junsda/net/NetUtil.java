package com.mycj.junsda.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/** 网络连接管理器 */
	private ConnectivityManager mConnectivityManager = null;
	public static NetUtil netUtil = null;

	public static NetUtil getNetutilInstance(Context context) {
		if (netUtil == null) {
			netUtil = new NetUtil(context);
		}
		return netUtil;
	}

	public NetUtil(Context context) {
		this.mConnectivityManager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetActive() {
		return isWiFiActive() || isMobileActive();
	}

	/**
	 * 判断WIFI环境
	 * 
	 * @return boolean true/false
	 */
	public boolean isWiFiActive() {
		return isNetworkConnection("WIFI");
	}

	/**
	 * 判断手机网络环境
	 * 
	 * @return boolean true/false
	 */
	public boolean isMobileActive() {
		return isNetworkConnection("MOBILE");
	}

	private boolean isNetworkConnection(String connType) {
		if (mConnectivityManager != null) {
			NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equalsIgnoreCase(connType)
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetAvailable(Context context) {
		if (context != null) {
			// 获得网络状态管理器
			// 如果等于null则标识没有可联网设备
			if (mConnectivityManager != null) {
				// 获取所有的网络信息
				NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
				if (info != null) {
					for (NetworkInfo network : info) {
						// 判断是否为连接状态
						if (network.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
