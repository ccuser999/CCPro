package com.core.base.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

public class AppUtil {

	/**
	 * 判断APP是否有更新
	 * 
	 * @param context
	 * @param packageName
	 * @param version
	 * @return
	 */
//	public static boolean isAppUpdate(Context context, String packageName,String version) {
//		if(version.equals("null") || SStringUtil.isEmpty(version)){
//			return false;
//		}
//		int curVerCode = ApkInfoUtil.getVersionCode(context, packageName);
//		if (Integer.parseInt(version) > curVerCode && curVerCode!=0) {
//			return true;
//		}
//		return false;
//	}
//

	/**
	 * 启动应用
	 * @param context
	 * @param packageName
	 */
	public static void startApp(Context context,String packageName){
		startApp(context, packageName, null);
	}
	
	public static void startApp(Context context,String packageName,String url){
		// 获取应用安装包管理类对象
		PackageManager packageManager = context.getPackageManager();
		try {
			packageManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if(!TextUtils.isEmpty(url)){
			Uri uriObj = Uri.parse(url);
			intent.setData(uriObj);
		}
		
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageName);

		List<ResolveInfo> apps = packageManager.queryIntentActivities(
				resolveIntent, 0);
		ResolveInfo ri = null;
		if(apps!=null && apps.size() != 0){
			ri = apps.iterator().next();
		}
		if (ri != null) {
			String className = ri.activityInfo.name;
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName(packageName,className);
			intent.setComponent(cn);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
	
	private static void startLineApp(Context context,String packageName,String url){
		// 获取应用安装包管理类对象
		PackageManager packageManager = context.getPackageManager();
		try {
			packageManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setPackage(packageName);
		if(!TextUtils.isEmpty(url)){
			Uri uriObj = Uri.parse(url);
			intent.setData(uriObj);
		}
		context.startActivity(intent);
	}

	/**
	 * 启动系统浏览器加载页面
	 *
	 * @param context
	 * @param url
	 */
	public static void openInOsWebApp(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);

		try {

			intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
			// intent.getComponent();
			context.startActivity(intent);

		} catch (Exception e) {
			try {
				intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				context.startActivity(intent);
			} catch (Exception e1) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		}
	}

	// 调用手机短信系统，发送短信
	public static void sendMessage(Context context, String phone, String message) {

		Uri uri = Uri.parse("smsto:" + phone);
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
		sendIntent.putExtra("sms_body", message);
		context.startActivity(sendIntent);

	}

/*
	*//**
	 * 通過包名，查找應用的信息
	 * @param context
	 * @param packageName
	 * @return
	 *//*
	public static AppInfoBean getAppInfoBeanByPackageName(Context context,String packageName){
		PackageInfo packageInfo;  
		ApplicationInfo info = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			info = context.getPackageManager().getApplicationInfo(packageName, 0);
		} catch (Exception e) {
			return null;
		}
		AppInfoBean appInfo = new AppInfoBean();
		appInfo.setAppLabel((String) info.loadLabel(context.getPackageManager()));
		appInfo.setAppIcon(info.loadIcon(context.getPackageManager()));
		appInfo.setPkgName(packageName);
		appInfo.setVersionCode(packageInfo.versionCode+"");
		return appInfo;
	}
	*//**
	 * 打开Line应用
	 * @param context
	 * @param url
	 *//*
	public static void openLineAPP(Context context,String url){
		if (!isAppInstalled(context,"jp.naver.line.android")) {
//			try {
//				comeDownloadPageInAndroidWeb(context, url);
//			} catch (Exception e) {				
//			}
			ToastUtils.toast(context, E_string.efun_pd_share_line_error);
			return;
		}
		startLineApp(context, "jp.naver.line.android", url);
	}*/
	
	/**
	 * 判断应用程序是否在运行
	 * @param context
	 * @param packageName
	 * @return
	 */
//	public static Object[] isAppRunning(Context context,String packageName){
//		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningTaskInfo> list = am.getRunningTasks(100);
//		boolean isAppRunning = false;
//		String topActivityName = "";
//		for (RunningTaskInfo info : list) {
//			if (info.topActivity.getPackageName().equals(packageName)||info.baseActivity.getPackageName().equals(packageName)) {
//				isAppRunning = true;
//				topActivityName = info.topActivity.getClassName();
//				break;
//			}
//		}
//		return new Object[]{isAppRunning,topActivityName};
//	}

	/**
	 * 隐藏虚拟按键，并且全屏
	 */
	public static void hideActivityBottomBar(Activity activity){
		//隐藏虚拟按键，并且全屏
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
			View v = activity.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			//for new api versions.
			View decorView = activity.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
					| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
	/**
	 * 隐藏虚拟按键，并且全屏
	 */
	public static void hideDialogBottomBar(Dialog dialog){
		//隐藏虚拟按键，并且全屏
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
			View v = dialog.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			//for new api versions.
			View decorView = dialog.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
					| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
			dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	public static void startMainActivitiy(Context context) {

		String pkName = context.getPackageName();

		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_LAUNCHER);

		Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(pkName);
		if (mainIntent != null && mainIntent.getComponent() != null) {
			String className = mainIntent.getComponent().getClassName();
			it.setClassName(context, className);
		} else if (mainIntent != null) {
			it = mainIntent;
		}
		//it.putExtra(EfunPushManager.PUSH_NOTIFICATION_START, true);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

		context.startActivity(it);

	}
}
