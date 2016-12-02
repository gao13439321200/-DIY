package com.example.mytest.activity.Main;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.mytest.R;
import com.example.mytest.util.ScreenUtil;

public class BaseMenu extends HorizontalScrollView {
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 菜单的宽度
     */
    private int mMenuWidth;
    boolean isOpen;
    private boolean once;

    public BaseMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BaseMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScreenWidth = ScreenUtil.getScreenWidth(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyle, 0);
//        int n = a.getIndexCount();
//        for (int i = 0; i < n; i++) {
//            int attr = a.getIndex(i);
//            switch (attr) {
//                case R.styleable.SlidingMenu_rightPadding:
//                    break;
//            }
//        }
        a.recycle();
    }

    public BaseMenu(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 显示的设置一个宽度
         */
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup subject = (ViewGroup) wrapper.getChildAt(0);//第一个子布局
            ViewGroup main = (ViewGroup) wrapper.getChildAt(1);//第二个子布局
            ViewGroup school = (ViewGroup) wrapper.getChildAt(2);//第三个子布局
            mMenuWidth = mScreenWidth/ 2;
            subject.getLayoutParams().width = mMenuWidth;
            main.getLayoutParams().width = mScreenWidth;
            school.getLayoutParams().width = mMenuWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch (action) {
//            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
//            case MotionEvent.ACTION_UP:
//                int scrollX = getScrollX();
//                if (scrollX > mHalfMenuWidth) {
//                    this.smoothScrollTo(mMenuWidth, 0);
//                    isOpen = false;
//                } else {
//                    this.smoothScrollTo(0, 0);
//                    isOpen = true;
//                }
//                return true;
//        }
//        return super.onTouchEvent(ev);
        return false;
    }

    /**
     * 打开菜单
     */
    private void openSubjectMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    void openSchoolMenu(){
        if (isOpen)
            return;
        this.smoothScrollTo(mScreenWidth, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换科目菜单状态
     */
    void toggle_subject() {
        if (isOpen) {
            closeMenu();
        } else {
            openSubjectMenu();
        }
    }
    /**
     * 切换学校菜单状态
     */
    void toggle_school() {
        if (isOpen) {
            closeMenu();
        } else {
            openSchoolMenu();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
