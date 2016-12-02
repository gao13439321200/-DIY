package com.example.mytest.activity.Other;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Other.presenter.OtherPresenter;
import com.example.mytest.activity.Other.view.StudentInfoView;
import com.example.mytest.util.Const;
import com.example.mytest.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaopeng on 2016/11/9.
 * 个人信息
 */
public class StudentInfoActivity extends BaseActivity implements StudentInfoView {
    @BindView(R.id.other_studentinfo_nick)
    EditText mOtherStudentinfoNick;
    @BindView(R.id.other_studentinfo_name)
    TextView mOtherStudentinfoName;
    @BindView(R.id.other_studentinfo_birth)
    EditText mOtherStudentinfoBirth;
    @BindView(R.id.other_studentinfo_sex)
    EditText mOtherStudentinfoSex;
    @BindView(R.id.other_studentinfo_city)
    EditText mOtherStudentinfoCity;
    @BindView(R.id.other_studentinfo_grade)
    TextView mOtherStudentinfoGrade;
    @BindView(R.id.other_studentinfo_school)
    EditText mOtherStudentinfoSchool;
    @Inject
    OtherPresenter mOtherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("个人信息");
        initData();
        setRightBtn("编辑");
        setLeftBtn("返回");
        mBaseComponent.inject(this);//依赖注入
    }

    /**
     * 添加数据
     */
    public void initData() {
        String nick = (String) SPUtil.get(this, Const.SP_NICK, "");
        mOtherStudentinfoNick.setText("".equals(nick) ? "真懒" : nick);
        mOtherStudentinfoName.setText((String) SPUtil.get(this, Const.SP_REALNAME, ""));
        String birth = (String) SPUtil.get(this, Const.SP_BIRTH, "");
        mOtherStudentinfoBirth.setText("".equals(birth) ? "1993-01-01" : birth);
        String sex = (String) SPUtil.get(this, Const.SP_SEX, "");
        mOtherStudentinfoSex.setText("".equals(sex) ? "保密" : sex);
        String city = (String) SPUtil.get(this, Const.SP_CITY, "");
        mOtherStudentinfoCity.setText("".equals(city) ? "北京市" : city);
        String school = (String) SPUtil.get(this, Const.SP_SCHOOL, "");
        mOtherStudentinfoSchool.setText("".equals(school) ? "分豆大学" : school);
        mOtherStudentinfoGrade.setText("2".equals(SPUtil.get(this, Const.SP_GRADE, "")) ? "初中" : "高中");
    }

    /**
     * 保存和编辑的点击事件
     */
    @OnClick(R.id.base_right)
    public void onClickRight(){
        switch (mBaseRight.getText().toString()){
            case "编辑":
                setRightBtn("保存");
                setLeftBtn("取消");
                setEdittext(true);
                break;
            case "保存":
                String nick = mOtherStudentinfoNick.getText().toString();
                String name = mOtherStudentinfoName.getText().toString();
                String birth = mOtherStudentinfoBirth.getText().toString();
                String sex = mOtherStudentinfoSex.getText().toString();
                String city = mOtherStudentinfoCity.getText().toString();
                String school = mOtherStudentinfoSchool.getText().toString();
                mOtherPresenter.saveInfo(birth,city,name,sex,nick,school);
                break;
        }
    }

    @OnClick(R.id.base_back)
    public void onClickBack(){
        switch (mBaseBack.getText().toString()){
            case "返回":
                this.finish();
                break;
            case "取消":
                setRightBtn("编辑");
                setLeftBtn("返回");
                initData();
                setEdittext(false);
                break;
        }
    }

    /**
     * 设置可以修改的输入框状态
     *
     * @param visible 是否可以修改
     */
    private void setEdittext(boolean visible){
        mOtherStudentinfoNick.setEnabled(visible);
        mOtherStudentinfoBirth.setEnabled(visible);
        mOtherStudentinfoSex.setEnabled(visible);
        mOtherStudentinfoCity.setEnabled(visible);
        mOtherStudentinfoSchool.setEnabled(visible);
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_other_studentinfo;
    }

    /**
     * 保存用户信息
     *
     * @param task
     */
    @Override
    public void saveInfo(String task) {
        switch (task){
            case Const.SUCCESS:
                showToast("保存成功");
                setRightBtn("编辑");
                setEdittext(false);
                SPUtil.put(this,Const.SP_NICK,mOtherStudentinfoNick.getText().toString());
                break;
            case Const.FAILED:
                showToast("保存失败");
                break;
            default:
                break;
        }
    }
}
