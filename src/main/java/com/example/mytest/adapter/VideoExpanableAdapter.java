package com.example.mytest.adapter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.activity.Video.SetSystem;
import com.example.mytest.dto.VideoInfoChildGson;
import com.example.mytest.dto.VideoInfoParent;
import com.example.mytest.util.SubjectUtil;
import com.example.mytest.util.VideoChangeObervable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 可展开得listView　，任务视频列表
 * Created by 李均 on 2016/12/6.
 */

public class VideoExpanableAdapter extends BaseExpandableListAdapter{
    private List<VideoInfoParent> mInfoParents;
    private List<String> list;
    private List<List<String>> childlist;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private VideoChangeObervable videoChangeObervable;

    public VideoExpanableAdapter(List<VideoInfoParent> mInfoParents, Context context, VideoChangeObervable videoChangeObervable) {
        this.mInfoParents = mInfoParents;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
        childlist = new ArrayList<>();
        this.videoChangeObervable = videoChangeObervable;
        // 对列表展示数据进行处理
        for (int i = 0; i <mInfoParents.size() ; i++) {
            list.add(mInfoParents.get(i).getName());
            List<String> mclist = new ArrayList<>();
            for (int j = 0; j <mInfoParents.get(i).getList().size() ; j++) {
                mclist.add(mInfoParents.get(i).getList().get(j).getTeacher()+"/"+mInfoParents.get(i).getList().get(j).getName());
            }
            childlist.add(mclist);
        }
    }

    ViewHolderGroup viewHolderGroup;
    static class ViewHolderGroup {
        ImageView imageView;
        TextView special_videoName;
        GridView gridView;
    }

    /**
     * 设置父布局
     * listview显示 某一专题视频名称
     * @param groupPosition
     * @param isExpanded  是否打开
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            viewHolderGroup = new ViewHolderGroup();
            convertView = mLayoutInflater.inflate(R.layout.video_expandable_listview_item,null);
            viewHolderGroup.special_videoName = (TextView) convertView.findViewById(R.id.tv_special_videoName);
            viewHolderGroup.imageView = (ImageView) convertView.findViewById(R.id.channel_imageview_orientation);
            convertView.setTag(viewHolderGroup);
        }else{
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }
        //设置引导imageview 显示图片
        if (isExpanded) {
            viewHolderGroup.imageView.setImageResource(R.drawable.channel_expandablelistview_top_icon);
        } else {
            viewHolderGroup.imageView.setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
        }
        //专题视频名称
        viewHolderGroup.special_videoName.setText(list.get(groupPosition));
        return convertView;
    }

    /**
     * group 展开之后得子布局，使用GridView展示
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            viewHolderGroup = new ViewHolderGroup();
            convertView = mLayoutInflater.inflate(R.layout.video_child_gridview,null); //inflate 子菜单GridView
            viewHolderGroup.gridView = (GridView) convertView.findViewById(R.id.channel_item_child_gridView);
            convertView.setTag(viewHolderGroup);
        }else{
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }

        // 使用 SimpleAdapter 来是设置GridView 数据
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(mContext, setGridViewData(childlist.get(groupPosition)), R.layout.video_child_gridview_item,
                new String[] { "video_gridview_item" }, new int[] { R.id.tv_gridview_video_name });
        viewHolderGroup.gridView.setAdapter(mSimpleAdapter);
        setGridViewListener(viewHolderGroup.gridView);
        viewHolderGroup.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        return convertView;
    }

    /**
     * 设置GridVIew 显示数据
     * @param list    子项 text
     * @return
     */
    private ArrayList<HashMap<String, Object>> setGridViewData(List<String> list) {
        ArrayList<HashMap<String, Object>> gridItem = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("video_gridview_item", list.get(i));
            gridItem.add(hashMap);
        }
        return gridItem;
    }

    /**
     * 设置子项视频点击事件
     * @param gridView
     */
    private void setGridViewListener(GridView gridView) {
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(view instanceof TextView){
                    //如果想要获取到哪一行，则自定义gridview的adapter，item设置2个textview一个隐藏设置id，显示哪一行
                    TextView tv = (TextView) view ;
//                    Toast.makeText(mContext, "position=" + position+"||"+tv.getText(), Toast.LENGTH_SHORT).show();
                    Log.e("lg", "gridView listaner position=" + position + "||text="+tv.getText());
                    //通过观察者模式通知VideoFragment 改变视频播放状态
                    videoChangeObervable.sendMsg(new VideoInfoChildGson(position+"",tv.getText().toString()));
                }

            }
        });

    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childlist.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 子项是否可选中，
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
