package com.roch.hzz_baidumap_demo.holder;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.roch.hzz_baidumap_demo.R;

/**
 * BaseViewHolder 顶级父类
 * Created by linlongxin on 2015/12/19.
 */
public class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder{

    private final String TAG = "BaseViewHolder";

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public BaseRecyclerViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        onInitializeView();
    }

    public void onInitializeView() {

    }

    public <T extends View> T findViewById(@IdRes int resId) {
        return (T) itemView.findViewById(resId);
    }

    public void setData(final T data) {
        CheckBox cb_qtype= (CheckBox) itemView.findViewById(R.id.cb_qtype);
        if(null != cb_qtype){
            cb_qtype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewClick(data);
                }
            });
        }else {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewClick(data);
                }
            });
        }
    }

    public void onItemViewClick(T data) {
    }

}
