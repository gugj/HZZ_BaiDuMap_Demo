package com.roch.hzz_baidumap_demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.MainBaseActivity;
import com.roch.hzz_baidumap_demo.listener.MyOnCheckedChangeListener;
import com.roch.hzz_baidumap_demo.view.MyLazyViewpager;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/21/021 17:11
 */
public class StartXunHeActivity extends MainBaseActivity {

    private Toolbar toolbar;
    private TextView tv_title;
    private MyLazyViewpager vp_home;
    private RadioButton rb_shouye;
    private RadioButton rb_xinxi_shangbao;
    private RadioButton rb_daiban_shixiang;
    private RadioGroup rg_home;

    Fragment[] fragments;
    FragmentManager manager;
    FragmentTransaction transaction;

    MyOnCheckedChangeListener myOnchekedChangeListener;
    private TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_xunhe);

        // 1.初始化查找View控件
        initView();
        //2.初始化Tool控件
        initToolbar();
        //3.初级化Fragment数组，并控制显示第一个fragment
        initFragment();
        //4.初始化RadioGroup导航按钮的点击监听
        initListener();
    }

    /**
     * 1.初始化查找View控件
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        vp_home = (MyLazyViewpager) findViewById(R.id.vp_home);
        rb_shouye = (RadioButton) findViewById(R.id.rb_shouye);
        rb_xinxi_shangbao = (RadioButton) findViewById(R.id.rb_xinxi_shangbao);
        rb_daiban_shixiang = (RadioButton) findViewById(R.id.rb_daiban_shixiang);
        rg_home = (RadioGroup) findViewById(R.id.rg_home);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(this);
    }

    /**
     * 2.初始化Tool控件
     */
    private void initToolbar() {
        toolbar.setTitle("");
        tv_title.setText("巡河首页");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 3.初级化Fragment数组，并控制显示第一个fragment
     */
    private void initFragment() {
        fragments = new Fragment[3];
        manager = getSupportFragmentManager();
        fragments[0] = manager.findFragmentById(R.id.fragment_home);
        fragments[1] = manager.findFragmentById(R.id.fragment_xinxi_shangbao);
        fragments[2] = manager.findFragmentById(R.id.fragment_daiban_shixiang);
        transaction = manager.beginTransaction()
                .hide(fragments[0])
                .hide(fragments[1])
                .hide(fragments[2]);
        transaction.show(fragments[0]).commit();
    }

    /**
     * 4.初始化RadioGroup导航按钮的点击监听
     */
    private void initListener() {
        rg_home.check(R.id.rb_shouye);
        myOnchekedChangeListener = new MyOnCheckedChangeListener(vp_home, tv_title, fragments, manager, transaction);
        rg_home.setOnCheckedChangeListener(myOnchekedChangeListener);
    }

}
