package com.mycj.junsda.bean;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 当前当次的运动信息
 * @author Administrator
 *
 */
public class CurrentSportInfo extends DataSupport implements Parcelable{
	private int step;
	private int time;
	private float distance;
	private float calorie;
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
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
	public CurrentSportInfo(int step, int time, float distance, float calorie) {
		super();
		this.step = step;
		this.time = time;
		this.distance = distance;
		this.calorie = calorie;
	}
	public CurrentSportInfo() {
		super();
	}
	@Override
	public String toString() {
		return "CurrentSportInfo [step=" + step + ", time=" + time
				+ ", distance=" + distance + ", calorie=" + calorie + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.step);
		dest.writeInt(this.time);
		dest.writeFloat(this.distance);
		dest.writeFloat(this.calorie);
	}
	
	public static final Parcelable.Creator<CurrentSportInfo> CREATOR = new Creator<CurrentSportInfo>() {
		
		@Override
		public CurrentSportInfo[] newArray(int size) {
			return new CurrentSportInfo[size];
		}
		
		@Override
		public CurrentSportInfo createFromParcel(Parcel source) {
			int step = source.readInt();
			int time = source.readInt();
			float distance = source.readFloat();
			float calorie = source.readFloat();
			return new CurrentSportInfo(step, time, distance, calorie);
		}
	};
	
	
}
