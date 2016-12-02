package com.example.mytest.activity.Video;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.util.ParamsUtil;
import com.example.mytest.util.ScreenUtil;
import com.example.mytest.view.MyVideoView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaopeng on 2016/11/22.
 * 视频播放公共fragment
 */
public class MyVideoFragment extends BaseFragment implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, SetSystem {
    @BindView(R.id.my_video_play)
    ImageButton mMyVideoPlay;
    @BindView(R.id.my_video_time_seekbar)
    SeekBar mMyVideoTimeSeekbar;
    @BindView(R.id.my_video_time_now)
    TextView mMyVideoTimeNow;
    @BindView(R.id.my_video_time_all)
    TextView mMyVideoTimeAll;
    @BindView(R.id.my_video_allscreen)
    ImageButton mMyVideoAllscreen;
    @BindView(R.id.my_video_bottom)
    RelativeLayout mMyVideoBottom;
    @BindView(R.id.my_video_main)
    SurfaceView mMyVideoMain;
    @BindView(R.id.my_video_light)
    MyVideoView mMyVideoLight;
    @BindView(R.id.my_video_sound)
    MyVideoView mMyVideoSound;
    @BindView(R.id.my_video_light_seekbar)
    SeekBar mMyVideoLightSeekbar;
    @BindView(R.id.my_video_light_textview)
    TextView mMyVideoLightTextview;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int nowTime;
    private String path;
    private boolean isAllScreen = false;
    private AudioManager mAudioManager;
    private AssetFileDescriptor fileDescriptor = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mView);
        initData();
        return mView;
    }

    @Override
    public int getLayoutID() {
        return R.layout.my_video;
    }

    private void initData() {
        try {//测试
            fileDescriptor = getActivity().getAssets().openFd("test.mp4");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        isAllScreen = getArguments().getBoolean("isAllScreen");//是否是全屏模式
        nowTime = getArguments().getInt("time");//播放的当前时间
        Log.d("MyVideoFragment", "nowtime" + nowTime);
        if (isAllScreen)//全屏
            mMyVideoAllscreen.setBackgroundResource(R.mipmap.btn_shou);
        else//不是全屏
            mMyVideoAllscreen.setBackgroundResource(R.mipmap.btn_fang);

        path = getArguments().getString("url");//播放的路径

        //设置定时器，用于更新进度条
        mTimer = new Timer();

        //设定进度条滑动监听和触摸监听
        mMyVideoTimeSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mMyVideoTimeSeekbar.setOnTouchListener(onSeekBarTouchListener);
        //绑定回调函数
        mMyVideoSound.setMyView(this, "sound");//音量
        mMyVideoLight.setMyView(this, "light");//亮度

        //音量控制,初始化定义
        if (mAudioManager == null)
            mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onResume() {
        Log.d("MyVideoFragment", "onResume");
        super.onResume();
        if (player == null) {
            //实例化MediaPlayer
            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnInfoListener(this);
            player.setOnPreparedListener(this);
            player.setOnSeekCompleteListener(this);
            player.setOnVideoSizeChangedListener(this);
        }
        if (holder == null) {
            //给SurfaceView添加CallBack监听
            holder = mMyVideoMain.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        try {
//            player.setDataSource(path); //然后指定需要播放文件的路径，初始化MediaPlayer
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength()); // 设置播放路径
            //player准备播放
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //主界面点击事件，用于隐藏或实现功能菜单
    @OnClick(R.id.my_video_ll)
    void onMainClick() {
        if (mMyVideoBottom.getVisibility() == View.VISIBLE)
            mMyVideoBottom.setVisibility(View.INVISIBLE);
        else
            mMyVideoBottom.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.my_video_play, R.id.my_video_allscreen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_video_play://播放和暂停
                if (player.isPlaying()) {
                    player.pause();
                    mMyVideoPlay.setBackgroundResource(R.mipmap.btn_play);
                } else {
                    player.start();
                    mMyVideoPlay.setBackgroundResource(R.mipmap.btn_pause);
                }
                break;
            case R.id.my_video_allscreen://全屏按钮
                player.pause();
                if (!isAllScreen) {//非全屏
                    mTimerTask.cancel();
                    Intent intent = new Intent(getActivity(), VideoAllScreenActivity.class);
                    intent.putExtra("time", nowTime);
                    intent.putExtra("url", path);
                    startActivity(intent);
                } else {//全屏
                    getActivity().finish();
                }
                break;
        }
    }

    //用于更新进度条
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("MyVideoFragment", "handleMessage");
            nowTime = player.getCurrentPosition();//当前时间
            mMyVideoTimeNow.setText(ParamsUtil.millsecondsToStr(nowTime));
            mMyVideoTimeSeekbar.setProgress((mMyVideoTimeSeekbar.getMax()) * nowTime / player.getDuration());
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("MyVideoFragment", "surfaceCreated");
        //将MediaPlayer和surfaceview进行绑定，指定在此surfaceview中播放
        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("MyVideoFragment", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("MyVideoFragment", "surfaceDestroyed");
        player.stop();
        if (player != null)
            player = null;
        mTimerTask.cancel();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // 当MediaPlayer播放完成后触发
        Log.d("MyVideoFragment", "onCompletion");
        mMyVideoTimeSeekbar.setProgress(100);
        showToast("放完啦");
        mMyVideoPlay.setBackgroundResource(R.mipmap.btn_play);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int whatError, int i1) {
        switch (whatError) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                showToast("又错了");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                showToast("总是错");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int whatInfo, int i1) {
        // 当一些特定信息出现或者警告时触发
        switch (whatInfo) {
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                showToast("这错了");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                showToast("那错了");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                showToast("怎么还有错");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                showToast("全是错");
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("MyVideoFragment", "onPrepared");
        //获取宽高
        int width = ScreenUtil.getScreenWidth(getActivity());
        int height = ScreenUtil.getScreenHeight(getActivity());
        //设置surfaceView的布局参数
        mMyVideoMain.setLayoutParams(new RelativeLayout.LayoutParams(width, height));//设置界面大小，全屏，所以使用全部屏幕大小
        Log.d("MyVideoFragment", "nowTime:" + nowTime);
        Log.d("MyVideoFragment", "mMyVideoTimeSeekbar.getMax():" + mMyVideoTimeSeekbar.getMax());
        Log.d("MyVideoFragment", "player.getDuration():" + player.getDuration());
        mMyVideoTimeSeekbar.setProgress(mMyVideoTimeSeekbar.getMax() * nowTime / player.getDuration());//设置进度条进度
        player.seekTo(nowTime);//设置视频进度
        mMyVideoTimeAll.setText(ParamsUtil.millsecondsToStr(player.getDuration()));//当前时间
        mMyVideoTimeNow.setText(ParamsUtil.millsecondsToStr(nowTime));//总时间
        //然后开始播放视频
        player.start();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);

    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        // seek操作完成时触发
        Log.d("MyVideoFragment", "onSeekComplete");

    }

    /**
     * 进度条滑动监听
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {//进度条滑动结束
            Log.d("MyVideoFragment", "进度:" + progress);
            player.seekTo(progress);//更新视频
            player.start();
            mMyVideoPlay.setBackgroundResource(R.mipmap.btn_pause);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {//进度条开始滑动
            player.pause();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//滑动条正在滑动
            Log.d("MyVideoFragment", "进度移动:" + progress);
            Log.d("MyVideoFragment", "进度总时间：" + player.getDuration());
            Log.d("MyVideoFragment", "进度总长度：" + seekBar.getMax());
            this.progress = progress * player.getDuration() / seekBar.getMax();
            if (this.progress < 0) {
                this.progress = this.progress * (-1);
            }
            mMyVideoTimeNow.setText(ParamsUtil.millsecondsToStr(this.progress));
        }
    };

    /**
     * 进度条触摸监听
     */
    private SeekBar.OnTouchListener onSeekBarTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.getParent().requestDisallowInterceptTouchEvent(true);//避免水平滑动拦截seek的滑动请求
            return false;
        }
    };


    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
        // 当video大小改变时触发
        //这个方法在设置player的source后至少触发一次
        Log.d("MyVideoFragment", "onVideoSizeChanged");
    }

    @Override
    public void onPause() {
        Log.d("MyVideoFragment", "onPause");
//        player.pause();
        super.onPause();
    }

    /**
     * 调节音量
     *
     * @param num 音量的变化值（手指在屏幕的移动量）
     */
    @Override
    public void setSound(int num) {
        Log.d("CCCC", "setSound: " + num);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("MyVideoFragment", "maxVolume:" + maxVolume);
        Log.d("MyVideoFragment", "currentVolume:" + currentVolume);


        if (num > 0) {//降低音量，调出系统音量控制
            currentVolume = currentVolume - 1 < 0 ? 0 : (currentVolume - 1);
        } else {//增加音量，调出系统音量控制
//            currentVolume = currentVolume == 0 ? 1 : currentVolume;
            currentVolume = currentVolume + 1 > 15 ? 15 : (currentVolume + 1);
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); //直接设置音量
        mMyVideoLightSeekbar.setProgress(mMyVideoLightSeekbar.getMax() * currentVolume / maxVolume);
    }

    /**
     * 调节亮度
     *
     * @param num 亮度的变化值（手指在屏幕的移动量）
     */
    @Override
    public void setLight(int num) {
        // 设置系统亮度模式
        try {
            int mode = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            switch (mode) {
                case Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC://自动亮度
                    Log.d(TAG, "setLight: 自动亮度无法调节");
                    break;
                case Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL://手动亮度
                    int light = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);// 获取系统亮度
                    if (num > 0) {//降低亮度
                        light = light - 10 < 0 ? 0 : light - 10;
                    } else {//增加亮度
                        light = light + 10 > 255 ? 255 : light + 10;
                    }
                    Log.d("MyVideoFragment", "light:" + light);
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, light);// 设置系统亮度
                    mMyVideoLightSeekbar.setProgress(light);
                    break;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSeekAndText(int visibility, String type) {
        mMyVideoLightTextview.setText(type);
        mMyVideoLightTextview.setVisibility(visibility);
        mMyVideoLightSeekbar.setVisibility(visibility);
        switch (type) {
            case "亮度：":
                try {
                    int light = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);// 获取系统亮度
                    mMyVideoLightSeekbar.setProgress(light);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "音量：":
                //当前音量
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //最大音量
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                mMyVideoLightSeekbar.setProgress(mMyVideoLightSeekbar.getMax() * currentVolume / maxVolume);
                break;
        }
    }

    /**
     * 切换视频，传递的参数也可以是路径
     *
     * @param fileDescriptor 读取项目中的文件
     */
    public void changeVideo(AssetFileDescriptor fileDescriptor) {
        player.pause();
        player.reset();
        mTimerTask.cancel();
        try {
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength()); // 设置播放路径
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setDisplay(holder);
        nowTime = 0;
        player.prepareAsync();
        mMyVideoPlay.setBackgroundResource(R.mipmap.btn_pause);
//        player.start();
//        mMyVideoMain.setVisibility(View.INVISIBLE);
//        mMyVideoMain.setVisibility(View.SIBLE);
    }

}
