package com.roch.hzz_baidumap_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.entity.LoginUser_Result;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;
import com.roch.hzz_baidumap_demo.view.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录界面
 * @author ZhaoDongShao
 * 2016年5月9日
 */
public class LoginActivity extends CommonBaseActivity {

	@BindView(R.id.btn_login)
	Button btn_login;

	@BindView(R.id.cb_remember)
	CheckBox cb_remember;

	@BindView(R.id.edt_username)
	ClearEditText edt_username;

	@BindView(R.id.edt_password)
	ClearEditText edt_password;

	@BindView(R.id.ll_top_toolbar_container)
	LinearLayout ll_top_toolbar_container;

	/**
	 * 1.获取该Activity中的布局View
	 */
	@Override
	public int getContentView() {
		return R.layout.activity_login;
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApplication.getInstance().addActivity_login_out(this);

		ll_top_toolbar_container.setVisibility(View.GONE);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Login();
				LogUtil.println("用户名为：" + getEditText(edt_username) + ",密码为：" + getEditText(edt_password) + ",选中状态为：" + checked);
				SharePreferencesUtil.saveNameAndPassword(LoginActivity.this, getEditText(edt_username), getEditText(edt_password), checked);
			}
		});

		checked = SharePreferencesUtil.getLonginChecked(this);
		if(checked){//如果是保存了登陆用户名和密码
			// 初始化记住用户名和密码，并设置checkbox的状态
			initLoginNameAndPassword();
		}
	}

	/**
	 * 初始化记住用户名和密码，并设置checkbox的状态  <br/>
	 */
	private void initLoginNameAndPassword() {
		String longinName = SharePreferencesUtil.getLonginName(this);
		String longinPassword = SharePreferencesUtil.getLonginPassword(this);
		edt_username.setText(longinName);
		edt_password.setText(longinPassword);
		cb_remember.setChecked(checked);
	}

	/**
	 * 是否记住用户名和密码
	 */
	public boolean checked=false;

	@OnClick({ R.id.btn_login ,R.id.cb_remember})
	public void onClick(View v) {
		if (v.getId() == R.id.btn_login) {
			Login();
			LogUtil.println("用户名为：" + getEditText(edt_username) + ",密码为：" + getEditText(edt_password) + ",选中状态为：" + checked);
			SharePreferencesUtil.saveNameAndPassword(this, getEditText(edt_username), getEditText(edt_password), checked);
		}else if (v.getId() == R.id.cb_remember) {
//			showToast("点击了记住用户名和密码");
			checked = cb_remember.isChecked();
		}
	}

	/**
	 * 登陆用户的密码
	 */
	String password;
	/**
	 * 网络请求的参数---键值对
	 */
	private Map<String,String> params=new HashMap<>();

	private void Login() {
		String username = getEditText(edt_username);
		password = getEditText(edt_password);
		View focusView = null;
		boolean isNot = false; // 判断输入框是否为空

		if (StringUtil.isEmpty(username)) {
			focusView = edt_password;
			isNot = true;
		} else if (StringUtil.isEmpty(password)) {// Check for a valid password.
			focusView = edt_password;
			isNot = true;
		}

		if (isNot) {
			focusView.requestFocus();
			ShowToast("请输入用户名或密码");
		} else {
			params.clear();
			params.put("userName", username);
			params.put("pwd", password);
			LogUtil.println("登陆页面的登陆网址为：===" + URLs.LOGIN + "&userName=" + username + "&pwd=" + password);
			MyApplication.getInstance().getHttpUtilsInstance().post(LoginActivity.this,
					URLs.LOGIN,
					params,
					null,
					MyConstans.FIRST);
		}
	}

	@Override
	public void onSuccessResult(String str, int flag) {
		super.onSuccessResult(str, flag);
		switch (flag) {
			case MyConstans.FIRST:
				LogUtil.println("登陆页面进入后获取的服务器数据为：===" + str);
				try {
					JSONArray array = new JSONArray(str);
					JSONObject jsonObject = array.getJSONObject(0);
					String str1 = jsonObject.toString();

					LoginUser_Result loginUser_result=LoginUser_Result.parseToT(str1,LoginUser_Result.class);
					if(null!=loginUser_result && loginUser_result.getSuccess()){
						List<LoginUser> loginUsers = loginUser_result.getJsondata();
						if(null != loginUsers && loginUsers.size()>0){
							LoginUser loginUser = loginUsers.get(0);
							if(null != loginUser){
								SharedPreferences sp = getSharedPreferences("loginactivty", Context.MODE_APPEND);
								SharedPreferences.Editor ed = sp.edit();
								ed.putString("password",password);
								ed.putString("username",loginUser.getUserName());
								ed.commit();

								SharePreferencesUtil.saveLoginUser(this, loginUser);
								Common.Login_User_Name=loginUser.getUserName();
								LogUtil.println("当前登陆用户的ID为：==="+loginUser.getId());
								Common.Login_User_Id=loginUser.getId();
								Intent intent=new Intent(this,MainActivity_Home.class);
								startActivity(intent);
								MyApplication.getInstance().finishActivity_login_out(LoginActivity.this);
							}
						}
					}else {
						ShowToast(loginUser_result.getMsg());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFaileResult(String str, int flag) {
		super.onFaileResult(str, flag);
		switch (flag) {
		case MyConstans.FIRST:
			ShowToast("网络或服务异常");
			LogUtil.println("登陆失败:==="+str);
			break;
		}
	}

}
