package com.laput.dialog;

import android.app.Dialog;
import android.view.View;

public interface IBaseDialogBuilder {
	public IBaseDialogBuilder setTitle(String title);
	
	public IBaseDialogBuilder setTitle(int title);
	
	public IBaseDialogBuilder setTitleIcon(int iconRes);
	
	public IBaseDialogBuilder setMsg(String msg);
	
	public IBaseDialogBuilder setMsg(int iconRes);
	
	public IBaseDialogBuilder setLeftText(String left);
	
	public IBaseDialogBuilder setLeftText(int lfet);
	
	public IBaseDialogBuilder setLeftIcon(int iconRes);
	
	public IBaseDialogBuilder setRightText(int right);
	
	public IBaseDialogBuilder setRightText(String right);
	
	public IBaseDialogBuilder setRightIcon(int iconRes);
	
	public IBaseDialogBuilder setLeftOnClickListener(View.OnClickListener onClickListener);
	
	public IBaseDialogBuilder setRightOnClickListener(View.OnClickListener onClickListener);
	
	public void show();
	
	public void dismiss();
	public IBaseDialogBuilder build();
	
	
}
