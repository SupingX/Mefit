package com.mycj.junsda.bean;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 运动历史记录
 * 包括运动日期 运动次数index 运动时间 运动步数 运动距离 运动卡路里
 */
public class HistorySportNew extends DataSupport implements Parcelable {
	
	private String sportDate; //yyyyMMdd
	private int sportTime; //运动时间 单位秒
	private int sportStep; //运动步数
	private float distance; //运动距离
	private float calorie; //运动消耗
	
	
	
	
	public HistorySportNew(String sportDate, int sportTime, int sportStep,
			float distance, float calorie) {
		super();
		this.sportDate = sportDate;
		this.sportTime = sportTime;
		this.sportStep = sportStep;
		this.distance = distance;
		this.calorie = calorie;
	}
	public HistorySportNew() {
		super();
	}
	
	
	
	@Override
	public String toString() {
		return "HistorySportNew [sportDate=" + sportDate + ", sportTime=" + sportTime + ", sportStep="
				+ sportStep + ", distance=" + distance + ", calorie=" + calorie
				+ "]";
	}
	public String getSportDate() {
		return sportDate;
	}
	public void setSportDate(String sportDate) {
		this.sportDate = sportDate;
	}
	public int getSportTime() {
		return sportTime;
	}
	public void setSportTime(int sportTime) {
		this.sportTime = sportTime;
	}
	public int getSportStep() {
		return sportStep;
	}
	public void setSportStep(int sportStep) {
		this.sportStep = sportStep;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public float getCalorie() {
		return calorie;
	}
	public void setCalorie(float calorie) {
		this.calorie = calorie;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.sportDate);
		dest.writeInt(this.sportTime);
		dest.writeInt(this.sportStep);
		dest.writeFloat(this.distance);
		dest.writeFloat(this.calorie);
	}
	public static final Parcelable.Creator<HistorySportNew> CREATOR = new Creator<HistorySportNew>() {

		@Override
		public HistorySportNew createFromParcel(Parcel source) {
			String sportDate = source.readString();
			int sportTime = source.readInt();
			int sportStep = source.readInt();
			float distance = source.readFloat();
			float calorie = source.readFloat();
			return new HistorySportNew(sportDate, sportTime, sportStep, distance, calorie);
		}

		@Override
		public HistorySportNew[] newArray(int size) {
			return new HistorySportNew[size];
		}
	
	};
}
