package com.example.mytest.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.activity.Main.BaseMenu;
import com.example.mytest.data.BaseView;
import com.example.mytest.data.dagger.BaseComponent;
import com.example.mytest.data.dagger.BaseModul;
import com.example.mytest.data.dagger.DaggerBaseComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaopeng on 2016/11/4.
 * 基础Acitivty//test
 */
public abstract class BaseActivity extends Activity implements BaseView {
    @BindView(R.id.base_back)//返回
    protected Button mBaseBack;
    @BindView(R.id.base_title)//标题
            TextView mBaseTitle;
    @BindView(R.id.base_rlTitle)//标题的布局
            RelativeLayout mBaseRlTitle;
    @BindView(R.id.base_rlMain)//主布局
            RelativeLayout mBaseRlMain;
    @BindView(R.id.base_right)//最右边的按钮
    protected Button mBaseRight;
    @BindView(R.id.base_right2)//最右边的第二个按钮
    protected Button mBaseRight2;
    @BindView(R.id.base_Menu)//科目按钮
    protected BaseMenu mBaseMenu;
    @BindView(R.id.subject_tv_book)//选择教材
    protected TextView mSubjectTvBook;
    @BindView(R.id.subject_rg_book)//教材列表
    protected RadioGroup mSubjectRgBook;
    @BindView(R.id.subject_rg_ce)//书列表
    protected RadioGroup mSubjectRgCe;
    @BindView(R.id.subject_biology)//生物或是科学
    protected RadioButton mSubjectBiology;
    @BindView(R.id.subject_math)//数学
    protected RadioButton mSubjectMath;
    @BindView(R.id.subject_sv_book)//教材的滚动菜单
    protected ScrollView mSubjectSvBook;
    @BindView(R.id.subject_sv_ce)//书的滚动菜单
    protected ScrollView mSubjectSvCe;
    @BindView(R.id.schoollist_rg)//学校的滚动菜单
    protected RadioGroup mSchoolListRg;

    public BaseComponent mBaseComponent;
    public String subjectID = "02";//课程id，默认是数学
    protected final String TAG = "MYLOG";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseSetContentView();//加载布局
        mBaseComponent = DaggerBaseComponent.builder().baseModul(new BaseModul(this, this)).build();
    }

    /**
     * 获取子类的布局
     *
     * @return 布局id
     */
    public abstract int getLayoutID();

    /**
     * 加载标题布局和主界面布局
     */
    private void BaseSetContentView() {
        setContentView(R.layout.activity_base);//加载标题布局
        mBaseRlMain = (RelativeLayout) findViewById(R.id.base_rlMain);//需要手动找此控件，否则会报空指针，因为没有绑定ButterKnife
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(getLayoutID(), null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        if (view != null)
            mBaseRlMain.addView(view, layoutParams);
        ButterKnife.bind(this);//绑定ButterKnife
    }


    /**
     * 弹窗提示
     *
     * @param message 提示信息
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        mBaseTitle.setText(title);
    }

    /**
     * 隐藏标题栏
     */
    public void hideTitle() {
        mBaseRlTitle.setVisibility(View.GONE);
    }
    /**
     * 隐藏右上角按钮
     */
    protected void hideRight() {
        mBaseRight.setVisibility(View.GONE);
    }


    /**
     * 设置右上角的按钮
     *
     * @param btnText 自定义文字
     */
    protected void setRightBtn(String btnText) {
        mBaseRight.setVisibility(View.VISIBLE);
        mBaseRight.setText(btnText);
    }

    /**
     * 设置右上角的第二个按钮
     *
     * @param btnText 自定义文字
     */
    protected void setRight2Btn(String btnText) {
        mBaseRight2.setVisibility(View.VISIBLE);
        mBaseRight2.setText(btnText);
    }

    /**
     * 设置左上角的按钮
     *
     * @param btnText 自定义文字
     */
    protected void setLeftBtn(String btnText) {
        mBaseBack.setVisibility(View.VISIBLE);
        mBaseBack.setText(btnText);
    }

    @OnClick(R.id.base_back)
    void onBackClick() {
        switch (mBaseBack.getText().toString()) {
            case "返回":
                this.finish();
                break;
        }
    }


    /**
     * 加载碎片
     *
     * @param layoutID 加载fragment的容器id
     * @param fragment 具体的fragment
     */
    protected void changeFragment(int layoutID, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutID, fragment);
        fragmentTransaction.commit();
    }


}
