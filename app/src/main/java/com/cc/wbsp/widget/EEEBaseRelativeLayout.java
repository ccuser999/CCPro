package com.cc.wbsp.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.core.base.utils.PL;

/**
 * Created by Efun on 2016/11/3.
 */

public class EEEBaseRelativeLayout extends RelativeLayout {


    public ConfigurationChangedListener getChangedListener() {
        return changedListener;
    }

    public void setChangedListener(ConfigurationChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    ConfigurationChangedListener changedListener;

    public EEEBaseRelativeLayout(Context context) {
        super(context);
    }

    public EEEBaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EEEBaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PL.d("FrameLayout onConfigurationChanged");
        if (changedListener != null){
            changedListener.onConfigurationChanged(newConfig);
        }
    }

    public interface ConfigurationChangedListener{
        void onConfigurationChanged(Configuration newConfig);
    }
}
