package com.roch.hzz_baidumap_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可以设置最大高度的ListView
 * 作者：GuGaoJie
 * 时间：2017/7/19/019 13:43
 */
public class MaxListView extends ListView {

    /**
     * listview高度
     */
    private int listViewHeight;

    public int getListViewHeight() {
        return listViewHeight;
    }

    public void setListViewHeight(int listViewHeight) {
        this.listViewHeight = listViewHeight;
    }

    public MaxListView(Context context) {
        super(context);
    }

    public MaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (listViewHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
