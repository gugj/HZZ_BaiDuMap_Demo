package com.roch.hzz_baidumap_demo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.ResourceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 通用的Activity页面，里面有：0.设置布局View；1.查找View控件；2.初始化传参Bundle和Toolbar；3.设置监听；4.初始化数据Data等
 * <br/><br/>继承自MainBaseActivity，父类只有一个方法：点击返回按钮关闭Activity
 * <br/><br/>父类又继承自BaseActivity，父类只有一个方法，即设置状态栏颜色：手机版本大于21即Android5.0时，先取消设置透明状态栏，然后设置自定义的状态栏颜色
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 14:24
 */
public abstract class CommonBaseActivity extends MainBaseActivity {

    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    /**
     * toolbar上面的自定义状态栏容器
     */
    @BindView(R.id.ll_top_toolbar)
    LinearLayout ll_top_toolbar;

    /**
     * Activity的标题Title
     */
    public String title;
    public Intent intent;
    public Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1.获取该Activity中的布局View
        setContentView(getContentView());
        // 初始化查找View控件
        unbinder = ButterKnife.bind(this);
        // 设置顶部状态栏的颜色
        setStatusBarColor();
        // 2.初始化传参数据---设置title标题
        initBundle();
        // 3.初始化Tool控件
        initToolbar();
        // 4.初始化控件的监听
        initListener();
        // 5.初始化数据
        initData();
    }

    /**
     * 1.获取该Activity中的布局View
     */
    public abstract int getContentView();

    /**
     * 2.初始化传参数据---设置title标题
     */
    public void initBundle() {
        LogUtil.println("CommomBaseActvity---2.initBundle()");
        intent = getIntent();
        title = intent.getStringExtra(Common.TITLE_KEY);
    }

    /**
     * 3.初始化Tool控件
     */
    public void initToolbar() {
        LogUtil.println("CommomBaseActvity---3.initToolbar()");
        toolbar.setTitle("");
        tvTitle.setText(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        tvBack.setVisibility(View.VISIBLE);
        tvBack.setOnClickListener(this);
    }

    /**
     * 4.初始化控件的监听
     */
    public void initListener() {
        LogUtil.println("CommomBaseActvity---4.initListener()");
    }

    /**
     * 5.初始化数据
     */
    public void initData() {
        LogUtil.println("CommomBaseActvity---5.initData()");
    }

    /**
     * 设置顶部状态栏的颜色
     * 2016年11月3日
     */
    private void setStatusBarColor() {
        // 如果版本 >=21即Android 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View statusBarView = mContentView.getChildAt(0);
            // 移除假的 View
            if (statusBarView != null && statusBarView.getLayoutParams() != null
                    && statusBarView.getLayoutParams().height == getStatusBarHeight()) {
                mContentView.removeView(statusBarView);
            }
            // 不预留空间
            if (mContentView.getChildAt(0) != null) {
                ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
            }
            LinearLayout.LayoutParams lParams = (android.widget.LinearLayout.LayoutParams) ll_top_toolbar.getLayoutParams();
            int height = getStatusBarHeight();
            lParams.height = height;

            ll_top_toolbar.setLayoutParams(lParams);
            ll_top_toolbar.setBackgroundColor(ResourceUtil.getInstance().getColorById(R.color.color_145bba));
        }

        // 如果版本 >=19即Android 4.4，<21即Android 5.0
        if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            // 首先使 ChildView 不预留空间
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
            }

            int statusBarHeight = getStatusBarHeight();
            // 需要设置这个 flag 才能设置状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 避免多次调用该方法时,多次移除了 View
            if (mChildView != null && mChildView.getLayoutParams() != null
                    && mChildView.getLayoutParams().height == statusBarHeight) {
                // 移除假的 View.
                mContentView.removeView(mChildView);
                mChildView = mContentView.getChildAt(0);
            }
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                // 清除 ChildView 的 marginTop 属性
                if (lp != null && lp.topMargin >= statusBarHeight) {
                    lp.topMargin -= statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }
            LinearLayout.LayoutParams lParams = (android.widget.LinearLayout.LayoutParams) ll_top_toolbar.getLayoutParams();
            int height = getStatusBarHeight();
            lParams.height = height;
            ll_top_toolbar.setLayoutParams(lParams);
            ll_top_toolbar.setBackgroundColor(ResourceUtil.getInstance().getColorById(R.color.color_145bba));
        }

        // 如果版本小于19，即小于Android 4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            ll_top_toolbar.setVisibility(View.GONE);
        }
    }

}
