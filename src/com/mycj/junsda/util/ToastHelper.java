package com.mycj.junsda.util;


import com.mycj.junsda.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义Toast样式，改变Toast的显示风格
 * 
 * @author Administrator
 * 
 */
public class ToastHelper extends Toast {

	public ToastHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 弹出提示框
	 * 
	 * @param context
	 *            上下文对象
	 * @param imgRsId
	 *            要显示的图片对象的id
	 * @param textId
	 *            要显示的文字Id
	 * @param duration
	 *            显示时间
	 * @return Toast
	 */
	public static Toast makeToast(Context context, int imgRsId, int textId,
			int duration) {
		Toast toast = new Toast(context);
		// 通过Context获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 初始化Toast界面布局
		View view = inflater.inflate(R.layout.common_toast_view, null);
		ImageView image = (ImageView) view.findViewById(R.id.cos_toast_icon);
		TextView txt = (TextView) view.findViewById(R.id.cos_toast_txt);

		/**
		 * 判断是否有传入图片ID
		 */
		if (imgRsId != 0) {
			image.setImageResource(imgRsId);
		} else {
			image.setVisibility(View.GONE);
		}

		txt.setText(context.getResources().getString(textId));

		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(duration);

		return toast;
	}

	/**
	 * 退出提示框
	 * 
	 * @param context
	 *            上下文对象
	 * @param textId
	 *            提示文字Id
	 * @param duration
	 *            Toast显示时间
	 * @return Toast
	 */
	public static Toast makeExitToast(Context context, int textId, int duration) {
		Toast toast = new Toast(context);
		// 通过Context获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 初始化退出提示Toast界面布局
		View view = inflater.inflate(R.layout.common_toast_normal_view, null);
		// 退出提示文字
		TextView exitText = (TextView) view
				.findViewById(R.id.cos_toast_normal_txt);

		exitText.setText(context.getResources().getString(textId));

		toast.setView(view);
		/**
		 * gravity:起点位置, xOffset:水平向右位移, yOffset:垂直向下位移
		 */
		toast.setGravity(Gravity.BOTTOM, 0, 100);
		toast.setDuration(duration);

		return toast;
	}

	/**
	 * 普通提示框 用于注册登录
	 * 
	 * @param context
	 *            上下文对象
	 * @param textId
	 *            提示文字Id
	 * @return
	 */
	public static Toast makeNormalToast(Context context, int textId) {
		Toast toast = new Toast(context);
		// 通过Context获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 初始化退出提示Toast界面布局
		View view = inflater.inflate(R.layout.common_toast_normal_view, null);
		// 退出提示文字
		TextView exitText = (TextView) view
				.findViewById(R.id.cos_toast_normal_txt);

		exitText.setText(context.getResources().getString(textId));

		toast.setView(view);
		/**
		 * gravity:起点位置, xOffset:水平向右位移, yOffset:垂直向下位移
		 */
		toast.setGravity(Gravity.BOTTOM, 0, 100);
		toast.setDuration(Toast.LENGTH_SHORT);

		return toast;
	}

}
