package me.iwf.photopicker;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Activity的基类：只有一个方法，即设置状态栏颜色：手机版本大于21即Android5.0时，先取消设置透明状态栏，然后设置自定义的状态栏颜色
 */
public class BaseActivity extends AppCompatActivity {

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
            window.setStatusBarColor(getResources().getColor(R.color.__picker_color_145bba));

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                // 注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }else if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //如果手机版本 >=19，即Android4.4并且 <21，即Android5.0
            // 通过WindowManager.LayoutParams设置Window的透明状态
            setTranslucentStatus(true);
        }
    }

    /**
     * 通过WindowManager.LayoutParams设置Window的透明状态
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        // 获取Windows对象
        Window win = this.getWindow();
        // 获取window对象的参数
        WindowManager.LayoutParams winParams = win.getAttributes();
        // window对象参数的常量
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        // 设置window对象的参数
        win.setAttributes(winParams);
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

}
