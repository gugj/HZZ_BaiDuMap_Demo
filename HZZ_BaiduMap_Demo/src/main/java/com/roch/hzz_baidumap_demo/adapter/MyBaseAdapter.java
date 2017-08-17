package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.roch.hzz_baidumap_demo.holder.BaseViewHolder;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/21/021 15:54
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> mData;
    public Context mContext;

    public MyBaseAdapter(Context mContext, List<T> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder viewHolder=null;
        if(convertView==null){
            // 1.根据mContext获取Adapter的条目布局View
            convertView=getConvertView(mContext);
            // 2.根据Adapter的条目布局View获取相应的ViewHolder
            viewHolder=getViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (BaseViewHolder) convertView.getTag();
        }

        //赋值
        T data=mData.get(position);
        // 3.在getView中最后给条目中的控件进行一一赋值
        converItemValue(data,viewHolder);

        return viewHolder.getConvertView();
    }

    /**
     * 1.根据mContext获取Adapter的条目布局View
     * @param mContext
     * @return
     */
    public abstract View getConvertView(Context mContext);

    /**
     * 2.根据Adapter的条目布局View获取相应的ViewHolder
     * @param convertView
     * @return
     */
    protected abstract BaseViewHolder getViewHolder(View convertView);

    /**
     * 3.在getView中最后给条目中的控件进行一一赋值
     * @param data
     * @param viewHolder
     */
    protected abstract void converItemValue(T data, BaseViewHolder viewHolder);

}
