package com.laput.dialog;

import com.mycj.junsda.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;

public abstract class BaseDialog  implements IBaseDialogBuilder{
	private Context context;
	private String title;
	private String msg;
	private int msgRes;
	public String getMsg() {
		return msg;
	}

	public int getMsgRes() {
		return msgRes;
	}

	public Dialog getDialog() {
		return dialog;
	}

	private int titleRes;
	private int titleIcon;
	private String left;
	private int leftRes;
	private String right;
	private int rightRes;
	private int leftIcon;
	private int rightIcon;
	private View.OnClickListener leftOnClickListener;
	private View.OnClickListener rightOnClickListener;
	private View contentView;
	private Dialog dialog;
	public BaseDialog(Context context){
		this(context, R.style.AlertDialogStyle);
	}
	
	public BaseDialog(Context context,int style){
		this.context = context;
		dialog = new Dialog(context,style);
	}
	
	@Override
	public BaseDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public BaseDialog setTitle(int title) {
		this.titleRes = title;
		return this;
	}

	@Override
	public BaseDialog setTitleIcon(int iconRes) {
		this.titleIcon = iconRes;
		return this;
	}

	@Override
	public BaseDialog setLeftText(String left) {
		this.left = left ;
		return this;
	}

	@Override
	public BaseDialog setLeftText(int lfet) {
		this.leftRes = lfet;
		return this;
	}

	@Override
	public BaseDialog setLeftIcon(int iconRes) {
		this.leftIcon = iconRes;
		return this;
	}

	@Override
	public BaseDialog setRightText(int right) {
		this.rightRes = right;
		return this;
	}

	@Override
	public BaseDialog setRightText(String right) {
		this.right = right;
		return this;
	}

	@Override
	public BaseDialog setRightIcon(int iconRes) {
		this.rightIcon = iconRes;
		return this;
	}

	@Override
	public BaseDialog setLeftOnClickListener(
			OnClickListener onClickListener) {
		this.leftOnClickListener = onClickListener;
		return this;
	}

	@Override
	public BaseDialog setRightOnClickListener(
			OnClickListener onClickListener) {
		this.rightOnClickListener = onClickListener;
		return this;
	}
	
	@Override
	public BaseDialog build(){
		contentView = LayoutInflater.from(context).inflate(getLayoutId(),null);
		if (contentView != null) {
			dialog.setContentView(contentView);
			setSize();
		}
		return this;
	}
	
	@Override
	public void show() {
		dialog.show();
	}
	
	private void setSize() {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		View llDialog = getDialogView();
		llDialog.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.65), LayoutParams.WRAP_CONTENT));
	}

	public abstract View getDialogView();

	public abstract int getLayoutId();

	public Context getContext(){
		return this.context;
	}

	public String getTitle() {
		return title;
	}

	public int getTitleRes() {
		return titleRes;
	}

	public int getTitleIcon() {
		return titleIcon;
	}

	public String getLeft() {
		return left;
	}

	public int getLeftRes() {
		return leftRes;
	}

	public String getRight() {
		return right;
	}

	public int getRightRes() {
		return rightRes;
	}

	public int getLeftIcon() {
		return leftIcon;
	}

	public int getRightIcon() {
		return rightIcon;
	}

	public View.OnClickListener getLeftOnClickListener() {
		return leftOnClickListener;
	}

	public View.OnClickListener getRightOnClickListener() {
		return rightOnClickListener;
	}

	public View getContentView() {
		return contentView;
	}

	@Override
	public BaseDialog setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	@Override
	public BaseDialog setMsg(int msg) {
		this.msgRes = msg;
		return this;
	}

	@Override
	public void dismiss() {
		if (dialog != null ) {
			dialog.dismiss();
		}
	}
	
	

}
