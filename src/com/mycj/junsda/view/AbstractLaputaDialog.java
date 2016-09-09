package com.mycj.junsda.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public abstract class AbstractLaputaDialog {
	protected Context context;
	protected Dialog dialog;

	public AbstractLaputaDialog(Context context) {
		this.context = context;
	}

	
	public AbstractLaputaDialog builder() {
		// 获取Dialog布局
		View view = getContentView(context);
		// 获取View控件
		getView(view);
		// 定义Dialog布局和参数
		setDialogWindow(context,view);
		/*dialog = new Dialog(context,R.style.XplAlertDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);*/
		return this;
	}
	
	public abstract View getContentView(Context context);
	public abstract void getView(View v);
	public abstract Dialog setDialogWindow(Context context,View view);

	public AbstractLaputaDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public AbstractLaputaDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}
	public boolean isShowing(){
		return dialog.isShowing();
	}

	public interface OnButtonClickListener {
		public void onPositiveClick(View v, WheelView wv, int number);

		public void onNegativeClick(View v, WheelView wv, int number);
	}

	public enum SheetItemColor {
		Blue("#037BFF"), Red("#FD4A2E");

		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
