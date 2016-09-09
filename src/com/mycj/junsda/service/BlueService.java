package com.mycj.junsda.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.litepal.crud.DataSupport;

import com.laput.service.AbstractBluetoothStateBroadcastReceiver;
import com.laput.service.MusicLoader;
import com.laput.service.XBlueBroadcastUtils;
import com.laputa.blue.core.AbstractSimpleLaputaBlue;
import com.laputa.blue.core.Configration;
import com.laputa.blue.core.OnBlueChangedListener;
import com.laputa.blue.core.SimpleLaputaBlue;
import com.laputa.blue.util.BondedDeviceUtil;
import com.laputa.blue.util.DataUtil;
import com.laputa.blue.util.XLog;
import com.mycj.junsda.R;
import com.mycj.junsda.bean.CurrentSportInfo;
import com.mycj.junsda.bean.HistorySleep;
import com.mycj.junsda.bean.HistorySport;
import com.mycj.junsda.bean.HistorySportNew;
import com.mycj.junsda.bean.Music;
import com.mycj.junsda.bean.Notify;
import com.mycj.junsda.bean.ProtolNotify;
import com.mycj.junsda.bean.ProtolWrite;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.util.MessageUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;
import com.mycj.junsda.view.DateUtil;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

public class BlueService extends Service implements OnPreparedListener,OnCompletionListener{
	
	private AbstractBluetoothStateBroadcastReceiver mBluetoothStateBroadcastReceiver = new AbstractBluetoothStateBroadcastReceiver() {

		@Override
		public void onBluetoothChange(int state, int previousState) {
			if (state == BluetoothAdapter.STATE_ON) {
//				startScan();
			} else {

			}
		}
		
		
		
		@Override
		public void onBluetoothDisconnect(final BluetoothDevice device) {
			/*// xplBluetoothService.close(AdressSaved.getBindedAddress(getApplicationContext()));
			Log.e("", "&^^^^^^^^^^^^^^^^^^^^^^^短线" +device.getAddress());
//			playResource(R.raw.crystal);
			stop();
			xHandler.post(new Runnable() {
				@Override
				public void run() {
					String address = device.getAddress();
					XBlueConnectResult result = xplBlueManager.getXBlueConnectResultByAddress(address);
					if (result != null) {
						result.close();
						xplBlueManager.getConnectResultsMap().remove(address);
						XBlueBroadcastUtils.instance().sendBroadcastConnectState(getApplicationContext(),device, BluetoothGatt.STATE_DISCONNECTED);
					}
					if (!isAllConnected()) {
						startScan();
//						xplBlueManager.startAutoConnecting();//
					}
					
					//
//					mHandler.removeCallbacks(ringRun);
//					mHandler.postDelayed(ringRun, 12 * 1000);
				
			});}*/
		}



		@Override
		public void onBondStateChanged(BluetoothDevice device, int state) {
		}

	};
	private Handler mHandler = new Handler(){};
	private Runnable taskIncoming;
	private int phoneNo;
	private int smsMNo;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		XLog.e(BlueService.class, "-------- onStartCommand() ---------");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				musicList = initMusic();
			}
		}, 3000);
		Configration configration = new Configration();
		simpleLaputaBlue = new SimpleLaputaBlue(getApplicationContext(), configration, new OnBlueChangedListener() {
			@Override
			public void reconnect(HashSet<String> devices) {
				final String addressA = BondedDeviceUtil.get(1,
						getApplicationContext());
				if (BluetoothAdapter.checkBluetoothAddress(addressA)) {
					try {
						// 当前app存贮的蓝牙
						BluetoothDevice remoteDevice = simpleLaputaBlue
								.getAdapter().getRemoteDevice(addressA);
						// 所有的绑定蓝牙列表
						Set<BluetoothDevice> bondedDevices = simpleLaputaBlue
								.getAdapter().getBondedDevices();
						//
						if (bondedDevices.contains(remoteDevice)) {
							XLog.e("_____已绑定 ：" + addressA);
							if (!isConnect()) {
								connect(addressA);
								return;
							}
						} else {
							XLog.e("_____未绑定 ：" + addressA);
							// 当搜索列表中包含保存的addressA,并且未连接，就连接。
							if (devices.contains(addressA)) {
								if (!isConnect()) {
									connect(addressA);
								}
							} else {
								XLog.i("搜索列表无：" + addressA);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						XLog.e("重新连接失败！");
					}

				} else {
					XLog.i("蓝牙地址不匹配，没有addessA" + addressA);
				}
			}
			
			@Override
			public void onStateChanged(String address, int state) {
				
			}
			
			@Override
			public void onServiceDiscovered(String address) {
				doUpdateSetting();
			}
			
			@Override
			public void onCharacteristicChanged(String address, byte[] value) {
				parseData( value);
			}
			
			@Override
			public boolean isAllConnected() {
				String address = BondedDeviceUtil.get(1, getApplicationContext());
				if (address !=null && !address.equals("")) {
					return isConnect();
				}
				return true;
				
			}
		});
	
		/*xplBlueManager = XBlueManager.instance(getApplicationContext());
		xplBlueManager.setXBlueManagerListener(new XBlueManagerListener() {
			@Override
			public void doDeviceFound(HashMap<String, BluetoothDevice> scanDeviceMap) {
				
			}

			@Override
			public void doPlayMusic(int order) {
				playMusic(order);
			}

			@Override
			public void doFindPhone(int order) {
				if (order==1) {
					playResource(R.raw.crystal);
				}else{
					stop();
				}
			}

			@Override
			public void doDisconnected(BluetoothDevice device) {
				stop();
			}
		});*/
		
		mBluetoothStateBroadcastReceiver.registerBoradcastReceiverForCheckBlueToothState(getApplicationContext());
		
		/**********************************
		 *             电话和短信                                     *                                    
		 **********************************/
		//可以先在同步里设置好。
		
		taskIncoming = new Runnable() {
			@Override
			public void run() {
				int mmsCount = MessageUtil.getNewMmsCount(getApplicationContext());
				int msmCount = MessageUtil.getNewSmsCount(getApplicationContext());
				int phoneCount = MessageUtil.readMissCall(getApplicationContext());
//				Log.e("BaseApp", "____电话数量 ： " + phoneNo + "-->" + phoneCount);
//				Log.e("BaseApp", "____短信 ： " + smsMNo + "-->" + (msmCount + mmsCount) + (smsMNo != (mmsCount + msmCount)));
				// 数量只要有一个变化就发送
//				boolean isCallRemind;
//				isCallRemind = (boolean) SharedPreferenceUtil.get(getApplicationContext(), StaticValue.SHARE_REMIND_INCOMING, false);
				if (phoneNo != phoneCount || smsMNo != (mmsCount + msmCount)) {
					Log.e("xpl", "_______________________________________________________________________________________读取短信和电话数量 ： 有变化");
					// if (mmsCount == 0 && msmCount == 0 && phoneCount == 0) {
					// doWriteUnReadPhoneAndSmsToWatch(0, 0);
					// return;
					// } else {
					write(ProtolWrite.instance().writeForPhoneAndSmsCount(phoneCount, mmsCount + msmCount));
					// }
					//修改与10.28
					phoneNo = phoneCount;
					smsMNo = (mmsCount + msmCount);
				} else {
					Log.e("BaseApp", "__读取短信和电话数量 ： 无变化");
				}
				mHandler.postDelayed(taskIncoming, 8000);
			}
		};
		mHandler.postDelayed(taskIncoming, 8 * 1000);
	}
	
	public void startScan(){
		simpleLaputaBlue.scanDevice(true);
	}
	public void stopScan(){
		simpleLaputaBlue.scanDevice(false);
	}
	
	private String getBondedAddress(){
		return BondedDeviceUtil.get(1, getApplicationContext());
		
	}
	
	public void write(byte [] data){
		
		simpleLaputaBlue.write(getBondedAddress(), data);
	}
	public void write(byte [][] datas){
		
//		new WriteSyncTask().execute(datas);
		simpleLaputaBlue.write(getBondedAddress(), datas);
	}
	
	public boolean isConnect() {
		return simpleLaputaBlue != null
				&& simpleLaputaBlue.isConnected(BondedDeviceUtil.get(1, this));
	}
	
	public void connect(String address) {
		simpleLaputaBlue.connect(address);
	}
	public void connect(BluetoothDevice device){
		simpleLaputaBlue.connect(device);
	}
	
	
	public void closeAll(){
		simpleLaputaBlue.closeAll();
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return new XBlueBinder();
	}
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	public class XBlueBinder extends Binder  {
		public BlueService getXBlueService(){
			return BlueService.this;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		closeAll();
		
		unregisterReceiver(mBluetoothStateBroadcastReceiver);
	}
	
	
	private int[] ids;// 保存音乐ID临时数组
	private String[] artists;
	private String[] titles;
	private Cursor cursor;
	private List<Music> musicList;
	private MediaPlayer mediaPlayer;
	private int playingPosition=0;
	/**
	 * 定义查找音乐信息数组，1.标题 2.音乐时间 3.艺术家 4.音乐id 5.显示名字 6.数据
	 */
	private String[] musicInfo = new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID

	};
	private AbstractSimpleLaputaBlue simpleLaputaBlue;
	/**
	 * 音乐相关
	 * @return
	 */
	public List<Music> initMusic() {
		MusicLoader.instance(getContentResolver());
		cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicInfo, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Music> listMusic;
		if (cursor != null) {
			cursor.moveToFirst();
			int size = cursor.getCount();
			ids = new int[size];
			artists = new String[size];
			titles = new String[size];
			for (int i = 0; i < size; i++) {
				ids[i] = cursor.getInt(3);
				artists[i] = cursor.getString(2);
				titles[i] = cursor.getString(0);
				cursor.moveToNext();
			}
			listMusic = MusicLoader.getMusicList();
			Log.d("TAG", "所有的音乐list : " + listMusic);
			return listMusic;
		}
		return null;
	}
	
	/**
	 * 停止播放
	 */
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}
	

	
	/**
	 * 释放
	 */
	public void release() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	public void reset() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		}
	}
	
	public void playResource(int resId){
		release();
		try {
			mediaPlayer = MediaPlayer.create(this, resId);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} 
//		mediaPlayer.setOnPreparedListener(this);
//		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.start();
	}
	
	
	public void play(Uri uri) {
		release();
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(this, uri);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.prepareAsync();
//			mediaPlayer.start();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public void playMusic(int order) {
		if (order ==1) {
			if (musicList!=null) {
				Log.e("service", "playingPosition :" + playingPosition);
				Uri musicUriById = MusicLoader.getMusicUriById(musicList.get(playingPosition).getId());
				play(musicUriById);
			}
		}else{
			stop();
		}
		
	}
	
	public boolean isEnable(){
//		return xplBlueManager.isBluetoothAdapterEnable();
		return simpleLaputaBlue.isEnable();
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		playDown();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mediaPlayer.start();
	}
	
	/**
	 * 上一曲
	 */
	public void playUp() {
		if (musicList != null && musicList.size() > 0) {
			playingPosition--;
			if (playingPosition < 0) {
				playingPosition = musicList.size() - 1;
			}
			play(MusicLoader.getMusicUriById(musicList.get(playingPosition).getId()));
		}
	};

	/**
	 * 下一曲
	 */
	public void playDown() {
		if (musicList != null && musicList.size() > 0) {
			playingPosition++;
			if (playingPosition == musicList.size()) {
				playingPosition = 0;
			}
			play(MusicLoader.getMusicUriById(musicList.get(playingPosition).getId()));
		}
	};
	
	
	/**
	 * 当发现service时，同步手机设置
	 */
	private void doUpdateSetting() {
		ProtolWrite pw = ProtolWrite.instance();
		// 0.同步时间
		byte[] hexDataForTimeSync = pw.hexDataForTimeSync(new Date(), this);
		// 1.睡眠设置
		byte[] sleepSetting = getSleepSetting(pw);
		// 2.来电提醒
		boolean iscoming = (boolean) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_INCOMING, false);
		byte[] inComing = pw.writeIncoming(iscoming?1:0);
		// 3.每日闹铃
		byte[] alarmSetting = getAlarmSetting(pw);
		// 4.久坐提醒
		boolean  isLongSit = (boolean) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_LONG_SIT, false);
//		byte[] longSitSetting = pw.writeLongSit(isLongSit?1:0);
		// 5.生日提醒
		byte [] birthSetting = getBirthdaySetting(pw);
		// 6.整点报时
		
		boolean isPointTime = (Boolean) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_POINT_TIME, false);
		byte[] longPointTime = pw.writeLongPointTime(isPointTime?1:0);
		// 7.未接短信和电话数量
		int mmsCount = MessageUtil.getNewMmsCount(getApplicationContext());
		int msmCount = MessageUtil.getNewSmsCount(getApplicationContext());
		int phoneCount = MessageUtil.readMissCall(getApplicationContext());
		byte[] writeForPhoneAndSmsCount = ProtolWrite.instance().writeForPhoneAndSmsCount(phoneCount, mmsCount + msmCount);
		XLog.e(BlueService.class,"doUpdateSetting() --开始"); 
		
		
/*		
		byte[][] datas = new byte[][]{
//				DataUtil.hexStringToByte("FB"),
				hexDataForTimeSync,
				sleepSetting,
				inComing,
				alarmSetting,
				longSitSetting,
				birthSetting,
				longPointTime,
				writeForPhoneAndSmsCount};*/
		
		
			//new 同步设置到手环
	    byte[] writeSetting = pw.writeSetting(this);
		byte[][] datas = new byte[][]{hexDataForTimeSync,writeSetting};
	
		
		
		write(datas);
	}
	
	/**
	 * 
	 * 异步任务
	 * 写入Ble蓝牙 没隔500毫秒
	 *
	 */
	private class WriteSyncTask extends AsyncTask<byte[][], Void, Void> {
		@Override
		protected Void doInBackground(byte[][]... params) {
			byte[][] bs = params[0];
			if (bs != null && bs.length >0) {
				for (int i = 0; i < bs.length; i++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					write(bs[i]);
				}
			}
			
			return null;
		}
	}
	
	private byte[] getSleepSetting(ProtolWrite pw){
		boolean isSleepSettingOnoff = (boolean) SharedPreferenceUtil.get(this, StaticValue.SHARE_SLEEP_ON_OFF, false);
		String sleepTime = (String) SharedPreferenceUtil.get(this, StaticValue.SHARE_SLEEP_TIME,  StaticValue.DEFAULT_SLEEP_TIME);
		String awakTime = (String) SharedPreferenceUtil.get(this, StaticValue.SHARE_AWAK_TIME,  StaticValue.DEFAULT_AWAK_TIME);
		String[] sleeps = sleepTime.split(":");
		int sleepHour = Integer.valueOf(sleeps[0]);
		int sleepMin = Integer.valueOf(sleeps[1]);
		String[] awaks = awakTime.split(":");
		int awakHour = Integer.valueOf(awaks[0]);
		int awakMin = Integer.valueOf(awaks[1]);
		byte[] writeSleepSetting = null ;
		try {
			writeSleepSetting = pw.writeSleepSetting(isSleepSettingOnoff?1:0, sleepHour, sleepMin, awakHour, awakMin);
			return writeSleepSetting;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writeSleepSetting;
	}
	
	private byte[] getAlarmSetting(ProtolWrite pw){
		boolean isAlarm = (Boolean) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_ALARM, false);
		String alarmTime = (String) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_ALARM_TIME, StaticValue.DEFAULT_ALARM);
		String[] alarms = alarmTime.split(":");
		int alarmHour = Integer.valueOf(alarms[0]);
		int alarmMin = Integer.valueOf(alarms[1]);
		return pw.writeAlarm(isAlarm?1:0, alarmHour, alarmMin);
	}
	
	private byte[] getBirthdaySetting(ProtolWrite pw) {
		boolean isBirthday = (Boolean) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_BIRTHDAY, false);
		String birthdayTime = (String) SharedPreferenceUtil.get(this, StaticValue.SHARE_REMIND_BIRTHDAY_TIME, StaticValue.DEFAULT_BIRTHDAY_TIME);
		String birthdayDate = (String) SharedPreferenceUtil.get(this, StaticValue.SHARE_BIRTHDAY,  StaticValue.DEFAULT_BIRTHDAY);
		String[] times = birthdayTime.split(":");
		String[] dates = birthdayDate.split("-");
		int month = Integer.valueOf(dates[1]);
		int day = Integer.valueOf(dates[2]);
		int hour = Integer.valueOf(times[0]);
		int min = Integer.valueOf(times[1]);
		byte[] writeBirthday = pw.writeBirthday(isBirthday?1:0, month, day, hour, min);
		return writeBirthday;
	}
	
	
	/**
	 * <p>
	 * 解析数据
	 * </p>
	 * 
	 * @param data
	 * @param gatt
	 */
	private void parseData( byte[] data) {
		int result = 0;
		ProtolNotify notify = ProtolNotify.instance();
		XBlueBroadcastUtils broad = XBlueBroadcastUtils.instance();
		result = notify.getResult(data);
//		Log.i("Service", "type : " + result);
			switch (result) {
			
			case 0xc:
				// 当前运动数据	（显示 当前运动信息）
				CurrentSportInfo notifyCurrentSport = notify.notifyCurrentSport(data);
				Log.i("Service", "获取当前运动数据：" +notifyCurrentSport);
				if (notifyCurrentSport != null) {
					broad.sendbroadcastForCurrentSportInfo(this, notifyCurrentSport);
				}
				break;
			case 1:
				// 历史运动数据（同步保存）
				/*HistorySport historySport = notify.notifyHistorySport(data);
				Log.i("Service", "获取历史运动数据：" + historySport);
				if (historySport!=null) {
					//save
					saveHistorySport(historySport);
					broad.sendbroadcastForHistorySport(this, historySport);
				}	*/
				
				/**************************
				   	运动新数据 ------带距离和卡路里        
				 **************************/
			
				 
				HistorySportNew notifyHistorySportNew = notify.notifyHistorySportNew(data);
//				Log.i("BlueService", "获取历史运动数据：" + notifyHistorySportNew);
				if (notifyHistorySportNew != null) {
					saveHistorySportNew(notifyHistorySportNew);
//					broad.sendbroadcastForHistorySportNew(this,notifyHistorySportNew);
				}
				break;
			case 2:
				// 开始/结束更新历史数据
				int notifySyncSportState = notify
						.notifySyncSportState(data);
				if (notifySyncSportState == 0) {
					Log.i("Service", "开始获取运动数据");
				} else {
					Log.i("Service", "获取运动数据结束");
				}
				broad.sendbroadcastForSyncSport(this,notifySyncSportState);
				break;
			case 3:
				// 更新睡眠数据
				Log.i("sleep", "获取睡眠数据" );
				HistorySleep notifyHistorySleep = notify
						.notifyHistorySleep(data);
				if (notifyHistorySleep != null) {
					Log.i("sleep", "获取睡眠数据" + notifyHistorySleep.toString());
					saveHistorySpleep(notifyHistorySleep); 
//					broad.sendbroadcastForHistorySleep(this,notifyHistorySleep);
				}else{
					Log.i("sleep", "获取睡眠数据 :  为空"  );
				}
				
				break;
			case 4:
				// 开始/结束更新睡眠数据
				int notifySyncSleepState = notify
						.notifySyncSleepState(data);
				if (notifySyncSleepState == 0) {
					Log.i("Service", "开始获取睡眠数据");
				} else {
					Log.i("Service", "获取睡眠数据结束");
				}
				broad.sendbroadcastForSyncSleep(this,notifySyncSleepState);
				break;
			case 5:
				// 心率数据
				int notifyHeartRate = notify.notifyHeartRate(data);
				Log.i("Service", "获取心率数据：" + notifyHeartRate);
				if (notifyHeartRate!=-1) {
					broad.sendbroadcastForHeadtRate(this,notifyHeartRate);
				}
				break;
			case 6:
				// 查找手机
				int notifyFindPhone = notify.notifyFindPhone(data);
	//					int notifyFindPhone = 1;
				Log.i("Service", "寻找手机！" +notifyFindPhone);
				if (notifyFindPhone!=-1) {
					broad.sendbroadcastForFindPhone(this,notifyFindPhone);
					if (notifyFindPhone==1) {
						playResource(R.raw.crystal);
					}else{
						stop();
					}
				}
				break;
			case 7:
				// 照相命令
				Log.i("Service", "照相！" );
				int notifyTakePicture = notify.notifyTakePicture(data);
				if (notifyTakePicture!=-1) {
					broad.sendbroadcastForTakePicture(this);
				}
				break;
			case 8:
				// 播放音乐
				int orderMusic = notify.notifyMusic(data);
				Log.i("Service", "音乐！"  + orderMusic);
				if (orderMusic!=-1) {
					playMusic(orderMusic);
				}
				break;
			
			default:
				break;
			}
	}
	
	
	private void saveHistorySportNew(HistorySportNew notifyHistorySportNew) {
		new SaveSportHistoryNewAsyncTask().execute(notifyHistorySportNew);
	}

	private void saveHistorySport(HistorySport historySport) {
		new SaveSportHistoryAsyncTask().execute(historySport);
	}
	
	private void saveHistorySpleep(HistorySleep notifyHistorySleep) {
		new SaveSleepHistoryAsyncTask().execute(notifyHistorySleep);
	}
	
	private class SaveSportHistoryAsyncTask extends AsyncTask<HistorySport, Void, Void>{
		
		@Override
		protected Void doInBackground(HistorySport... params) {
			HistorySport sport = params[0];
			Date date = new Date();
			String dateStr = DateUtil.dateToString(date, "yyyyMMdd");
			if (sport!=null) {
				List<HistorySport> sportAtDate = DataSupport.where("date =?",dateStr).find(HistorySport.class);
				if (sportAtDate!=null && sportAtDate.size()>0) {	//说明存在记录 ， 更新值就行了
					Log.e("xpl", dateStr + "已存记录");
					ContentValues values = new ContentValues();
					values.put("sportTime", sport.getSportTime());
					values.put("step", sport.getStep());
					DataSupport.updateAll(HistorySport.class, values,"date =?",dateStr);
				}else{
					sport.save(); //不存在直接保存记录
				}
				
			}
			return null;
		}
	} 
	
	private class SaveSportHistoryNewAsyncTask extends AsyncTask<HistorySportNew, Void, Void>{
		
		@Override
		protected Void doInBackground(HistorySportNew... params) {
			HistorySportNew sport = params[0];
			
			Date date = new Date();
//			String dateStr = DateUtil.dateToString(date, "yyyyMMdd");
			String dateStr = sport.getSportDate();
			if (sport!=null &&sport.getSportStep() >0 ) {
				List<HistorySportNew> sportAtDate = DataSupport.where("sportDate =?",dateStr).find(HistorySportNew.class);
				if (sportAtDate!=null && sportAtDate.size()>0) {	//说明存在记录 ， 更新值就行了
					Log.e("BlueService", dateStr + "------------〉 已存记录");
					ContentValues values = new ContentValues();
					values.put("sportTime", sport.getSportTime());
					values.put("sportStep", sport.getSportStep());
					values.put("distance", sport.getDistance());
					values.put("calorie", sport.getCalorie());
					DataSupport.updateAll(HistorySportNew.class, values,"sportDate =?",dateStr);
				}else{
					Log.i("BlueService", " ------------〉保存运动历史记录" + sport);
					sport.save(); //不存在直接保存记录
				}
				
			}
			return null;
		}
	} 
	
	
    private class SaveSleepHistoryAsyncTask extends AsyncTask<HistorySleep, Void, Void>{
		
		@Override
		protected Void doInBackground(HistorySleep... params) {
			HistorySleep sleep = params[0];
			Date date = new Date();
			Log.e("xpl", "保存睡眠记录--------------------");
//			String dateStr = DateUtil.dateToString(date, "yyyyMMdd");
			String dateStr = sleep.getDate();
			if (sleep!=null && !(sleep.getDeep()==0 && sleep.getLight()==0 )) {
				List<HistorySleep> sleepAtDate = DataSupport.where("date =?",dateStr).find(HistorySleep.class);
				if (sleepAtDate!=null && sleepAtDate.size()>0) {	//说明存在记录 ， 更新值就行了
					Log.e("xpl", dateStr + "已存记录");
					ContentValues values = new ContentValues();
					values.put("deep", sleep.getDeep());
					values.put("light", sleep.getLight());
					values.put("startTime", sleep.getStartTime());
					values.put("endTime", sleep.getEndTime());
					DataSupport.updateAll(HistorySleep.class, values,"date =?",dateStr);
				}else{
					sleep.save(); //不存在直接保存记录
				}
				
			}
			return null;
		}
	}
    
    public AbstractSimpleLaputaBlue getSimpleLaputaBlue (){
    	return this.simpleLaputaBlue;
    }
}
