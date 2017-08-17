package com.roch.hzz_baidumap_demo.utils;

import android.os.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 常量类，保存了各种数据的常量
 */
public class Common {
	
	/**
	 * 保存登陆界面输入的登陆用户名和密码的sp的名字
	 */
	public static String SP_NameAndPassword = "SP_NameAndPassword";
	/**
	 * 开始或结束巡河的广播
	 */
	public static String Start_End_XunHe_BroadCast = "Start_End_XunHe_BroadCast";

	public static int TEXT_SIZE = 15;

	/**
	 * 标题
	 */
	public static final String TITLE_KEY = "title";

	/**
	 * Bundle传参key
	 */
	public static final String BUNDEL_KEY = "bundle_key";

	/**
	 * Intent传参key
	 */
	public static final String INTENT_KEY = "intent_key";
	public static final String UP_LOAD_PHOTO_KEY = "image_url";

	/**
	 * 巡河上报时选中的问题类型的集合
	 */
	public static Map<String,String> qtype_select=new HashMap<>();

	/**
	 * 开始定位后，最新的当前定位经度----全局变量
	 */
	public static double mCurrentLon=0.0;
	/**
	 * 开始定位后，最新的当前定位纬度----全局变量
	 */
	public static double mCurrentLat=0.0;
	/**
	 * 开始定位后，最新的当前定位位置----全局变量Str
	 */
	public static String mCurrentLocationAddr="";
	
	/**
	 * 保存当前手机DPI
	 */
	public static int densityDpi = 0;
	
	/**
	 * 保存手机的宽度
	 */
	public static int Width = 0;
	
	/**
	 * 保存手机的高度
	 */
	public static int Hight = 0;
	
	/**
	 * 缓存路径
	 */
	public static final String CACHE_PATHE = "/Android/data/com.roch.hzz_baidumap_demo";
	
	/**
	 * 数据库版本
	 */
	public static final int DB_VERSION = 1;

	/**
	 * 数据库名称
	 */
	public static final String DB_NAME = "hzz_app-db";
	/**
	 * 定时进行网络判断action
	 */
	public static final String MESSAGE_RECEIVED_ACTION = "com.roch.hzz_baidumap_demo.MESSAGE_RECEIVED_INTERNET";

	/**
	 * 发送的信息
	 */
	public static final String KEY_MESSAGE = "message";

	/**
	 * APP更新action
	 */
	public static final String UPDATA_APP = "com.roch.hzz_baidumap_demo.update";
	/**
	 * 取消更新
	 */
	public static final String CANCEL_BROADCAST = "com.roch.hzz_baidumap_demo.update_cancel";

	/**
	 * 全局的保存当前登陆用户的登录名
	 */
	public static String Login_User_Name="";
	/**
	 * 全局的保存当前登陆用户的ID
	 */
	public static String Login_User_Id="";

	/**
	 * 存储卡路径
	 */
	public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath();
	public static final String CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + CACHE_PATHE + "/cache";
	public static final String DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getPath() + CACHE_PATHE + "/download";

	/**
	 * sp中保存登陆用户所有信息的键----用户ID
	 */
	public static String USER_ID="user_id";
	/**
	 * sp中保存登陆用户所有信息的键----用户行政区划
	 */
	public static String Ad_Cd="ad_cd";
	/**
	 * sp中保存登陆用户所有信息的键----用户登陆名
	 */
	public static final String USER_LOGINNAME = "user_loginname";
	/**
	 * sp中保存登陆用户所有信息的键----用户真实名
	 */
	public static String User_RealName="User_RealName";
	/**
	 * sp中保存登陆用户所有信息的键----用户所在部门名称
	 */
	public static String User_DepartName="User_DepartName";
	/**
	 * sp中保存登陆用户所有信息的键----用户职位
	 */
	public static String User_Email="User_Email";
	/**
	 * sp中保存登陆用户所有信息的键----用户河长级别
	 */
	public static String User_orgType="User_orgType";
	/**
	 * sp中保存登陆用户所有信息的键----用户电话
	 */
	public static String User_mobilePhone="User_mobilePhone";

	/**
	 * 返回uuid
	 * @param str
	 * @return
	 */
	public static String uuidToStr(String str) {
		String objectKey = null;
		if (null != str && str.length() > 0) {
			int index = str.lastIndexOf(".");
			int len = str.length();
			objectKey = UUID.randomUUID().toString().replaceAll("-", "") + str.substring(index, len);
		}
		return objectKey;
	}
}
