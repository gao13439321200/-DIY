package com.example.mytest.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.mytest.activity.Video.SetSystem;

/**
 * Created by GaoPeng on 2016/11/24.
 * 自定义view，用于控制音量和亮度
 */

public class MyVideoView extends View {
    private SetSystem mSetSystem;
    private float lastY;
    private int num = 0;
    private String type = "";

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMyView(SetSystem setSystem, String type) {
        this.mSetSystem = setSystem;
        this.type = type;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String DEBUG_TAG = "ABC";
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN)://手指按下
                Log.d(DEBUG_TAG, "Action was DOWN");
                lastY = event.getY();//获取按下时的坐标
                num = 2;
                switch (type) {
                    case "light"://亮度
                        mSetSystem.setSeekAndText(View.VISIBLE, "亮度：");
                        break;
                    case "sound"://音量
                        mSetSystem.setSeekAndText(View.VISIBLE, "音量：");
                        break;
                }
                return true;
            case (MotionEvent.ACTION_MOVE)://手指移动
                Log.d(DEBUG_TAG, "滑动event.getY():" + event.getY());
                if (num == 3) {
                    float Y = event.getY();
                    switch (type) {
                        case "light"://亮度
                            mSetSystem.setLight((int) (Y - lastY));
                            break;
                        case "sound"://音量
                            mSetSystem.setSound((int) (Y - lastY));
                            break;
                    }
                    lastY = Y;
                    num = 0;
                } else {
                    num++;
                }
                return true;
            case (MotionEvent.ACTION_UP)://手指抬起
                Log.d(DEBUG_TAG, "Action was UP");
                switch (type) {
                    case "light"://亮度
                        mSetSystem.setSeekAndText(View.INVISIBLE, "亮度：");
                        break;
                    case "sound"://音量
                        mSetSystem.setSeekAndText(View.INVISIBLE, "音量：");
                        break;
                }
                return true;
            case (MotionEvent.ACTION_CANCEL)://取消
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
