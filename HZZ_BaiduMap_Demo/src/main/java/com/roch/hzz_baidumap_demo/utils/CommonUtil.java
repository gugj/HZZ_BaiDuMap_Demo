package com.roch.hzz_baidumap_demo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.systembarinit.SystemBarTintManager;
import java.text.NumberFormat;

/**
 * 通用工具类，内含各种工具方法以供调用
 */
public class CommonUtil {

	private static CommonUtil commonMethod;
	private Activity activity;

	/**
	 * 获取CommonUtil工具类的对象
	 */
	public static CommonUtil getInstance(Activity activity) {
		commonMethod = new CommonUtil();
		commonMethod.activity = activity;
		return commonMethod;
	}

	/**
	 * 获取手机型号
	 * @return
	 */
	public static String getPhone_Xinghao() {
		return Build.MODEL;
	}

	/**
	 * 获取手机版本
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getPhone_Ver() {
		return Build.VERSION.SDK;
	}

	/**
	 * 获取手机品牌
	 * @return
	 */
	public static String getPhone_Pinpai() {
		return Build.BRAND;
	}

	/**
	 * 获取手机系统版本
	 * @return
	 */
	public static String getPhone_OS_Ver() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机imei号码
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = manager.getDeviceId();
		return imei;
	}

	/**
	 * 获取android设备的唯�?ID
	 * @return
	 */
	public static String getAndroidId(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	/**
	 * 获取日期
	 */
	public static String getSpliteDate(String date) {
		return date != null ? date.split(" ")[0] : "";
	}

	/**
	 * 获取两个数字的比例
	 */
	public static String getBili(double d, double e) {
		String bili = "";
		if (StringUtil.isNotEmpty(d) && StringUtil.isNotEmpty(e)) {
			NumberFormat dFormat = NumberFormat.getInstance();
			dFormat.setMaximumFractionDigits(2);
			bili = dFormat.format(d / e);
			return bili + "%";
		}
		return bili + "%";
	}

	/**
	 * 通过获取的android版本改变状态栏的颜色
	 */
	public void getState() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //如果手机版本 大于等于19，即Android4.4
			// 通过WindowManager.LayoutParams设置Window的透明状态
			setTranslucentStatus(true);
			// 设置通知栏颜色
			SystemBarTintManager tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintEnabled(true);
//			tintManager.setStatusBarTintDrawable(ResourceUtil.getInstance().getDrawableByID(R.color.bule_155bbb));// 通知栏颜色
			tintManager.setStatusBarTintDrawable(activity.getResources().getDrawable(R.color.bule_155bbb));// 通知栏颜色
		}
	}

	/**
	 * 通过WindowManager.LayoutParams设置Window的透明状态
	 */
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		// 获取Windows对象
		Window win = activity.getWindow();
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

}
