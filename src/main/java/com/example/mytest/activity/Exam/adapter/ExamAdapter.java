package com.example.mytest.activity.Exam.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.dto.ShiJuanGson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by GaoPeng on 2016/12/6.
 */

public class ExamAdapter extends SwipeMenuAdapter {
    private OnItemClick mOnItemClick;
    private List<ShiJuanGson> mList;

    public ExamAdapter(List<ShiJuanGson> list) {
        this.mList = list;
    }


    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_shijuan, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.name.setText(mList.get(position).getName());
        viewHolder.name.setTextColor(Color.BLACK);
        viewHolder.setOnItemClick(mOnItemClick);
    }

    @Override
    public int getItemViewType(int position) {
        if (!"".equals(mList.get(position).getAnswer_pdf_src()))
            return 1;//有答案
        else
            return 0;//无答案
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ExamAdapter.OnItemClick mOnItemClick;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.exam_text_name);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClick(OnItemClick onItemClick) {
            this.mOnItemClick = onItemClick;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClick != null) {
                mOnItemClick.onItemClick(getAdapterPosition());
            }
        }
    }


    public interface OnItemClick {
        void onItemClick(int position);
    }
}
