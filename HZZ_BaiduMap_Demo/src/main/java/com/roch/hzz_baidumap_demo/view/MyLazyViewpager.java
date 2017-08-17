package com.roch.hzz_baidumap_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wangdh on 2016/8/15.
 * 屏蔽viewpager的左右滑动

 Viewpager的左右滑动实现:  先中断事件(中途中断事件),再消费事件

 Viewpager屏蔽左右滑动: 不中断,不消费
 */
public class MyLazyViewpager extends  LazyViewPager {

    public MyLazyViewpager(Context context) {
        super(context);
    }

    public MyLazyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
