package com.example.mytest.activity.Main.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.activity.Exam.adapter.ExamAdapter;
import com.example.mytest.activity.Exam.presenter.ExamPresenter;
import com.example.mytest.activity.Exam.view.ExamView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.ShiJuanGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.SchoolUtil;
import com.example.mytest.util.SubjectUtil;
import com.example.mytest.view.MyItemDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaopeng on 2016/11/8.
 * 试卷
 */
public class ExamFragment extends BaseFragment implements Observer, ExamView {
    @BindView(R.id.exam_recycler)
    SwipeMenuRecyclerView mExamRecycler;
    @BindView(R.id.exam_swipe_layout)
    SwipeRefreshLayout mExamSwipeLayout;

    @Inject
    ExamPresenter mExamPresenter;
    private String schoolID = "0";//默认是“全部”
    private int pageNum = 1;//默认是“第一页”
    private int pageAll = 0;//总页数
    private List<ShiJuanGson> mJuanGsonList;
    private ExamAdapter mExamAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mView);
        mBaseComponent.inject(this);
        initData();
        return mView;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_exam;
    }


    private void initData() {
        subjectID = getArguments().getString("subjectID");
        mJuanGsonList = new ArrayList<>();
        mExamAdapter = new ExamAdapter(mJuanGsonList);
        mExamAdapter.setOnItemClick(OnItemClick);
        MyItemDecoration myItemDecoration = new MyItemDecoration(MyItemDecoration.VERTICAL);
        myItemDecoration.setColor(Color.BLUE);
        myItemDecoration.setSize(1);
        mExamRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        mExamRecycler.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mExamRecycler.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mExamRecycler.addItemDecoration(myItemDecoration);// 添加分割线。
        mExamRecycler.setSwipeMenuCreator(mSwipeMenuCreator);//设置菜单
        mExamRecycler.setSwipeMenuItemClickListener(mItemClickListener);//设置菜单点击事件
        mExamSwipeLayout.setOnRefreshListener(mOnRefreshListener);//设置下拉刷新
        mExamRecycler.addOnScrollListener(mOnScrollListener);// 添加滚动监听。
        mExamRecycler.setAdapter(mExamAdapter);
        updateShiJuan();
    }

    /**
     * 更新试卷
     */
    private void updateShiJuan() {
        String num = String.valueOf(pageNum);
        mExamPresenter.getShiJuan(num, subjectID, schoolID);
    }

    //条目点击事件
    private ExamAdapter.OnItemClick OnItemClick = new ExamAdapter.OnItemClick() {
        @Override
        public void onItemClick(int position) {
            showToast("跳转试卷");
            openPDFReader(0);
        }
    };

    //设置菜单
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_100);
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            //添加右侧菜单
            //试卷
            SwipeMenuItem shiJuanItem = new SwipeMenuItem(getActivity())
                    .setBackgroundColor(Color.GREEN)
                    .setText("试卷")
                    .setTextSize(18)
                    .setTextColor(Color.WHITE)
                    .setHeight(height)
                    .setWidth(width);
            swipeRightMenu.addMenuItem(shiJuanItem);
            if (viewType == 1) {
                //答案
                SwipeMenuItem answerItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(Color.RED)
                        .setText("答案")
                        .setTextSize(18)
                        .setTextColor(Color.WHITE)
                        .setHeight(height)
                        .setWidth(width);
                swipeRightMenu.addMenuItem(answerItem);
            }
        }
    };

    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageNum = 1;//刷新默认回到第一页
            updateShiJuan();
            mExamSwipeLayout.setRefreshing(false);//取消转圈
        }
    };

    //上拉加载
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
                if (pageNum >= pageAll)
                    showToast("试卷全部加载完啦");
                else {
                    pageNum++;
                    updateShiJuan();
                }
            }
        }
    };

    //菜单点击监听
    private OnSwipeMenuItemClickListener mItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            switch (menuPosition) {
                case 0://试卷
                    showToast("跳转试卷");
                    break;
                case 1://答案
                    showToast("跳转答案");
                    break;
            }
        }
    };


    @Override
    public void update(Observable observable, Object object) {
        pageNum = 1;
        if (observable.getClass().equals(SubjectUtil.class))
            subjectID = object.toString();
        else if (observable.getClass().equals(SchoolUtil.class))
            schoolID = object.toString();
        updateShiJuan();
    }


    /**
     * 获取试卷
     */
    @Override
    public void getShiJuan(ApiResponse<List<ShiJuanGson>> apiResponse) {
        if (pageNum == 1)//如果是第一页就把之前的清空
            mJuanGsonList.clear();
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                mJuanGsonList.addAll(apiResponse.getObjList());
                if (apiResponse.getObjList() != null && apiResponse.getObjList().size() != 0)
                    pageAll = Integer.parseInt(apiResponse.getObjList().get(0).getAllNum());
                else
                    pageAll = 0;
                mExamAdapter.notifyDataSetChanged();
                break;
            case Const.FAILED:
                showToast("试卷获取失败");
                break;
        }
    }

//    //跳转到PDF播放器
// TODO: 2016/12/6 高鹏 添加pdf下载功能，然后调用第三方的播放器 
    public void openPDFReader(int index) {
            //   Log.v("wenjianming>>>>>>>>>>", Global.magazinePath+tempData.name+".pdf");
            File file = new File("https://we1cm.b0.upaiyun.com/HY2012-013-IFDOO-DATA-0412/exam/pdf/daan/99/7566.pdf");
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(),
                            "No Application Available to View PDF",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }
//answer_pdf_src=https://we1cm.b0.upaiyun.com/HY2012-013-IFDOO-DATA-0412/exam/pdf/daan/99/7566.pdf
//
    }
