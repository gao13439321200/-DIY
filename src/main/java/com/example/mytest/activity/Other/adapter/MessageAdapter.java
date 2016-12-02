package com.example.mytest.activity.Other.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.dto.MessageGson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by gaopeng on 2016/11/10.
 * 消息adapter
 */
public class MessageAdapter extends SwipeMenuAdapter {
    public List<MessageGson> mArrayList = null;
    public OnItemClick mOnItemClick;

    public MessageAdapter(List<MessageGson> list) {
        this.mArrayList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.textMsg.setText(mArrayList.get(position).getTitle());
        mHolder.textTime.setText(mArrayList.get(position).getCreated());
        mHolder.mImageView.setImageResource("0".equals(mArrayList.get(position).getStatus()) ? R.mipmap.img_noread : R.mipmap.img_read);
        mHolder.setOnItemClick(mOnItemClick);
    }

    /**
     * item点击事件的回调函数
     */
    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(mArrayList.get(position).getStatus());
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_message, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textMsg, textTime;
        OnItemClick mOnItemClick;
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textMsg = (TextView) itemView.findViewById(R.id.item_other_message_msg);
            textTime = (TextView) itemView.findViewById(R.id.item_other_message_time);
            mImageView = (ImageView) itemView.findViewById(R.id.item_other_message_img);
            itemView.setOnClickListener(this);//绑定回调函数
        }

        public void setOnItemClick(OnItemClick onItemClick) {
            this.mOnItemClick = onItemClick;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClick != null) {
                mOnItemClick.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemClick {
        void onItemClick(int positon);
    }
}
