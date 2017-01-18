package com.example.mytest.activity.Video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.SeekBar;

import com.example.mytest.util.NetUtil;
import com.example.mytest.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李均 on 2017/1/6.
 * 视频播放
 */

public class VideoBasePlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.
                OnSeekCompleteListener, MediaPlayer.OnBufferingUpdateListener,SurfaceHolder.Callback{

    private final String TAG = "videoPlayer";
    private Context mContext;
    private String videoPath;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer player;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int currentTime; //当前播放时长
    private int videoDuration;//视频持续时长
    private int bufferPercent ;// 缓存百分比
    private int videoBufferDuration;//缓存时长
    private VideoControl videoControl;
    private boolean isSurfaceDestroy = false;
    private boolean isPrepared = false;
    private int currentPosition = 0;
    private boolean isShow = true;//加载的旋转框 默认显示
    private boolean changeVisibility = false;//是否隐藏菜单栏
    private int overTime = 0;//菜单栏自动隐藏的时间，当前时间+3秒
    private NetChangeReceiver netChangeReceiver;
    /**
     * 标识当前网络状态为wifi
     *  {@link  VideoBaseFragment#netStateListener(int)}.
     *  {@link  NetChangeReceiver#onReceive(Context, Intent)}
     */
    public static final int Net_State_WIFI = 1;
    /**
     * 标识当前网络状态为 手机移动数据流量
     */
    public static final int Net_State_MOBILE = 2;
    /**
     * 标识当前 网络断开
     */
    public static final int Net_State_DISCONNECT = 3;
    /**
     * 标识当前网路不可用
     */
    public static final int Net_State_NOAVAILABLE = 4;
    public static final int STATUS_ERROR = 100;
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_LOADING = 5;
    public static final int STATUS_PLAYING = 6;
    public static final int STATUS_PAUSE = 7;
    public static final int STATUS_COMPLETED = 8;
    public static final int STATUS_PREPARED = 9;

    private boolean isNetWork = true;

    VideoBasePlayer(Context context, String path, VideoControl control , int currentPosition) {
        this.mContext = context;
        this.videoPath = path;
        this.videoControl = control;
        this.currentPosition = currentPosition;
        Logger.init(TAG);
    }

    void setHolder(SurfaceHolder holder) {
        this.surfaceHolder = holder;
        initPlayer();
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        if (player == null) {
            //实例化MediaPlayer
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnInfoListener(this);
            player.setOnPreparedListener(this);
            player.setOnSeekCompleteListener(this);
            player.setOnBufferingUpdateListener(this);
        }
        if (surfaceHolder != null) {
            //给SurfaceView添加CallBack监听
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        try {
            player.setDataSource(videoPath); //然后指定需要播放文件的路径，初始化MediaPlayer
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //用于更新进度条
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(player == null )
                return;

            if (player.isPlaying()) {//防止player为null崩溃，当player在播放的状态时再进行进度的变换
                currentTime = player.getCurrentPosition();//当前时间
                videoControl.setCurrentPlayTime(currentTime);
                videoControl.setSeekBar(currentTime, videoDuration);
            }
            if (changeVisibility) {//开始计时
                if (overTime == 3)
                    videoControl.setBottomVisibility();//隐藏菜单栏
                else
                    overTime++;
            }
        }
    };

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Utils.muteAudioFocus(mContext,true);//暂停后台音乐播放
        isPrepared = true;
        isShow = false;
        videoDuration = player.getDuration();
        videoControl.setProgressBar(isShow);
        videoControl.setVideoTotaltime(videoDuration);
        //设置定时器，用于更新进度条
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
        //然后开始播放视频
        play();
//        currentPosition = 0;
        //注册网络状态监听
        registerNetReceiver();
    }

    //开始播放/暂停播放
    void play() {
        if(currentPosition > 0){
            player.seekTo(currentPosition);
        }
        if (player.isPlaying()) {
            player.pause();
            videoControl.setPlayBtnState(1);//已暂停
        } else {
            if(!isNetWork && !isPrepared){
                videoControl.videoStatusChange(STATUS_ERROR);
            }else {
                player.start();
                videoControl.videoStatusChange(STATUS_PLAYING);
                videoControl.setPlayBtnState(2);//继续播放
            }

        }
    }

    /**
     * 停止/释放播放器
     * Started或者Paused状态下均可调用stop()停止MediaPlayer，而处于Stop状态的MediaPlayer要想重新播放
     * ，需要通过prepareAsync()和prepare()回到先前的Prepared状态重新开始才可以。
     * stop 之后调用 release (),需要重新初始化mediaplayer 才能播放
     */
    public void stop() {
        mTimer.cancel();
        isPrepared = false;
        player.stop();
        player = null;
//        player.release();
    }

    /**
     * 暂停
     */
    public void pause() {
        Log.d("VideoBasePlayer", "暂停");
        player.pause();
        videoControl.setPlayBtnState(1);//已暂停
        currentPosition =player.getCurrentPosition();
    }

    /**
     * 获取视频的播放状态
     */
    public boolean isPlay() {
        return player.isPlaying();
    }

    /**
     * 设置是否开始计时（3秒隐藏菜单栏）
     *
     * @param visibility 状态
     */
    public void setChangeVisibility(boolean visibility) {
        this.changeVisibility = visibility;
        overTime = 0;//重置3秒限制时间
    }

    /**
     * 适用以下情况：
     * 1：onError  播放出现未知错误时。{@link  #onError(MediaPlayer, int, int)}.
     * 2：surfaceDestroyed surfaceHolder销毁时{@link #surfaceDestroyed(SurfaceHolder)}
     */
    private void resetPlayer(){
        //终止计时器，重置player
        if (mTimer != null)
            mTimer.cancel();
        isPrepared = false;
        isSurfaceDestroy = true;
        player.stop();
        player.reset();
    }

    /**
     * 适用以下情况：
     * 1：NetChangeReceiver 网络重新连接时。{@link  NetChangeReceiver#onReceive(Context, Intent)}.
     * 2：surfaceCreated()  surfaceHolder创建时 销毁时{@link #surfaceCreated(SurfaceHolder)}
     */
    private void resetPrepared(){
        if (!isPrepared && isSurfaceDestroy) {
            try {
                player.setDataSource(videoPath);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "SurfaceHolder--Created");
        //将MediaPlayer和surfaceview进行绑定，指定在此surfaceview中播放
        try {
            player.setDisplay(surfaceHolder);
            resetPrepared();
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "SurfaceHolder--Changed--" + "width= " + width + "; height= " + height);
        holder.setFixedSize(width, height);
        player.setDisplay(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "SurfaceHolder--Destroyed");
        //播放出现问题时，标记当前播放位置，重置播放器
        currentPosition = currentTime;
        resetPlayer();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        // seek操作完成时触发
        Log.d("MyVideoFragment", "onSeekComplete");

    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, "onBufferingUpdate: 缓存百分比 "+percent+"%");
        Log.d(TAG, "onBufferingUpdate: currentTime= "+currentTime+";Duration= "+videoDuration);
        bufferPercent = percent;
        if(player.isPlaying()&&isNetWork){
            videoBufferDuration = player.getDuration()*bufferPercent/100;
        }
        Log.d(TAG, "onBufferingUpdate: BufferDuration= "+videoBufferDuration);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // 当MediaPlayer播放完成后触发
        currentPosition = 0;
        videoControl.playComplete(mediaPlayer);
    }

    /**
     * 进度条滑动监听
     */
    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {//进度条滑动结束
            Log.d(TAG, "进度:" + progress);
            player.seekTo(progress);//更新视频
            player.start();
            if (progress != 100)
                videoControl.setPlayBtnState(2);//继续播放
            else
                videoControl.setPlayBtnState(3);//播放完成
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {//进度条开始滑动
            player.pause();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//滑动条正在滑动
//            Log.d(TAG, "进度移动:" + progress);
//            Log.d(TAG, "进度总时间：" + player.getDuration());
//            Log.d(TAG, "进度总长度：" + seekBar.getMax());
            this.progress = progress;
            videoControl.setCurrentPlayTime(this.progress);
        }
    };

    /**
     * 进度条触摸监听
     */
    public SeekBar.OnTouchListener onSeekBarTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.getParent().requestDisallowInterceptTouchEvent(true);//避免水平滑动拦截seek的滑动请求
            return false;
        }
    };

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int whatError, int i1) {
        Log.e(TAG, "onError:" + whatError);
        if(!isNetWork){
            //播放出现问题时，标记当前播放位置，重置播放器
            currentPosition = videoBufferDuration;
            videoControl.videoStatusChange(STATUS_ERROR);
            resetPlayer();
        }
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int whatInfo, int i1) {
        // 当一些特定信息出现或者警告时触发
        switch (whatInfo) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                videoControl.setProgressBar(true);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                videoControl.setProgressBar(false);
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                statusChange(STATUS_PLAYING);
                break;
        }
        return false;
    }
    /**
     * 注册网络监听器
     */
    private void registerNetReceiver() {
        if (netChangeReceiver == null) {
            IntentFilter filter = new IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION);
            netChangeReceiver = new NetChangeReceiver();
            mContext.registerReceiver(netChangeReceiver, filter);
        }
    }

    /**
     * 销毁网络监听器
     */
    public void unregisterNetReceiver() {
        if (netChangeReceiver != null) {
            mContext.unregisterReceiver(netChangeReceiver);
            netChangeReceiver = null;
        }
    }


    /** 网络变化监听
     * 私有云状况下只有wifi可以获取数据。
     * 其他情况都设定网络无法使用
     */
    public class NetChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (videoControl == null) {
                return;
            }
            if (NetUtil.getNetworkType(context) == 3) {// 网络是WIFI
                isNetWork = true;
                resetPrepared();
                //  网络状态改变之后重新连接，开始播放
//                videoControl.netStateListener(Net_State_WIFI);
            } else {
                //网络无法使用时不做处理，继续使用缓存播放。
                isNetWork = false;
                isPrepared = false;
//                videoControl.netStateListener(Net_State_NOAVAILABLE); //网络不可用
            }
        }
    }

    int getCurrentPosition(){
        Log.d("VideoBasePlayer", "player里边的进度：" + currentTime);
        return currentTime;
    }


}
