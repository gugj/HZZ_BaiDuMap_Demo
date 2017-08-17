package com.roch.hzz_baidumap_demo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.holder.BaseRecyclerViewHolder;
import com.roch.hzz_baidumap_demo.holder.QtypeHolder;


public class QtypeAdapter extends MyRecyclerAdapter<WenTiLeiXin> {

    public QtypeAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseRecyclerViewHolder<WenTiLeiXin> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new QtypeHolder(parent);
    }
}