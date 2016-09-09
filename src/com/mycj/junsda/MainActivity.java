package com.mycj.junsda;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.laput.dialog.BaseDialog;
import com.laput.dialog.SimpleAlertDialog;
import com.laput.service.XBlueBroadcastReceiver;
import com.laput.service.XBlueBroadcastUtils;
import com.laputa.blue.broadcast.LaputaBroadcast;
import com.laputa.blue.core.AbstractSimpleLaputaBlue;
import com.laputa.blue.util.BondedDeviceUtil;
import com.laputa.blue.util.XLog;
import com.mycj.junsda.activity.CameraActivity;
import com.mycj.junsda.activity.DeviceAcitivy;
import com.mycj.junsda.activity.HistorySleepActivity;
import com.mycj.junsda.activity.HistorySportActivity;
import com.mycj.junsda.activity.SettingPersonalActivity;
import com.mycj.junsda.activity.SettingRemindActivity;
import com.mycj.junsda.base.BaseActivity;
import com.mycj.junsda.base.BaseApp;
import com.mycj.junsda.bean.CurrentSportInfo;
import com.mycj.junsda.bean.HistorySleep;
import com.mycj.junsda.bean.HistorySport;
import com.mycj.junsda.bean.HistorySportNew;
import com.mycj.junsda.bean.LitePalManager;
import com.mycj.junsda.bean.ProtolWrite;
import com.mycj.junsda.bean.StaticValue;
import com.mycj.junsda.fragment.HomeFragment;
import com.mycj.junsda.fragment.MeFragment;
import com.mycj.junsda.fragment.SettingFragment;
import com.mycj.junsda.net.NetUtil;
import com.mycj.junsda.net.UpdateHelper;
import com.mycj.junsda.net.ToastHelper;
import com.mycj.junsda.service.BlueService;
import com.mycj.junsda.util.PMUtils;
import com.mycj.junsda.util.ShareUtil;
import com.mycj.junsda.util.SharedPreferenceUtil;
import com.mycj.junsda.view.ActionSheetDialog;
import com.mycj.junsda.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mycj.junsda.view.ActionSheetDialog.SheetItemColor;
import com.mycj.junsda.view.AbstraceDialog;
import com.mycj.junsda.view.AlertDialog;
import com.mycj.junsda.view.DateUtil;
import com.mycj.junsda.view.FangRadioButton;
import com.mycj.junsda.view.LaputaAlertDialog;
import com.mycj.junsda.view.LaputaLoadingAlertDialog;

/**
 * 
 * Created by zeej on 2015/11/19.
 * 
 */
public class MainActivity extends BaseActivity {
	private AbstraceDialog dialogSex ;
	private RadioGroup rgTab;
	private FangRadioButton rbHome;
	private FangRadioButton rbMe;
	private FangRadioButton rbSetting;
	private List<Fragment> fragments;
	private HomeFragment homeFragment;
	private MeFragment meFragment;
	private SettingFragment settingFragment;
//	private BlueService xBlueService;
	private FragmentManager fragmentManager;
	private final int MAX_STEP = 5000;
	private Toast mToast;
	
	private Handler mHandler = new Handler() {
	

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case StaticValue.MSG_SHARE:
				String path = (String) msg.obj;
				ShareUtil.shareImage(path, MainActivity.this,
						getString(R.string.share));
				
				
//			    ActivityManager manager = this.getSystemService(Context.ACTIVITY_SERVICE);  
//			    manager.restartPackage("com.mycj.aust");  

				break;
			case NEED_UPDATE:
				UpdateHelper.hideLoaddingWindow();
				
				 dialogSex = new AbstraceDialog(MainActivity.this).builder().setTitle("是否更新最新程序?").setNegativeButton("现在更新", new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (downloadUrl!=null) {
							Intent intent = new Intent(
									Intent.ACTION_VIEW, Uri
											.parse(downloadUrl));
							startActivity(intent);
							UpdateHelper.cancelUpdateDialog();
							}
					}
				}).setPositiveButton(("下次更新"),new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(dialogSex!=null){
						dialogSex.dismiss();}
					}
				}).setLeftVisiable(false).setRightVisiable(false);
				dialogSex.show();
				
			/*	// 有更新
				UpdateHelper.pop2ButtonDialog(MainActivity.this);
				if (UpdateHelper.getUpdateButton() != null) {
					UpdateHelper.getUpdateButton().setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (downloadUrl!=null) {
									Intent intent = new Intent(
											Intent.ACTION_VIEW, Uri
													.parse(downloadUrl));
									startActivity(intent);
									UpdateHelper.cancelUpdateDialog();
									}
								}
							});
				}*/
				break;
			case NET_NOT_AVAILABLE:
				UpdateHelper.hideLoaddingWindow();
				if (mToast != null) {
					mToast.cancel();
				}
				mToast = ToastHelper.makeNormalToast(MainActivity.this,
						R.string.net_not_available);
//				mToast.show();
				break;
			case SERVER_ERROR:
				UpdateHelper.hideLoaddingWindow();
				if (mToast != null) {
					mToast.cancel();
				}
				mToast =  ToastHelper.makeNormalToast(MainActivity.this,
						R.string.server_error);
//				mToast.show();
				break;

			default:
				break;
			}

		}

		private ActivityManager getSystemService(String activityService) {
			// TODO Auto-generated method stub
			return null;
		};
	};

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			final int state = intent.getExtras().getInt(
					LaputaBroadcast.EXTRA_LAPUTA_STATE);
			if (action.equals(LaputaBroadcast.ACTION_LAPUTA_STATE)) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (settingFragment != null && settingFragment.isAdded()) {
							if (state == AbstractSimpleLaputaBlue.STATE_SERVICE_DISCOVERED) {
								settingFragment.updateSyncText(true);
							} else {
								settingFragment.updateSyncText(false);
							}
						}
					}
				});

			}
		}
	};
	
	

	private XBlueBroadcastReceiver xReceiver = new XBlueBroadcastReceiver() {
		@Override
		public void doSportSyncStateChanged(final int state) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (state == 0) {
						// toast("开始同步");
					} else {
						// toast("结束同步");
					}
				}
			});

		}

		@Override
		public void doSportChanged(final HistorySport sport) {
			/*
			 * mHandler.post(new Runnable() {
			 * 
			 * @Override public void run() { Log.e("", "______________sport : "
			 * + sport); if (sport != null) { int sportTime =
			 * sport.getSportTime(); int step = sport.getStep(); if
			 * (homeFragment != null) { homeFragment.freshCircleSport(MAX_STEP,
			 * step); homeFragment.freshSportInfo(step, sportTime); } } } });
			 */
		}

		@Override
		public void doCurrentSportInfoChanged(final CurrentSportInfo info) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Log.e("", "______________sport : " + info);
					if (homeFragment != null) {
						homeFragment.freshCircleSport(info);
					}
				}
			});
		}

		@Override
		public void doSleepSyncStateChanged(int state) {

		}

		@Override
		public void doSleepChanged(HistorySleep sleep) {

		}

		@Override
		public void doHeartRateChanged(final int hr) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (homeFragment != null) {
						homeFragment.freshHeartRateInfo(hr);
					}
				}
			});
		}

	};
	private AlertDialog isDisBindedDialog;
	private NetUtil netutil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		xBlueService = getBlueService();
		fragmentManager = getSupportFragmentManager();
		initFragments();
		initViews();
		setListener();
		checkVersion();
		rgTab.check(R.id.rb_tab_home);
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(xReceiver, XBlueBroadcastUtils.instance()
				.getIntentFilter());
		registerReceiver(receiver, LaputaBroadcast.getIntentFilter());
		if (getBlueService() != null && getBlueService().isConnect()) {
			getBlueService().write(ProtolWrite.instance().writeForStep(0));
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(xReceiver);
		unregisterReceiver(receiver);
	}

	private void initFragments() {
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
		}

		homeFragment
				.setOnHomeFragmentClickListener(new HomeFragment.OnHomeFragmentClickListener() {
					@Override
					public void doShare() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								share(mHandler);
							}
						});

						/*
						 * HistorySportNew hs = new HistorySportNew("20160607",
						 * 3, 23, 0.55f, 0.01f); hs.save();
						 */

						// List<HistorySport> findAll =
						// DataSupport.findAll(HistorySport.class);
						//
						// if (findAll != null) {
						// Log.e("", "------------------------");
						// for (HistorySport s : findAll) {
						//
						// Log.e("", s.toString());
						// }
						// Log.e("", "------------------------");
						// }

					}

					@Override
					public void doRefresh() {
						// toast("当前计步！！！");
						if (getBlueService() != null && getBlueService().isConnect()) {
							getBlueService().write(ProtolWrite.instance()
									.writeForStep(0));
						}
						
						
						//
//						LitePalManager.instance().saveHistory(false, true);
						
						/*
						 * List<HistorySportNew> findAll =
						 * HistorySportNew.findAll(HistorySportNew.class);
						 * Log.e("pal", "findAll :" + findAll); if (findAll!=
						 * null ) { Log.e("pal", "size :" + findAll.size()); if
						 * (findAll.size()>0) { for (HistorySportNew
						 * historySportNew : findAll) { Log.e("pal",
						 * "historySportNew :" + historySportNew); } } }
						 */

						/*
						 * List<HistorySleep> findAll =
						 * HistorySleep.findAll(HistorySleep.class);
						 * Log.e("pal", "findAll :" + findAll); if (findAll!=
						 * null ) { Log.e("pal", "size :" + findAll.size()); if
						 * (findAll.size()>0) { for (HistorySleep
						 * historySportNew : findAll) { Log.e("pal",
						 * "historySportNew :" + historySportNew); } } }
						 */

						// setTestHistoryData();
						
						

						/*
						 * SimpleAlertDialog d = (SimpleAlertDialog) new
						 * SimpleAlertDialog(
						 * MainActivity.this).setLeftText("hahah"); d.show();
						 */

					}
				});
		if (meFragment == null) {
			meFragment = new MeFragment();
		}
		meFragment
				.setOnMeFragmentClickListener(new MeFragment.OnMeFragmentClickListener() {
					@Override
					public void doLookSleepHistory() {
						Intent intent = new Intent(MainActivity.this,
								HistorySleepActivity.class);
						startActivity(intent);
					}

					@Override 
					public void doLookSportHistory() {
						Intent intent = new Intent(MainActivity.this,
								HistorySportActivity.class);
						startActivity(intent);
					}
				});

		if (settingFragment == null) {
			settingFragment = new SettingFragment();
		}
		settingFragment
				.setOnSettingFragmentListener(new SettingFragment.OnSettingFragmentListener() {
					private LaputaLoadingAlertDialog loadDialog;

					@Override
					public void onClick(View v) {
						Intent intent = null;
						switch (v.getId()) {
						case R.id.rl_setting_device:
							intent = new Intent(getApplicationContext(),
									DeviceAcitivy.class);
							break;
						case R.id.rl_setting_information:
							intent = new Intent(getApplicationContext(),
									SettingPersonalActivity.class);
							break;
						case R.id.rl_setting_sync:

							// Toast.makeText(getApplicationContext(),
							// getString(R.string.sync_data),
							// Toast.LENGTH_SHORT).show();

							/*
							 * 判断是否连接，决定是否加载同步
							 */
							
							
//							XLog.e("````xBlueService : " + xBlueService);
//							XLog.e("````BaseApp : " + ((BaseApp)getApplication()).getBlueService());
//							XLog.e("````xBlueService.isConnect() : " + xBlueService.isConnect());
							if (getBlueService() !=null 
								&& getBlueService().isConnect()) {
								// if (true) {
								getBlueService().write(ProtolWrite.instance()
										.writeForSyncStep());
								loadDialog = new LaputaLoadingAlertDialog(
										MainActivity.this).builder("").max(200)
										.setListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												loadDialog.dismiss();
											}
										}).duration(5000);
								loadDialog.show();
								// loadDialog.start();
								getBlueService().write(new byte[][] {
										ProtolWrite.instance()
												.writeForSyncSleep(),
										ProtolWrite.instance()
												.writeForSyncStep() });
							} else {
								LaputaAlertDialog dialog = new LaputaAlertDialog(
										MainActivity.this)
										.builder(getString(R.string.pls_connect_mefit));
								dialog.show();
							}

							break;
						case R.id.rl_setting_remind:
							// Toast.makeText(getApplicationContext(), "个人提醒",
							// Toast.LENGTH_SHORT).show();
							intent = new Intent(getApplicationContext(),
									SettingRemindActivity.class);
							break;
						case R.id.rl_setting_camera:

							// Toast.makeText(getApplicationContext(), "远程拍照",
							// Toast.LENGTH_SHORT).show();
							boolean checkPermission = PMUtils.checkPermission(getApplicationContext());
							if (checkPermission) {
								intent = new Intent(getApplicationContext(),
										CameraActivity.class);
							}else{
								toast("请打开照相权限");
								 Intent intentC =  new Intent(Settings.ACTION_APPLICATION_SETTINGS);  
						         startActivity(intentC);
							}
							
							
							
							break;
						case R.id.rl_setting_about:
							// Toast.makeText(getApplicationContext(),
							// getString(R.string.about),
							// Toast.LENGTH_SHORT).show();
							intent = new Intent(getApplicationContext(),
									AboutActivity.class);

							break;
						case R.id.rl_setting_unbind:
							showDialogForDisBindedDevice();
							break;
						}
						if (intent != null) {
							startActivity(intent);
						}
					}

				});

		// fragments = new ArrayList<Fragment>();
		// fragments.add(homeFragment);
		// fragments.add(meFragment);
		// fragments.add(settingFragment);
	}

	protected void setTestHistoryData() {
		/**
		 * 模拟数据
		 */
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		for (int i = 0; i < 30; i++) {
			c.set(Calendar.DAY_OF_MONTH, i + 1);
			String dateStr = DateUtil.dateToString(c.getTime(), "yyyyMMdd");
			HistorySportNew sport = new HistorySportNew();
			sport = new HistorySportNew(dateStr,
					(int) (Math.random() * 60 * 24),
					(int) (Math.random() * 1000),
					(float) (Math.random() * 1000),
					(float) (Math.random() * 1000));
			sport.save();
		}

		for (int i = 0; i < 30; i++) {
			c.set(Calendar.DAY_OF_MONTH, i + 1);
			String dateStr = DateUtil.dateToString(c.getTime(), "yyyyMMdd");
			HistorySleep sleep = new HistorySleep();
			int deep = (int) (Math.random() * 60 * 12);
			int light = (int) (Math.random() * 60 * 12);
			sleep.setDate(dateStr);
			sleep.setDeep(deep);
			sleep.setLight(light);
			;
			sleep.save();
		}
	}

	private void initViews() {

		rgTab = (RadioGroup) findViewById(R.id.rg_tab);
		rbHome = (FangRadioButton) findViewById(R.id.rb_tab_home);
		rbMe = (FangRadioButton) findViewById(R.id.rb_tab_me);
		rbSetting = (FangRadioButton) findViewById(R.id.rb_tab_setting);

	}

	private void setListener() {
		rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				switch (i) {
				case R.id.rb_tab_home:
					if (homeFragment == null) {
						homeFragment = new HomeFragment();
					}
					if (homeFragment.isAdded()) {
						transaction.show(homeFragment);
					} else {
						transaction.replace(R.id.frame_main, homeFragment,
								HomeFragment.class.getSimpleName());
					}

					
					// 请求今天步数
					if (getBlueService() != null && getBlueService().isConnect()) {
						getBlueService().write(ProtolWrite.instance().writeForStep(
								0));
					}
					
					
					break;
				case R.id.rb_tab_me:
					if (meFragment == null) {
						meFragment = new MeFragment();
					}
					if (meFragment.isAdded()) {
						transaction.show(meFragment);
					} else {
						transaction.replace(R.id.frame_main, meFragment,
								MeFragment.class.getSimpleName());
					}
					break;
				case R.id.rb_tab_setting:
					if (settingFragment == null) {
						settingFragment = new SettingFragment();
					}
					if (settingFragment.isAdded()) {
						transaction.show(settingFragment);
					} else {
						transaction.replace(R.id.frame_main, settingFragment,
								SettingFragment.class.getSimpleName());
					}
					break;
				}
				transaction.commit();
			}
		});
	}

	/**
	 * 提示框：是否移除绑定设备
	 */
	private void showDialogForDisBindedDevice() {
		if (isDisBindedDialog == null) {
			isDisBindedDialog = new AlertDialog(this)
					.builder()
					.setMsg(getString(R.string.delete_the_device))
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

								}
							})
					.setPositiveButton(getString(R.string.confirm),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									try {
										String address = BondedDeviceUtil.get(
												1, getApplicationContext());
										// BluetoothDevice remoteDevice =
										// BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

										if (getBlueService() != null) {
											BluetoothDevice remoteDevice = getBlueService()
													.getSimpleLaputaBlue()
													.getRemoteDevice(address);

											if (remoteDevice != null) {
												BondedDeviceUtil
														.removeBond(remoteDevice);
											}
										}

										BondedDeviceUtil.save(1, "",
												getApplicationContext());
										// 2.关闭连接（当时连接状态时）
										if (getBlueService() != null) {
											getBlueService().closeAll();
										}

										if (settingFragment != null
												&& rbSetting.isChecked()) {

											settingFragment
													.updateSyncText(getBlueService()!=null && getBlueService().isConnect());
										}
										// initBindedDeviceInfo("", "");
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
		}
		isDisBindedDialog.show();

	}

	@Override
	public void onBackPressed() {

		/*
		 * BaseDialog sad = new SimpleAlertDialog(this)
		 * .setLeftIcon(R.drawable.ic_action_refresh) .setLeftText("左边")
		 * .setRightIcon(R.drawable.ic_action_refresh) .setRightText("右")
		 * .setMsg("哈哈哈") .setTitle("heiniu") .build(); sad.show();
		 */

		/*
		 * ProtolWrite pw = ProtolWrite.instance(); byte[]
		 * writeForIncomingNumber = pw.writeForIncomingNumber("13040815454",
		 * true); if (xBlueService != null) {
		 * xBlueService.write(writeForIncomingNumber); }
		 */

		// Onoff ==1 0 1 1 1 sb.toString() : FC17 0c1f 1716 0b0d 0c1f 1601 0004
		// 0003 01aa b400
		// pw.writeSetting(1, 12, 31, 23,22, 0, 1, 1, 12, 31, 22, 01, 1, 11, 13,
		// 3, 4,1,170,180);
		// byte[] hexDataForTimeSync = pw.hexDataForTimeSync(new Date(), this);
		// byte[] writeForPhoneAndSmsCount =
		// ProtolWrite.writeForPhoneAndSmsCount(0, 2);
		// byte[] writeSleepSetting;
		// writeSleepSetting = ProtolWrite.instance().writeSleepSetting(1, 13,
		// 13, 15, 15);
		// if (writeSleepSetting!=null) {
		// xBlueService.write(writeSleepSetting);
		// }
		// boolean isIncoming = (Boolean) SharedPreferenceUtil.get(this,
		// StaticValue.SHARE_REMIND_INCOMING, false);
		// byte[] writeForIncomingNumber =
		// ProtolWrite.instance().writeForIncomingNumber("13040815454",isIncoming);
		// xBlueService.write(writeForIncomingNumber);

		ActionSheetDialog exitDialog = new ActionSheetDialog(this).builder();
		exitDialog.setTitle(getString(R.string.exit_app));
		exitDialog.addSheetItem(getString(R.string.confirm),
				SheetItemColor.Red, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (getBlueService() != null
										&& getBlueService().isConnect()) {
									getBlueService().closeAll();
								}
								mHandler.postDelayed(new Runnable() {
									@Override
									public void run() {
										finish();
										System.exit(0);
										// android.os.Process.killProcess(android.os.Process.myPid());
									}
								}, 1000);
							}
						});
					}
				}).show();
	}
	
	private String localVersion;
	private String appName = "JUNSD";
	/** 需要更新 */
	private static final int NEED_UPDATE = 1;
	/** 当前网路不可用 */
	private static final int NET_NOT_AVAILABLE = 3;
	/** 服务器异常 */
	private static final int SERVER_ERROR = 4;
	private String downloadUrl;
	private void checkVersion(){
		netutil = NetUtil.getNetutilInstance(this);

		new Thread(new Runnable() {

		

		

			@Override
			public void run() {
				// TODO Auto-generated method stub
				localVersion = UpdateHelper.getLocalVersion(MainActivity.this);
				if (netutil.isNetActive()) {
					String result = UpdateHelper.getServerAppVersion(appName);
					System.out.println("开启线程执行任务" + result);
					if (null != result && !"".equals(result)) {
						if (result.equals(UpdateHelper.SERVER_ERROR)) {
							mHandler.sendEmptyMessage(SERVER_ERROR);
							return;
						}
						if (result.equals(UpdateHelper.CONNECTION_TIMEOUT)) {
							mHandler.sendEmptyMessage(NET_NOT_AVAILABLE);
							return;
						}
						try {
							JSONObject json = new JSONObject(result);
							String serverVersion = json
									.getString(StaticValue.APPVERSION);
							String serverAppName = json
									.getString(StaticValue.APPNAME);
							if (serverAppName.equals(appName)) {
								// 字符串比较，前大于后时，返回大于0的差值
								if (!serverVersion.equals("null")
										&& !localVersion.equals(serverVersion)
										&& localVersion
												.compareTo(serverVersion) < 0) {
									// 有更新
									downloadUrl = json
											.getString(StaticValue.APPDOWNLOADURL);
									mHandler.sendEmptyMessage(NEED_UPDATE);
									System.out.println("查找好了");
									// UpdateHelper.hideLoaddingWindow();
									// pop2ButtonDialog();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						mHandler.sendEmptyMessage(SERVER_ERROR);
					}
				} else{
					mHandler.sendEmptyMessage(NET_NOT_AVAILABLE);
					System.out.println("网络不可用 ");
					return;
				}
			}
		}).start();
	}
}
