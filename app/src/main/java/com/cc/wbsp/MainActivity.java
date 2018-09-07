package com.cc.wbsp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.cc.wbsp.ijk.VideoActivity;
import com.cc.wbsp.webvideo.FloatVideoPlayer;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }

    Button startAct;
    FloatVideoPlayer f_Player;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        startAct = (Button) findViewById(R.id.start_act);

        startAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.intentTo(MainActivity.this, "http://vod.cntv.lxdns.com/flash/mp4video61/TMS/2017/08/17/63bf8bcc706a46b58ee5c821edaee661_h264818000nero_aac32-5.mp4","test ijk");
            }
        });

        findViewById(R.id.start_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://v.baidu.com/channel/amuse";
                if (!TextUtils.isEmpty(url)) {
                    if (f_Player != null && !f_Player.isDestroy()) {
                        f_Player.destroy(MainActivity.this);
                        f_Player = null;
                    }
                    f_Player = new FloatVideoPlayer();
                    f_Player.create(MainActivity.this,url);
                }

            }
        });


    }


}
