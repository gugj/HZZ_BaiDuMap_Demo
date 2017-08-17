package com.roch.hzz_baidumap_demo;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.roch.hzz_baidumap_demo.greendao.GreenDaoManager;
import com.roch.hzz_baidumap_demo.service.LocationService;
import com.roch.hzz_baidumap_demo.utils.HttpUtils;
import com.roch.hzz_baidumap_demo.utils.NetConnect;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/14/014 17:54
 */
public class MyApplication extends Application implements HttpUtils.SuccessResult{

    private static MyApplication myapp;
    private static NetConnect netConnect;
    ProgressDialog mProgressDialog;
    Stack<Activity> activityStack;
    Stack<Activity> activityStack_login_out;

    public LocationService locationService;
    public Vibrator mVibrator;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        myapp = this;
        mContext = getApplicationContext();

        // 初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        /***
         * 初始化百度定位配置，建议在Application中创建---此时已经配置好定位的参数
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        // 初始化GreenDao数据库配置
        GreenDaoManager.getInstance();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);

        // 初始化ImagerLoader配置
        initImageLoaderConfig();
    }

    /**
     * 初始化ImagerLoader配置
     */
    private void initImageLoaderConfig() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.empty_photo) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.empty_photo) //设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.empty_photo) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT) //设置图片的缩放方式
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取application的实例
     */
    public static MyApplication getInstance() {
        return myapp;
    }

    /**
     * @return 获取手机连接网络状态的实体类对象NetConnect
     */
    public NetConnect getNetConnectInstance() {
        if (netConnect == null) {
            netConnect = new NetConnect();
        }
        return netConnect;
    }

    /*
   * 显示进度条对话框--可取消
   */
    public void showProgressDialog(String msg, boolean cancleble) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(cancleble);
            mProgressDialog.setMessage(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.cancel();
    }

    HttpUtils httpUtils;

    public HttpUtils getHttpUtilsInstance() {
        if (httpUtils != null) {
            return httpUtils;
        } else {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    /**
     * 1.add Activity 添加Activity到栈
     */
    public void addActivity_login_out(Activity activity) {
//        LogUtil.println("添加Activity时该Activity：==="+activity);
        if (activityStack_login_out == null) {
            activityStack_login_out = new Stack<Activity>();
        }
        activityStack_login_out.add(activity);
//        LogUtil.println("添加Activity后栈中的Activity数量为：==" + activityStack_login_out.size());

//        for (Activity activity0 : activityStack_login_out) {
//            LogUtil.println("添加Activity后打印栈中的每一个Activity：==="+activity0);
//        }
    }

    /**
     * 2.结束指定的Activity
     */
    public void finishActivity_login_out(Activity activity) {
//        LogUtil.println("关闭Activity前(Activity activity)栈中的Activity数量为：==" + activityStack_login_out.size());
        if (activity != null) {
//            LogUtil.println("打印栈中关闭的这个Activity：==="+activity);
            activityStack_login_out.remove(activity);
            activity.finish();
            activity = null;
        }
//        LogUtil.println("关闭Activity后(Activity activity)栈中的Activity数量为：=="+activityStack_login_out.size());
    }

    /**
     * 3.结束指定类名的Activity
     */
    public void finishActivity_login_out(Class<?> cls) {
//        LogUtil.println("关闭Activity前(Class<?> cls)栈中的Activity数量为：=="+activityStack_login_out.size());
        for (Activity activity : activityStack_login_out) {
//            LogUtil.println("打印栈中的每一个Activity：==="+activity);
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
//        LogUtil.println("关闭Activity后(Class<?> cls)栈中的Activity数量为：=="+activityStack_login_out.size());
    }

    /**
     * 4.结束所有Activity
     */
    public void finishAllActivity_login_out() {
        for (int i = 0, size = activityStack_login_out.size(); i < size; i++) {
            if (null != activityStack_login_out.get(i)) {
                activityStack_login_out.get(i).finish();
            }
        }
        activityStack_login_out.clear();
    }




    /**
     * 1.add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
//        LogUtil.println("添加Activity时该Activity：==="+activity);
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
//        LogUtil.println("添加Activity后栈中的Activity数量为：==" + activityStack.size());

//        for (Activity activity0 : activityStack) {
//            LogUtil.println("添加Activity后打印栈中的每一个Activity：==="+activity0);
//        }
    }

    /**
     * 2.结束指定的Activity
     */
    public void finishActivity(Activity activity) {
//        LogUtil.println("关闭Activity前(Activity activity)栈中的Activity数量为：==" + activityStack.size());
        if (activity != null) {
//            LogUtil.println("打印栈中关闭的这个Activity：==="+activity);
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
//        LogUtil.println("关闭Activity后(Activity activity)栈中的Activity数量为：=="+activityStack.size());
    }

    /**
     * 3.结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
//        LogUtil.println("关闭Activity前(Class<?> cls)栈中的Activity数量为：=="+activityStack.size());
        for (Activity activity : activityStack) {
//            LogUtil.println("打印栈中的每一个Activity：==="+activity);
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
//        LogUtil.println("关闭Activity后(Class<?> cls)栈中的Activity数量为：=="+activityStack.size());
    }

    /**
     * 4.结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    @Override
    public void onSuccessResult(String str, int flag) {

    }

    @Override
    public void onFaileResult(String str, int flag) {

    }
}
