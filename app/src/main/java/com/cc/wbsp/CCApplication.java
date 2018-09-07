package com.cc.wbsp;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class CCApplication extends Application {
    private static final String TAG = "CCApplication";

    @Override
    public void onCreate() {    	     
    	 Logger.d(TAG, "CCApplication onCreate");
         super.onCreate();

         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
    }
}
