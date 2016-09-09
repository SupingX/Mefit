package com.mycj.junsda.util;

import com.mycj.junsda.MainActivity;
import com.mycj.junsda.activity.CameraActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PMUtils {
	public static  boolean  checkPermission(Context context){
//	    boolean checkPermission = checkPermission(context, "android.permission.CAMERA", "com.mycj.junsda");
//	    return checkPermission;

	    //        boolean permission = (PackageManager.PERMISSION_GRANTED ==   
//                pm.checkPermission("android.permission.CAMERA", "com.mycj.junsda"));  
//      int permission = context.checkCallingOrSelfPermission("android.permission.CAMERA");
//        return permission == PackageManager.PERMISSION_GRANTED;
	  int checkSelfPermission = ContextCompat.checkSelfPermission(context, "android.permission.CAMERA");
      return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
	}
	/**
	 *     [android.permission.INTERNET,   
             android.permission.READ_PHONE_STATE,   
             android.permission.READ_CONTACTS,   
             ……  
             android.permission.READ_EXTERNAL_STORAGE,   
             android.permission.READ_CALL_LOG,   
             android.permission.WRITE_CALL_LOG]  
	 * @param context
	 * @return
	 */
	public static String[] getPermission(Context context){
		try {  
		    PackageManager pm = context.getPackageManager();  
            PackageInfo pack = pm.getPackageInfo("packageName",PackageManager.GET_PERMISSIONS);
            String[] permissionStrings = pack.requestedPermissions;  
            return permissionStrings;
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        } 
		return null;
	}
	
	public static boolean checkPermission(Context context, String permName, String pkgName){
		PackageManager pm = context.getPackageManager();
		if(PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, pkgName)){
			System.out.println(pkgName + " has permission : " + permName);
			return true;
		}else{
			System.out.println(pkgName + "not has permission : " + permName);
			return false;
		}
	}
	
}
