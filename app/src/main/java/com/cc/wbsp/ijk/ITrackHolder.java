package com.cc.wbsp.ijk;

import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by gan on 2018/5/10.
 */

public interface ITrackHolder {

    ITrackInfo[] getTrackInfo();
    int getSelectedTrack(int trackType);
    void selectTrack(int stream);
    void deselectTrack(int stream);
}
