package com.example.mytest.activity.Video;

import android.media.MediaPlayer;

/**
 * Created by 李均 on 2017/1/6.
 */

public interface VideoControl {
    void videoStop();
    void videoStart();
    void setCurrentPlayTime(int playTime);//设置当前播放时长
    void setVideoTotaltime(int totaltime);//设置总播放时长
    void playComplete(MediaPlayer mediaPlayer);//播放完成
    void setBottomVisibility();//设置底部菜单栏

    /**设置进度条
    * @param progress 当前进度
     * @param duration 总时长
     */
    void setSeekBar(int progress, int duration);
    void setPlayBtnState(int btnState);//设置播放按钮背景 1：已暂停  2：继续播放  3：播放完成
    void setProgressBar(boolean isShow);

    /**
     * 处理视频播放过程中，状态变时view 变化
     * @param currentStatus 当前所处状态
     */
    void videoStatusChange(int currentStatus);

    // wifi / 手机 / 网络断开 / 网路不可用
    void netStateListener(int status);

    void changePortrait();//竖屏的回调函数
    void changeLandscape();//横屏的回调函数

}
