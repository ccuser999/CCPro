package com.cc.wbsp.webvideo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cc.wbsp.R;
import com.cc.wbsp.widget.EEEBaseRelativeLayout;
import com.core.base.utils.PL;
import com.core.base.utils.ScreenHelper;

/**
 * Created by Efun on 2016/10/21.
 */

public class FloatVideoPlayer {

    Activity activity;

    WindowManager windowManager;
    WebView videoWebView;
    EEEBaseRelativeLayout videoLayout;
    ScreenHelper screenHelper;
    WindowManager.LayoutParams mWindowParams;
    ProgressBar progressBar;
    ImageView closeImageView;

    int defVideoWith = 800;
    int defVideoHeight = 600;

    private WebChromeClient.CustomViewCallback 	xCustomViewCallback;


    int _xDelta = 0;
    int _yDelta  = 0;

    private String loadUrl;

    public void create(final Activity activity, String videoUrl){
        PL.d("videoUrl:" + videoUrl);
//        videoUrl = "https://player.twitch.tv/?video=v111789341";
//        videoUrl = "https://youtu.be/WqcBNQwCA40";
        loadUrl = videoUrl;
        this.activity = activity;
        screenHelper  = new ScreenHelper(activity);

    /*    if (screenHelper.isPortrait()) {
            defVideoHeight = screenHelper.getScreenWidth();//这里是为了视频宽度总比高度大
            defVideoWith = screenHelper.getScreenHeight() ;
        }else{
            defVideoWith = screenHelper.getScreenWidth();
            defVideoHeight = screenHelper.getScreenHeight();
        }
*/
        windowManager = activity.getWindowManager();

        videoLayout = (EEEBaseRelativeLayout) activity.getLayoutInflater().inflate(R.layout.eee_player_video_layout,null);
        //这里通过layout的onConfigurationChanged来通知WindowManager屏幕改变，以便更改其大小和可拖动的范围
       /* videoLayout.setChangedListener(new EEEBaseRelativeLayout.ConfigurationChangedListener() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                ScreenHelper mScreenHelper  = new ScreenHelper(activity);
                screenHelper = mScreenHelper;
                if (mScreenHelper.isPortrait()) {
                    defVideoHeight = mScreenHelper.getScreenWidth() * 2 / 5;//这里是为了视频宽度总比高度大
                    defVideoWith = mScreenHelper.getScreenHeight() * 2 / 5;
                }else{
                    defVideoWith = mScreenHelper.getScreenWidth() * 2 / 5;
                    defVideoHeight = mScreenHelper.getScreenHeight() * 2 / 5;
                }
                mWindowParams.width = defVideoWith;
                mWindowParams.height = defVideoHeight;
                windowManager.updateViewLayout(videoLayout,mWindowParams);
            }
        });
*/
        FrameLayout videoContent = (FrameLayout) videoLayout.findViewById(R.id.eee_web_video_frame_layout_id);

        progressBar = (android.widget.ProgressBar) videoLayout.findViewById(R.id.webview_pager_loading_percent);
        closeImageView = (ImageView) videoLayout.findViewById(R.id.video_close_id);



        videoWebView = new WebView(activity);
        //设置webview
        initPlayerWebView();
        //添加webview
        videoContent.addView(videoWebView);

        mWindowParams =  createWebViewManagerParams();

        windowManager.addView(videoLayout,mWindowParams);

        videoWebView.loadUrl(videoUrl);

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy(null);
            }
        });


    }

    public static View playingView;
    public static Activity playingViewActivity;
    private static boolean mm = false;

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    private void initPlayerWebView() {

        videoWebView.setBackgroundColor(Color.BLACK);
        videoWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCachePath(activity.getCacheDir().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }

        webSettings.setDefaultTextEncodingName("UTF-8");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);

        videoWebView.setWebViewClient(new WebViewClient(){


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //return super.shouldOverrideUrlLoading(view, request);

                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (mm){
                    handler.cancel();
                }else{
                    handler.proceed();
                }
            }
        });

        videoWebView.setWebChromeClient(new WebChromeClient(){
            View xprogressvideo;
            View xCustomView;
            @Override
            public Bitmap getDefaultVideoPoster() {
                return BitmapFactory.decodeResource(activity.getResources(),R.drawable.eee_pd_video_image_loading);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar !=null) {
                    progressBar.setProgress(newProgress);
                    if (newProgress > 85){
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }


            @Override
            public View getVideoLoadingProgressView() {
                if (xprogressvideo == null) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
                }
                return xprogressvideo;
            }

            @Override
            public void onHideCustomView() {
                PL.d("onHideCustomView");
                if (xCustomViewCallback != null) {
                    xCustomViewCallback.onCustomViewHidden();
                    xCustomViewCallback = null;
                }
                xCustomView = null;
                playingView = null;
                if (playingViewActivity != null){
                    playingViewActivity.finish();
                    playingViewActivity = null;
                }
            }


            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                PL.d("onShowCustomView");

                //如果一个视图已经存在，那么立刻终止并新建一个
                if (xCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                xCustomView = view;
                xCustomViewCallback = callback;
                //通过activity播放
                playingView = view;
                activity.startActivity(new Intent(activity,VideoPlayerActivity.class));
            }
        });

        videoWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PL.d("onLongClick");
                return true;
            }
        });

        videoWebView.setOnTouchListener(new View.OnTouchListener() {

            private int count;
            long firClick;
            long  secClick;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int star_x_raw = (int)event.getRawX();//获取屏幕的原始坐标
                int star_y_raw =(int) event.getRawY();

                //玩家的手势动作
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        _xDelta = star_x_raw - mWindowParams.x;
                        _yDelta = star_y_raw - mWindowParams.y;

                        if (!TextUtils.isEmpty(loadUrl) && !loadUrl.contains("embed")) {
                            count++;
                            if(count == 1){
                                firClick = System.currentTimeMillis();

                            } else if (count == 2){
                                secClick = System.currentTimeMillis();
                                if(secClick - firClick < 1000){
                                    //双击事件
                                    if (mWindowParams.width == WindowManager.LayoutParams.MATCH_PARENT) {
                                        mWindowParams.width= defVideoWith;
                                        mWindowParams.height = defVideoHeight;
                                    }else{
                                        mWindowParams.width= WindowManager.LayoutParams.MATCH_PARENT;
                                        mWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    }
                                    mWindowParams.x = 0;
                                    mWindowParams.y = 0;
                                    if (windowManager != null) {
                                        windowManager.updateViewLayout(videoLayout,mWindowParams);
                                    }
                                    count = 0;
                                    firClick = 0;
                                    secClick = 0;

                                    return true;
                                }
                                count = 0;
                                firClick = 0;
                                secClick = 0;

                            }
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:

                        mWindowParams.x = star_x_raw - _xDelta;
                        mWindowParams.y = star_y_raw - _yDelta;

                        if (mWindowParams.x >= screenHelper.getScreenWidth()- mWindowParams.width){
                            mWindowParams.x = screenHelper.getScreenWidth() - mWindowParams.width;
                        }

                        if (mWindowParams.x < 0){
                            mWindowParams.x = 0;
                        }

                        if (mWindowParams.y >= screenHelper.getScreenHeight()  - mWindowParams.height){
                            mWindowParams.y = screenHelper.getScreenHeight() - mWindowParams.height;
                        }
                        if (mWindowParams.y < 0){
                            mWindowParams.y = 0;
                        }

                        if (windowManager != null) {
                            windowManager.updateViewLayout(videoLayout,mWindowParams);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:


                }
                return false;
            }
        });


    }


    private WindowManager.LayoutParams createFullSceenParams(){
        WindowManager.LayoutParams fullScreenParams = new WindowManager.LayoutParams();

        fullScreenParams.x = Gravity.START;
        fullScreenParams.y = Gravity.LEFT;
        fullScreenParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        fullScreenParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        fullScreenParams.format = 1;
        fullScreenParams.gravity = Gravity.LEFT | Gravity.TOP;
        fullScreenParams.type = WindowManager.LayoutParams.LAST_SUB_WINDOW;
        fullScreenParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        return  fullScreenParams;

    }

    private WindowManager.LayoutParams createWebViewManagerParams(){
        WindowManager.LayoutParams  mmLayoutParams = new WindowManager.LayoutParams();
        mmLayoutParams.x =0;
        mmLayoutParams.y = 0;
        mmLayoutParams.width = defVideoWith;
        mmLayoutParams.height = defVideoHeight;
        mmLayoutParams.format = 1;
        mmLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mmLayoutParams.type = WindowManager.LayoutParams.LAST_SUB_WINDOW;
        mmLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        return mmLayoutParams;

    }

    public void destroy(Activity activity) {
        try {
            if (windowManager != null) {
                windowManager.removeViewImmediate(videoLayout);
                windowManager = null;
            }
            if (videoWebView != null){
                videoWebView.destroy();
                videoWebView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean isDestroy() {
        return windowManager == null && videoWebView==null;
    }

    //關閉虛擬軟鍵盤
    public static void closeSoftwarKeyboard(Activity ctx){
        try{
            ctx.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }catch(Exception e){
            e.printStackTrace();
        }catch(Error e){
            e.printStackTrace();
        }
    }

}
