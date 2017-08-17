package com.roch.hzz_baidumap_demo.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.MyApplication;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.CommonUtil;
import com.roch.hzz_baidumap_demo.utils.HttpUtils;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.ToastUtils;

/**
 * Activity的基类：只有一个方法，即设置状态栏颜色：手机版本大于21即Android5.0时，先取消设置透明状态栏，然后设置自定义的状态栏颜色
 */
public class BaseActivity extends AppCompatActivity implements HttpUtils.SuccessResult{

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //设置状态栏的颜色
//        setMyStatusBarColor();
    }

    /**
     * 设置状态栏的颜色
     */
    @SuppressLint({ "InlinedApi", "NewApi" })
    private void setMyStatusBarColor() {
        //如果当前手机版本 >=21，即Android5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           //获取Window对象
            Window window = getWindow();
            // 取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.color_145bba));

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                // 注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }else if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //如果手机版本 >=19，即Android4.4并且 <21，即Android5.0
            CommonUtil.getInstance(this).getState();
        }
    }

    /**
     * 获取状态栏的高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取手机的屏幕密度DPI、屏幕的宽度和高度
     */
    protected void initDensityDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Common.densityDpi = metrics.densityDpi;
        Common.Width = metrics.widthPixels;
        Common.Hight = metrics.heightPixels;
    }

    /*
	 * 显示进度条对话框--不可取消
	 */
    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
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

    /**
     * 显示一般时间的吐司
     */
    public void ShowToast(String msg) {
        ToastUtils.showNormalToast(this, msg);
    }

    /**
     * 返回EditText所编辑的内容
     * @param ed
     * @return
     */
    public String getEditText(EditText ed) {
        return ed.getText().toString().trim();
    }

    /**
     * 返回TextView所显示的内容
     * @param tv
     * @return
     */
    public String getTextView(TextView tv) {
        return tv.getText().toString().trim();
    }

    /**
     * 判断点击位置是否在EditText上，进而判断是否隐藏软键盘：否，隐藏输入框；
     */
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查当前手机连接网络的状态--WiFi、网络...
     * @return     如果网络可用返回true，否则返回false <br/>
     */
    public boolean ischeackNet(Context ctx) {
        ConnectivityManager conManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        NetworkInfo mobileInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if ((networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable())
                || (null != wifiInfo && wifiInfo.isAvailable() && wifiInfo.isConnected())
                || (null != mobileInfo && mobileInfo.isAvailable() && mobileInfo.isConnected())) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.println("BaseActivity取消请求网络");
        MyApplication.getInstance().getHttpUtilsInstance().canclePost(this);
    }

    @Override
    public void onSuccessResult(String str, int flag) {

    }

    @Override
    public void onFaileResult(String str, int flag) {

    }

}
