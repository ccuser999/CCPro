package com.core.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

public class ApkInstallUtil {
	
	/**
	 * 安装apk
	 * 
	 * @param apkPath
	 */
	public static void installApk(Context context, String apkPath) {
		PL.i("installApk："+apkPath);
		File apkfile = new File(apkPath);
		if (!apkfile.exists()) {
			PL.i("---------------没有发现要安装的文件------------------");
			return;
		}
		Uri uri = Uri.fromFile(apkfile);      
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
	}

	
	public static boolean isInstallApp(Context context, String packageName) {
		
		PackageManager pManager = context.getPackageManager();
		// 获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			String pn = paklist.get(i).packageName;
			if (!TextUtils.isEmpty(pn) && pn.equals(packageName)) {
				return true;
			}
		}
		return false;
		
	}

}
