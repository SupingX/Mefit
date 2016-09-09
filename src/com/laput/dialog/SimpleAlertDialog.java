package com.laput.dialog;

import com.mycj.junsda.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleAlertDialog extends BaseDialog{

	private TextView tvLeft;
	private TextView tvMsg;
	private TextView tvRight;
	private TextView tvTitle;
	private ImageView iconTitle;

	public SimpleAlertDialog(Context context) {
		super(context);
	}

	@Override
	public View getDialogView() {
		// 左按钮
		tvLeft = (TextView) getContentView().findViewById(R.id.tv_dialog_left);
		if (!TextUtils.isEmpty(getLeft())) {
			tvLeft.setText(getLeft());
		}
		if (getLeftRes() >0) {
			tvLeft.setText(getLeftRes());
		}
		if (getLeftOnClickListener()!=null) {
			tvLeft.setOnClickListener(getLeftOnClickListener());
		}
		
		// 右按钮
		tvRight = (TextView) getContentView().findViewById(R.id.tv_dialog_right);
		if (!TextUtils.isEmpty(getRight())) {
			tvRight.setText(getRight());
		}
		if (getRightRes() >0) {
			tvRight.setText(getRightRes());
		}
		if (getRightOnClickListener() != null) {
			tvRight.setOnClickListener(getRightOnClickListener());
		}
		
		// 消息
		tvMsg = (TextView) getContentView().findViewById(R.id.tv_dialog_msg);
		if (!TextUtils.isEmpty(getMsg())) {
			tvMsg.setText(getMsg());
		}
		if (getMsgRes() >0) {
			tvMsg.setText(getMsgRes());
		}
		
		// 标题
		tvTitle = (TextView) getContentView().findViewById(R.id.tv_dialog_title);
		if (!TextUtils.isEmpty(getTitle())) {
			tvTitle.setText(getTitle());
		}
		if (getTitleRes() >0) {
			tvTitle.setText(getTitleRes());
		}
		
		// 标题icon
		iconTitle = (ImageView) getContentView().findViewById(R.id.img_dialog_title);
		if (getTitleIcon() >0) {
			iconTitle.setImageResource(getTitleIcon());
		}
		
		
		return getContentView().findViewById(R.id.ll_dialog);
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_base;
	}

}
