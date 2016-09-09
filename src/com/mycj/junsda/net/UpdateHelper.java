package com.mycj.junsda.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.conn.ConnectTimeoutException;




import com.mycj.junsda.R;
import com.mycj.junsda.bean.StaticValue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.widget.Button;

public class UpdateHelper {

	private static Dialog loaddingDialog;
	private static Dialog updateDialog;
	public static final String SERVER_ERROR = "SERVER_ERROR";
	public static final String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";

	private static Button btn_next_time;
	private static Button btn_update;

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return 应用当前版本号
	 */
	public static String getLocalVersion(Context context) {
		String versionName = "";
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return versionName;
	}

	/**
	 * 请求服务器上的应用版本号
	 * 
	 * @param appName
	 * @return
	 */
	public static String getServerAppVersion(String appName) {
		HttpURLConnection conn = null;
		URL url = null;
		DataOutputStream out = null;
		InputStreamReader isr = null;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(StaticValue.APP_URL + StaticValue.UPDATE_URL);
			conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			// 设置connection输出
			conn.setDoOutput(true);
			// 发送指定参数，指定编码
			String paramString = "appName="
					+ URLEncoder.encode(appName, "utf-8");
			// 设置请求方式,必须大写
			conn.setRequestMethod("POST");
			// conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Length",
					String.valueOf(paramString.getBytes().length));
			// 设置不能使用缓存
			conn.setUseCaches(false);
			// 开始连接
			conn.connect();
			// 获取数据输出流对象
			out = new DataOutputStream(conn.getOutputStream());
			out.write(paramString.getBytes());
			out.flush();
			if (conn.getResponseCode() == 200) {
				System.out.println("app Name：" + appName + "  code:"
						+ conn.getResponseCode());
				// 获取输入流读取对象
				isr = new InputStreamReader(conn.getInputStream());
				int len = -1;
				char[] chars = new char[1024];
				// 读取服务器返回结果
				while ((len = isr.read(chars)) != -1) {
					sb.append(chars, 0, len);
				}
			}
		} catch (ConnectTimeoutException e) {
			System.out.println("网络连接超时");
			// e.printStackTrace();
			return CONNECTION_TIMEOUT;
		} catch (SocketTimeoutException e) {
			System.out.println("SocketTimeoutException异常");
			e.printStackTrace();
			return CONNECTION_TIMEOUT;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return SERVER_ERROR;
		} finally {
			try {
				if (null != isr)
					isr.close();
				if (null != out)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	/**
	 * 弹出加载框
	 */
	public static void popLoaddingWindow(Activity activity) {

		View view = activity.getLayoutInflater().inflate(
				R.layout.common_loading_view, null);
		loaddingDialog = new Dialog(activity, R.style.MyDialogStyle);
		loaddingDialog.setContentView(view);
		// 设置可点击返回不可取消对话框
		loaddingDialog.setCancelable(false);
		// 设置触摸不可隐藏
		loaddingDialog.setCanceledOnTouchOutside(false);
		loaddingDialog.show();
	}

	/**
	 * 隐藏加载框
	 */
	public static void hideLoaddingWindow() {
		if (loaddingDialog != null && loaddingDialog.isShowing()) {
			loaddingDialog.cancel();
		}
	}

	/**
	 * 加载框是否显示
	 * 
	 * @return
	 */
	public static boolean isLoaddingWindowShowing() {
		if (loaddingDialog == null) {
			return false;
		} else {
			return loaddingDialog.isShowing();
		}
	}

	/**
	 * 创建更新对话框
	 * 
	 * @param activity
	 */
	public static void pop2ButtonDialog(Activity activity) {
		View view = activity.getLayoutInflater().inflate(
				R.layout.layout_update_dialog, null);
		btn_next_time = (Button) view.findViewById(R.id.next_time);
		btn_update = (Button) view.findViewById(R.id.update);

		updateDialog = new Dialog(activity, R.style.MyDialogStyle);
		updateDialog.setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.show();
		btn_next_time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelUpdateDialog();
			}
		});

	}

	/**
	 * 获取更新按钮
	 * 
	 * @return
	 */
	public static Button getUpdateButton() {
		return btn_update;
	}

	/**
	 * 取消更新按钮
	 */
	public static void cancelUpdateDialog() {
		if (updateDialog != null && updateDialog.isShowing()) {
			updateDialog.cancel();
		}
	}

}
