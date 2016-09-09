package com.mycj.junsda.view;




import com.mycj.junsda.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
public class LaputaInputDialog extends AbstractLaputaDialog{
	private Display display;
	private FangTextView tvTitle;
	private FangTextView tvPositive;
	private FangTextView tvNegative;
	private EditText etNumber;
	
	
	public LaputaInputDialog(Context context) {
		super(context);
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	@Override
	public View getContentView(Context context) {
		return LayoutInflater.from(context).inflate(R.layout.view_laputa_input_dialog, null);
	}

	@Override
	public void getView(View v) {
		tvTitle = (FangTextView) v.findViewById(R.id.tv_title);
		tvPositive = (FangTextView) v.findViewById(R.id.tv_positive);
		tvNegative = (FangTextView) v.findViewById(R.id.tv_negative);
		etNumber = (EditText) v.findViewById(R.id.et_number);
		
		
	}
	
	public LaputaInputDialog setTitle(String title){
		if (tvTitle != null) {
			tvTitle.setText(title);
		}
		return this;
	}
	
	public LaputaInputDialog setPositiveListener(String text,View.OnClickListener l){
		if (tvPositive!=null) {
			tvPositive.setOnClickListener(l);
		}
		return this;
	}
	
	public LaputaInputDialog setNegativeListener(String text,View.OnClickListener l){
	
		if (tvNegative!=null) {
			tvNegative.setOnClickListener(l);
			dismiss();
		}
		return this;
	}
	
	public int getNumber() {
		int result = -1;
		if (etNumber != null) {
			try {
				String number = etNumber.getText().toString();
				 result = Integer.parseInt(number);
			} catch (Exception e) {
				result = -1;
				Log.e("xpl", "数字转换错误");
			}
		}
		return result;
	}
	
	public LaputaInputDialog setNumber(String number){
		if (etNumber !=null) {
//			Log.e("xpl", "设置数字————————————————————————————————————————————");
			etNumber.setText(number);
			etNumber.setSelection(number.length());
		}
		return this;
	}

	@Override
	public Dialog setDialogWindow(Context context,View view) {
		dialog = new Dialog(context,R.style.XplAlertDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		lp.width = (int) (0.85 * display.getWidth());
		
		dialogWindow.setAttributes(lp);
		return dialog;
	}
	
}
	
