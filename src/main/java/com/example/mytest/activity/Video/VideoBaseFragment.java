package com.example.mytest.activity.Video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.util.DensityUtil;
import com.example.mytest.util.ParamsUtil;
import com.example.mytest.util.ScreenSwitchUtils;
import com.example.mytest.util.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by gaopeng on 2016/11/22.
 * 视频播放公共fragment,用于对视屏播放过程得控制
 * GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
 */
public class VideoBaseFragment extends BaseFragment implements VideoControl,
        View.OnTouchListener  {

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
    @BindView(R.id.gesture_iv_player_bright_volume)
    ImageView gestureIvPlayer;
    @BindView(R.id.geture_tv_bright_volume_percentage)
    TextView getureTvPercentage;
    @BindView(R.id.gesture_bright_layout)
    RelativeLayout gestureLayout;
    @BindView(R.id.root_videoview)
    RelativeLayout video_rootView;
    @BindView(R.id.bufferProgressBar)
    ProgressBar bufferProgressBar;
    @BindView(R.id.view_player_tip_text)
    TextView viewPlayerTipText; //无法正常播放时提示文字
    @BindView(R.id.view_player_tv_continue)
    TextView viewPlayerTvContinue;//无法正常播放时，显示重试
    @BindView(R.id.view_player_tip_control)
    LinearLayout viewPlayerTipControl; //无法正常播放时提示UI

    private boolean isAllScreen = false;
    private AudioManager mAudioManager;

    private RelativeLayout relativeLayout;
    private float mBrightness = -1f; // 亮度
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量,3.调节亮度 ，4.暂停和播放
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHT = 3;
    private static final int GESTURE_MODIFY_PLAY = 4;
    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
    private GestureDetector gestureDetector;
    /**
     * 视频窗口的宽和高
     */
    private int playerWidth, playerHeight;
    private VideoBasePlayer videoPlayer;
    private Context context;
    private FrameLayout video_fullScreen;
    private  Activity mActivity;
    private boolean isHuDongVideo = false;
    private VideoHuDong videoHuDong;
    private boolean isSupportGesture = true;//是否至此手势操作，false ：小屏幕的时候不支持，全屏的支持；true : 小屏幕还是全屏都支持
    private String path;
    private ScreenSwitchUtils mScreenSwitchUtils;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d("VideoFragment", "VideoBaseFragment走了onCreateView");
        super.onCreateView(inflater,container,savedInstanceState);
        ButterKnife.bind(this, mView);
        mScreenSwitchUtils = ScreenSwitchUtils.init(mActivity,this);
        initData();
        return mView;
    }

    public boolean getAllScreen(){
        return isAllScreen;
    }

    public String getPath(){
        return path;
    }

    public int getCurrentPosition(){
        Log.d("VideoFragment", "VideoBaseFragment中的进度：" + videoPlayer.getCurrentPosition());
        return videoPlayer.getCurrentPosition();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_base_video;
    }

    private void initData() {
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.video_Screen);
        path = getArguments().getString("url");//播放的路径
        isAllScreen = getArguments().getBoolean("isAllScreen");
        int currentPosition = getArguments().getInt("currentPosition");//播放的时间位置
        video_fullScreen = (FrameLayout) getActivity().findViewById(R.id.video_fullScreen);
        setmMyVideoAllscreenState();
        //音量控制,初始化定义
        if (mAudioManager == null)
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //初始化手势监听
        gestureDetector = new GestureDetector(context, new PlayerGestureListener());
        gestureDetector.setIsLongpressEnabled(true);
        //绑定holder
        SurfaceHolder holder = mMyVideoMain.getHolder();
        videoPlayer = new VideoBasePlayer(context, path, this , currentPosition);
        videoPlayer.setHolder(holder);
        //给视频播放得视图设置触摸监听
        initVideoSize(relativeLayout , false);
        //设定进度条滑动监听和触摸监听
        mMyVideoTimeSeekbar.setOnSeekBarChangeListener(videoPlayer.onSeekBarChangeListener);
        mMyVideoTimeSeekbar.setOnTouchListener(videoPlayer.onSeekBarTouchListener);
        videoPlayer.setChangeVisibility(true);//3秒隐藏菜单栏
    }

    /**
     * 设置全屏按钮显示状态
     */
    private void setmMyVideoAllscreenState() {
        if (isAllScreen) { //全屏
//            ((BaseActivity) context).hideMrlTitle();
            mMyVideoAllscreen.setBackgroundResource(R.mipmap.all_shou);
        } else {//不是全屏
//            ((BaseActivity) context).ShowMrlTitle();
            mMyVideoAllscreen.setBackgroundResource(R.mipmap.all_fang);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.pause();
    }

    @OnClick({R.id.my_video_play, R.id.my_video_allscreen,R.id.view_player_tv_continue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_video_play://播放和暂停
                GESTURE_FLAG = GESTURE_MODIFY_PLAY;//设置状态
                videoPlayer.play();
                break;
            case R.id.my_video_allscreen://全屏按钮
                Log.d("VideoFragment", "VideoBaseFragment通过按钮切换，isAllScreen值：" + isAllScreen);
                bufferProgressBar.setVisibility(View.VISIBLE);//切换全屏时显示转圈
                changeScreen(isAllScreen);
                isAllScreen = !isAllScreen;
                setmMyVideoAllscreenState();
                break;
            case R.id.view_player_tv_continue: //尝试重新播放
                videoPlayer.play();
                break;
        }
    }

    // TODO: 2017/1/17 高鹏 原因 横竖屏重力切换后没有重置videoFragment
    //切换成竖屏
    @Override
    public void changePortrait() {
        Log.d("VideoFragment", "VideoBaseFragment切换成竖屏，isAllScreen值：" + isAllScreen);
        bufferProgressBar.setVisibility(View.VISIBLE);//切换全屏时显示转圈
        changeScreen(isAllScreen);
        isAllScreen = !isAllScreen;
        setmMyVideoAllscreenState();
    }

    //切换成横屏
    @Override
    public void changeLandscape() {
        Log.d("VideoFragment", "VideoBaseFragment切换成横屏，isAllScreen值：" + isAllScreen);
        bufferProgressBar.setVisibility(View.VISIBLE);//切换全屏时显示转圈
        changeScreen(isAllScreen);
        isAllScreen = !isAllScreen;
        setmMyVideoAllscreenState();
    }


    /**
     * 满屏和小屏
     */
    private void changeScreen(boolean isFull) {
        // 切换父容器时，必须先从当前父容器中移除。
        ViewGroup viewGroup = (ViewGroup) video_rootView.getParent();
        if (viewGroup == null) {
            Log.d("VideoFragment", "VideoBaseFragment:viewGroup是null");
            return;
        }
        viewGroup.removeAllViews();
        if (isFull) { //当前状态为全屏，切换为默认大小
            Log.d("VideoFragment", "VideoBaseFragment全屏");
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            relativeLayout.addView(video_rootView);
            relativeLayout.setVisibility(View.VISIBLE);
            video_fullScreen.setVisibility(View.GONE);
            initVideoSize(relativeLayout , false);
        } else {//当前状态非全屏，切换为全屏
            Log.d("VideoFragment", "VideoBaseFragment非全屏");
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
            video_fullScreen.addView(video_rootView);
            video_fullScreen.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            initVideoSize(video_fullScreen,true);
        }
        Log.d("VideoFragment", "VideoBaseFragment:changeScreen走完了");
    }

    private View viewTreeObserver;

    /**
     * @param view 当前播放视频的容器
     */
    private void initVideoSize(View view , boolean isFull) {
        if (isFull) {//全屏
            Log.d("VideoFragment", "VideoBaseFragment进来了");
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.width = ScreenUtil.getScreenWidth(context);
            layoutParams.height = ScreenUtil.getScreenHeight(context);
            view.setLayoutParams(layoutParams);
        }
        /** 获取视频播放窗口的尺寸 */
        viewTreeObserver = view;
        ViewTreeObserver viewObserver = viewTreeObserver.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewTreeObserver.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                playerWidth = viewTreeObserver.getWidth();
                playerHeight = viewTreeObserver.getHeight();
//                Log.d("VideoBaseFragment", "playerWidth=" + playerWidth + ";playerHeight=" + playerHeight);
            }
        });

        viewTreeObserver.setLongClickable(true);
        viewTreeObserver.setOnTouchListener(this);

    }


    /**
     * 设置底部菜单栏的显示状态
     */
    public void setBottomVisibility() {
        if (mMyVideoBottom.getVisibility() == View.VISIBLE) {//隐藏
            mMyVideoBottom.setVisibility(View.INVISIBLE);
            videoPlayer.setChangeVisibility(false);//隐藏时取消3秒
        } else {//显示
            mMyVideoBottom.setVisibility(View.VISIBLE);
            videoPlayer.setChangeVisibility(videoPlayer.isPlay());//3秒隐藏菜单栏
        }
        gestureLayout.setVisibility(videoPlayer.isPlay() ? View.INVISIBLE : View.VISIBLE);//视频暂停时不隐藏中部提示框
    }

    @Override
    public void videoStop() {

    }

    @Override
    public void videoStart() {
        //获取宽高
        int width = ScreenUtil.getScreenWidth(context);
        int height = ScreenUtil.getScreenHeight(context);
        //设置surfaceView的布局参数
        mMyVideoMain.setLayoutParams(new RelativeLayout.LayoutParams(width, height));//设置界面大小，全屏，所以使用全部屏幕大小
    }

    @Override
    public void setCurrentPlayTime(int playTime) {
        mMyVideoTimeNow.setText(ParamsUtil.millsecondsToStr(playTime));
    }

    @Override
    public void setVideoTotaltime(int totaltime) {
        mMyVideoTimeAll.setText(ParamsUtil.millsecondsToStr(totaltime));//总时间
    }

    /**
     * 播放完成
     */
    @Override
    public void playComplete(MediaPlayer mediaPlayer) {
        // 当MediaPlayer播放完成后触发
        setSeekBar(mediaPlayer.getCurrentPosition(),mediaPlayer.getDuration());
        GESTURE_FLAG = GESTURE_MODIFY_PLAY;//重新播放也可以点击中间的按钮
        setPlayBtnState(3);//重新播放
        if (isHuDongVideo && videoHuDong != null){//如果是互动视频
            videoHuDong.playComplete();
        }

    }

    /**
     * 设置是互动视频
     * @param isHuDongVideo 是否是互动视频
     * @param videoHuDong   互动视频的回调函数
     */
    public void setHuDongVideo(boolean isHuDongVideo , VideoHuDong videoHuDong){
        this.isHuDongVideo = isHuDongVideo;
        this.videoHuDong = videoHuDong;
    }


    @Override
    public void setSeekBar(int progress, int duration) {
        if (duration == 0) {
            return;
        }
        mMyVideoTimeSeekbar.setMax(duration);
        mMyVideoTimeSeekbar.setProgress(progress);
    }

    @Override
    public void setPlayBtnState(int btnState) {
        switch (btnState){
            case 1://已暂停
                videoPlayer.setChangeVisibility(false);//取消3秒隐藏菜单栏
                mMyVideoPlay.setBackgroundResource(R.mipmap.btn_play);
                getureTvPercentage.setText("已暂停");
                gestureIvPlayer.setImageResource(R.mipmap.btn_play); //设置显示图片
                gestureIvPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//点击继续播放
                        if (GESTURE_FLAG == GESTURE_MODIFY_PLAY){
                            videoPlayer.play();
//                            ToastUtil.showMessage(context,"继续播放");
                        }
                    }
                });
                gestureLayout.setVisibility(View.VISIBLE);
                break;
            case 2://继续播放
                mMyVideoPlay.setBackgroundResource(R.mipmap.btn_pause);
                gestureLayout.setVisibility(View.GONE);
                if (mMyVideoBottom.getVisibility() == View.VISIBLE)//菜单显示时
                    videoPlayer.setChangeVisibility(true);//3秒隐藏菜单栏
                break;
            case 3://播放完成
                videoPlayer.setChangeVisibility(false);//取消3秒隐藏菜单栏
                mMyVideoPlay.setBackgroundResource(R.mipmap.btn_refresh);
                getureTvPercentage.setText("重新播放");
                gestureIvPlayer.setImageResource(R.mipmap.btn_refresh); //设置显示图片
                gestureIvPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//点击继续播放
                        if (GESTURE_FLAG == GESTURE_MODIFY_PLAY){
                            videoPlayer.play();
//                            ToastUtil.showMessage(context,"重新播放");
                        }
                    }
                });
                gestureLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setProgressBar(boolean isShow) {
        if (isShow) {
            bufferProgressBar.setVisibility(View.VISIBLE);
        } else {
            bufferProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void videoStatusChange(int currentStatus) {
        switch (currentStatus){
            case VideoBasePlayer.STATUS_ERROR:
                isSupportGesture = false;
                viewPlayerTipControl.setVisibility(View.VISIBLE);
                mMyVideoBottom.setVisibility(View.GONE);
                gestureLayout.setVisibility(View.GONE);
                showToast("播放失败");
                break;
            case VideoBasePlayer.STATUS_PLAYING:
                viewPlayerTipControl.setVisibility(View.GONE);
                isSupportGesture = true;
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (GESTURE_FLAG != GESTURE_MODIFY_PLAY) {//调整音量和亮度时
                gestureLayout.setVisibility(View.GONE); //设置调整声音或亮度调整框隐藏
                GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            }
//            changeBottomViewState();//手指离开时处理播放控制界面得显示和隐藏
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onDestroy() {
        Log.d("VideoFragment", "VideoBaseFragment已经销毁");
        super.onDestroy();
        //取消网络监听
        videoPlayer.unregisterNetReceiver();
    }

    @Override
    public void netStateListener(int state) {
        String str = "";
        switch (state) {
            case VideoBasePlayer.Net_State_WIFI:
                str = "指定wifi可用";
                break;
            case VideoBasePlayer.Net_State_NOAVAILABLE:
                str = "当前网络不支持视频播放！";
                break;
        }

        showToast(str);
    }

    /**
     * 视频播放手势控制类
     */
    public class PlayerGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(!isSupportGesture ){
                return false;
            }
            GESTURE_FLAG = GESTURE_MODIFY_PLAY;
            if (videoPlayer.isPlay())
                videoPlayer.pause();
            else {
                showToast("继续播放");
                videoPlayer.play();
            }

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志
            return super.onDown(e);

        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if(!isSupportGesture ){
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            int y = (int) e2.getRawY();

            if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                if(Math.abs(distanceX) >= Math.abs(distanceY)) //横向滑动大于纵向，标记进度调整
                { //abs绝对值
                    GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                }else{
                    // 调整音量或者亮度
                    //滑动调整声音/亮度
                    float percent = deltaY / playerHeight;
                    if (mOldX > playerWidth * 0.5f) {// 音量
                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    } else if (mOldX < playerWidth * 0.5f) {// 亮度
                        GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                    }
                }

                firstScroll = false;// 第一次scroll执行完成，修改标志
            }

//        if (toSeek) {
//            if (!isLive) { //设置视频播放进度
//                onProgressSlide(-deltaX / videoView.getWidth());
//            }
//        } else { //滑动调整声音/亮度
//            float percent = deltaY / videoView.getHeight();
//            if (volumeControl) {
//                onVolumeSlide(percent);
//            } else {
//                onBrightnessSlide(percent);
//            }
//        }

            // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
            if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
                //最大音量
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                //当前音量
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                gestureLayout.setVisibility(View.VISIBLE);
                if (distanceY >= DensityUtil.dip2px(mActivity, STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                    if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                        currentVolume++;
                    }
                    gestureIvPlayer.setImageResource(R.mipmap.souhu_player_volume); //设置显示图片
                } else if (distanceY <= -DensityUtil.dip2px(mActivity, STEP_VOLUME)) {// 音量调小
                    if (currentVolume > 0) {
                        currentVolume--;
                        if (currentVolume == 0) {// 静音，设定静音独有的图片
                            gestureIvPlayer.setImageResource(R.mipmap.souhu_player_silence);
                        }
                    }
                }
                int percentage = (currentVolume * 100) / maxVolume;
                getureTvPercentage.setText(percentage + "%");
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); //直接设置音量
            }
            // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
            else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
                gestureLayout.setVisibility(View.VISIBLE);
                gestureIvPlayer.setImageResource(R.mipmap.souhu_player_bright);
                Window myWindow = mActivity.getWindow();
                if (mBrightness < 0) {
                    mBrightness = myWindow.getAttributes().screenBrightness;
                    if (mBrightness <= 0.00f)
                        mBrightness = 0.50f;
                    if (mBrightness < 0.01f)
                        mBrightness = 0.01f;
                }
                WindowManager.LayoutParams lpa = myWindow.getAttributes();
                lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
                if (lpa.screenBrightness > 1.0f)
                    lpa.screenBrightness = 1.0f;
                else if (lpa.screenBrightness < 0.01f)
                    lpa.screenBrightness = 0.01f;
                myWindow.setAttributes(lpa);
                getureTvPercentage.setText((int) (lpa.screenBrightness * 100) + "%");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(!isSupportGesture ){
                return false;
            }
            setBottomVisibility();
            return super.onSingleTapConfirmed(e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mScreenSwitchUtils.start(mActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
        mScreenSwitchUtils.stop();
    }

}
