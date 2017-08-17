package com.roch.hzz_baidumap_demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.roch.hzz_baidumap_demo.entity.LoginUser;

/**
 * Sharepreferences 工具类
 * @author zds
 */
public class SharePreferencesUtil {

	private static final String CONFIG = "config";
	// 登录用户
	private static final String LOGIN = "login";
	// 当前城市名字
	public static final String NOW_CITY = "now_city";
	// 是否显示引导页
	public static final String GUIDE_FLAG = "sp_config";
	// 历史搜索记录
	public static final String SEARCH_HISTORY = "history";
	// 服务器地址
	public static final String SERVER_ADDRESS = "server";
	//极光推送设备标签（Tag）
	public static final String Tag = "Tag";
	//极光推送设备别名（Alias）
	public static final String Alias = "Alias";

	/**
	 * 保存登陆时的用户名和密码
	 * @param context
	 * @param loginName
	 * @param loginPassword
	 * @param checked
	 * 2016年11月8日
	 */
	public static void saveNameAndPassword(Context context,String loginName,String loginPassword,boolean checked){
		SharedPreferences sp = context.getSharedPreferences(Common.SP_NameAndPassword, Context.MODE_APPEND);
		Editor ed = sp.edit();

		ed.putString("loginName", loginName);
		ed.putString("loginPassword", loginPassword);
		ed.putBoolean("isChecked", checked);
		ed.commit();
		System.out.println("保存成功用户名和密码！");
	}

	/**
	 * 获取保存的登陆时的用户名
	 * @param context
	 * @return
	 * 2016年11月8日
	 */
	public static String getLonginName(Context context){
		SharedPreferences sp = context.getSharedPreferences(Common.SP_NameAndPassword, Context.MODE_APPEND);
		return sp.getString("loginName", "");
	}

	/**
	 * 获取保存的登陆时的密码
	 * @param context
	 * @return
	 * 2016年11月8日
	 */
	public static String getLonginPassword(Context context){
		SharedPreferences sp = context.getSharedPreferences(Common.SP_NameAndPassword, Context.MODE_APPEND);
		return sp.getString("loginPassword", "");
	}

	/**
	 * 获取保存的登陆时是否保存登陆用户名和密码
	 * @param context
	 * @return
	 * 2016年11月8日
	 */
	public static boolean getLonginChecked(Context context){
		SharedPreferences sp = context.getSharedPreferences(Common.SP_NameAndPassword, Context.MODE_APPEND);
		return sp.getBoolean("isChecked", false);
	}

	/**
	 * 清空登录信息
	 * @param context
	 */
	public void clearLoginUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.clear();
		ed.commit();
	}

	// ----------当前城市-------------
	public void saveNowCity(Context context, String str) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(NOW_CITY, str);
		ed.commit();
	}

	public String getNowCity(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return sp.getString(NOW_CITY, "");
	}

	// ----------当前时间-------------
	public static void saveIsShowGuide(Context context, int isUpdate) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt(GUIDE_FLAG, isUpdate);
		ed.commit();
	}

	/**
	 * 是否第一次运行
	 * @param context
	 * @return
	 */
	public static int getGuide(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return sp.getInt(GUIDE_FLAG, -1);
	}

	/**
	 * 保存历史搜索记录
	 * @param msg
	 */
	public static void saveHistory(String msg, Context context) {
		SharedPreferences sp = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.putString("history", msg);
		editor.commit();
	}

	/**
	 * 获取历史记录
	 * @param context
	 * @return
	 */
	public static String getHistory(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
		return sp.getString("history", "");
	}

	/**
	 * 保存服务地址
	 * @param serveraddress
	 * @param context
	 */
	public static void saveServerAddress(String serveraddress, Context context) {
		SharedPreferences sp = context.getSharedPreferences(SERVER_ADDRESS, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("server", serveraddress);
		editor.commit();
	}

	/**
	 * 获取服务地址
	 * @param context
	 */
	public static String getServerAddress(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SERVER_ADDRESS, Context.MODE_PRIVATE);
		return sp.getString("server", "");
	}

	/**
	 * 保存设备标签和别名
	 */
	public static void saveTagAndAlias(String tag, String alias,Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Tag, tag);
		editor.putString(Alias, alias);
		editor.commit();
	}

	/**
	 * 获取设备标签
	 * @param context
	 */
	public static String getTag(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return sp.getString(Tag, "");
	}

	/**
	 * 获取设备标签
	 * @param context
	 */
	public static String getAlias(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return sp.getString(Alias, "");
	}

	/**
	 * 保存当前登陆用户
	 * @param activity
	 * @param loginUser
	 */
	public static void saveLoginUser(Activity activity, LoginUser loginUser) {
		SharedPreferences sp = activity.getSharedPreferences(loginUser.getUserName(), Context.MODE_APPEND);
		Editor ed = sp.edit();
		ed.putString(Common.USER_ID, loginUser.getId()); // UserId---用户ID
		ed.putString(Common.Ad_Cd, loginUser.getUserOrgList().get(0).getTsDepart().getDescription()); // 用户Ad_Cd---行政区
		ed.putString(Common.USER_LOGINNAME, loginUser.getUserName());  // 用户登陆名
		ed.putString(Common.User_RealName, loginUser.getRealName());   // 用户名真实名
		ed.putString(Common.User_DepartName, loginUser.getUserOrgList().get(0).getTsDepart().getDepartname());   // 用户所在部门名称
		ed.putString(Common.User_Email, loginUser.getEmail());   // 用户职位
		ed.putString(Common.User_orgType, loginUser.getUserOrgList().get(0).getTsDepart().getOrgType());   // 河长级别
		ed.putString(Common.User_mobilePhone, loginUser.getMobilePhone());   // 用户电话
		ed.commit();
	}

	/**
	 * 获取当前登陆用户
	 * @param activity
	 * @param loginUserName
	 */
	public static LoginUser getLoginUser(Activity activity, String loginUserName) {
		SharedPreferences sp = activity.getSharedPreferences(loginUserName, Context.MODE_APPEND);
		LoginUser loginUser=new LoginUser();

		loginUser.setId(sp.getString(Common.USER_ID, "")); // UserId---用户ID
		loginUser.setAd_cd(sp.getString(Common.Ad_Cd, "")); // 用户Ad_Cd---行政区
		loginUser.setUserName(sp.getString(Common.USER_LOGINNAME, "")); // 用户登陆名
		loginUser.setRealName(sp.getString(Common.User_RealName, "")); // 用户名真实名
		loginUser.setDepartname(sp.getString(Common.User_DepartName, "")); // 用户所在部门名称
		loginUser.setEmail(sp.getString(Common.User_Email, "")); // 用户职位
		loginUser.setOrgType(sp.getString(Common.User_orgType, "")); // 河长级别
		loginUser.setMobilePhone(sp.getString(Common.User_mobilePhone, "")); // 用户电话

		return loginUser;
	}

	/**
	 * 获取登陆用户的行政区划--Ad_Cd
	 */
	public static String getLoginUserAdCd(Activity activity){
		SharedPreferences sp = activity.getSharedPreferences(Common.Login_User_Name, Context.MODE_APPEND);
		return sp.getString(Common.Ad_Cd,"");
	}

	/**
	 * 保存当前的巡河状态：是否为正在巡河
	 */
	public static void saveIsStartXunHe(Activity activity,boolean isStarXunHe){
		SharedPreferences sp = activity.getSharedPreferences(CONFIG, Context.MODE_APPEND);
		Editor ed = sp.edit();
		ed.putBoolean("isStarXunHe",isStarXunHe);
		ed.commit();
		if(isStarXunHe){
			LogUtil.println("保存巡河状态成功----开始巡河");
		}else {
			LogUtil.println("保存巡河状态成功----结束巡河");
		}
	}

	/**
	 * 保存是否开始记录并保存巡河轨迹：是否保存并记录巡河轨迹
	 */
	public static void saveStartSavePointsAndGuiJi(Activity activity,boolean startSavePointsAndGuiJi){
		SharedPreferences sp = activity.getSharedPreferences(CONFIG, Context.MODE_APPEND);
		Editor ed = sp.edit();
		ed.putBoolean("startSavePointsAndGuiJi",startSavePointsAndGuiJi);
		ed.commit();
		if(startSavePointsAndGuiJi){
			LogUtil.println("保存是否开始记录巡河轨迹成功----开始保存并记录巡河轨迹");
		}else {
			LogUtil.println("保存是否开始记录巡河轨迹成功----取消保存并记录巡河轨迹");
		}
	}

	/**
	 * 获取当前的巡河状态：是否为正在巡河
	 */
	public static boolean getIsStartXunHe(Activity activity){
		SharedPreferences sp = activity.getSharedPreferences(CONFIG, Context.MODE_APPEND);
		return sp.getBoolean("isStarXunHe",false);
	}

	/**
	 * 获取是否保存并记录巡河轨迹：是否保存并记录巡河轨迹
	 */
	public static boolean getStartSavePointsAndGuiJi(Activity activity){
		SharedPreferences sp = activity.getSharedPreferences(CONFIG, Context.MODE_APPEND);
		return sp.getBoolean("startSavePointsAndGuiJi",false);
	}

	/**
	 * 保存选择巡河河道的ID
	 */
	public static void saveXunShiHeDaoId(Activity activity, String xunShiHeDaoId) {
		SharedPreferences sp = activity.getSharedPreferences(CONFIG, Context.MODE_APPEND);
		Editor ed = sp.edit();
		ed.putString("xunShiHeDaoId",xunShiHeDaoId);
		ed.commit();
	}

	/**
	 * 获取选择巡河河道的ID
	 */
	public static String getXunShiHeDaoId(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences(CONFIG, Context.MODE_APPEND);
		return sp.getString("xunShiHeDaoId","");
	}
}
