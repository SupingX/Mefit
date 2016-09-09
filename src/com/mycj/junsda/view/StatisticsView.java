package com.mycj.junsda.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class StatisticsView extends View{
	private Paint paint = new Paint();
	public StatisticsView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public StatisticsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public StatisticsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawText("舞低杨柳楼心月", 100, 100, paint);
		canvas.drawColor(Color.RED);
		
	}

}
