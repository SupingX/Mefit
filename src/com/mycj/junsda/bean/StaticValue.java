package com.mycj.junsda.bean;

public class StaticValue {
	
//	public static final int MAX_HEIGHT_EN = 99;
//	public static final int MIN_HEIGHT_EN = 15;
//	public static final int MAX_WEIGHT_EN = 440;
//	public static final int MIN_WEIGHT_EN = 22;
//	
//	public static final int MAX_HEIGHT_ZH = 99;
//	public static final int MIN_HEIGHT_ZH = 15;
//	public static final int MAX_WEIGHT_ZH = 440;
//	public static final int MIN_WEIGHT_ZH = 22;
	
	public static final int MAX_SLEEP = 12*60;
	public static final int MAX_DISTANCE = 10;
	public static final int MAX_STEP = 1000;
	public static final int MAX_TIME = 12*60;
	public static final String UNIT_ZH = "zh";
	public static final String UNIT_EN = "en";
	
	public static final int DEFAULT_WEIGHT = 70;//默认体重70kg
	public static final int DEFAULT_HEIGHT = 170;//默认体重170CM
	public static final String DEFAULT_BIRTHDAY = "1985-01-01";//默认生日
	public static final String DEFAULT_UNIT = UNIT_ZH;//默认单位
	public static final String DEFAULT_SLEEP_TIME = "22:00";//默认睡觉时间
	public static final String DEFAULT_AWAK_TIME = "06:00";//默认起床时间
	public static final String DEFAULT_ALARM = "06:00";//默认每日闹铃时间
	public static final String DEFAULT_BIRTHDAY_TIME = "08:00";//默认生日提醒时间
	public static final String DEFAULT_LONG_SIT = "60";//默认生日提醒时间
	public static final int DEFAULT_GOAL_STEP = 20000;//运动目标
	
	public static final String ACTION_DELETE_STARTED = "ACTION_DELETE_STARTED";
	//setting_personal
	public static final String SHARE_LONG_SIT = "SHARE_LONG_SIT";
	public static final String SHARE_UNIT = "SHARE_HNIT"; //String 
	public static final String SHARE_SEX = "SHARE_SEX"; // int
	public static final String SHARE_WEIGHT = "SHARE_WEIGHT";
	public static final String SHARE_HEIGHT = "SHARE_HEIGHT";
	public static final String SHARE_BIRTHDAY = "SHARE_BIRTHDAY";//保存格式2011-2-3
	public static final String SHARE_SLEEP_ON_OFF = "SHARE_SLEEP_ON_OFF";
	public static final String SHARE_SLEEP_TIME = "SHARE_SLEEP_TIME";
	public static final String SHARE_AWAK_TIME = "SHARE_AWAK_TIME";
	public static final String SHARE_GOAL_STEP = "SHARE_GOAL_STEP";
	//setting_remind
	public static final String SHARE_REMIND_INCOMING = "SHARE_REMIND_INCOMING";
	public static final String SHARE_REMIND_ALARM = "SHARE_REMIND_ALARM";
	public static final String SHARE_REMIND_LONG_SIT = "SHARE_REMIND_LONG_SIT";
	public static final String SHARE_REMIND_BIRTHDAY = "SHARE_REMIND_BIRTHDAY";
	public static final String SHARE_REMIND_POINT_TIME = "SHARE_REMIND_POINT_TIME";
	public static final String SHARE_REMIND_ALARM_TIME = "SHARE_REMIND_ALARM_TIME";
	public static final String SHARE_REMIND_BIRTHDAY_TIME = "SHARE_REMIND_BIRTHDAY_TIME";
	//handler
	public static final int MSG_SHARE = 0xA1;
	
	
	/** 应用程序的服务器地址 */
	public static final String APP_URL = "http://www.mymcuapp.com";
	/** 应用更新接口 */
	public static final String UPDATE_URL = "/appversion";
	public static final String APPNAME = "appName";
	public static final String APPVERSION = "appVersion";
	public static final String APPDOWNLOADURL = "appDownLoadUrl";
	
}
