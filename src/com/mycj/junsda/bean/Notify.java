package com.mycj.junsda.bean;

public enum Notify {
	NOTIFY_NON("FFFF",0x0),
	NOTIFY_STEP("A0FB",0x1),
	NOTIFY_STEP_STATE("A0F1",0x2),
	NOTIFY_SLEEP("B0F0",0x3),
	NOTIFY_SLEEP_STATE("B0F1",0x4),
	NOTIFY_HEART_RATE("C0F0",0x5),
	NOTIFY_FIND_PHONE("D0F0",0x6),
	NOTIFY_CAMERA("E0F0",0x7),
	NOTIFY_MUSIC("F0F0",0x8),
	NOTIFY_SETTING_SEX("F2B0",0x9),
	NOTIFY_SETTING_HEIGHT("F2B2",0xa),
	NOTIFY_SETTING_WEIGHT("F2B1",0xb),
	NOTIFY_STEP_CURRENT("A0FA",0xc)
	
	
	;
	private String protol;
	private int index;
	Notify(String protol,int index){
		this.protol = protol;
		this.index = index;
	}
	
	public String getProtol(){
		return this.protol;
	}
	
	public int getIndex(){
		return this.index;
	}
}
