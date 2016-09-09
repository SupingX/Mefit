package com.mycj.junsda.bean;

import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

import com.laputa.blue.util.XLog;
import com.mycj.junsda.util.DataUtil;
import com.mycj.junsda.view.DateUtil;

public class ProtolNotify {
	private static ProtolNotify protolNotify;

	private ProtolNotify() {
	}

	public static ProtolNotify instance() {
		if (protolNotify == null) {
			protolNotify = new ProtolNotify();
		}
		return protolNotify;
	}

	public int getResult(byte[] data) {
		if (data.length >= 2) {
			String head = DataUtil.byteToHexString(new byte[] { data[0],
					data[1] });// 头
			if (head.equals(Notify.NOTIFY_STEP.getProtol())) { // 计步
				return 1;
			} else if (head.equals(Notify.NOTIFY_STEP_STATE.getProtol())) {
				return 2;
			} else if (head.equals(Notify.NOTIFY_SLEEP.getProtol())) {
				return 3;
			} else if (head.equals(Notify.NOTIFY_SLEEP_STATE.getProtol())) {
				return 4;
			} else if (head.equals(Notify.NOTIFY_HEART_RATE.getProtol())) {
				return 5;
			} else if (head.equals(Notify.NOTIFY_MUSIC.getProtol())) {
				return Notify.NOTIFY_MUSIC.getIndex();
			} else if (head.equals(Notify.NOTIFY_FIND_PHONE.getProtol())) {
				return Notify.NOTIFY_FIND_PHONE.getIndex();
			} else if (head.equals(Notify.NOTIFY_CAMERA.getProtol())) {
				return Notify.NOTIFY_CAMERA.getIndex();
			} else if (head.equals(Notify.NOTIFY_STEP_CURRENT.getProtol())) {
				return Notify.NOTIFY_STEP_CURRENT.getIndex();
			}
		}
		return 0;
	}

	public Notify getResultNew(byte[] data) {
		if (data.length >= 2) {
			String value = DataUtil.byteToHexString(data);
			String head = value.substring(0, 4);
			// String head = DataUtil.byteToHexString(new byte[] { data[0],
			// data[1] });// 头
			if (head.equals(Notify.NOTIFY_STEP.getProtol())) { // 计步
				return Notify.NOTIFY_STEP;
			} else if (head.equals(Notify.NOTIFY_STEP_STATE.getProtol())) {
				return Notify.NOTIFY_STEP_STATE;
			} else if (head.equals(Notify.NOTIFY_SLEEP.getProtol())) {
				return Notify.NOTIFY_SLEEP;
			} else if (head.equals(Notify.NOTIFY_SLEEP_STATE.getProtol())) {
				return Notify.NOTIFY_SLEEP_STATE;
			} else if (head.equals(Notify.NOTIFY_HEART_RATE.getProtol())) {
				return Notify.NOTIFY_HEART_RATE;
			} else if (head.equals(Notify.NOTIFY_MUSIC.getProtol())) {
				return Notify.NOTIFY_MUSIC;
			} else if (head.equals(Notify.NOTIFY_FIND_PHONE.getProtol())) {
				return Notify.NOTIFY_FIND_PHONE;
			} else if (head.equals(Notify.NOTIFY_CAMERA.getProtol())) {
				return Notify.NOTIFY_CAMERA;
			} else if (head.equals(Notify.NOTIFY_SETTING_SEX.getProtol())) {
				return Notify.NOTIFY_SETTING_SEX;
			} else if (head.equals(Notify.NOTIFY_SETTING_HEIGHT.getProtol())) {
				return Notify.NOTIFY_SETTING_HEIGHT;
			} else if (head.equals(Notify.NOTIFY_SETTING_WEIGHT.getProtol())) {
				return Notify.NOTIFY_SETTING_WEIGHT;
			} else if (head.equals(Notify.NOTIFY_STEP_CURRENT.getProtol())) {
				return Notify.NOTIFY_STEP_CURRENT;
			}
		}
		return Notify.NOTIFY_NON;
	}

	/**
	 * 
	 * <p>
	 * 计步数据 旧
	 * </p>
	 * A0 F0 00 01 86 A0 00 00 A8 今天 计步 运动时间
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public HistorySport notifyHistorySport(byte[] data) {
		if (data == null) {
			XLog.e(getClass(), "数据为空");
			return null;
		}
		if (data.length != 9) {
			Log.e("", "接受的协议长度有错误");
			return null;
		}
		// try {
		// A0 F0 00 00 00 1D 00 00 06
		Date today = new Date();
		String index = DataUtil.byteToHexString(new byte[] { data[2] });// 天数
		Date date = DateUtil.getDateOfDiffDay(today, Integer.valueOf(index));
		String value = DataUtil.byteToHexString(data);
		String step = value.substring(6, 12);
		String time = value.substring(12, 18);
		// String step = DataUtil.byteToHexString(new byte[] { data[3], data[4],
		// data[5] });// 步数
		// String time = DataUtil.byteToHexString(new byte[] { data[6], data[7],
		// data[8] });//
		HistorySport historySport = new HistorySport();
		historySport.setDate(DateUtil.dateToString(date, "yyyyMMdd"));
		historySport.setStep(Integer.valueOf(step, 16));
		historySport.setSportTime(Integer.valueOf(time, 16));
		return historySport;
		// } catch (Exception e) {
		// }
		// return null;
	}

	/**
	 * <p>
	 * 发送所有计步数据前后状态
	 * </p>
	 * 
	 * @param data
	 * @return 0：开始 ；1：结束
	 * @throws Exception
	 */
	public int notifySyncSportState(byte[] data) {
		if (data.length != 3) {
			// throw new Exception("接受的协议长度有错误");
			return -1;
		}
		String state = DataUtil.byteToHexString(new byte[] { data[2] });// 发送开始/结束
		return Integer.valueOf(state);
	}

	/**
	 * 
	 * <p>
	 * 睡眠数据
	 * </p>
	 * 0x B0 F0 01 01 68 00 46
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public HistorySleep notifyHistorySleep(byte[] data) {
		/*
		 * if (data.length != 7) { // throw new Exception("接受的协议长度有错误"); return
		 * null; } try { Date today = new Date(); String index =
		 * DataUtil.byteToHexString(new byte[] { data[2] });// 天数 Date date =
		 * DateUtil.getDateOfDiffDay(today, -Integer.valueOf(index));
		 * 
		 * // String value = DataUtil.byteToHexString(data); int deepInt =
		 * Integer.valueOf(value.substring(6, 10), 16); int lightInt =
		 * Integer.valueOf(value.substring(10, 14), 16);
		 * 
		 * String deep = String.valueOf(deepInt);// 深睡 String light
		 * =String.valueOf(lightInt);// 浅睡 HistorySleep historySleep = new
		 * HistorySleep(); historySleep.setDate(DateUtil.dateToString(date,
		 * "yyyyMMdd")); historySleep.setDeep(Integer.valueOf(deep));
		 * historySleep.setLight(Integer.valueOf(light)); return historySleep; }
		 * catch (Exception e) { } return null;
		 */

		Log.e("zeej", "解析睡眠 : " +  DataUtil.byteToHexString(data));
		if (data.length == 11 || data.length == 7) {

			try {
				Date today = new Date();
				String index = DataUtil.byteToHexString(new byte[] { data[2] });// 天数
				Date date = DateUtil.getDateOfDiffDay(today,
						-Integer.valueOf(index));

				//
				String value = DataUtil.byteToHexString(data);
				int deepInt = Integer.valueOf(value.substring(6, 10), 16);
				int lightInt = Integer.valueOf(value.substring(10, 14), 16);
				

				String deep = String.valueOf(deepInt);// 深睡
				String light = String.valueOf(lightInt);// 浅睡
				String startTime = "";
				String endTime = "";
				if (data.length == 11) {
					int startHour = Integer.valueOf(value.substring(14, 16), 16);
					int startMin = Integer.valueOf(value.substring(16, 18), 16);
					int endHour = Integer.valueOf(value.substring(18, 20), 16);
					int endMin = Integer.valueOf(value.substring(20, 22), 16);
					startTime = getString(startHour) + getString(startMin);
					endTime = getString(endHour) + getString(endMin);
				}
				HistorySleep historySleep = new HistorySleep();
				historySleep.setDate(DateUtil.dateToString(date, "yyyyMMdd"));
				historySleep.setDeep(Integer.valueOf(deep));
				historySleep.setLight(Integer.valueOf(light));
				historySleep.setStartTime(startTime);
				historySleep.setEndTime(endTime);
				
				Log.e("zeej", "解析到的数据为 historySleep ：" + historySleep);
				return historySleep;
			} catch (Exception e) {
				Log.e("zeej", "解析异常！！！！！！"+e);
			}
		}
		return null;

	}

	private String getString(int value) {
		String result = "";
		if (value < 10) {
			result = "0" + String.valueOf(value);
		} else {
			result = String.valueOf(value);
		}
		return result;
	}

	/**
	 * <p>
	 * 发送所有睡眠数据前后状态
	 * </p>
	 * 
	 * @param data
	 * @return 0：开始 ；1：结束
	 * @throws Exception
	 */
	public int notifySyncSleepState(byte[] data) {
		if (data.length != 3) {
			// throw new Exception("接受的协议长度有错误");
			return -1;
		}
		String state = DataUtil.byteToHexString(new byte[] { data[2] });// 发送开始/结束
		return Integer.valueOf(state);
	}

	/**
	 * <p>
	 * 心率数据/p> 只有当手环处于测试心率界面，开始测试心率并得到测试结果时，才会发送此协议
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public int notifyHeartRate(byte[] data) {
		if (data.length != 3) {
			// throw new Exception("接受的协议长度有错误");
			return -1;
		}
		String value = DataUtil.byteToHexString(data);
		String substring = value.substring(0, 4);
		if (substring.equals(Notify.NOTIFY_HEART_RATE.getProtol())) {
			return data[2];
		}
		return -1;
	}

	/**
	 * 查找手机
	 * 
	 * @param data
	 * @return
	 */
	public int notifyFindPhone(byte[] data) {
		if (data.length != 3) {
			return -1;
		}
		String value = DataUtil.byteToHexString(data);
		String substring = value.substring(0, 4);
		if (substring.equals(Notify.NOTIFY_FIND_PHONE.getProtol())) {
			return data[2];
		}
		return -1;
	}

	/**
	 * 拍照
	 * 
	 * @param data
	 * @return
	 */
	public int notifyTakePicture(byte[] data) {
		if (data.length != 2) {
			return -1;
		}
		String value = DataUtil.byteToHexString(data);
		if (value.equals(Notify.NOTIFY_CAMERA.getProtol())) {
			return 1;
		}
		return -1;
	}

	/**
	 * 音乐 0 ：停止 ；1 ：开始；2：上一曲；3：下一曲；
	 * 
	 * @param data
	 * @return
	 */
	public int notifyMusic(byte[] data) {
		if (data.length != 3) {
			return -1;
		}
		String value = DataUtil.byteToHexString(data);
		String substring = value.substring(0, 4);
		if (substring.equals(Notify.NOTIFY_MUSIC.getProtol())) {
			return data[2];
		}
		return -1;
	}

	/**
	 * 运动历史 新
	 * 
	 * @param data
	 * @return
	 */
	public HistorySportNew notifyHistorySportNew(byte[] data) {
		if (data == null) {
			XLog.e(getClass(), "数据为空");
			return null;
		}
		if (data.length != 15) {
			Log.e("", "接受的历史运动数据协议长度有错误");
			return null;
		}

		String value = DataUtil.byteToHexString(data);
		String order = value.substring(0, 4);
		if (!TextUtils.equals(order, Notify.NOTIFY_STEP.getProtol())) {
			XLog.e(getClass(), "接受的历史运动数据协议不匹配");
			return null;
		}

		int date = Integer.valueOf(value.substring(4, 6), 16);
		int step = Integer.valueOf(value.substring(6, 12), 16);
		int time = Integer.valueOf(value.substring(12, 18), 16);
		float distance = Integer.valueOf(value.substring(18, 24), 16) * 1.0F / 100;
		float calorie = Integer.valueOf(value.substring(24, 30), 16) * 1.0F / 100;

		String sportDate = DateUtil.dateToString(
				DateUtil.getDateOfDiffDay(new Date(), 0 - date), "yyyyMMdd");

		HistorySportNew historySportNew = new HistorySportNew(sportDate, time,
				step, distance, calorie);

		XLog.i(getClass(), "解析历史运动 ：" + historySportNew.toString());

		return historySportNew;
	}

	/**
	 * 当前运动信息
	 * 
	 * @param data
	 * @return
	 */
	public CurrentSportInfo notifyCurrentSport(byte[] data) {
		if (data == null) {
			XLog.e(getClass(), "数据为空");
			return null;
		}
		if (data.length != 14) {
			Log.e("", "接受的历史运动数据协议长度有错误");
			return null;
		}

		String value = DataUtil.byteToHexString(data);
		String order = value.substring(0, 4);
		if (!TextUtils.equals(order, Notify.NOTIFY_STEP_CURRENT.getProtol())) {
			XLog.e(getClass(), "接受的当前运动数据协议不匹配");
			return null;
		}
		int step = Integer.valueOf(value.substring(4, 10), 16);
		int time = Integer.valueOf(value.substring(10, 16), 16);
		float distance = Integer.valueOf(value.substring(16, 22), 16) * 1.0F / 100;
		float calorie = Integer.valueOf(value.substring(22, 28), 16) * 1.0F / 100;

		CurrentSportInfo info = new CurrentSportInfo(step, time, distance,
				calorie);

		XLog.i(getClass(), "解析历史运动 ：" + info.toString());
		return info;
	}

	public int notifySettingSex(byte[] data) {
		if (data == null) {
			XLog.e(getClass(), "数据为空");
			return -1;
		}
		if (data.length != 4) {
			return -1;
		}

		String value = DataUtil.byteToHexString(data);
		String order = value.substring(0, 4);
		if (!TextUtils.equals(order, Notify.NOTIFY_SETTING_SEX.getProtol())) {
			XLog.e(getClass(), "设置性别状态不匹配");
			return -1;
		}
		return data[2];
	}

	public int notifySettingHeight(byte[] data) {
		if (data == null) {
			XLog.e(getClass(), "数据为空");
			return -1;
		}
		if (data.length != 4) {
			return -1;
		}

		String value = DataUtil.byteToHexString(data);
		String order = value.substring(0, 4);
		if (!TextUtils.equals(order, Notify.NOTIFY_SETTING_HEIGHT.getProtol())) {
			XLog.e(getClass(), "设置性别身高不匹配");
			return -1;
		}
		return data[2];
	}

	public int notifySettingWeight(byte[] data) {
		if (data == null) {
			XLog.e(getClass(), "数据为空");
			return -1;
		}
		if (data.length != 4) {
			return -1;
		}

		String value = DataUtil.byteToHexString(data);
		String order = value.substring(0, 4);
		if (!TextUtils.equals(order, Notify.NOTIFY_SETTING_WEIGHT.getProtol())) {
			XLog.e(getClass(), "设置性别体重不匹配");
			return -1;
		}
		return data[2];
	}

}
