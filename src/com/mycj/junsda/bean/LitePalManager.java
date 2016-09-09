package com.mycj.junsda.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.util.Log;

import com.mycj.junsda.view.DateUtil;

public class LitePalManager {
	private static LitePalManager litePal;

	public static LitePalManager instance() {
		if (litePal == null) {
			litePal = new LitePalManager();
		}
		return litePal;
	}

	private LitePalManager() {

	}

	private void log(String msg) {
		System.out.println(msg);
	}

	public int getTotalStep() {
		Integer sum = DataSupport
				.sum(HistorySport.class, "step", Integer.class);
		log("累计步数:" + sum);
		return sum;
	}

	// 总的步数
	public int getTotalStepNew() {
		Integer sum = DataSupport.sum(HistorySportNew.class, "sportStep",
				Integer.class);
		log("累计步数:" + sum);
		return sum;
	}

	public int getTotalSportTime() {
		Integer sum = DataSupport.sum(HistorySport.class, "sportTime",
				Integer.class);
		log("累计时间:" + sum);
		return sum;
	}

	// 总的时间
	public int getTotalSportTimeNew() {
		Integer sum = DataSupport.sum(HistorySportNew.class, "sportTime",
				Integer.class);
		log("累计时间:" + sum);
		return sum;
	}

	// 总的距离
	public float getTotalSportDistanceNew() {
		float sum = DataSupport.sum(HistorySportNew.class, "distance",
				Float.class);
		log("累计距离:" + sum);
		return sum;
	}

	// 总的卡路里
	public float getTotalSportCalorieNew() {
		float sum = DataSupport.sum(HistorySportNew.class, "calorie",
				Float.class);
		log("累计卡路里:" + sum);
		return sum;
	}

	// 一个月内所有的 睡眠记录
	public List<HistorySleep> findHistroySleepByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		List<HistorySleep> datas = DataSupport.where("date like ?",
				dateCondition).find(HistorySleep.class);
		sys("日期 ：" + DateUtil.dateToString(date, "yyyyMM") + "下的睡眠数据-->"
				+ datas);
		return datas;
	}

	public int findHistroySportStepsByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("date like ?", dateCondition).sum(
				HistorySport.class, "step", Integer.class);
		return sum;
	}

	// 查询一个月总的运动步数
	public int findHistroySportStepsByDateNew(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("sportDate like ?", dateCondition).sum(
				HistorySportNew.class, "sportStep", Integer.class);
		return sum;
	}

	// 一个月内睡眠的次数
	public int findHistroySleepCount(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("date like ?", dateCondition).count(
				HistorySleep.class);
		return sum;
	}

	public int findHistroySportTimeByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("date like ?", dateCondition).sum(
				HistorySport.class, "sportTime", Integer.class);
		return sum;
	}

	// 查询一个月总的运动时间
	public int findHistroySportTimeByDateNew(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("sportDate like ?", dateCondition).sum(
				HistorySportNew.class, "sportTime", Integer.class);
		return sum;
	}

	// 一个月内总的深睡时间
	public float findHistroySleepDeepByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("date like ?", dateCondition).sum(
				HistorySleep.class, "deep", Integer.class);
		return sum;
	}

	// 一个月内总的浅睡时间
	public float findHistroySleepLightByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("date like ?", dateCondition).sum(
				HistorySleep.class, "light", Integer.class);
		return sum;
	}

	public List<HistorySport> findHistroySportByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		List<HistorySport> datas = DataSupport.where("date like ?",
				dateCondition).find(HistorySport.class);
		return datas;
	}

	// 查询一个月内的运动信息
	public List<HistorySportNew> findHistroySportNewByDate(Date date) {

		/*
		 * int monthMaxDay = DateUtil.getMonthMaxDay(date);
		 * List<HistorySportNew> datas = new ArrayList<HistorySportNew>(); Date
		 * monthFirstDay = DateUtil.getMonthFirstDay(); for (int i = 0; i <
		 * monthMaxDay; i++) { Date dateOfDiffMonth =
		 * DateUtil.getDateOfDiffDay(monthFirstDay, i); String dateCondition =
		 * DateUtil.dateToString(dateOfDiffMonth, "yyyyMMdd") + "%"; int
		 * sportStep=DataSupport.where("sportDate like ?",
		 * dateCondition).sum(HistorySportNew.class, "sportStep",
		 * Integer.class);;; // if (sportStep > 0) { //当步数>0就添加。 int sportTime =
		 * DataSupport.where("sportDate like ?",
		 * dateCondition).sum(HistorySportNew.class, "sportTime",
		 * Integer.class);;; float
		 * distance=DataSupport.where("sportDate like ?",
		 * dateCondition).sum(HistorySportNew.class, "distance",
		 * Integer.class);;; float calorie=DataSupport.where("sportDate like ?",
		 * dateCondition).sum(HistorySportNew.class, "calorie",
		 * Integer.class);;; HistorySportNew hsn = new
		 * HistorySportNew(DateUtil.dateToString(dateOfDiffMonth, "yyyyMMdd"),
		 * sportTime, sportStep, distance, calorie); datas.add(hsn); // } }
		 * 
		 * return datas;
		 */

		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		List<HistorySportNew> datas = DataSupport.where("sportDate like ?",
				dateCondition).find(HistorySportNew.class);
		return datas;
	}

	public int findHistroySportCountByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int count = DataSupport.where("date like ?", dateCondition).count(
				HistorySport.class);
		return count;
	}

	// 查询一个月总的运动次数
	public int findHistroySportCountByDateNew(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int count = DataSupport.where("sportDate like ?", dateCondition).count(
				HistorySportNew.class);
		return count;
	}

	private void sys(String msg) {
		System.out.println(msg);
	}

	// 查询一个月总的运动距离
	public float findHistroySportDistanceByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("sportDate like ?", dateCondition).sum(
				HistorySportNew.class, "distance", Integer.class);
		return sum;
	}

	// 查询一个月总的运动消耗
	public float findHistroySportCalorieByDate(Date date) {
		String dateCondition = DateUtil.dateToString(date, "yyyyMM") + "%";
		int sum = DataSupport.where("sportDate like ?", dateCondition).sum(
				HistorySportNew.class, "calorie", Integer.class);
		return sum;
	}

	public void saveHistory(boolean sleepOnoff, boolean sportOnoff) {
		/**
		 * 模拟数据
		 */
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (sportOnoff) {
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
		}

		if (sleepOnoff) {
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
	}

}
