package com.mycj.junsda.util;

import java.text.DecimalFormat;

import com.mycj.junsda.R;

import android.content.Context;

/**
 * 公英制换算
 * @author Zeej
 *
 */
public class ConversionUtil {
	public enum Unit {
		Distance
		,Length 
		,Weight
		,Speed
	};
	
	private static final float RATE_DISTANCE = 0.6213712f; // 千米--〉英里 0.6213712
	private static final float RATE_LENGTH =0.3937008f; // 厘米 --〉英寸 0.3937008
	private static final float RATE_WEIGHT = 2.2046226f; // 千克--〉磅 2.2046226
	private static final float RATE_SPEED = 0.6213711f; // 1千米/时(km/h)=0.6213711英里/时(mile/h)
	
	private static final float RATE_DISTANCE_RE = 0.6213712f; // 1英里(mi)=1.609344千米(km)
	private static final float RATE_LENGTH_RE =0.3937008f; // 1英寸(in)=2.54厘米(cm)
	private static final float RATE_WEIGHT_RE = 2.2046226f; // 1磅(lb)=0.4535924千克(kg)
	private static final float RATE_SPEED_RE = 0.6213711f; // 1英里/时(mile/h)=1.609344千米/时(km/h)
	
	public static float metricToLnchValue(float metric,Unit unit){
		float result = 0;
		float rate = 0;
		switch (unit) {
		case Distance:
			rate = RATE_DISTANCE;
			break;
		case Length:
			rate = RATE_LENGTH;
			break;
		case Weight:
			rate = RATE_WEIGHT;
			break;
		case Speed:
			rate = RATE_SPEED;
			break;

		default:
			break;
		}
		result = metric * rate *1.0f;
		return result;
	}
	
	public static String metricToLnch(Context context,float metric,Unit unit){
		String result = "";
		String unitStr = "";
		float rate = 0;
		switch (unit) {
		case Distance:
			rate = RATE_DISTANCE;
			unitStr = context.getString(R.string.unit_inch_distance);
			break;
		case Length:
			rate = RATE_LENGTH;
			unitStr = context.getString(R.string.unit_inch_length);
			break;
		case Weight:
			rate = RATE_WEIGHT;
			unitStr = context.getString(R.string.unit_inch_weight);
			break;
		case Speed:
			rate = RATE_SPEED;
			unitStr = context.getString(R.string.unit_inch_speed);
			break;

		default:
			break;
		}
		result = format(metric * rate) + unitStr;
		return result;
	}
	
	
	public static float lnchToMetricValue(float lnch,Unit unit){
		float result = 0;
		float rate = 0;
		switch (unit) {
		case Distance:
			rate = RATE_DISTANCE_RE;
			break;
		case Length:
			rate = RATE_LENGTH_RE;
			break;
		case Weight:
			rate = RATE_WEIGHT_RE;
			break;
		case Speed:
			rate = RATE_SPEED_RE;
			break;

		default:
			break;
		}
		result = lnch * rate *1.0f;
		return result;
	}
	
	public static String lnchToMetric(Context context,float metric,Unit unit){
		String result = "";
		String unitStr = "";
		float rate = 0;
		switch (unit) {
		case Distance:
			rate = RATE_DISTANCE_RE;
			unitStr = context.getString(R.string.unit_metric_distance);
			break;
		case Length:
			rate = RATE_LENGTH_RE;
			unitStr = context.getString(R.string.unit_metric_length);
			break;
		case Weight:
			rate = RATE_WEIGHT_RE;
			unitStr = context.getString(R.string.unit_metric_weight);
			break;
		case Speed:
			rate = RATE_SPEED_RE;
			unitStr = context.getString(R.string.unit_metric_speed);
			break;

		default:
			break;
		}
		result = format(metric * rate) + unitStr;
		return result;
	}
	public static String format(double value) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(value);
	}
	public static String format1(double value) {
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(value);
	}
}
