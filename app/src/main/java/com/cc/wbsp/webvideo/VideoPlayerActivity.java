package com.cc.wbsp.webvideo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;


public class VideoPlayerActivity extends Activity {


    FrameLayout playerFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerFrameLayout = new FrameLayout(this);
        playerFrameLayout.setBackgroundColor(Color.BLACK);
        playerFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(playerFrameLayout);
        FloatVideoPlayer.playingViewActivity = this;
        View playingView = FloatVideoPlayer.playingView;
        if (playingView != null){
            playerFrameLayout.removeAllViews();
            playerFrameLayout.addView(playingView ,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }else{
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerFrameLayout.removeAllViews();
    }
}
