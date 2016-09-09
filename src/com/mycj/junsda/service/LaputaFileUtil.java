package com.mycj.junsda.service;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 保存图片
 * @author Administrator
 *
 */
public class LaputaFileUtil {
	/**
	 * 
	 * @param bitmap 保存的图片
	 * @param packageName 保存的文件夹名字
	 * @param context 
	 */
	public static  void saveImageToGallery(Bitmap bitmap , String packageName , Context context ){
		//1.保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),"Mefit");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() +".jpg";
		File file = new File(appDir,fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.e("LaputaFileUtil", "saveBitmapToGallery()--保存图片失败");
		}
		
		//2.把文件插入系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, "myWatch");
		} catch (Exception e) {
			Log.e("LaputaFileUtil", "saveBitmapToGallery()--插入系统图库失败");
		}
		//3.通知图库更新
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+file.getAbsolutePath()));
		context.sendBroadcast(intent);
	}
	
	
}
