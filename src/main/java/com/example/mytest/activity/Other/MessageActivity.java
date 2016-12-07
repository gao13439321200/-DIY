package com.example.mytest.activity.Other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Other.adapter.MessageAdapter;
import com.example.mytest.activity.Other.presenter.OtherPresenter;
import com.example.mytest.activity.Other.view.MessageView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.MessageGson;
import com.example.mytest.util.Const;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity implements MessageView {

    @Inject
    OtherPresenter mOtherPresenter;
    @BindView(R.id.other_message_recycler)
    SwipeMenuRecyclerView mOtherMessageRecycler;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    private MessageAdapter mMessageAdapter;
    private int page = 1;
    private List<MessageGson> mMessageGsonList = new ArrayList<>();
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftBtn("返回");
        setTitle("消息");
        mContext = this;
        mBaseComponent.inject(this);//依赖注入
        initData();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_other_message;
    }

    private void initData() {
        mMessageAdapter = new MessageAdapter(mMessageGsonList);
        mMessageAdapter.setOnItemClick(onItemClick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mOtherMessageRecycler.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mOtherMessageRecycler.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mOtherMessageRecycler.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//        mOtherMessageRecycler.addItemDecoration(new ListViewDecoration());// 添加分割线。
        mOtherMessageRecycler.setSwipeMenuCreator(mSwipeMenuCreator);//设置菜单
        mOtherMessageRecycler.setSwipeMenuItemClickListener(mItemClickListener);//设置菜单点击事件
        mSwipeLayout.setOnRefreshListener(mOnRefreshListener);//设置下拉刷新
        mOtherMessageRecycler.addOnScrollListener(mOnScrollListener);// 添加滚动监听。
        mOtherMessageRecycler.setAdapter(mMessageAdapter);
        mOtherPresenter.getMessageList(page);//获取消息列表
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_60);
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            //添加右侧菜单
            //查看
            SwipeMenuItem lookItem = new SwipeMenuItem(mContext)
                    .setBackgroundColor(Color.GREEN)
                    .setText("查看")
                    .setTextColor(Color.WHITE)
                    .setTextSize(18)
                    .setHeight(height)
                    .setWidth(width);
            swipeRightMenu.addMenuItem(lookItem);

            if (viewType == 0) {//未读才添加此按钮
                //标为已读
                SwipeMenuItem setLookedItem = new SwipeMenuItem(mContext)
                        .setBackgroundColor(Color.RED)
                        .setText("标为已读")
                        .setTextColor(Color.WHITE)
                        .setTextSize(18)
                        .setHeight(height)
                        .setWidth(width);
                swipeRightMenu.addMenuItem(setLookedItem);
            }
        }
    };
    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener mItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
            //无论哪个，点击之后都将消息设置为已读
            mMessageGsonList.get(adapterPosition).setStatus("1");
            mMessageAdapter.notifyDataSetChanged();
            switch (menuPosition) {
                case 1://标为已读
                    mOtherPresenter.readMessage(adapterPosition + "");
                    break;
                case 0://查看
                    Intent intent = new Intent(MessageActivity.this, MessageInfoActivity.class);
                    intent.putExtra("id", mMessageGsonList.get(adapterPosition).getId());
                    intent.putExtra("content", mMessageGsonList.get(adapterPosition).getContenturl());
                    startActivity(intent);
                    break;
            }

        }
    };

    /**
     * 条目点击事件
     */
    private MessageAdapter.OnItemClick onItemClick = new MessageAdapter.OnItemClick() {
        @Override
        public void onItemClick(int positon) {
            Intent intent = new Intent(MessageActivity.this, MessageInfoActivity.class);
            intent.putExtra("id", mMessageGsonList.get(positon).getId());
            intent.putExtra("content", mMessageGsonList.get(positon).getContenturl());
            startActivity(intent);
            //本地设为已读
            mMessageGsonList.get(positon).setStatus("1");
            mMessageAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData(true);
            mSwipeLayout.setRefreshing(false);//取消转圈
        }
    };

    /**
     * 上拉加载
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
                loadData(false);
            }
        }
    };

    /**
     * 清空
     */
    @OnClick(R.id.base_right)
    public void OnClickClear(View view) {
        mOtherPresenter.clearMessage();
        mMessageGsonList.clear();
        mMessageAdapter.notifyDataSetChanged();
        showToast("消息全都消失啦~~");
        mBaseRight.setVisibility(View.GONE);
        mBaseRight2.setVisibility(View.GONE);
    }

    /**
     * 全部标为已读
     */
    @OnClick(R.id.base_right2)
    public void OnClickRead(View view) {
        mOtherPresenter.readMessage("");
        for (MessageGson messageGson : mMessageGsonList) {
            messageGson.setStatus("1");
        }
        mMessageAdapter.notifyDataSetChanged();
        showToast("全都标为已读啦~~");
        mBaseRight2.setVisibility(View.GONE);
    }


    /**
     * 获取消息
     */
    private void loadData(boolean isFirst) {
        if (isFirst) {//判断是否是刷新
            page = 1;
        } else {
            page++;
        }
        mOtherPresenter.getMessageList(page);
    }

    /**
     * 获取消息列表
     *
     * @param apiResponse 列表结合
     */
    @Override
    public void getMessageList(ApiResponse<List<MessageGson>> apiResponse, boolean isLast) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                List<MessageGson> list = apiResponse.getObjList();

                if (page == 1)
                    mMessageGsonList.clear();
                mMessageGsonList.addAll(list);
                if (mMessageGsonList == null || mMessageGsonList.size() == 0) {
                    mBaseRight.setVisibility(View.GONE);//隐藏清空按钮
                    if (page == 1)
                        showToast("什么消息都没有");
                } else {
                    if (isLast)
                        showToast("所有消息都加载完啦~~");
                    setRightBtn("清空");//显示清空按钮
                    boolean status = false;
                    for (MessageGson message : mMessageGsonList) {
                        if (message.getStatus().equals("0")) {//有一条消息就显示标为已读
                            status = true;
                            break;
                        }
                    }
                    if (status)
                        setRight2Btn("全部标为已读");
                    else
                        mBaseRight2.setVisibility(View.GONE);//默认隐藏标为已读按钮
                }
                mMessageAdapter.notifyDataSetChanged();
                break;
            case Const.FAILED:
                showToast("信息获取失败");
                break;
        }
    }

}
