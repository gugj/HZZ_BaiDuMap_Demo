package com.roch.hzz_baidumap_demo.holder;

import android.view.View;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/21/021 16:05
 */
public abstract class BaseViewHolder {

    public View rootView;

    public BaseViewHolder(View rootView) {
        this.rootView = rootView;
        initFindView(rootView);
    }

    /**
     * 在创建BaseViewHolder后，开始查找BaseViewHolder中的相关view控件
     * @param rootView
     */
    public abstract void initFindView(View rootView) ;

    /**
     * 返回创建BaseViewHolder时传进来的View
     * @return
     */
    public View getConvertView(){
        return rootView;
    }

}
