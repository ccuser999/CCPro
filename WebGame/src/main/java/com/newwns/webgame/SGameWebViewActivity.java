package com.newwns.webgame;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.core.base.BaseWebChromeClient;
import com.core.base.BaseWebViewClient;
import com.core.base.SWebView;
import com.core.base.cipher.DESCipher;
import com.core.base.utils.PL;
import com.core.base.utils.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by GanYuanrong on 2016/12/1.
 */

public class SGameWebViewActivity extends AppCompatActivity {

    private SWebView sWebView;
    private String webUrl;
    private ProgressBar progressBar;

    private Button preButton;
    private Button nextButton;

    OkHttpClient client ;
    private static final String gameKey = "app_key_ioved1cm!@#$8800";
    private static final String iv = "10009991";// String iv = "10009991";

    View webContentView;
    FrameLayout videoFullViewFrameLayout;
    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.s_web_view_with_bar_layout);

        client = new OkHttpClient();

        progressBar = (ProgressBar) findViewById(R.id.s_webview_pager_loading_percent);
        sWebView = (SWebView) findViewById(R.id.s_webview_id);

        sWebView.setBaseWebChromeClient(new MyBaseWebChromeClient(progressBar,this));
        sWebView.setWebViewClient(new MyBaseWebViewClient(this));


        preButton = findViewById(R.id.web_pre);
        nextButton = findViewById(R.id.web_next);
        webContentView = findViewById(R.id.web_content_layout);


        videoFullViewFrameLayout = findViewById(R.id.video_fullView);


        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webGoBack();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webGoForward();
            }
        });


//        try {
//            runRequest("https://iosdown.tyv800.com/dconf/andconf/webgameconf.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        sWebView.loadUrl("http://m.iqiyi.com/m5/hot/hotFeeds.html");

    }

    private void runRequest(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                try {

                    String resultMM = response.body().string();

                    if (TextUtils.isEmpty(resultMM)){
                        ToastUtils.toast(getApplicationContext(),"url error");
                        PL.i("webUrl is empty");
                        return;
                    }


                    webUrl = DESCipher.decrypt3DES(resultMM,gameKey,iv);

                    PL.i("SWebViewActivity url:" + webUrl);
                    SGameWebViewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sWebView.loadUrl(webUrl);
                        }
                    });

                } catch (Exception e) {
                    PL.i("解码出错");
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (sWebView != null){
            sWebView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sWebView != null){
            sWebView.destroy();
            sWebView = null;
        }
    }


    @Override
    public void onBackPressed() {

        if (sWebView != null && sWebView.canGoBack()) {

            sWebView.goBack(); // goBack()表示返回WebView的上一页面
            return;
        }
        super.onBackPressed();

    }

    private void webGoBack(){

        if (sWebView != null && sWebView.canGoBack()) {
            sWebView.goBack(); // goBack()表示返回WebView的上一页面
        }
    }

    private void webGoForward(){

        if (sWebView != null && sWebView.canGoForward()) {
            sWebView.goForward();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus){
//            hideActivityBottomBar(this);
        }
    }

    private static void hideActivityBottomBar(Activity activity){
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

    private class MyBaseWebViewClient extends BaseWebViewClient{

        public MyBaseWebViewClient() {
        }

        public MyBaseWebViewClient(Activity activity) {
            super(activity);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (sWebView != null && sWebView.canGoBack()) {

                preButton.setBackgroundResource(R.drawable.btn_web_pre_highlighted);
                preButton.setClickable(true);

            }else {
                preButton.setBackgroundResource(R.drawable.btn_web_pre_gray);
                preButton.setClickable(false);
            }

            if (sWebView != null && sWebView.canGoForward()) {
                nextButton.setBackgroundResource(R.drawable.btn_web_next_highlighted);
                nextButton.setClickable(true);
            }else {
                nextButton.setBackgroundResource(R.drawable.btn_web_next_gray);
                nextButton.setClickable(false);
            }
        }
    }

    private class MyBaseWebChromeClient extends BaseWebChromeClient{

        private View xprogressvideo;

        public MyBaseWebChromeClient(ProgressBar progressBar, Activity activity) {
            super(progressBar, activity);
        }

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            PL.d("onShowCustomView");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webContentView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            videoFullViewFrameLayout.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoFullViewFrameLayout.setVisibility(View.VISIBLE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {

            PL.d("onHideCustomView");
            if (xCustomView == null)// 不是全屏播放状态
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            videoFullViewFrameLayout.removeView(xCustomView);
            xCustomView = null;
            videoFullViewFrameLayout.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webContentView.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {

            PL.d("getVideoLoadingProgressView");
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(SGameWebViewActivity.this);
                xprogressvideo = inflater.inflate( R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }


    }

}
