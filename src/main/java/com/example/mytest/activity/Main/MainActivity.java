package com.example.mytest.activity.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Main.fragment.ExamFragment;
import com.example.mytest.activity.Main.fragment.OtherFragment;
import com.example.mytest.activity.Main.fragment.VideoFragment;
import com.example.mytest.activity.Main.presenter.MainPresenter;
import com.example.mytest.activity.Main.view.MainView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.BookGson;
import com.example.mytest.dto.CeGson;
import com.example.mytest.dto.JiaoCai;
import com.example.mytest.dto.School;
import com.example.mytest.dto.UserInfoGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.SchoolUtil;
import com.example.mytest.util.SubjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaopeng on 2016/11/8.
 * 主页面
 */
public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.main_rbTest)
    RadioButton mMainRbTest;
    @Inject
    MainPresenter mMainPresenter;
    private SubjectUtil mSubjectUtil;
    private SchoolUtil mSchoolUtil;
    private Map<String, List<BookGson>> mBookList = new HashMap<>();
    private Map<String, JiaoCai> mJiaocaiList = new HashMap<>();
    private List<School> mSchoolList = new ArrayList<>();
    private RadioGroup.LayoutParams mLayoutParams;
    private String schoolID = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("练习与PK");
        setLeftBtn("科目");
        mBaseComponent.inject(this);
        initData();
    }

    private void initData() {
        mLayoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        mLayoutParams.setMargins(0, 10, 0, 10);//设置边距
        mSubjectUtil = new SubjectUtil();
        mSchoolUtil = new SchoolUtil();
        mMainRbTest.setChecked(true);//设置默认选中
        mSubjectMath.setChecked(true);//设置默认选中
        mMainPresenter.getJiaoCai();//获取教材信息
        mMainPresenter.getUserPower();//获取个人教材
        mMainPresenter.getUserInfo();//获取个人信息
        mMainPresenter.getSchoolList();//获取学校列表
        mSubjectBiology.setTag("2".equals(Const.XUEDUAN) ? "06" : "05");
        mSubjectBiology.setText("2".equals(Const.XUEDUAN) ? "科学" : "生物");
        //书的选中事件
        mSubjectRgBook.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = (RadioButton) findViewById(i);
                setCeRaioButton(Integer.parseInt(button.getTag() + ""));//设置书的列表
                String id = mBookList.get(subjectID).get(Integer.parseInt(button.getTag() + "")).getId();
                mJiaocaiList.get(subjectID).setBookID(id);//存储教材的id
            }
        });
        //册的选中事件
        mSubjectRgCe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = (RadioButton) findViewById(i);
                mJiaocaiList.get(subjectID).setCeID(button.getTag() + "");
            }
        });
        //学校的选中事件
        mSchoolListRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(checkedId);
                if (!button.getTag().equals(schoolID)) {
                    schoolID = (String) button.getTag();
                    mSchoolUtil.sendMessage(schoolID);//学校的点击事件发送消息
                }
            }
        });
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    /**
     * 科目点击事件
     */
    public void onSubjectClick(View view) {
        if (!view.getTag().equals(subjectID)) {
            subjectID = (String) view.getTag();
            // TODO: 2016/11/18 高鹏 使用观察者模式在视频页判断subjectID是否改变了
            mSubjectUtil.sendMsg(subjectID);
            if ("全部保存".equals(mSubjectTvBook.getText().toString().trim())) {
                setBookRadioButton();
            }
        }
    }

    /**
     * 选择教材点击事件
     */
    @OnClick(R.id.subject_tv_book)
    public void onClick() {
        switch (mSubjectTvBook.getText().toString().trim()) {
            case "教材":
                mSubjectTvBook.setText("全部保存");
                mSubjectSvBook.setVisibility(View.VISIBLE);
                mSubjectSvCe.setVisibility(View.VISIBLE);
                setBookRadioButton();
                break;
            case "全部保存":
                mSubjectTvBook.setText("教材");
                mSubjectSvBook.setVisibility(View.GONE);
                mSubjectSvCe.setVisibility(View.GONE);
                StringBuilder buffer = new StringBuilder();
                for (Map.Entry<String, JiaoCai> map : mJiaocaiList.entrySet()) {
                    buffer.append(map.getKey());
                    buffer.append(":");
                    buffer.append(map.getValue().getBookID());
                    buffer.append(":");
                    buffer.append(map.getValue().getCeID());
                    buffer.append("##");
                }
                String banben = buffer.toString().substring(0, buffer.length() - 2);
                Log.d(TAG, "版本：" + banben);
                mMainPresenter.saveJiaoCai(banben);
                break;
        }
    }

    /**
     * 设置教材的单选按钮
     */
    private void setBookRadioButton() {
        mSubjectRgBook.removeAllViews();
        for (int i = 0; i < mBookList.get(subjectID).size(); i++) {
            BookGson book = mBookList.get(subjectID).get(i);
            RadioButton rb = new RadioButton(this);
            rb.setText(book.getName());
            rb.setTag(i);
            rb.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            rb.setBackgroundResource(R.drawable.base_subjectrb);
            rb.setGravity(Gravity.CENTER);
            mSubjectRgBook.addView(rb, mLayoutParams);
            if (mJiaocaiList.get(subjectID).getBookID().equals(book.getId())) {
                rb.setChecked(true);
                setCeRaioButton(i);
            }
        }
    }

    /**
     * 设置书的单选按钮
     */
    private void setCeRaioButton(int num) {
        mSubjectRgCe.removeAllViews();
        for (CeGson ce : mBookList.get(subjectID).get(num).getGsonList()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(ce.getBook_name());
            rb.setTag(ce.getId());
            rb.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            rb.setBackgroundResource(R.drawable.base_subjectrb);
            rb.setGravity(Gravity.CENTER);
            mSubjectRgCe.addView(rb, mLayoutParams);
            if (mJiaocaiList.get(subjectID).getCeID().equals(ce.getId()))
                rb.setChecked(true);
        }
    }

    /**
     * 设置学校列表
     */
    private void setSchoolList() {
        mSchoolListRg.removeAllViews();
        for (School school : mSchoolList) {
            RadioButton rb = new RadioButton(this);
            rb.setText(school.getName());
            rb.setTag(school.getId());
            rb.setTextSize(20);
            rb.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            rb.setBackgroundResource(R.drawable.base_subjectrb);
            rb.setGravity(Gravity.CENTER);
            mSchoolListRg.addView(rb, mLayoutParams);
            if (school.getId().equals(schoolID))
                rb.setChecked(true);
        }
    }


    /**
     * 点击屏幕取消侧滑菜单
     */
    @OnClick(R.id.base_Lltitle)
    void onLinearClick() {
        if (mBaseMenu.isOpen) {
            mBaseMenu.closeMenu();
        }
    }

    /**
     * 科目点击事件
     */
    @OnClick(R.id.base_back)
    void onSubjectClick() {
        mBaseMenu.toggle_subject();
    }

    /**
     * 学校列表点击事件
     */
    @OnClick(R.id.base_right)
    void onSchoolClick() {
        setSchoolList();
        mBaseMenu.toggle_school();
    }

    /**
     * 导航栏点击事件
     */
    public void onMainClick(View view) {
        String tag = view.getTag().toString();
        hideRight();
        mSubjectUtil.deleteObservers();
        mSchoolUtil.deleteObservers();
        Bundle bundle = new Bundle();
        bundle.putString("subjectID", subjectID);
        switch (tag) {
            case "练习":
                break;
            case "视频":
                VideoFragment videoFragment = new VideoFragment();
                mSubjectUtil.addObserver(videoFragment);
                changeFragment(R.id.main_rl, videoFragment);
                break;
            case "任务":
                break;
            case "真题":
                setRightBtn("学校");
                ExamFragment examFragment = new ExamFragment();
                examFragment.setArguments(bundle);
                mSubjectUtil.addObserver(examFragment);
                mSchoolUtil.addObserver(examFragment);
                changeFragment(R.id.main_rl, examFragment);
                break;
            case "我的":
                changeFragment(R.id.main_rl, new OtherFragment());
                break;
        }
        setTitle(tag);
        Log.d(TAG, "观察者数量：" + mSubjectUtil.countObservers());
    }

    /**
     * 获取教材
     */
    @Override
    public void getJiaoCai(ApiResponse<Map<String, List<BookGson>>> apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                mBookList = apiResponse.getObjList();
                break;
            case Const.FAILED:
                Log.d(TAG, "教材获取失败");
                break;
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    public void getUserPower(ApiResponse<Map<String, JiaoCai>> apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                mJiaocaiList = apiResponse.getObjList();
                break;
            case Const.FAILED:
                Log.d(TAG, "用户信息获取失败");
                break;
        }
    }

    /**
     * 保存教材
     */
    @Override
    public void saveJiaoCai(ApiResponse apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                showToast("教材保存成功啦~");
                break;
            case Const.FAILED:
                showToast("教材保存失败了。。");
                break;
        }
    }

    /**
     * 获取个人信息
     */
    @Override
    public void getUserInfo(ApiResponse<UserInfoGson> apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                Log.d(TAG, "获取个人信息成功");
                break;
            case Const.FAILED:
                Log.d(TAG, "获取个人信息失败");
                break;
        }
    }

    /**
     * 获取学校列表
     */
    @Override
    public void getSchoolList(ApiResponse<List<School>> apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                mSchoolList.clear();
                mSchoolList.addAll(apiResponse.getObjList());
                break;
            case Const.FAILED:
                Log.d(TAG, "获取学校列表失败");
                break;
        }
    }
}
