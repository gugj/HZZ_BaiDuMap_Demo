package com.roch.hzz_baidumap_demo.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.view.MyLazyViewpager;

/**
 * 作者：ZDS
 * 时间：2017/3/8/008 14:45
 */
public class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    private FragmentTransaction transaction;
    private FragmentManager manager;
    private Fragment[] fragments;
    private TextView tv_title;
    private MyLazyViewpager vp_home;
    int index= 0;

    public MyOnCheckedChangeListener(MyLazyViewpager vp_home, TextView tv_title, Fragment[] fragments, FragmentManager manager, FragmentTransaction transaction){
        this.vp_home=vp_home;
        this.tv_title=tv_title;
        this.fragments=fragments;
        this.manager=manager;
        this.transaction=transaction;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedRBid) {
        switch (checkedRBid){
            case  R.id.rb_shouye:
                index = 0;
                tv_title.setText("巡河首页");
                transaction = manager.beginTransaction()
                        .hide(fragments[0])
                        .hide(fragments[1])
                        .hide(fragments[2]);
                transaction.show(fragments[0]).commit();
                break;

            case  R.id.rb_xinxi_shangbao:
                index = 1;
                tv_title.setText("信息上报");
                transaction = manager.beginTransaction()
                        .hide(fragments[0])
                        .hide(fragments[1])
                        .hide(fragments[2]);
                transaction.show(fragments[1]).commit();
                break;

            case  R.id.rb_daiban_shixiang:
                index = 2;
                tv_title.setText("待办事项");
                transaction = manager.beginTransaction()
                        .hide(fragments[0])
                        .hide(fragments[1])
                        .hide(fragments[2]);
                transaction.show(fragments[2]).commit();
                break;
        }
//        vp_home.setCurrentItem(index);//item 当前界面索引
    }

    public int getIndex(){
        return index;
    }

}
